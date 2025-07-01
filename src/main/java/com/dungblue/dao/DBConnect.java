package com.dungblue.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

public class DBConnect {
    private static Connection connection = null;

    private DBConnect() {}

    public static Connection getConnection() {
        try {
            Properties props = new Properties();
            InputStream input = DBConnect.class.getClassLoader().getResourceAsStream("config.properties");
            if (input == null) {
                throw new RuntimeException("Không tìm thấy file config.properties");
            }
            props.load(input);

            String driver = props.getProperty("db.driver");
            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String password = props.getProperty("db.password");

            Class.forName(driver);
            return DriverManager.getConnection(url, user, password);

        } catch (ClassNotFoundException e) {
            System.err.println("Lỗi: Oracle Driver không tồn tại!");
            e.printStackTrace();
        } catch (SQLException | IOException e) {
            System.err.println("Lỗi kết nối Oracle: " + e.getMessage());
        }
        return null;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Đã đóng kết nối Oracle");
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi đóng kết nối: " + e.getMessage());
        }
    }
}
