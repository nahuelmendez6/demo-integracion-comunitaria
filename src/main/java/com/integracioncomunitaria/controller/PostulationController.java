package com.integracioncomunitaria.controller;

import com.integracioncomunitaria.database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PostulationController {

    public boolean applyToPetition(int petitionId, int providerId, String proposal, int cost) {
        String query = """
            INSERT INTO postulation (id_petition, id_provider, winner, proposal, cost, id_state, current, id_user_create, id_user_update)
            VALUES (?, ?, 'NO', ?, ?, 1, 'YES', ?, ?)
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
}
