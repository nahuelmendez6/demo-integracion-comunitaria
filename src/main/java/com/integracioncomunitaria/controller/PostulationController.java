package com.integracioncomunitaria.controller;

import com.integracioncomunitaria.database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PostulationController {

    public boolean applyToPetition(int petitionId, int providerId, String proposal, int cost) {
        String query = """
            INSERT INTO postulation (id_petition, id_provider, winner, proposal, cost, id_state, current, id_user_create, id_user_update)
            VALUES (?, ?, 'NO', ?, ?, 3, 'YES', ?, ?)
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, petitionId);
            pstmt.setInt(2, providerId);
            pstmt.setString(3, proposal);
            pstmt.setInt(4, cost);
            pstmt.setInt(5, providerId);
            pstmt.setInt(6, providerId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String[]> getPostulationsByCustomer(int customerId) {
        List<String[]> postulations = new ArrayList<>();
    
        // Primero, obtener todas las peticiones del cliente
        String petitionQuery = "SELECT id_petition FROM petition WHERE id_customer = ?";
        List<Integer> petitionIds = new ArrayList<>();
    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(petitionQuery)) {
    
            pstmt.setInt(1, customerId);
            ResultSet rs = pstmt.executeQuery();
    
            while (rs.next()) {
                petitionIds.add(rs.getInt("id_petition"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return postulations; // Si hay error, devolver lista vacía
        }
    
        // Si el cliente no tiene peticiones, no hay postulaciones que mostrar
        if (petitionIds.isEmpty()) {
            return postulations;
        }
    
        // Construir consulta dinámica para obtener postulaciones de todas las peticiones
        StringBuilder queryBuilder = new StringBuilder("""
            SELECT po.idpostulation, po.id_petition, pr.name, po.proposal, po.cost, st.state
            FROM postulation po
            JOIN provider pr ON po.id_provider = pr.id_provider
            JOIN state st ON po.id_state = st.id_state
            WHERE po.id_petition IN (
        """);

    
        for (int i = 0; i < petitionIds.size(); i++) {
            queryBuilder.append("?");
            if (i < petitionIds.size() - 1) {
                queryBuilder.append(",");
            }
        }
        queryBuilder.append(")");
    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(queryBuilder.toString())) {
    
            for (int i = 0; i < petitionIds.size(); i++) {
                pstmt.setInt(i + 1, petitionIds.get(i));
            }
    
            ResultSet rs = pstmt.executeQuery();
    
            while (rs.next()) {
                postulations.add(new String[]{
                    rs.getString("idpostulation"),
                    rs.getString("id_petition"),
                    rs.getString("name"),
                    rs.getString("proposal"),
                    rs.getString("cost"),
                    rs.getString("state")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return postulations;
    }

    public Integer getIdPetitionByPostulation(int postulationId) {
        String query = "SELECT id_petition FROM postulation WHERE idpostulation = ?";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, postulationId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt("id_petition");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return -1;

    }
    
    public boolean finishPostulation(int postulationId, int petitionId) {
        String query = """
                UPDATE postulation
                SET winner = 'NO', id_state = 7
                WHERE idpostulation = ? AND id_petition = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, postulationId);
                pstmt.setInt(2, petitionId);

                int rowsAffected = pstmt.executeUpdate();
                return rowsAffected > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
    }



    public boolean cancelPostulation(int postulationId, int petitionId) {
        String query = """
                UPDATE postulation
                SET winner = 'NO', id_state = 6
                WHERE idpostulation = ? AND id_petition = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, postulationId);
                pstmt.setInt(2, petitionId);

                int rowsAffected = pstmt.executeUpdate();
                return rowsAffected > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
    }

    public boolean acceptPostulation(int postulationId, int petitionId) {
        String query = """
            UPDATE postulation 
            SET winner = 'YES', id_state = 1 
            WHERE idpostulation = ? AND id_petition = ?
        """;
    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
    
            pstmt.setInt(1, postulationId);
            pstmt.setInt(2, petitionId);
    
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
    
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    


}
