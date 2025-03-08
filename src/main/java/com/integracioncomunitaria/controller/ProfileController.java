package com.integracioncomunitaria.controller;

import com.integracioncomunitaria.database.DatabaseConnection;
import com.integracioncomunitaria.database.ResultDataBase;

import java.sql.*;
import java.util.ArrayList;

public class ProfileController {

        /**
     * Obtiene el ID del proveedor asociado a un usuario específico.
     * @param userId El ID del usuario para el cual se busca el proveedor.
     * @return El ID del proveedor si se encuentra, o -1 si no se encuentra el proveedor.
     */
    public int getProviderIdByUserId(int userId) {
        String query = "SELECT id_provider FROM provider WHERE id_user = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_provider");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Retorna -1 si no se encuentra el proveedor
    }

       /**
     * Obtiene el ID del usuario asociado a un proveedor específico.
     * @param providerId El ID del proveedor para el cual se busca el usuario.
     * @return El ID del usuario si se encuentra, o -1 si no se encuentra el usuario.
     */
    public int getUserByProviderId(int providerId) {
        String query = "SELECT id_user FROM provider WHERE id_provider = ?";
        try (Connection connection = DatabaseConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setInt(1, providerId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("id_user");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return -1;

    }
    
        /**
     * Carga todas las categorías disponibles en la base de datos.
     * @return Un array de strings que contiene los nombres de todas las categorías.
     */
    public String[] loadCategories() {
        ArrayList<String> categories = new ArrayList<>();
        String sql = "SELECT name FROM category";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                categories.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories.toArray(new String[0]);
    }


    /**
     * Carga todas las profesiones disponibles en la base de datos.
     * @return Un array de strings que contiene los nombres de todas las profesiones.
     */
    public String[] loadProfessions() {
        ArrayList<String> professions = new ArrayList<>();
        String sql = "SELECT name FROM profession";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                professions.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return professions.toArray(new String[0]);
    }


    /**
     * Carga todos los tipos de proveedor disponibles en la base de datos.
     * @return Un array de strings que contiene los tipos de proveedor.
     */
    public String[] loadTypeProvider() {
        ArrayList<String> typeProviders = new ArrayList<>();
        String sql = "SELECT type FROM type_provider";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                typeProviders.add(rs.getString("type")); // Corregido de "name" a "type"
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return typeProviders.toArray(new String[0]);
    }

        /**
     * Obtiene el ID de tipo de proveedor asociado a un tipo específico de proveedor.
     * @param typeProvider El nombre del tipo de proveedor.
     * @return El ID del tipo de proveedor si se encuentra, o -1 si no se encuentra.
     */
    public int getTypeProviderId(String typeProvider) {
        String query = "SELECT id_type_provider FROM type_provider WHERE type = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, typeProvider);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_type_provider");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

        /**
     * Obtiene el ID de categoría asociada a una categoría específica.
     * @param category El nombre de la categoría.
     * @return El ID de la categoría si se encuentra, o -1 si no se encuentra.
     */
    public int getCategoryId(String category) {
        String query = "SELECT id_category FROM category WHERE name = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, category);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_category");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

        /**
     * Obtiene el ID de profesión asociada a una profesión específica.
     * @param profession El nombre de la profesión.
     * @return El ID de la profesión si se encuentra, o -1 si no se encuentra.
     */
    public int getProfessionId(String profession) {
        String query = "SELECT id_profession FROM profession WHERE name = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, profession);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_profession");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

        /**
     * Actualiza el perfil de un proveedor con nueva categoría, profesión, tipo de proveedor y dirección.
     * @param userId El ID del usuario cuya información se actualizará.
     * @param category La nueva categoría asociada al proveedor.
     * @param profession La nueva profesión asociada al proveedor.
     * @param typeProvider El nuevo tipo de proveedor.
     * @param address La nueva dirección asociada al proveedor.
     * @return Un objeto ResultDataBase que indica si la actualización fue exitosa o no, junto con un mensaje.
     */
    public ResultDataBase updateProfile(int userId, String category, String profession, String typeProvider, String address) {
        ResultDataBase result = new ResultDataBase();
        String sql = "UPDATE provider SET address = ?, id_category = (SELECT id_category FROM category WHERE name = ?), " +
                     "id_profession = (SELECT id_profession FROM profession WHERE name = ?), " +
                     "id_type_provider = (SELECT id_type_provider FROM type_provider WHERE type = ?) " +
                     "WHERE id_user = ?";
    
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
    
            stmt.setString(1, address);
            stmt.setString(2, category);
            stmt.setString(3, profession);
            stmt.setString(4, typeProvider);
            stmt.setInt(5, userId);
    
            int rowsUpdated = stmt.executeUpdate();
    
            if (rowsUpdated > 0) {
                result.setSuccess(true);
                result.setMessage("Perfil actualizado correctamente.");
            } else {
                result.setSuccess(false);
                result.setMessage("No se encontró el usuario para actualizar.");
            }
        } catch (SQLException e) {
            result.setSuccess(false);
            result.setMessage("Error al actualizar el perfil: " + e.getMessage());
        }
    
        return result;
    }
    
}
