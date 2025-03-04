package com.integracioncomunitaria.controller;

import com.integracioncomunitaria.database.DatabaseConnection;
import com.integracioncomunitaria.model.Provider;
import com.integracioncomunitaria.database.ResultDataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProviderController {

    public ResultDataBase registerProvider(Provider provider) {
        ResultDataBase result = new ResultDataBase();
        String sql = "INSERT INTO provider (name, address ,id_user) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, provider.getName());
            statement.setString(2, "");
            statement.setInt(3, provider.getIdUser());

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                result.setSuccess(true);
                result.setMessage("Proveedor registrado correctamente.");
            } else {
                result.setSuccess(false);
                result.setMessage("No se pudo registrar el proveedor.");
            }
        } catch (SQLException e) {
            result.setSuccess(false);
            result.setMessage("Error al registrar el proveedor: " + e.getMessage());
        }

        return result;
    }

    // Método para obtener proveedores filtrados por zona y categoría
    public List<String[]> getFilteredProviders(int customerId) {
        List<String[]> providers = new ArrayList<>();

        String query = """
            SELECT DISTINCT p.id_provider, p.name, p.id_category, p.id_profession, p.id_type_provider
            FROM provider p
            JOIN category pc ON p.id_category = pc.id_category
            JOIN interest i ON i.id_category = pc.id_category
            JOIN customer_address ca ON ca.id_customer = i.id_customer
            JOIN provider_zone pz ON pz.id_provider = p.id_provider
            JOIN zone_city zc ON zc.id_zone = pz.id_zone
            WHERE i.id_customer = ? AND zc.id_city = ca.id_city
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String[] provider = {
                        String.valueOf(rs.getInt("id_provider")),
                        rs.getString("name"),
                        String.valueOf(rs.getInt("id_category")),
                        String.valueOf(rs.getInt("id_profession")),
                        String.valueOf(rs.getInt("id_type_provider"))
                };
                providers.add(provider);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return providers;
    }

    public String getProviderName(int providerId) {
        String query = "SELECT name FROM provider WHERE id_provider = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, providerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Proveedor desconocido";
    }

    public List<String> getCategories() {
        List<String> categories = new ArrayList<>();
        String query = "SELECT name FROM category";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                categories.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categories;
    }

    public List<String> getProfessions() {
        List<String> professions = new ArrayList<>();
        String query = "SELECT name FROM profession";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                professions.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return professions;
    }

    public List<String[]> getCateogryFilteredProviders(String category, String profession) {
        List<String[]> providers = new ArrayList<>();
        String query = "SELECT p.id_provider, p.name, c.name AS category, pr.name AS profession " +
                       "FROM provider p " +
                       "JOIN category c ON p.id_category = c.id_category " +
                       "JOIN profession pr ON p.id_profession = pr.id_profession " +
                       "WHERE (? IS NULL OR c.name = ?) AND (? IS NULL OR pr.name = ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, category);
            pstmt.setString(2, category);
            pstmt.setString(3, profession);
            pstmt.setString(4, profession);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    providers.add(new String[]{
                            String.valueOf(rs.getInt("id_provider")),
                            rs.getString("name"),
                            rs.getString("category"),
                            rs.getString("profession")
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return providers;
    }
}

