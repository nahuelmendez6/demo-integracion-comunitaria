package com.integracioncomunitaria.controller;


import com.integracioncomunitaria.database.DatabaseConnection;
import com.integracioncomunitaria.model.Customer;
import com.integracioncomunitaria.database.ResultDataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerController {


        /**
     * Registra un nuevo cliente en la base de datos.
     */
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


    /**
     * Obtiene el nombre de un cliente por su ID.
     */
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


    /**
     * Obtiene la dirección de un cliente por su ID.
     */
    public List<String[]> getCustomerAddress(int id_customer) {
        List<String[]> customerAddress = new ArrayList<>();

        String query = """
                SELECT c.id_customer, c.name, a.id_province, a.id_departament, a.id_city, 
                       a.street, a.number_str, a.dpto, a.floor_dpto,
                       p.name AS province, d.name_departament AS departament, ci.name AS city
                FROM customer c
                JOIN customer_address a ON c.id_customer = a.id_customer
                JOIN province p ON a.id_province = p.id_province
                JOIN departament d ON a.id_departament = d.id_departament
                JOIN city ci ON a.id_city = ci.id_city
                WHERE c.id_customer = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
             
            stmt.setInt(1, id_customer);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                customerAddress.add(new String[]{
                    String.valueOf(rs.getInt("id_customer")),
                    rs.getString("name"),
                    rs.getString("province"),
                    rs.getString("departament"),
                    rs.getString("city"),
                    rs.getString("street"),
                    rs.getString("number_str"),
                    rs.getString("dpto"),
                    rs.getString("floor_dpto")
                });
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerAddress;
    }


    /**
     * Actualiza la dirección de un cliente en la base de datos.
     */
    public boolean updateCustomerAddress(int id_customer, String province, String departament, String city, String street, String number, String dpto, String floor) {
        String query = """
                UPDATE customer_address 
                SET id_province = (SELECT id_province FROM province WHERE name = ?),
                    id_departament = (SELECT id_departament FROM departament WHERE name_departament = ?),
                    id_city = (SELECT id_city FROM city WHERE name = ?),
                    street = ?, number_str = ?, dpto = ?, floor_dpto = ?
                WHERE id_customer = ?
                """;
    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
             
            stmt.setString(1, province);
            stmt.setString(2, departament);
            stmt.setString(3, city);
            stmt.setString(4, street);
            stmt.setString(5, number);
            stmt.setString(6, dpto);
            stmt.setString(7, floor);
            stmt.setInt(8, id_customer);
    
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
        /**
     * Obtiene los datos generales de un cliente por su ID.
     */

    public List<String[]> getCustomerData(int id_customer) {
        List<String[]> customerData = new ArrayList<>();
    
        String query = """
            SELECT c.id_customer, u.name, u.last_name, u.email
            FROM customer c
            JOIN user u ON c.id_user = u.id_user
            WHERE c.id_customer = ?
        """;
    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
    
            stmt.setInt(1, id_customer);
            ResultSet rs = stmt.executeQuery();
    
            while (rs.next()) {
                customerData.add(new String[]{
                    String.valueOf(rs.getInt("id_customer")),
                    rs.getString("name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return customerData;
    }
        /**
     * Actualiza el perfil de un cliente en la base de datos.
     */
    public boolean updateCustomerProfile(int customerId, String name, String lastName, String email) {
        String query = """
            UPDATE customer c
            JOIN user u ON p.id_user = u.id_user
            SET u.name = ?, u.last_name = ?, u.email = ?
            WHERE c.id_customer = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, name);
            stmt.setString(2, lastName);
            stmt.setString(3, email);
            stmt.setInt(4, customerId);


            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
