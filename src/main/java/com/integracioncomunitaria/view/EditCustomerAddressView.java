package com.integracioncomunitaria.view;

import com.integracioncomunitaria.controller.CustomerController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditCustomerAddressView extends JFrame {
    private CustomerController customerController;
    private JTextField streetField, numberField, dptoField, floorField;
    private JComboBox<String> provinceCombo, departamentCombo, cityCombo;
    private int customerId;

    public EditCustomerAddressView(int customerId) {
        this.customerController = new CustomerController();
        this.customerId = customerId;
        setTitle("Editar Dirección del Cliente");
        setSize(400, 300);
        setLayout(new GridLayout(6, 2, 10, 10));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        add(new JLabel("Provincia:"));
        provinceCombo = new JComboBox<>(new String[]{"Provincia 1", "Provincia 2"});  // Cargar desde la BD
        add(provinceCombo);

        add(new JLabel("Departamento:"));
        departamentCombo = new JComboBox<>(new String[]{"Departamento 1", "Departamento 2"});  // Cargar desde la BD
        add(departamentCombo);

        add(new JLabel("Ciudad:"));
        cityCombo = new JComboBox<>(new String[]{"Ciudad 1", "Ciudad 2"});  // Cargar desde la BD
        add(cityCombo);

        add(new JLabel("Calle:"));
        streetField = new JTextField();
        add(streetField);

        add(new JLabel("Número:"));
        numberField = new JTextField();
        add(numberField);

        add(new JLabel("Dpto:"));
        dptoField = new JTextField();
        add(dptoField);

        add(new JLabel("Piso:"));
        floorField = new JTextField();
        add(floorField);

        JButton saveButton = new JButton("Guardar");
        saveButton.addActionListener(e -> saveAddress());
        add(saveButton);
    }

    private void saveAddress() {
        String province = (String) provinceCombo.getSelectedItem();
        String departament = (String) departamentCombo.getSelectedItem();
        String city = (String) cityCombo.getSelectedItem();
        String street = streetField.getText();
        String number = numberField.getText();
        String dpto = dptoField.getText();
        String floor = floorField.getText();

        boolean success = customerController.updateCustomerAddress(customerId, province, departament, city, street, number, dpto, floor);
        
        if (success) {
            JOptionPane.showMessageDialog(this, "Dirección actualizada correctamente");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error al actualizar la dirección", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
