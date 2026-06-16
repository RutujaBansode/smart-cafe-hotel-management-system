package com.smartcafe.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.smartcafe.dao.UserDAO;
import com.smartcafe.model.User;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (email == null || password == null ||
            email.trim().isEmpty() || password.trim().isEmpty()) {
            response.sendRedirect("login.jsp?error=empty");
            return;
        }

        email = email.trim();
        password = password.trim();

        System.out.println("LOGIN ATTEMPT: " + email);

        UserDAO dao = new UserDAO();
        User user = dao.validateUser(email, password);

        if (user != null) {

            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            System.out.println("LOGIN SUCCESS: " + user.getName() +
                               " (" + user.getRole() + ")");

            response.sendRedirect("dashboard");
            return;

        } else {
            System.out.println("LOGIN FAILED: Invalid credentials");
            response.sendRedirect("login.jsp?error=true");
        }
    }
}