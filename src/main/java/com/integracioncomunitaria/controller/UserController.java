package com.integracioncomunitaria.controller;

import com.integracioncomunitaria.database.DatabaseConnection;
import com.integracioncomunitaria.model.User;
import com.integracioncomunitaria.database.ResultDataBase;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class UserController {

    private static final String UPLOAD_DIRECTORY = "uploads/";

    // Metodo para registrar un nuevo usuario en la base de datos
    public ResultDataBase registerUser(User user, String group) {
        ResultDataBase result = new ResultDataBase();
        String sql = "INSERT INTO user (name, last_name, email, password, id_profile) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, user.getName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPassword());
            statement.setInt(5, 0);

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int userId = generatedKeys.getInt(1);
                        result.setSuccess(true);
                        result.setObject(userId);
                    }
                }
            } else {
                result.setSuccess(false);
                result.setMessage("No se pudo registrar el usuario.");
            }
        } catch (SQLException e) {
            result.setSuccess(false);
            result.setMessage("Error al registrar el usuario: " + e.getMessage());
        }

        return result;
    }


    public boolean uploadProfilePicture(String email, File imageFile) {
        try {
            // Crear la carpeta de destino si no existe
            File uploadDir = new File(UPLOAD_DIRECTORY);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            
            // Definir el archivo de destino
            File destinationFile = new File(UPLOAD_DIRECTORY + email + "_profile.jpg");
            
            // Copiar el archivo seleccionado al destino
            try (FileInputStream fis = new FileInputStream(imageFile);
                 FileOutputStream fos = new FileOutputStream(destinationFile)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    fos.write(buffer, 0, length);
                }
            }
            
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}