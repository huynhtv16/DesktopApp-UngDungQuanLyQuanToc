package com.dungblue.dao;

import java.sql.Connection;
import java.sql.SQLException;

public class TestOracleConnection {
    public static void main(String[] args) throws SQLException {
        Connection conn = DBConnect.getConnection();
        if (conn != null) {
            System.out.println("Kết nối Oracle thành công!");
            DBConnect.getConnection();
        } else {
            System.out.println("Kết nối Oracle thất bại!");
        }
    }
}
