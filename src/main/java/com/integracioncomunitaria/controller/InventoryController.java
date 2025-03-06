package com.integracioncomunitaria.controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.integracioncomunitaria.database.DatabaseConnection;

import com.integracioncomunitaria.model.Inventory;

public class InventoryController {
    private Connection connection;

    public InventoryController() {
        this.connection = DatabaseConnection.getConnection();
    }

    // Método para agregar un nuevo artículo a la tabla `article`
    public int addArticle(String name, int categoryId, int userId) {
        String sql = "INSERT INTO article (name, id_category, id_user_create, id_user_update) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, name);
            stmt.setInt(2, categoryId);
            stmt.setInt(3, userId);
            stmt.setInt(4, userId);
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // Retorna el ID del artículo creado
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Método modificado para agregar productos al inventario
    public boolean addProductToInventory(int providerId, String articleName, int categoryId, int quantity, double cost, int userId) {
        int articleId = addArticle(articleName, categoryId, userId);
        if (articleId == -1) {
            return false;
        }
        
        String sql = "INSERT INTO inventory (id_article, quantity, cost, id_user_create, id_user_update) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, articleId);
            stmt.setInt(2, quantity);
            stmt.setDouble(3, cost);
            stmt.setInt(4, userId);
            stmt.setInt(5, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Método para obtener los productos del inventario con el nombre del artículo
    public List<Inventory> getInventoryByProvider(int providerId) {
        List<Inventory> inventoryList = new ArrayList<>();
        String sql = "SELECT i.id_inventory, a.name AS article, i.quantity, i.cost " +
                     "FROM inventory i JOIN article a ON i.id_article = a.id_article " +
                     "WHERE i.id_user_create = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, providerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Inventory item = new Inventory(
                    rs.getInt("id_inventory"),
                    rs.getString("article"), 
                    rs.getInt("quantity"), 
                    rs.getBigDecimal("cost")
                );
                inventoryList.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return inventoryList;
    }
}
