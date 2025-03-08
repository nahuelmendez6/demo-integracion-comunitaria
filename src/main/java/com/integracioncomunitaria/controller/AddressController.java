package com.integracioncomunitaria.controller;

import com.integracioncomunitaria.database.DatabaseConnection;
import com.integracioncomunitaria.database.ResultDataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * Controlador para gestionar la carga y almacenamiento de direcciones en la base de datos.
 */
public class AddressController {



    /**
     * Carga las provincias del país con ID 7.
     * @return Un array de Strings con los nombres de las provincias.
     */
    public String[] loadProvinces() {
        String query = "SELECT name FROM province WHERE id_country = ?";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, 7);  // id_country = 7, como se mencionó en la descripción

            ResultSet rs = stmt.executeQuery();
            List<String> provinces = new ArrayList<>();
            while (rs.next()) {
                provinces.add(rs.getString("name"));
            }

            return provinces.toArray(new String[0]);
        } catch (SQLException e) {
            e.printStackTrace();
            return new String[0];  // Retorna un arreglo vacío en caso de error
        }
    }

    /**
     * Carga los departamentos de una provincia específica.
     * @param provinceId ID de la provincia.
     * @return Lista de nombres de los departamentos.
     */
    public List<String> loadDepartments(int provinceId) {
        String query = "SELECT name_departament FROM departament WHERE id_province = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, provinceId);  // Usamos el ID de la provincia seleccionada
    
            ResultSet rs = stmt.executeQuery();
            List<String> departments = new ArrayList<>();
            while (rs.next()) {
                departments.add(rs.getString("name_departament"));
            }
    
            return departments;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();  // Retorna una lista vacía en caso de error
        }
    }
    

    /**
     * Carga las ciudades de un departamento específico.
     * @param departmentId ID del departamento.
     * @return Lista de nombres de las ciudades.
     */
    public List<String> loadCities(int departmentId) {
        String query = "SELECT name FROM city WHERE id_department = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, departmentId);  // Usamos el ID del departamento seleccionado
    
            ResultSet rs = stmt.executeQuery();
            List<String> cities = new ArrayList<>();
            while (rs.next()) {
                cities.add(rs.getString("name"));
            }
    
            return cities;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();  // Retorna una lista vacía en caso de error
        }
    }
    
 /**
     * Obtiene el ID de una provincia dado su nombre.
     * @param provinceName Nombre de la provincia.
     * @return ID de la provincia o -1 si no se encuentra.
     */
public int getProvinceId(String provinceName) {
        String query = "SELECT id_province FROM province WHERE name= ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, provinceName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_province");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Retorna -1 si no se encuentra la provincia
    }
    /**
     * Obtiene el ID de un departamento dado su nombre.
     * @param departmentName Nombre del departamento.
     * @return ID del departamento o -1 si no se encuentra.
     */
    public int getDepartmentId(String departmentName) {
        String query = "SELECT id_departament FROM departament WHERE name_departament = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, departmentName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_departament");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Retorna -1 si no se encuentra el departamento
    }

    /**
     * Obtiene el ID de una ciudad dado su nombre.
     * @param cityName Nombre de la ciudad.
     * @return ID de la ciudad o -1 si no se encuentra.
     */
    public int getCityId(String cityName) {
        String query = "SELECT id_city FROM city WHERE name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, cityName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_city");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Retorna -1 si no se encuentra la ciudad
    }


    /**
     * Guarda la dirección de un proveedor en la base de datos.
     * @param idProvider ID del proveedor.
     * @param provinceId ID de la provincia.
     * @param departmentId ID del departamento.
     * @param cityId ID de la ciudad.
     * @param street Calle.
     * @param number Número.
     * @param dpto Departamento.
     * @param floor Piso.
     * @return Objeto ResultDataBase con el resultado de la operación.
     */
    public ResultDataBase saveAddress(int idProvider, int provinceId, int departmentId, int cityId, String street, String number, String dpto, String floor) {
        String query = "INSERT INTO provider_address (id_provider, id_country,id_province, id_departament, id_city, street, number_str, dpto, floor_dpto, date_create) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, idProvider);
            stmt.setInt(2, 7);
            stmt.setInt(3, provinceId);
            stmt.setInt(4, departmentId);
            stmt.setInt(5, cityId);
            stmt.setString(6, street);
            stmt.setString(7, number);
            stmt.setString(8, dpto);
            stmt.setString(9, floor);
    
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                return new ResultDataBase(true, "Dirección guardada exitosamente.");
            } else {
                return new ResultDataBase(false, "No se pudo guardar la dirección.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResultDataBase(false, "Error al guardar la dirección: " + e.getMessage());
        }
    }

    public ResultDataBase saveCustomerAddress(int idCustomer, int provinceId, int departmentId, int cityId, String street, String number, String dpto, String floor) {
        String query = "INSERT INTO customer_address (id_customer, id_country,id_province, id_departament, id_city, street, number_str, dpto, floor_dpto, date_create) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, idCustomer);
            stmt.setInt(2, 7);
            stmt.setInt(3, provinceId);
            stmt.setInt(4, departmentId);
            stmt.setInt(5, cityId);
            stmt.setString(6, street);
            stmt.setString(7, number);
            stmt.setString(8, dpto);
            stmt.setString(9, floor);
    
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                return new ResultDataBase(true, "Dirección guardada exitosamente.");
            } else {
                return new ResultDataBase(false, "No se pudo guardar la dirección.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResultDataBase(false, "Error al guardar la dirección: " + e.getMessage());
        }
    }
    
}
