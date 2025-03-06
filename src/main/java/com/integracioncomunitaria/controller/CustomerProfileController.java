package com.integracioncomunitaria.controller;

import com.integracioncomunitaria.database.DatabaseConnection;
import com.integracioncomunitaria.view.CustomerProfileView;
import java.sql.*;

import javax.swing.*;

public class CustomerProfileController {
    private CustomerProfileView view;
    
    public CustomerProfileController(CustomerProfileView view) {
        this.view = view;
    }
    
    public void loadProfileData(int customerId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Obtener información del usuario
            String query = "SELECT u.name, u.last_name, u.email, ca.id_country, ca.id_province, ca.id_departament, ca.id_city, ca.street, ca.number_str, ca.dpto, ca.floor_dpto " +
                           "FROM user u " +
                           "JOIN customer c ON u.id_user = c.id_user " +
                           "LEFT JOIN customer_address ca ON c.id_customer = ca.id_customer " +
                           "WHERE c.id_customer = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                view.getTxtName().setText(rs.getString("name"));
                view.getTxtLastName().setText(rs.getString("last_name"));
                view.getTxtEmail().setText(rs.getString("email"));
                view.getTxtStreet().setText(rs.getString("street"));
                view.getTxtNumber().setText(rs.getString("number_str"));
                view.getTxtDpto().setText(rs.getString("dpto"));
                view.getTxtFloor().setText(rs.getString("floor_dpto"));
                
                // Cargar selecciones de ubicación
                loadComboBox(view.getCbCountry(), "country", rs.getInt("id_country"));
                loadComboBox(view.getCbProvince(), "province", rs.getInt("id_province"));
                loadComboBox(view.getCbDepartment(), "departament", rs.getInt("id_departament"));
                loadComboBox(view.getCbCity(), "city", rs.getInt("id_city"));
            }
            
            // Obtener categorías de interés
            String categoryQuery = "SELECT c.name FROM interest i " +
                                   "JOIN category c ON i.id_category = c.id_category " +
                                   "WHERE i.id_customer = ?";
            PreparedStatement categoryStmt = conn.prepareStatement(categoryQuery);
            categoryStmt.setInt(1, customerId);
            ResultSet categoryRs = categoryStmt.executeQuery();
            
            view.getCategoryListModel().clear();
            while (categoryRs.next()) {
                view.getCategoryListModel().addElement(categoryRs.getString("category_name"));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void loadComboBox(JComboBox<String> comboBox, String tableName, int selectedId) throws SQLException {
        comboBox.removeAllItems();
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "";
            if (tableName.equalsIgnoreCase("departament")) {
                query = "SELECT id_" + tableName + ", name_departament FROM " + tableName; 
                Statement stmtd = conn.createStatement();
                ResultSet rsd = stmtd.executeQuery(query);
                while (rsd.next()) {
                    comboBox.addItem(rsd.getString("name_departament"));  
                }  
            }
            query = "SELECT id_" + tableName + ", name FROM " + tableName;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                comboBox.addItem(rs.getString("name"));
            }
        }
    }
    
    public void saveProfile() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "UPDATE user u " +
                           "JOIN customer c ON u.id_user = c.id_user " +
                           "SET u.name = ?, u.last_name = ?, u.email = ? " +
                           "WHERE c.id_customer = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, view.getTxtName().getText());
            stmt.setString(2, view.getTxtLastName().getText());
            stmt.setString(3, view.getTxtEmail().getText());
            stmt.setInt(4, Integer.parseInt(view.getTxtName().getText())); // Asegúrate de usar el ID correcto
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(view, "Perfil actualizado correctamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
