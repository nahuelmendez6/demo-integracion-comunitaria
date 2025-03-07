package com.integracioncomunitaria.controller;

import com.integracioncomunitaria.database.DatabaseConnection;
import com.integracioncomunitaria.database.ResultDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ZoneController {


    public List<String> loadCitiesByProvince(String province) {
        List<String> cities = new ArrayList<>();
        String sql = "SELECT name FROM city WHERE province = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, province);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                cities.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cities;
    }

    public int createZone(int providerId) {
        String sql = "INSERT INTO provider_zone (id_provider) VALUES (?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, providerId);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getCityIdByName(String cityName) {
        String sql = "SELECT id_city FROM city WHERE name = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, cityName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_city");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public ResultDataBase saveZoneCities(int idZone, List<Integer> cityIds) {
        ResultDataBase result = new ResultDataBase();
        String sql = "INSERT INTO zone_city (id_zone, id_city) VALUES (?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            for (int cityId : cityIds) {
                ps.setInt(1, idZone);
                ps.setInt(2, cityId);
                ps.addBatch();
            }
            ps.executeBatch();
            result.setSuccess(true);
            result.setMessage("Zona guardada correctamente.");
        } catch (SQLException e) {
            result.setSuccess(false);
            result.setMessage("Error al guardar la zona: " + e.getMessage());
        }
        return result;
    }


    public Integer getZones(int providerId) {
        int id_zone;
        String query = "SELECT id_zone FROM provider_zone WHERE id_provider = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                id_zone =rs.getInt("id_zone");
                return id_zone;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List<String> getCitiesByZone(int idZone) {
        List<String> cities = new ArrayList<>();
        String query = """
            SELECT ci.name FROM city ci
            JOIN zone_city zc ON ci.id_city = zc.id_city
            WHERE zc.id_zone = ?
            ORDER BY ci.name
        """;
    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
    
            stmt.setInt(1, idZone);
            ResultSet rs = stmt.executeQuery();
    
            while (rs.next()) {
                cities.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cities;
    }
    
}