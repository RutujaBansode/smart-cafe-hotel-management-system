package com.smartcafe.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.smartcafe.dao.InventoryDAO;
import com.smartcafe.model.InventoryItem;
import com.smartcafe.model.User;

@WebServlet("/inventory")
public class InventoryServlet extends HttpServlet {

    private final InventoryDAO inventoryDAO = new InventoryDAO();

    // SESSION CHECK METHOD
    private User checkLogin(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            response.sendRedirect("login.jsp");
        }

        return user;
    }
    
    private boolean isAdmin(User user) {
        return user != null &&
               "admin".equalsIgnoreCase(user.getRole());
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User loggedUser = checkLogin(request, response);
        if (loggedUser == null) return;
        
        
        if (!isAdmin(loggedUser)) {
            response.sendRedirect("access-denied.jsp");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) action = "list";

        try {

            switch (action) {

                case "delete":
                    int idToDelete = Integer.parseInt(request.getParameter("id"));
                    inventoryDAO.deleteInventoryItem(idToDelete);
                    response.sendRedirect("inventory?action=list");
                    break;

                case "edit":
                    int idToEdit = Integer.parseInt(request.getParameter("id"));
                    InventoryItem itemToEdit = inventoryDAO.getInventoryItemById(idToEdit);

                    if (itemToEdit == null) {
                        response.sendRedirect("inventory?action=list&error=notfound");
                        return;
                    }

                    request.setAttribute("item", itemToEdit);
                    request.getRequestDispatcher("inventory-edit.jsp").forward(request, response);
                    break;

                default:
                    List<InventoryItem> allItems = inventoryDAO.getAllInventory();
                    request.setAttribute("inventoryList", allItems);
                    request.getRequestDispatcher("inventory.jsp").forward(request, response);
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("inventory?action=list&error=true");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User loggedUser = checkLogin(request, response);
        if (loggedUser == null) return;

        String action = request.getParameter("action");

        try {

            if ("add".equalsIgnoreCase(action)) {

                InventoryItem item = new InventoryItem(
                        0,
                        request.getParameter("itemName"),
                        request.getParameter("category"),
                        Integer.parseInt(request.getParameter("quantity")),
                        Integer.parseInt(request.getParameter("threshold")),
                        Double.parseDouble(request.getParameter("unitPrice"))
                );

                inventoryDAO.addInventoryItem(item);

            } else if ("update".equalsIgnoreCase(action)) {

                InventoryItem item = new InventoryItem(
                        Integer.parseInt(request.getParameter("id")),
                        request.getParameter("itemName"),
                        request.getParameter("category"),
                        Integer.parseInt(request.getParameter("quantity")),
                        Integer.parseInt(request.getParameter("threshold")),
                        Double.parseDouble(request.getParameter("unitPrice"))
                );

                inventoryDAO.updateInventoryItem(item);
            }

            response.sendRedirect("inventory?action=list");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("inventory?action=list&error=true");
        }
    }
}