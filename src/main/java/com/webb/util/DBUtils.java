package com.webb.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBUtils {
    private static final Logger logger = LoggerFactory.getLogger(DBUtils.class);

    // 数据库连接参数
    private static final String DB_URL = "jdbc:mysql://localhost:3306/webb_db?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=UTF-8";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "7235506"; // 您的密码

    // 静态代码块，用于加载驱动程序
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            logger.info("MySQL JDBC 驱动加载成功。");
        } catch (ClassNotFoundException e) {
            logger.error("加载 MySQL JDBC 驱动失败！", e);
            throw new RuntimeException("无法加载数据库驱动", e);
        }
    }


     //获取数据库连接
    public static Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            logger.debug("数据库连接获取成功。");
            return connection;
        } catch (SQLException e) {
            logger.error("获取数据库连接失败！URL: {}, User: {}", DB_URL, DB_USER, e);
            return null; // 或者抛出自定义异常
        }
    }

    //关闭数据库资源
    public static void close(ResultSet rs, Statement stmt, Connection conn) {
        try {
            if (rs != null) {
                rs.close();
                logger.debug("ResultSet 已关闭。");
            }
        } catch (SQLException e) {
            logger.error("关闭 ResultSet 失败。", e);
        }
        try {
            if (stmt != null) {
                stmt.close();
                logger.debug("Statement 已关闭。");
            }
        } catch (SQLException e) {
            logger.error("关闭 Statement 失败。", e);
        }
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                logger.debug("Connection 已关闭。");
            }
        } catch (SQLException e) {
            logger.error("关闭 Connection 失败。", e);
        }
    }

    public static void close(PreparedStatement pstmt, Connection conn) {
        close(null, pstmt, conn);
    }
}