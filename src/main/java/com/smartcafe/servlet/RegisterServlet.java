package com.smartcafe.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.smartcafe.dao.UserDAO;
import com.smartcafe.model.User;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("register.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        System.out.println("REGISTER ATTEMPT: " + name + " " + email);

        if (name == null || email == null || password == null || role == null ||
            name.trim().isEmpty() || email.trim().isEmpty() ||
            password.trim().isEmpty() || role.trim().isEmpty()) {

            response.sendRedirect("register.jsp?error=empty");
            return;
        }

        name = name.trim();
        email = email.trim().toLowerCase();
        password = Integer.toString(password.trim().hashCode());

        // safe role handling
        if (!role.equals("ADMIN") && !role.equals("STAFF")) {
            role = "STAFF";
        }

        User user = new User(0, name, email, password, role);

        UserDAO dao = new UserDAO();
        boolean result = dao.registerUser(user);

        if (result) {
            System.out.println("REGISTER SUCCESS: " + email);
            response.sendRedirect("login.jsp?registered=true");
        } else {
            System.out.println("REGISTER FAILED");
            response.sendRedirect("register.jsp?error=true");
        }
    }
}