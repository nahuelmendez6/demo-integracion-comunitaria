package com.integracioncomunitaria.controller;

import com.integracioncomunitaria.database.DatabaseConnection;
import com.integracioncomunitaria.model.Provider;
import com.integracioncomunitaria.database.ResultDataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
}