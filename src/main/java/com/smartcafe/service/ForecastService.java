package com.smartcafe.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.smartcafe.dao.InventoryDAO;
import com.smartcafe.dao.OrderDAO;
import com.smartcafe.model.InventoryItem;

public class ForecastService {

    private final InventoryDAO inventoryDAO = new InventoryDAO();
    private final OrderDAO orderDAO = new OrderDAO();

    public static class Prediction {
        private String itemName;
        private int itemId;
        private double predictedQuantity;
        private double recentAverage;
        private String trend;
        private String recommendation;

        public Prediction(String itemName, int itemId,
                          double predictedQuantity,
                          double recentAverage,
                          String trend,
                          String recommendation) {
            this.itemName = itemName;
            this.itemId = itemId;
            this.predictedQuantity = predictedQuantity;
            this.recentAverage = recentAverage;
            this.trend = trend;
            this.recommendation = recommendation;
        }

        public String getItemName() { return itemName; }
        public int getItemId() { return itemId; }
        public double getPredictedQuantity() { return predictedQuantity; }
        public double getRecentAverage() { return recentAverage; }
        public String getTrend() { return trend; }
        public String getRecommendation() { return recommendation; }
    }

    public List<Prediction> getTomorrowForecast() {

        List<Prediction> predictions = new ArrayList<>();
        List<InventoryItem> items = inventoryDAO.getAllInventory();

        for (InventoryItem item : items) {

            Map<Integer, Integer> sales =
                    orderDAO.getItemSalesHistory(item.getId(), 7);

            // SAFE GET METHOD (prevents null crash)
            double d1 = getSafe(sales, 1);
            double d2 = getSafe(sales, 2);
            double d3 = getSafe(sales, 3);
            double d4 = getSafe(sales, 4);
            double d5 = getSafe(sales, 5);
            double d6 = getSafe(sales, 6);
            double d7 = getSafe(sales, 7);

            // Weighted Moving Average
            double wma =
                    (d1 * 0.40) +
                    (d2 * 0.25) +
                    (d3 * 0.15) +
                    (d4 * 0.10) +
                    (d5 * 0.05) +
                    (d6 * 0.03) +
                    (d7 * 0.02);

            double sameDayLastWeek = d7;

            double predictedQty = (wma * 0.7) + (sameDayLastWeek * 0.3);
            predictedQty = Math.round(predictedQty * 10.0) / 10.0;

            double recentAvg = (d1 + d2 + d3) / 3.0;
            recentAvg = Math.round(recentAvg * 10.0) / 10.0;

            String trend = "STABLE";
            if (predictedQty > recentAvg + 0.5) trend = "UP";
            else if (predictedQty < recentAvg - 0.5) trend = "DOWN";

            String recommendation = "Maintain current stock.";

            int currentStock = item.getQuantity();

            if (predictedQty > currentStock) {
                int buyQty = (int) Math.ceil(predictedQty * 1.5) - currentStock;
                recommendation = "Low Stock Risk! Order " + buyQty + " units immediately.";
            } else if (currentStock <= item.getThreshold()) {
                recommendation = "Below safety threshold. Restock " + (item.getThreshold() * 2) + " units.";
            } else if (predictedQty > recentAvg * 1.25) {
                recommendation = "Demand rising. Prepare extra stock.";
            }

            predictions.add(new Prediction(
                    item.getItemName(),
                    item.getId(),
                    predictedQty,
                    recentAvg,
                    trend,
                    recommendation
            ));
        }

        return predictions;
    }

    // SAFE helper method (VERY IMPORTANT)
    private double getSafe(Map<Integer, Integer> map, int key) {
        Integer value = map.get(key);
        return value == null ? 0.0 : value;
    }

    public Prediction getTopPredictedSeller(List<Prediction> predictions) {

        if (predictions == null || predictions.isEmpty()) return null;

        Prediction top = predictions.get(0);

        for (Prediction p : predictions) {
            if (p.getPredictedQuantity() > top.getPredictedQuantity()) {
                top = p;
            }
        }

        return top;
    }
}