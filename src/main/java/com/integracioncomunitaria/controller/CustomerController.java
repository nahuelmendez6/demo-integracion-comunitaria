package com.integracioncomunitaria.controller;


import com.integracioncomunitaria.database.DatabaseConnection;
import com.integracioncomunitaria.model.Customer;
import com.integracioncomunitaria.database.ResultDataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerController {

    public ResultDataBase registerCustomer(Customer customer) {
        ResultDataBase result = new ResultDataBase();
        String sql = "INSERT INTO customer (name, adress ,id_user) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, customer.getName());
            statement.setString(2, "");
            statement.setInt(3, customer.getIdUser());

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                result.setSuccess(true);
                result.setMessage("Cliente registrado correctamente.");
            } else {
                result.setSuccess(false);
                result.setMessage("No se pudo registrar al cliente.");
            }
        } catch (SQLException e) {
            result.setSuccess(false);
            result.setMessage("Error al registrar el proveedor: " + e.getMessage());
        }

        return result;
    }

    public String getCustomerName(int customerId) {
        String query = "SELECT name FROM customer WHERE id_customer = ?";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, customerId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getString("name");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return "Cliente desconocido";
    }

}
