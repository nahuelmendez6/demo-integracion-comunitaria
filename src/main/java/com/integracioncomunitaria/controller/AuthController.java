package com.integracioncomunitaria.controller;

import com.integracioncomunitaria.model.User;
import com.integracioncomunitaria.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthController {


    /**
     * Autentica a un usuario en el sistema.
     * @param email Correo electrónico del usuario.
     * @param password Contraseña ingresada por el usuario.
     * @return Un objeto User si la autenticación es exitosa, null en caso contrario.
     */
    public User login(String email, String password) {
        User user = null;
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Buscar el usuario en la tabla user
            String sqlUser = "SELECT id_user, name, email, password FROM user WHERE email = ?";
            try (PreparedStatement stmtUser = conn.prepareStatement(sqlUser)) {
                stmtUser.setString(1, email);
                ResultSet rsUser = stmtUser.executeQuery();

                if (rsUser.next()) {
                    int userId = rsUser.getInt("id_user");
                    String name = rsUser.getString("name");
                    String storedPassword = rsUser.getString("password");

                    // Verificar si la contraseña ingresada coincide con la almacenad
                    if (!password.equals(storedPassword)) {
                        return null;
                    }

                    user = new User(userId, name, email);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
       /**
     * Verifica si un usuario es un cliente.
     * @param userId ID del usuario a verificar.
     * @return true si el usuario es un cliente, false en caso contrario.
     */
    public Boolean userIsCustomer(int userId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // buscar el usuario en la tabla user_profile
            String sqlUserProfile = "SELECT id_profile, role_type FROM user_profile WHERE user_id = ?";
            try (PreparedStatement stmtUser = conn.prepareStatement(sqlUserProfile)) {
                stmtUser.setInt(1, userId);
                ResultSet rsUser = stmtUser.executeQuery();

                if (rsUser.next()) {
                    int id_profile = rsUser.getInt("id_profile");
                    String role_type = rsUser.getString("role_type");

                    if (role_type.equals("cliente")) {
                        return true;
                    }

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

        /**
     * Verifica si un usuario es un proveedor.
     * @param userId ID del usuario a verificar.
     * @return true si el usuario es un proveedor, false en caso contrario.
     */
    public Boolean userIsProvider(int userId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Buscar el usuario en la tabla user_prfile
            String sqlUserProfile = "SELECT id_profile, role_type FROM user_profile WHERE user_id = ?";
            try (PreparedStatement stmtUser = conn.prepareStatement(sqlUserProfile)) {
                stmtUser.setInt(1, userId);
                ResultSet rsUser = stmtUser.executeQuery();

                if (rsUser.next()) {
                    int id_profile = rsUser.getInt("id_profile");
                    String role_type = rsUser.getString("role_type");

                    if (role_type.equals("proveedor")) {
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
        /**
     * Obtiene el ID del proveedor asociado a un usuario.
     * @param userId ID del usuario.
     * @return ID del proveedor si existe, null en caso contrario.
     */
    public Integer getProviderId(int userId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT id_provider FROM provider WHERE id_user = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt("id_provider");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
        /**
     * Obtiene el ID del cliente asociado a un usuario.
     * @param userId ID del usuario.
     * @return ID del cliente si existe, null en caso contrario.
     */
    public Integer getCustomerId(int userId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT id_customer FROM customer WHERE id_user = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt("id_customer");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
