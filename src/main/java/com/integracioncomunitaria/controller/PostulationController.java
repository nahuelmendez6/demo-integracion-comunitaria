package com.integracioncomunitaria.controller;

import com.integracioncomunitaria.database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PostulationController {

        /**
     * Permite que un proveedor se postule a una petición específica, enviando su propuesta y costo.
     * @param petitionId El ID de la petición a la cual el proveedor está aplicando.
     * @param providerId El ID del proveedor que está realizando la postulación.
     * @param proposal La propuesta enviada por el proveedor.
     * @param cost El costo asociado a la propuesta del proveedor.
     * @return True si la postulación fue realizada exitosamente, false en caso contrario.
     */
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

        /**
     * Obtiene todas las postulaciones realizadas por un cliente.
     * @param customerId El ID del cliente cuyos proveedores postulantes se desean obtener.
     * @return Una lista de arreglos de cadenas con información sobre las postulaciones (ID, propuesta, costo, nombre del proveedor, etc.).
     */
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

        /**
     * Obtiene el ID de la petición asociada a una postulación específica.
     * @param postulationId El ID de la postulación.
     * @return El ID de la petición asociada a la postulación o -1 si no se encuentra.
     */
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
    
        /**
     * Finaliza una postulación, cambiando su estado a "NO" y actualizando el estado de la postulación a finalizado.
     * @param postulationId El ID de la postulación a finalizar.
     * @param petitionId El ID de la petición relacionada con la postulación.
     * @return True si la postulación fue finalizada correctamente, false en caso contrario.
     */
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


        /**
     * Cancela una postulación, cambiando su estado a cancelado y marcando que no es el ganador.
     * @param postulationId El ID de la postulación a cancelar.
     * @param petitionId El ID de la petición relacionada con la postulación.
     * @return True si la postulación fue cancelada correctamente, false en caso contrario.
     */
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


    /**
     * Acepta una postulación, marcando al proveedor como el ganador y actualizando el estado de la postulación.
     * @param postulationId El ID de la postulación a aceptar.
     * @param petitionId El ID de la petición relacionada con la postulación.
     * @return True si la postulación fue aceptada correctamente, false en caso contrario.
     */
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
