package com.smartcafe.servlet;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.smartcafe.dao.InventoryDAO;
import com.smartcafe.dao.OrderDAO;
import com.smartcafe.model.InventoryItem;
import com.smartcafe.model.Order;
import com.smartcafe.model.OrderItem;
import com.smartcafe.model.User;

@WebServlet("/orders")
public class OrderServlet extends HttpServlet {

    private final InventoryDAO inventoryDAO = new InventoryDAO();
    private final OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User loggedUser = (session != null)
                ? (User) session.getAttribute("user")
                : null;

        if (loggedUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        List<InventoryItem> allItems = inventoryDAO.getAllInventory();

        request.setAttribute("menuList", allItems);

        request.getRequestDispatcher("orders.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User loggedUser = (session != null)
                ? (User) session.getAttribute("user")
                : null;

        if (loggedUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {

            String customerName =
                    request.getParameter("customerName");

            String paymentMethod =
                    request.getParameter("paymentMethod");

            String[] itemIds =
                    request.getParameterValues("itemId");

            String[] quantities =
                    request.getParameterValues("quantity");

            String[] prices =
                    request.getParameterValues("price");

            String[] itemNames =
                    request.getParameterValues("itemName");

            if (itemIds == null || itemIds.length == 0) {

                response.sendRedirect("orders?error=noItems");
                return;
            }

            List<OrderItem> orderItems = new ArrayList<>();

            double totalAmount = 0;

            for (int i = 0; i < itemIds.length; i++) {

                int qty = Integer.parseInt(quantities[i]);

                if (qty <= 0) {
                    continue;
                }

                int itemId =
                        Integer.parseInt(itemIds[i]);

                double price =
                        Double.parseDouble(prices[i]);

                String itemName =
                        itemNames[i];

                totalAmount += qty * price;

                OrderItem item = new OrderItem(
                        0,
                        0,
                        itemId,
                        itemName,
                        qty,
                        price
                );

                orderItems.add(item);
            }

            if (orderItems.isEmpty()) {

                response.sendRedirect(
                        "orders?error=noQuantitySelected");

                return;
            }

            System.out.println(
                    "Received Total = " + totalAmount);

            Order order = new Order();

            order.setCustomerName(customerName);
            order.setPaymentMethod(paymentMethod);
            order.setOrderDate(
                    new Timestamp(System.currentTimeMillis()));
            order.setTotalAmount(totalAmount);
            order.setItems(orderItems);

            boolean success =
                    orderDAO.saveOrder(order);

            if (success) {

                response.sendRedirect(
                    "order-success.jsp"
                    + "?amount=" + totalAmount
                    + "&customer=" + customerName
                    + "&payment=" + paymentMethod
                );

            }
            else {

                response.sendRedirect(
                        "orders?error=saveFailed");
            }

        } catch (Exception e) {

            e.printStackTrace();

            response.sendRedirect(
                    "orders?error=exception");
        }
    }
}