package com.integracioncomunitaria.controller;

import com.integracioncomunitaria.database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryController {

    // Método para agregar un artículo al inventario
    public boolean addInventoryItem(int providerId, int articleId, int quantity, double cost, int userId) {
        String query = "INSERT INTO inventory (id_provider, id_article, quantity, cost, id_user_create, id_user_update, date_create, date_update) " +
                       "VALUES (?, ?, ?, ?, ?, ?, NOW(), NOW())";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, providerId);
            stmt.setInt(2, articleId);
            stmt.setInt(3, quantity);
            stmt.setDouble(4, cost);
            stmt.setInt(5, userId);
            stmt.setInt(6, userId);

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para obtener el inventario de un proveedor
    public List<Map<String, Object>> getInventoryByProvider(int providerId) {
        List<Map<String, Object>> inventoryList = new ArrayList<>();
        String query = "SELECT i.id_inventory, a.name AS article_name, i.quantity, i.cost " +
                       "FROM inventory i " +
                       "JOIN article a ON i.id_article = a.id_article " +
                       "WHERE i.id_provider = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, providerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> item = new HashMap<>();
                item.put("id_inventory", rs.getInt("id_inventory"));
                item.put("article_name", rs.getString("article_name"));
                item.put("quantity", rs.getInt("quantity"));
                item.put("cost", rs.getDouble("cost"));
                inventoryList.add(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return inventoryList;
    }
}
