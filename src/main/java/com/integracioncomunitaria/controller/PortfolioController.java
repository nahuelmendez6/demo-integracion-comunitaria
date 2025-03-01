package com.integracioncomunitaria.controller;

import com.integracioncomunitaria.database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PortfolioController {

    // Método para obtener todos los portfolios de un proveedor
    public List<Map<String, Object>> getPortfoliosByProvider(int providerId) {
        List<Map<String, Object>> portfolios = new ArrayList<>();
        String query = "SELECT * FROM portfolio WHERE id_provider = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, providerId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> portfolio = new HashMap<>();
                portfolio.put("id_portfolio", rs.getInt("id_portfolio"));
                portfolio.put("name", rs.getString("name"));
                portfolio.put("description", rs.getString("description"));
                portfolio.put("date_create", rs.getDate("date_create"));
                portfolios.add(portfolio);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return portfolios;
    }

    // Método para crear un nuevo portfolio
    public boolean createPortfolio(int providerId, String name, String description) {
        String query = "INSERT INTO portfolio (name, description, id_provider) " +
                       "VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, name);
            stmt.setString(2, description);
            stmt.setInt(3, providerId);
            
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Método para agregar un archivo adjunto a un portfolio
    public boolean addAttachmentToPortfolio(int portfolioId, String fileName, String filePath, int userId) {
        String query = "INSERT INTO attachment (id_portfolio, name, path, id_user_create, id_user_update, date_create, date_update) " +
                       "VALUES (?, ?, ?, ?, ?, NOW(), NOW())";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, portfolioId);
            stmt.setString(2, fileName);
            stmt.setString(3, filePath);
            stmt.setInt(4, userId);
            stmt.setInt(5, userId);
            
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para obtener archivos adjuntos de un portfolio
    public List<Map<String, Object>> getAttachmentsByPortfolio(int portfolioId) {
        List<Map<String, Object>> attachments = new ArrayList<>();
        String query = "SELECT * FROM attachment WHERE id_portfolio = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, portfolioId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> attachment = new HashMap<>();
                attachment.put("id_attachment", rs.getInt("id_attachment"));
                attachment.put("name", rs.getString("name"));
                attachment.put("path", rs.getString("path"));
                attachments.add(attachment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return attachments;
    }
}
