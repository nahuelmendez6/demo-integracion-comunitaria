package com.integracioncomunitaria.controller;

import com.integracioncomunitaria.database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InterestController {

    // Obtener todas las categorías disponibles
    public List<String[]> getAllCategories() {
        List<String[]> categories = new ArrayList<>();
        String query = "SELECT id_category, name FROM category";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                String[] category = {String.valueOf(rs.getInt("id_category")), rs.getString("name")};
                categories.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    // Guardar intereses seleccionados por un cliente
    public boolean saveCustomerInterests(int customerId, List<Integer> categoryIds, int userId) {
        String query = "INSERT INTO interest (id_customer, id_category, date_create, date_update) VALUES (?, ?, NOW(), NOW())";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            for (int categoryId : categoryIds) {
                stmt.setInt(1, customerId);
                stmt.setInt(2, categoryId);
                stmt.addBatch();
            }

            int[] rowsInserted = stmt.executeBatch();
            return rowsInserted.length > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
