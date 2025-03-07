package com.integracioncomunitaria.view;


import com.integracioncomunitaria.controller.ProviderController;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ProviderAddressView extends JFrame {
    private JTable addressTable;
    private int providerId;
    private ProviderController providerController;
    
    public ProviderAddressView(int providerId) {
        this.providerId = providerId;
        this.providerController = new ProviderController();

        setTitle("Dirección del Proveedor");
        setSize(800, 300);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] columnNames = {"ID Proveedor", "Nombre", "Provincia", "Departamento", "Ciudad", "Calle", "Número", "Dpto", "Piso"};
        List<String[]> providerAddresses = providerController.getProviderAddress(providerId);
        System.out.println(providerAddresses);
        String[][] data = providerAddresses.toArray(new String[0][]);

        addressTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(addressTable);
        add(scrollPane, BorderLayout.CENTER);

        JButton editButton = new JButton("Editar Dirección");
        editButton.addActionListener(e -> openEditAddressForm());
        add(editButton, BorderLayout.SOUTH);
    }

    private void openEditAddressForm() {
        new EditCustomerAddressView(providerId).setVisible(true);
    }

    
}
