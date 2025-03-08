package com.integracioncomunitaria.controller;

import com.integracioncomunitaria.database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OfferController {


    
    /**
     * Obtiene los tipos de oferta desde la base de datos.
     * @return Una lista de mapas con los tipos de oferta disponibles, cada mapa contiene el ID y el nombre de cada tipo de oferta.
     */
    public List<Map<String, Object>> getOfferTypes() {
        List<Map<String, Object>> offerTypes = new ArrayList<>();
        String query = "SELECT id_type_offer, nombre FROM type_offer";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> typeOffer = new HashMap<>();
                typeOffer.put("id_type_offer", rs.getInt("id_type_offer"));
                typeOffer.put("nombre", rs.getString("nombre"));
                offerTypes.add(typeOffer);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return offerTypes;
    }

        /**
     * Obtiene las ofertas asociadas a un proveedor específico.
     * @param providerId El ID del proveedor cuya ofertas se desean obtener.
     * @return Una lista de mapas con las ofertas del proveedor, cada mapa contiene los detalles de la oferta.
     */
    public List<Map<String, Object>> getOffersByProvider(int providerId) {
        List<Map<String, Object>> offers = new ArrayList<>();
        String query = "SELECT o.id_offer, o.id_type_offer, t.nombre AS type_offer, o.name, o.date_open, o.date_close, o.description " +
                       "FROM offer o " +
                       "INNER JOIN type_offer t ON o.id_type_offer = t.id_type_offer " +
                       "WHERE o.id_user_create = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, providerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> offer = new HashMap<>();
                offer.put("id_offer", rs.getInt("id_offer"));
                offer.put("id_type_offer", rs.getInt("id_type_offer"));
                offer.put("type_offer", rs.getString("type_offer"));
                offer.put("name", rs.getString("name"));
                offer.put("date_open", rs.getTimestamp("date_open"));
                offer.put("date_close", rs.getTimestamp("date_close"));
                offer.put("description", rs.getString("description"));
                offers.add(offer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return offers;
    }

    
    /**
     * Crea una nueva oferta y la guarda en la base de datos.
     * @param idTypeOffer El ID del tipo de oferta.
     * @param name El nombre de la oferta.
     * @param dateOpen La fecha de apertura de la oferta en formato de cadena.
     * @param dateClose La fecha de cierre de la oferta en formato de cadena.
     * @param description La descripción de la oferta.
     * @param userId El ID del usuario que crea la oferta.
     * @return True si la oferta fue creada con éxito, false en caso contrario.
     */
    public boolean createOffer(int idTypeOffer, String name, String dateOpen, String dateClose, String description, int userId) {
        String query = "INSERT INTO offer (id_type_offer, name, date_open, date_close, description, id_user_create, id_user_update, date_create, date_update) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idTypeOffer);
            stmt.setString(2, name);
            stmt.setTimestamp(3, Timestamp.valueOf(dateOpen));
            stmt.setTimestamp(4, Timestamp.valueOf(dateClose));
            stmt.setString(5, description);
            stmt.setInt(6, userId);
            stmt.setInt(7, userId);

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

        /**
     * Elimina una oferta de la base de datos.
     * @param offerId El ID de la oferta que se desea eliminar.
     * @return True si la oferta fue eliminada con éxito, false en caso contrario.
     */
    public boolean deleteOffer(int offerId) {
        String query = "DELETE FROM offer WHERE id_offer = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, offerId);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

        /**
     * Desactiva una oferta actualizando la fecha de cierre.
     * @param offerId El ID de la oferta que se desea desactivar.
     * @return True si la oferta fue desactivada con éxito, false en caso contrario.
     */
    public boolean deactivateOffer(int offerId) {
        String query = "UPDATE offer SET date_close = NOW() WHERE id_offer = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, offerId);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
