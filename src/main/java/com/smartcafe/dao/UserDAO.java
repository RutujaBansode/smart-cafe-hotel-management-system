package com.smartcafe.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.smartcafe.model.User;
import com.smartcafe.util.DBConnection;

public class UserDAO {

    // Validate login
    public User validateUser(String email, String password) {

        if (email == null || password == null) return null;

        User user = null;

        String sql = "SELECT * FROM users WHERE email=? AND password=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email.trim().toLowerCase());
            ps.setString(2, password.trim());

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    user = new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("role")
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    // Register user
    public boolean registerUser(User user) {

        if (user == null ||
            user.getEmail() == null ||
            user.getPassword() == null) {
            return false;
        }

        String sql = "INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, user.getName().trim());
            ps.setString(2, user.getEmail().trim().toLowerCase());
            ps.setString(3, user.getPassword().trim());
            ps.setString(4, user.getRole().trim());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}