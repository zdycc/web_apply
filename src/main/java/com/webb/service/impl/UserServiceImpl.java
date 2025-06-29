package com.webb.service.impl;

import com.webb.dao.UserDAO;
import com.webb.dao.impl.UserDAOImpl;
import com.webb.model.User;
import com.webb.service.LoginException;
import com.webb.service.UserService;
import com.webb.service.UserServiceException;
import com.webb.util.CryptoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.regex.Pattern;

public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private UserDAO userDAO = new UserDAOImpl();

    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final int LOCKOUT_DURATION_MINUTES = 30;
    private static final int PASSWORD_VALIDITY_DAYS = 90;

    @Override
    public User login(String username, String password) throws LoginException {
        try {
            User user = userDAO.findByUsername(username);

            if (user == null) {
                logger.warn("登录尝试失败：用户 {} 不存在。", username);
                throw new LoginException("用户名或密码错误。");
            }
            if (!user.getIsActive()) {
                logger.warn("登录尝试失败：用户 {} 账户未激活。", username);
                throw new LoginException("账户未激活，请联系管理员。");
            }

            boolean isSystemAdmin = "系统管理员".equals(user.getRole().getRoleName());

            // --- 安全策略检查 (仅对非系统管理员) ---
            if (!isSystemAdmin) {
                // 1. 检查账户是否被锁定
                if (user.getLockoutUntil() != null && Timestamp.valueOf(LocalDateTime.now()).before(user.getLockoutUntil())) {
                    long remainingLockoutMinutes = ChronoUnit.MINUTES.between(LocalDateTime.now(), user.getLockoutUntil().toLocalDateTime());
                    logger.warn("登录尝试失败：用户 {} 账户被锁定，剩余锁定时间 {} 分钟。", username, remainingLockoutMinutes);
                    throw new LoginException("账户已被锁定，请于 " + (remainingLockoutMinutes + 1) + " 分钟后重试。");
                }

                // 2. 检查密码是否过期 (基于最后登录时间)
                if (user.getLastLoginTime() != null) {
                    LocalDateTime lastLogin = user.getLastLoginTime().toLocalDateTime();
                    if (LocalDateTime.now().isAfter(lastLogin.plusDays(PASSWORD_VALIDITY_DAYS))) {
                        logger.info("用户 {} 密码已过期 (基于最后登录时间)。", username);
                        user.setPasswordExpired(true); // 设置密码过期标志
                    }
                }
            }

            // --- 验证密码 ---
            String hashedPassword = CryptoUtils.sm3Hash(password);
            if (!hashedPassword.equals(user.getPasswordHash())) {
                // 对非系统管理员处理登录失败计数和锁定
                if (!isSystemAdmin) {
                    int attempts = user.getFailedLoginAttempts() + 1;
                    Timestamp lockoutUntil = null;
                    if (attempts >= MAX_FAILED_ATTEMPTS) {
                        lockoutUntil = Timestamp.valueOf(LocalDateTime.now().plusMinutes(LOCKOUT_DURATION_MINUTES));
                        logger.warn("用户 {} 登录失败次数达到上限，账户已锁定至 {}", username, lockoutUntil);
                        userDAO.updateLoginAttempts(user.getId(), attempts, lockoutUntil);
                        throw new LoginException("密码错误次数过多，账户已锁定 " + LOCKOUT_DURATION_MINUTES + " 分钟。");
                    } else {
                        userDAO.updateLoginAttempts(user.getId(), attempts, null);
                    }
                }
                logger.warn("用户 {} 登录密码错误。", username);
                throw new LoginException("用户名或密码错误。");
            }

            // --- 登录成功后的操作 ---
            userDAO.resetLoginAttempts(user.getId()); // 重置所有用户的失败尝试次数
            userDAO.updateLastLoginTime(user.getId()); // 更新所有用户的最后登录时间

            logger.info("用户 {} 登录成功。角色：{}", username, user.getRole().getRoleName());
            return user;

        } catch (SQLException e) {
            logger.error("登录服务发生数据库错误 for user: {}", username, e);
            throw new LoginException("登录服务暂时不可用，请稍后重试。");
        }
    }

    @Override
    public List<User> getAllUsers() throws UserServiceException {
        try {
            return userDAO.findAll();
        } catch (SQLException e) {
            logger.error("获取所有用户列表失败", e);
            throw new UserServiceException("获取用户列表失败：" + e.getMessage(), e);
        }
    }

    @Override
    public User getUserById(int userId) throws UserServiceException {
        if (userId <= 0) throw new UserServiceException("无效的用户ID。");
        try {
            return userDAO.findById(userId);
        } catch (SQLException e) {
            logger.error("根据ID获取用户失败: {}", userId, e);
            throw new UserServiceException("获取用户信息失败：" + e.getMessage(), e);
        }
    }

    @Override
    public User addNewUser(User user, String rawPassword) throws UserServiceException {
        if (user == null || user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new UserServiceException("用户名不能为空。");
        }
        validatePasswordComplexity(rawPassword);

        try {
            if (userDAO.findByUsername(user.getUsername()) != null) {
                throw new UserServiceException("用户名 '" + user.getUsername() + "' 已存在。");
            }
            user.setPasswordHash(CryptoUtils.sm3Hash(rawPassword));
            user.setLastPasswordChangeDate(new Timestamp(System.currentTimeMillis()));

            int newUserId = userDAO.addUser(user);
            if (newUserId > 0) {
                user.setId(newUserId);
                return user;
            } else {
                throw new UserServiceException("添加用户失败，数据库操作未返回新ID。");
            }
        } catch (SQLException e) {
            logger.error("添加新用户失败: {}", user.getUsername(), e);
            throw new UserServiceException("添加用户失败：" + e.getMessage(), e);
        }
    }

    @Override
    public User updateUser(User user) throws UserServiceException {
        if (user == null || user.getId() <= 0) throw new UserServiceException("无效的用户信息或ID。");
        try {
            if (!userDAO.updateUser(user)) {
                throw new UserServiceException("更新用户信息失败，可能用户不存在。");
            }
            return user;
        } catch (SQLException e) {
            logger.error("更新用户信息失败, UserID: {}", user.getId(), e);
            throw new UserServiceException("更新用户信息失败：" + e.getMessage(), e);
        }
    }

    @Override
    public boolean resetPassword(int userId, String newRawPassword) throws UserServiceException {
        if (userId <= 0) throw new UserServiceException("无效的用户ID。");
        validatePasswordComplexity(newRawPassword);
        try {
            String newPasswordHash = CryptoUtils.sm3Hash(newRawPassword);
            return userDAO.resetUserPassword(userId, newPasswordHash);
        } catch (SQLException e) {
            logger.error("重置用户密码失败, UserID: {}", userId, e);
            throw new UserServiceException("重置密码失败：" + e.getMessage(), e);
        }
    }

    @Override
    public boolean unlockUser(int userId) throws UserServiceException {
        if (userId <= 0) throw new UserServiceException("无效的用户ID。");
        try {
            logger.info("管理员正在尝试解锁用户ID: {}", userId);
            return userDAO.resetLoginAttempts(userId);
        } catch (SQLException e) {
            logger.error("解锁用户失败, UserID: {}", userId, e);
            throw new UserServiceException("解锁用户失败：" + e.getMessage(), e);
        }
    }

    @Override
    public boolean changePassword(int userId, String oldRawPassword, String newRawPassword) throws UserServiceException {
        if (userId <= 0) throw new UserServiceException("无效的用户ID。");
        try {
            User user = userDAO.findById(userId);
            if (user == null) throw new UserServiceException("用户不存在。");

            String oldPasswordHash = CryptoUtils.sm3Hash(oldRawPassword);
            if (!oldPasswordHash.equals(user.getPasswordHash())) {
                throw new UserServiceException("当前密码不正确！");
            }

            validatePasswordComplexity(newRawPassword);

            String newPasswordHash = CryptoUtils.sm3Hash(newRawPassword);
            return userDAO.resetUserPassword(userId, newPasswordHash);
        } catch (SQLException e) {
            logger.error("用户修改密码失败, UserID: {}", userId, e);
            throw new UserServiceException("修改密码失败：" + e.getMessage(), e);
        }
    }

    private void validatePasswordComplexity(String password) throws UserServiceException {
        if (password == null || password.length() < 8) {
            throw new UserServiceException("密码长度不能少于8位。");
        }
        String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=])(?=\\S+$).{8,}$";
        if (!Pattern.matches(pattern, password)) {
            throw new UserServiceException("密码必须包含大写字母、小写字母、数字和特殊字符的组合。");
        }
    }
}