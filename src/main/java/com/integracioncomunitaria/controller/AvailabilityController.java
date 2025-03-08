package com.integracioncomunitaria.controller;

import com.integracioncomunitaria.database.DatabaseConnection;
import java.sql.*;
import java.util.*;

public class AvailabilityController {

/**
     * Obtiene la lista de días de la semana desde la tabla `week`.
     * 
     * @return Lista de mapas con la información de los días de la semana (id y nombre).
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
     * Obtiene la lista de horas disponibles en un día específico.
     *
     * @param idWeek Identificador del día de la semana.
     * @return Lista de mapas con la información de las horas disponibles (id y nombre).
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
     * Guarda la disponibilidad de un proveedor en la base de datos.
     *
     * @param providerId   Identificador del proveedor.
     * @param idWeek       Día de la semana.
     * @param idFromHour   Hora de inicio de disponibilidad.
     * @param idUntilHour  Hora de fin de disponibilidad.
     * @param userCreateId Identificador del usuario que registra la disponibilidad.
     * @return `true` si el registro fue exitoso, `false` en caso de error.
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

        /**
     * Obtiene la disponibilidad horaria de un proveedor.
     *
     * @param providerId Identificador del proveedor.
     * @return Cadena de texto con la disponibilidad del proveedor por día.
     */
    public String getProviderAvailability(int providerId) {
        String query = """
            SELECT w.name AS day, h.name AS from_hour, h2.name AS until_hour
            FROM availability a
            JOIN week w ON a.id_week = w.id_week
            JOIN hour h ON a.id_from_hour = h.id_hour
            JOIN hour h2 ON a.id_until_hour = h2.id_hour
            WHERE a.id_provider = ?
            ORDER BY w.id_week, h.id_hour
        """;
        
        StringBuilder result = new StringBuilder("Disponibilidad:\n");
        Map<String, StringBuilder> schedule = new HashMap<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, providerId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                String day = rs.getString("day");
                String fromHour = rs.getString("from_hour");
                String untilHour = rs.getString("until_hour");
                
                schedule.putIfAbsent(day, new StringBuilder());
                schedule.get(day).append(fromHour).append(" - ").append(untilHour).append("\n");
            }
            
            schedule.forEach((day, hours) -> {
                result.append(day).append(":\n").append(hours).append("\n");
            });
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error al obtener la disponibilidad.";
        }
        
        return result.toString();
    }

}