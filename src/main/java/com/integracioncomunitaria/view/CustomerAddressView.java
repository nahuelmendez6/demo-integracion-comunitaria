package com.integracioncomunitaria.view;


import com.integracioncomunitaria.controller.CustomerController;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CustomerAddressView extends JFrame {
    private JTable addressTable;
    private int customerId;
    private CustomerController customerController;
    
    public CustomerAddressView(int customerId) {
        this.customerId = customerId;
        this.customerController = new CustomerController();

        setTitle("Dirección del Cliente");
        setSize(800, 300);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] columnNames = {"ID Cliente", "Nombre", "Provincia", "Departamento", "Ciudad", "Calle", "Número", "Dpto", "Piso"};
        List<String[]> customerAddresses = customerController.getCustomerAddress(customerId);
        String[][] data = customerAddresses.toArray(new String[0][]);

        addressTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(addressTable);
        add(scrollPane, BorderLayout.CENTER);

        JButton editButton = new JButton("Editar Dirección");
        editButton.addActionListener(e -> openEditAddressForm());
        add(editButton, BorderLayout.SOUTH);
    }

    private void openEditAddressForm() {
        new EditCustomerAddressView(customerId).setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CustomerAddressView(1).setVisible(true));
    }
}
