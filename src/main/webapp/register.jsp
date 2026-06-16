<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Smart Cafe & Hotel - Register</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>

    <div class="auth-wrapper">
        <div class="auth-container">
            <div class="auth-header">
                <h1>Smart Cafe & Hotel</h1>
                <p>Register New Staff Member</p>
            </div>

            <!-- Error Alerts -->
            <% if ("true".equals(request.getParameter("error"))) { %>
                <div class="alert-box">Registration failed. Email might already be in use.</div>
            <% } else if ("empty".equals(request.getParameter("error"))) { %>
                <div class="alert-box">All fields are required.</div>
            <% } %>

            <!-- Registration Form -->
            <form action="register" method="post">
                <div class="form-group">
                    <label for="name">Full Name</label>
                    <input type="text" id="name" name="name" class="input-glow" placeholder="Enter full name" required>
                </div>

                <div class="form-group">
                    <label for="email">Email Address</label>
                    <input type="email" id="email" name="email" class="input-glow" placeholder="name@smartcafe.com" required>
                </div>

                <div class="form-group">
                    <label for="password">Password</label>
                    <input type="password" id="password" name="password" class="input-glow" placeholder="Create password" required>
                </div>

                <div class="form-group">
                    <label for="role">Role Permission</label>
                    <select id="role" name="role" class="input-glow" style="cursor: pointer; background-color: #0f172a;">
                        <option value="staff">Staff Manager</option>
                        <option value="admin">Administrator</option>
                    </select>
                </div>

                <button type="submit" class="btn-primary auth-btn">Register Account</button>
            </form>

            <div class="auth-footer">
                Already registered? <a href="login">Login here</a>
            </div>
        </div>
    </div>
<footer class="footer">
         Smart Cafe & Hotel Management System
    </footer>

</body>
</html>
