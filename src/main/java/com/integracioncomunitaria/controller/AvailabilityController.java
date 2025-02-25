package com.integracioncomunitaria.controller;

import com.integracioncomunitaria.database.DatabaseConnection;
import java.sql.*;
import java.util.*;

public class AvailabilityController {

    /**
     * Obtiene la lista de días de la tabla `week`
     */
    public List<Map<String, Object>> getWeekDays() {
        List<Map<String, Object>> days = new ArrayList<>();
        String sql = "SELECT id_week, name FROM week";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Map<String, Object> day = new HashMap<>();
                day.put("id_week", rs.getInt("id_week"));
                day.put("name", rs.getString("name"));
                days.add(day);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return days;
    }

    /**
     * Obtiene la lista de horas de la tabla `hour` según un `id_week` dado.
     */
    public List<Map<String, Object>> getHoursByDay(int idWeek) {
        List<Map<String, Object>> hours = new ArrayList<>();
        String sql = "SELECT id_hour, name FROM hour WHERE id_week = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idWeek);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> hour = new HashMap<>();
                    hour.put("id_hour", rs.getInt("id_hour"));
                    hour.put("name", rs.getString("name"));
                    hours.add(hour);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hours;
    }

    /**
     * Guarda la disponibilidad del proveedor en la tabla `availability`.
     */
    public boolean saveAvailability(int providerId, int idWeek, int idFromHour, int idUntilHour, int userCreateId) {
        String sql = "INSERT INTO availability (id_provider, id_week, id_from_hour, id_until_hour, id_user_create, date_create) " +
                     "VALUES (?, ?, ?, ?, ?, NOW())";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, providerId);
            stmt.setInt(2, idWeek);
            stmt.setInt(3, idFromHour);
            stmt.setInt(4, idUntilHour);
            stmt.setInt(5, userCreateId);
            
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
