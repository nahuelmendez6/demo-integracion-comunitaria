package com.integracioncomunitaria.controller;

import com.integracioncomunitaria.database.DatabaseConnection;
import com.integracioncomunitaria.database.ResultDataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserProfileController {

    public ResultDataBase registerUserProfile(int userId, String email, String roleType) {
        ResultDataBase result = new ResultDataBase();
        String sql = "INSERT INTO user_profile (email, role_type, user_id) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);
            statement.setString(2, roleType);
            statement.setInt(3, userId);

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                result.setSuccess(true);
                result.setMessage("Perfil de usuario registrado correctamente.");
            } else {
                result.setSuccess(false);
                result.setMessage("No se pudo registrar el perfil de usuario.");
            }
        } catch (SQLException e) {
            result.setSuccess(false);
            result.setMessage("Error al registrar el perfil de usuario: " + e.getMessage());
        }

        return result;
    }
}