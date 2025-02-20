package com.integracioncomunitaria.controller;

import com.integracioncomunitaria.database.DatabaseConnection;
import com.integracioncomunitaria.database.ResultDataBase;

import java.sql.*;
import java.util.ArrayList;

public class ProfileController {

    public String[] loadCategories() {
        ArrayList<String> categories = new ArrayList<>();
        String sql = "SELECT name FROM category";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                categories.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories.toArray(new String[0]);
    }

    public String[] loadProfessions() {
        ArrayList<String> professions = new ArrayList<>();
        String sql = "SELECT name FROM profession";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                professions.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return professions.toArray(new String[0]);
    }

    public String[] loadTypeProvider() {
        ArrayList<String> typeProviders = new ArrayList<>();
        String sql = "SELECT type FROM type_provider";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                typeProviders.add(rs.getString("type")); // Corregido de "name" a "type"
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return typeProviders.toArray(new String[0]);
    }

    public int getTypeProviderId(String typeProvider) {
        String query = "SELECT id_type_provider FROM type_provider WHERE type = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, typeProvider);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_type_provider");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getCategoryId(String category) {
        String query = "SELECT id_category FROM category WHERE name = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, category);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_category");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getProfessionId(String profession) {
        String query = "SELECT id_profession FROM profession WHERE name = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, profession);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_profession");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public ResultDataBase updateProfile(int userId, String category, String profession, String typeProvider, String address) {
        ResultDataBase result = new ResultDataBase();
        String sql = "UPDATE provider SET address = ?, id_category = (SELECT id_category FROM category WHERE name = ?), " +
                     "id_profession = (SELECT id_profession FROM profession WHERE name = ?), " +
                     "id_type_provider = (SELECT id_type_provider FROM type_provider WHERE type = ?) " +
                     "WHERE id_user = ?";
    
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
    
            stmt.setString(1, address);
            stmt.setString(2, category);
            stmt.setString(3, profession);
            stmt.setString(4, typeProvider);
            stmt.setInt(5, userId);
    
            int rowsUpdated = stmt.executeUpdate();
    
            if (rowsUpdated > 0) {
                result.setSuccess(true);
                result.setMessage("Perfil actualizado correctamente.");
            } else {
                result.setSuccess(false);
                result.setMessage("No se encontró el usuario para actualizar.");
            }
        } catch (SQLException e) {
            result.setSuccess(false);
            result.setMessage("Error al actualizar el perfil: " + e.getMessage());
        }
    
        return result;
    }
    
}
