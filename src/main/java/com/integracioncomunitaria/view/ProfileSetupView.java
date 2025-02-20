package com.integracioncomunitaria.view;

import com.integracioncomunitaria.controller.ProfileController;
import com.integracioncomunitaria.database.ResultDataBase;

import javax.swing.*;
import java.awt.*;

public class ProfileSetupView extends JFrame {
    private int userId;
    private JComboBox<String> cmbCategory;
    private JComboBox<String> cmbProfession;
    private JComboBox<String> cmbTypeProvider;
    private JButton btnSave;
    private ProfileController controller;

    public ProfileSetupView(int userId) {
        this.userId = userId;
        controller = new ProfileController();

        setTitle("Configuración de Perfil - Proveedor");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campo Categoría
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Categoría:"), gbc);
        gbc.gridy = 1;
        cmbCategory = new JComboBox<>(controller.loadCategories());
        add(cmbCategory, gbc);

        // Campo Profesión
        gbc.gridy = 2;
        add(new JLabel("Profesión:"), gbc);
        gbc.gridy = 3;
        cmbProfession = new JComboBox<>(controller.loadProfessions());
        add(cmbProfession, gbc);

        // Campo Tipo de Proveedor
        gbc.gridy = 4;
        add(new JLabel("Tipo de Proveedor:"), gbc);
        gbc.gridy = 5;
        cmbTypeProvider = new JComboBox<>(controller.loadTypeProvider());
        add(cmbTypeProvider, gbc);

        // Botón Guardar
        gbc.gridy = 6;
        btnSave = new JButton("Guardar y Continuar");
        btnSave.setBackground(new Color(72, 201, 176));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);
        add(btnSave, gbc);

        // Acción del botón
        btnSave.addActionListener(e -> saveProfile());
    }

    private void saveProfile() {
        String category = (String) cmbCategory.getSelectedItem();
        String profession = (String) cmbProfession.getSelectedItem();
        String typeProvider = (String) cmbTypeProvider.getSelectedItem();

        // Validación
        if (category == null || profession == null || typeProvider == null) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Guardar perfil
        ResultDataBase result = controller.updateProfile(userId, category, profession, typeProvider, "Dirección Test");

        JOptionPane.showMessageDialog(this, result.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);

        if (result.getSuccess()) {
            // Obtener id_provider basado en userId
            int idProvider = controller.getProviderIdByUserId(userId);

            if (idProvider == -1) {
                JOptionPane.showMessageDialog(this, "No se pudo obtener el ID del proveedor.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Si el perfil se guardó correctamente, abrir la pantalla de dirección con el id_provider
            SwingUtilities.invokeLater(() -> {
                new AddressSetupView(idProvider).setVisible(true);
                dispose(); // Cierra la ventana actual
            });
        }
    }
}
