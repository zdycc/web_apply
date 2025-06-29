package com.webb.dao;

import com.webb.model.User;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public interface UserDAO {

    /**
     * 根据用户名查询用户信息
     * @param username 用户名
     * @return User 对象，如果未找到则返回 null
     * @throws SQLException SQL执行异常
     */
    User findByUsername(String username) throws SQLException;

    /**
     * 根据用户ID查询用户信息
     * @param id 用户ID
     * @return User 对象，如果未找到则返回 null
     * @throws SQLException SQL执行异常
     */
    User findById(int id) throws SQLException;

    /**
     * 查询所有用户信息
     * @return 用户列表
     * @throws SQLException SQL执行异常
     */
    List<User> findAll() throws SQLException;

    /**
     * 添加新用户
     * @param user 用户对象
     * @return 添加成功后的用户ID，失败返回-1或抛异常
     * @throws SQLException SQL执行异常
     */
    int addUser(User user) throws SQLException;

    /**
     * 更新用户信息（例如角色、关联员工、账户状态）
     * @param user 要更新的用户对象
     * @return 更新成功返回 true，否则 false
     * @throws SQLException SQL执行异常
     */
    boolean updateUser(User user) throws SQLException;

    /**
     * 重置用户密码
     * @param userId 用户ID
     * @param newPasswordHash 新的SM3密码哈希
     * @return 更新成功返回 true，否则 false
     * @throws SQLException SQL执行异常
     */
    boolean resetUserPassword(int userId, String newPasswordHash) throws SQLException;

    /**
     * 更新用户登录失败次数和锁定状态
     * @param userId 用户ID
     * @param attempts 失败次数
     * @param lockoutUntil 锁定截止时间 (null表示未锁定)
     * @return 更新是否成功
     * @throws SQLException SQL执行异常
     */
    boolean updateLoginAttempts(int userId, int attempts, Timestamp lockoutUntil) throws SQLException;

    /**
     * 重置用户登录失败次数 (登录成功后调用)
     * @param userId 用户ID
     * @return 更新是否成功
     * @throws SQLException SQL执行异常
     */
    boolean resetLoginAttempts(int userId) throws SQLException;

    /**
     * 更新用户最后修改密码日期
     * @param userId 用户ID
     * @param changeDate 新的密码修改日期
     * @return 更新是否成功
     * @throws SQLException SQL执行异常
     */
    boolean updateLastPasswordChangeDate(int userId, Timestamp changeDate) throws SQLException;
    /**
     * 根据角色ID统计用户数量
     * @param roleId 角色ID
     * @return 拥有该角色的用户数量
     * @throws SQLException SQL执行异常
     */
    int countByRoleId(int roleId) throws SQLException;
    /**
     * 更新用户的最后登录时间为当前时间
     * @param userId 用户ID
     * @return 更新成功返回 true
     * @throws SQLException SQL执行异常
     */
    boolean updateLastLoginTime(int userId) throws SQLException;
}