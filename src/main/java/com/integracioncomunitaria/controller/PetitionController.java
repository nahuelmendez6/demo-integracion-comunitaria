package com.integracioncomunitaria.controller;

import com.integracioncomunitaria.database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PetitionController {

    public List<String[]> getTypePetitions() {
        List<String[]> typePetitions = new ArrayList<>();
        String query = "SELECT id_type_petition, name FROM type_petition";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                typePetitions.add(new String[]{
                        String.valueOf(rs.getInt("id_type_petition")),
                        rs.getString("name")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return typePetitions;
    }

    public boolean createPetition(int idTypePetition, String description, Date dateSince, Date dateUntil, int idCustomer) {
        String query = "INSERT INTO petition (id_type_petition, description, date_since, date_until, id_customer, id_user_create) " +
                       "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, idTypePetition);
            pstmt.setString(2, description);
            pstmt.setDate(3, dateSince);
            pstmt.setDate(4, dateUntil);
            pstmt.setInt(5, idCustomer);
            pstmt.setInt(6, idCustomer); // El cliente es quien crea la petición

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String[]> getPetitionsByCustomer(int customerId) {
        List<String[]> petitions = new ArrayList<>();
        String query = "SELECT id_petition, id_type_petition, description, date_since, date_until FROM petition WHERE id_customer = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, customerId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                petitions.add(new String[]{
                        String.valueOf(rs.getInt("id_petition")),
                        String.valueOf(rs.getInt("id_type_petition")),
                        rs.getString("description"),
                        rs.getDate("date_since").toString(),
                        rs.getDate("date_until").toString()
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return petitions;
    }

    public List<String[]> getAvailablePetitions(int providerId) {
        List<String[]> petitions = new ArrayList<>();
        String query = """
            SELECT DISTINCT p.id_petition, tp.name, p.description, p.date_since, p.date_until, ca.id_city
            FROM petition p
            JOIN type_petition tp ON p.id_type_petition = tp.id_type_petition
            JOIN customer_address ca ON ca.id_customer = p.id_customer
            JOIN provider_zone pz ON pz.id_provider = ?
            JOIN zone_city zc ON zc.id_zone = pz.id_zone
            WHERE ca.id_city = zc.id_city
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, providerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                petitions.add(new String[]{
                        String.valueOf(rs.getInt("id_petition")),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDate("date_since").toString(),
                        rs.getDate("date_until").toString(),
                        String.valueOf(rs.getInt("id_city"))
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return petitions;
    }

    public List<String[]> getAvailablePetitionsByType(int providerId, int typePetitionId) {
        List<String[]> petitions = new ArrayList<>();
        String query = """
            SELECT DISTINCT p.id_petition, tp.name, p.description, p.date_since, p.date_until, ca.id_city
            FROM petition p
            JOIN type_petition tp ON p.id_type_petition = tp.id_type_petition
            JOIN customer_address ca ON ca.id_customer = p.id_customer
            JOIN provider_zone pz ON pz.id_provider = ?
            JOIN zone_city zc ON zc.id_zone = pz.id_zone
            WHERE ca.id_city = zc.id_city AND p.id_type_petition = ?
        """;
    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
    
            stmt.setInt(1, providerId);
            stmt.setInt(2, typePetitionId);
            ResultSet rs = stmt.executeQuery();
    
            while (rs.next()) {
                petitions.add(new String[]{
                        String.valueOf(rs.getInt("id_petition")),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDate("date_since").toString(),
                        rs.getDate("date_until").toString(),
                        String.valueOf(rs.getInt("id_city"))
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return petitions;
    }
    

}
