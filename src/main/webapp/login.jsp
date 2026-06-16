<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Smart Cafe & Hotel - Login</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body class="login-page">
    <div class="auth-wrapper">
        <div class="auth-container">
            <div class="auth-header">
                <h1>Smart Cafe & Hotel</h1>
                <p>Management System Portal</p>
            </div>

            <!-- Error Notification Alert Box -->
            <% if ("true".equals(request.getParameter("error"))) { %>
                <div class="alert-box">Invalid email or password. Please try again.</div>
            <% } else if ("empty".equals(request.getParameter("error"))) { %>
                <div class="alert-box">Please fill in all the input fields.</div>
            <% } %>

            <!-- Success Registration Box -->
            <% if ("true".equals(request.getParameter("registered"))) { %>
                <div class="alert-success-box">Account created successfully! Please log in.</div>
            <% } %>

            <!-- Login Form -->
            <form action="login" method="post">
                <div class="form-group">
                    <label for="email">Email Address</label>
                    <input type="email" id="email" name="email" class="input-glow" placeholder="name@smartcafe.com" required>
                </div>

                <div class="form-group">
                    <label for="password">Password</label>
                    <input type="password" id="password" name="password" class="input-glow" placeholder="Enter your password" required>
                </div>

                <button type="submit" class="btn-primary auth-btn">Access Dashboard</button>
            </form>

            <div class="auth-footer">
                Don't have an account? <a href="register">Create Account</a>
            </div>
        </div>
    </div>
    
    <footer class="footer">
         Smart Cafe & Hotel Management System
    </footer>
    

</body>
</html>
