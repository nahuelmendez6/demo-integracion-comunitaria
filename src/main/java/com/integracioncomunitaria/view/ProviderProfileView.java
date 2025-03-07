package com.integracioncomunitaria.view;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import com.integracioncomunitaria.controller.ProviderController;

public class ProviderProfileView extends JFrame {
    
    private ProviderController providerController;

    public ProviderProfileView(int providerId) {
        this.providerController = new ProviderController();

        setTitle("Perfil del Proveedor");
        setSize(600, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        

        List<String[]> providerData = providerController.getProviderData(providerId);

        if (providerData.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontró información del proveedor", "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        String[] columnNames = {"Campo", "Información"};
        String[] data = providerData.get(0);

        String[][] tableData = {
            {"Nombre", data[1]},
            {"Apellido", data[2]},
            {"Email", data[3]},
            {"Categoría", data[4]},
            {"Profesión", data[5]}
        };

        JTable profileTable = new JTable(tableData, columnNames);
        profileTable.setEnabled(false); // Deshabilitar edición
        JScrollPane scrollPane = new JScrollPane(profileTable);
        
        JButton updateProfile = new JButton("Actualizar perfil");
        updateProfile.addActionListener(e -> updateProviderProfile(providerId));

        JButton closeButton = new JButton("Cerrar");
        closeButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        buttonPanel.add(updateProfile);

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        
    }

    private void updateProviderProfile(int id_provider) {
        new EditProviderProfileView(id_provider).setVisible(true);
    }

}
