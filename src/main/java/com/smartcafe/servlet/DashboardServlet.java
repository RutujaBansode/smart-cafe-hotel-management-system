package com.smartcafe.servlet;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.smartcafe.dao.InventoryDAO;
import com.smartcafe.dao.OrderDAO;
import com.smartcafe.model.InventoryItem;
import com.smartcafe.model.Order;
import com.smartcafe.model.User;
import com.smartcafe.service.ForecastService;
import com.smartcafe.service.ForecastService.Prediction;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User loggedUser = (session != null) ? (User) session.getAttribute("user") : null;

        if (loggedUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        InventoryDAO inventoryDAO = new InventoryDAO();
        OrderDAO orderDAO = new OrderDAO();
        ForecastService forecastService = new ForecastService();

        List<InventoryItem> allInventory = inventoryDAO.getAllInventory();
        List<InventoryItem> lowStockList = inventoryDAO.getLowStockItems();
        List<Order> recentOrders = orderDAO.getRecentOrders(5);

        List<Prediction> predictions = forecastService.getTomorrowForecast();
        Prediction topSeller = forecastService.getTopPredictedSeller(predictions);

        int totalItemsCount = allInventory.size();
        int lowStockCount = lowStockList.size();

        double recentRevenueSum = 0;
        for (Order o : recentOrders) {
            recentRevenueSum += o.getTotalAmount();
        }
        recentRevenueSum = Math.round(recentRevenueSum * 100.0) / 100.0;

        request.setAttribute("totalItems", totalItemsCount);
        request.setAttribute("lowStockCount", lowStockCount);
        request.setAttribute("lowStockList", lowStockList);
        request.setAttribute("recentOrders", recentOrders);
        request.setAttribute("predictions", predictions);
        request.setAttribute("topSeller", topSeller);
        request.setAttribute("recentRevenue", recentRevenueSum);

        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}