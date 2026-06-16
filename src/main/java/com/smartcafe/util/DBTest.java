package com.smartcafe.util;

import java.sql.Connection;

public class DBTest {

    public static void main(String[] args) {

        Connection con = DBConnection.getConnection();

        if (con != null) {
            System.out.println("🎉 SUCCESS: Database is connected successfully!");
        } else {
            System.out.println("❌ FAILED: Connection not established");
        }
    }
}