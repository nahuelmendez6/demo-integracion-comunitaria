package com.integracioncomunitaria.controller;

import com.integracioncomunitaria.database.DatabaseConnection;
import java.sql.*;
import java.util.*;

public class NotificationController {
    
     // Método para enviar una notificación a un cliente o proveedor.
    public boolean sendNotification(int providerId, int customerId, String type, String message) {
        String query = "INSERT INTO notification (id_provider, id_customer, type, message, viewed, id_user_create) VALUES (?, ?, ?, ?, 0, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            // Se establece el proveedor y el cliente según el tipo de notificación.
            stmt.setInt(1, providerId);
            stmt.setInt(2, customerId);
            stmt.setString(3, type);
            stmt.setString(4, message);
            if (type.equals("customer")) {
                 // Si es un cliente, el ID del usuario que creó la notificación es el del cliente
                stmt.setInt(5, customerId);
            } else if (type.equals("provider")) {
                // Si es un proveedor, el ID del usuario que creó la notificación es el del proveedor
                stmt.setInt(5, providerId);
            }
            
            // Ejecuta la inserción de la notificación en la base de datos
            stmt.executeUpdate();

            return true;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

     // Método para enviar una respuesta a una notificación existente.
    public void sendReply(int providerId, int notificationId, String message) {
        String query = "INSERT INTO notification (id_provider, id_customer, type, message, viewed, id_user_create)" + 
                        "SELECT id_provider, id_customer, 'provider', ?, 0, ? FROM notification WHERE id_notification = ?";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
                
                // Se establece el mensaje de la respuesta y el ID del proveedor que responde
                stmt.setString(1, message);
                stmt.setString(1, message);
                stmt.setInt(2, providerId);
                stmt.setInt(3, notificationId);
                stmt.executeUpdate();
            } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para obtener todas las notificaciones no leídas de un usuario específico.
    public List<Map<String, Object>> getNotifications(int userId, String type) {
        List<Map<String, Object>> notifications = new ArrayList<>();
        String query = "SELECT * FROM notification WHERE (id_provider = ? OR id_customer = ?) AND type = ? AND viewed = 0 ORDER BY date_create DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            // Se establece el ID del usuario y el tipo de notificación
            stmt.setInt(1, userId);
            stmt.setInt(2, userId);
            stmt.setString(3, type);
            ResultSet rs = stmt.executeQuery();
            
            // Se agrega cada notificación a la lista
            while (rs.next()) {
                Map<String, Object> notification = new HashMap<>();
                notification.put("id_notification", rs.getInt("id_notification"));
                notification.put("message", rs.getString("message"));
                notification.put("date_create", rs.getTimestamp("date_create"));
                notification.put("id_user_create", rs.getString("id_user_create"));
                notification.put("type", rs.getString("type"));
                notifications.add(notification);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications;
    }
    
    public void markAsRead(int notificationId) {
        String query = "UPDATE notification SET viewed = 1 WHERE id_notification = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, notificationId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Map<String, Object>> getNotificationsByprovider(int providerId) {
        List<Map<String, Object>> notifications = new ArrayList<>();
        String query = "SELECT id_notification, message FROM notification WHERE id_provider = ? ORDER BY date_create DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, providerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> notification = new HashMap<>();
                notification.put("id_notification", rs.getInt("id_notification"));
                notification.put("message", rs.getString("message"));
                notifications.add(notification);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications;
    }

    public static int getUnreadNotificationsCount(int providerId) {
        String query = "SELECT COUNT(*) FROM notification WHERE id_provider = ? AND viewed = 0";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, providerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void markAllAsRead(int providerId) {
        String query = "UPDATE notification SET viewed = 1 WHERE id_provider = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, providerId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
