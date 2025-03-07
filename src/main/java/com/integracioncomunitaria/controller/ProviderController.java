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

    public Integer getProviderIdByName(String name) {
        String query = "SELECT id_provider FROM provider WHERE name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
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

    public boolean submitRating(int providerId, int rating, String comment, int userId) {
        String query = "INSERT INTO grade_provider (id_provider, id_grade, coment, id_user_create, id_user_update) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, providerId);
            stmt.setInt(2, rating);
            stmt.setString(3, comment);
            stmt.setInt(4, userId);
            stmt.setInt(5, userId);
            
            stmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

     // Método para obtener el promedio de puntuaciones de un proveedor
     public double getProviderAverageRating(int providerId) {
        String query = "SELECT AVG(id_grade) AS average FROM grade_provider WHERE id_provider = ?";

        try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, providerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("average");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0; // Si hay un error o no hay calificaciones, retorna 0
    }

    public List<String[]> getAllProviders() {
        List<String[]> providers = new ArrayList<>();
        String query = """
            SELECT p.id_provider, p.name, c.name AS category, pr.name AS profession
            FROM provider p
            JOIN category c ON p.id_category = c.id_category
            JOIN profession pr ON p.id_profession = pr.id_profession
        """;
    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
    
            while (rs.next()) {
                providers.add(new String[]{
                        String.valueOf(rs.getInt("id_provider")),
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getString("profession")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return providers;
    }
    
    public List<String[]> getProvidersByCategory(String category) {
        List<String[]> providers = new ArrayList<>();
        String query = """
            SELECT p.id_provider, p.name, c.name AS category, pr.name AS profession
            FROM provider p
            JOIN category c ON p.id_category = c.id_category
            JOIN profession pr ON p.id_profession = pr.id_profession
            WHERE c.name = ?
        """;
    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
    
            stmt.setString(1, category);
            ResultSet rs = stmt.executeQuery();
    
            while (rs.next()) {
                providers.add(new String[]{
                        String.valueOf(rs.getInt("id_provider")),
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getString("profession")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return providers;
    }

    public List<String[]> getProviderData(int id_provider) {
        List<String[]> providerData = new ArrayList<>();
    
        String query = """
            SELECT p.id_user, p.id_category, p.id_profession, u.name, u.last_name, u.email,c.name AS category, pr.name AS profession
            FROM provider p
            JOIN user u ON p.id_user = u.id_user
            JOIN category c ON p.id_category = c.id_category
            JOIN profession pr ON p.id_profession = pr.id_profession
            WHERE p.id_provider = ?
        """;
    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
    
            stmt.setInt(1, id_provider);
            ResultSet rs = stmt.executeQuery();
    
            while (rs.next()) {
                providerData.add(new String[]{
                    String.valueOf(rs.getInt("id_user")),
                    rs.getString("name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    rs.getString("category"),
                    rs.getString("profession")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return providerData;
    }
    
    
    public List<String[]> getProvidersByProfession(String profession) {
        List<String[]> providers = new ArrayList<>();
        String query = """
            SELECT p.id_provider, p.name, c.name AS category, pr.name AS profession
            FROM provider p
            JOIN category c ON p.id_category = c.id_category
            JOIN profession pr ON p.id_profession = pr.id_profession
            WHERE pr.name = ?
        """;
    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
    
            stmt.setString(1, profession);
            ResultSet rs = stmt.executeQuery();
    
            while (rs.next()) {
                providers.add(new String[]{
                        String.valueOf(rs.getInt("id_provider")),
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getString("profession")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return providers;
    }

    public boolean updateProviderProfile(int providerId, String name, String lastName, String email, int categoryId, int professionId) {
        String query = """
            UPDATE provider p
            JOIN user u ON p.id_user = u.id_user
            SET u.name = ?, u.last_name = ?, u.email = ?, p.id_category = ?, p.id_profession = ?
            WHERE p.id_provider = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, name);
            stmt.setString(2, lastName);
            stmt.setString(3, email);
            stmt.setInt(4, categoryId);
            stmt.setInt(5, professionId);
            stmt.setInt(6, providerId);

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    


}

