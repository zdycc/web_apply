package com.webb.service;

import com.webb.model.User;
import java.util.List;

public interface UserService {

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码（明文）
     * @return 登录成功返回 User 对象
     * @throws LoginException 登录业务异常
     */
    User login(String username, String password) throws LoginException;

    /**
     * 获取所有系统用户
     * @return 用户列表
     * @throws UserServiceException 业务异常
     */
    List<User> getAllUsers() throws UserServiceException;

    /**
     * 根据ID获取用户信息
     * @param userId 用户ID
     * @return User对象
     * @throws UserServiceException 业务异常
     */
    User getUserById(int userId) throws UserServiceException;

    /**
     * 添加新用户
     * @param user 要添加的用户对象（不含密码哈希）
     * @param rawPassword 明文密码
     * @return 添加成功后的User对象
     * @throws UserServiceException 如果用户名已存在、密码不符合复杂度等
     */
    User addNewUser(User user, String rawPassword) throws UserServiceException;

    /**
     * 更新用户信息（角色、关联员工、状态）
     * @param user 包含更新信息的用户对象
     * @return 更新后的User对象
     * @throws UserServiceException 业务异常
     */
    User updateUser(User user) throws UserServiceException;

    /**
     * 管理员重置用户密码
     * @param userId 用户ID
     * @param newRawPassword 新的明文密码
     * @return 操作成功返回 true
     * @throws UserServiceException 密码不符合复杂度等
     */
    boolean resetPassword(int userId, String newRawPassword) throws UserServiceException;

    /**
     * 解锁一个被锁定的用户账户
     * @param userId 要解锁的用户ID
     * @return 操作是否成功
     * @throws UserServiceException 业务异常
     */
    boolean unlockUser(int userId) throws UserServiceException;

    /**
     * 当前登录用户修改自己的密码
     * @param userId 当前用户ID
     * @param oldRawPassword 旧的明文密码
     * @param newRawPassword 新的明文密码
     * @return 操作是否成功
     * @throws UserServiceException 业务异常
     */
    boolean changePassword(int userId, String oldRawPassword, String newRawPassword) throws UserServiceException;
}

