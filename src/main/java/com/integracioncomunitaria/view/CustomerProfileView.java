package com.integracioncomunitaria.view;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import com.integracioncomunitaria.controller.CustomerController;

public class CustomerProfileView extends JFrame {
    
    private CustomerController customerController;

    public CustomerProfileView(int customerId) {
        this.customerController = new CustomerController();

        setTitle("Perfil del Proveedor");
        setSize(600, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        

        List<String[]> customerData = customerController.getCustomerData(customerId);

        if (customerData.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontró información del cliente", "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        String[] columnNames = {"Campo", "Información"};
        String[] data = customerData.get(0);

        String[][] tableData = {
            {"Nombre", data[1]},
            {"Apellido", data[2]},
            {"Email", data[3]}
        };

        JTable profileTable = new JTable(tableData, columnNames);
        profileTable.setEnabled(false); // Deshabilitar edición
        JScrollPane scrollPane = new JScrollPane(profileTable);
        
        JButton updateProfile = new JButton("Actualizar perfil");
        updateProfile.addActionListener(e -> updateCustomerProfile(customerId));

        JButton closeButton = new JButton("Cerrar");
        closeButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        buttonPanel.add(updateProfile);

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);


    }

    private void updateCustomerProfile(int customerId) {
        new EditCustomerProfileView(customerId).setVisible(true);
    }

}
