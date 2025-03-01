package com.integracioncomunitaria.controller;

import com.integracioncomunitaria.database.DatabaseConnection;
import com.integracioncomunitaria.model.Provider;
import com.integracioncomunitaria.database.ResultDataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

}