package com.ling.generator.utils;

import com.ling.generator.model.Db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * JDBC连接工具类
 * @author zhangling 2021/4/2 9:35
 */
public class DbUtils {

    private static Connection connection;

    public static Connection getConnection() {
        return connection;
    }

    /**
     * 初始化数据库连接
     * @param db
     * @return
     */
    public static Connection initDb(Db db) {
        if (connection == null) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection(db.getUrl(), db.getUsername(), db.getPassword());
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }
}
