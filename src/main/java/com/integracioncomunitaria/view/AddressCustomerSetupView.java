package com.integracioncomunitaria.view;

import com.integracioncomunitaria.controller.AddressController;
import com.integracioncomunitaria.database.ResultDataBase;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AddressCustomerSetupView extends JFrame {
    private JComboBox<String> cmbProvince;
    private JComboBox<String> cmbDepartment;
    private JComboBox<String> cmbCity;
    private JTextField txtStreet;
    private JTextField txtNumber;
    private JTextField txtDpto;
    private JTextField txtFloor;
    private JButton btnSave;
    
    private int idCustomer;
    private AddressController controller;

    public AddressCustomerSetupView(int idCustomer) {
        this.idCustomer = idCustomer;
        controller = new AddressController();

        setTitle("Configuración de Dirección");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Etiqueta y selector de provincia
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Provincia:"), gbc);

        gbc.gridx = 1;
        cmbProvince = new JComboBox<>(controller.loadProvinces());
        add(cmbProvince, gbc);

        // Evento para filtrar departamentos según provincia
        cmbProvince.addActionListener(e -> loadDepartments());

        // Etiqueta y selector de departamento
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Departamento:"), gbc);

        gbc.gridx = 1;
        cmbDepartment = new JComboBox<>();
        add(cmbDepartment, gbc);

        // Evento para filtrar ciudades según departamento
        cmbDepartment.addActionListener(e -> loadCities());

        // Etiqueta y selector de ciudad
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Ciudad:"), gbc);

        gbc.gridx = 1;
        cmbCity = new JComboBox<>();
        add(cmbCity, gbc);

        // Campos de dirección
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Calle:"), gbc);
        gbc.gridx = 1;
        txtStreet = new JTextField(20);
        add(txtStreet, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel("Número:"), gbc);
        gbc.gridx = 1;
        txtNumber = new JTextField(10);
        add(txtNumber, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        add(new JLabel("Departamento:"), gbc);
        gbc.gridx = 1;
        txtDpto = new JTextField(10);
        add(txtDpto, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        add(new JLabel("Piso:"), gbc);
        gbc.gridx = 1;
        txtFloor = new JTextField(10);
        add(txtFloor, gbc);

        // Botón Guardar
        gbc.gridx = 1;
        gbc.gridy = 7;
        btnSave = new JButton("Guardar Dirección");
        btnSave.setBackground(new Color(72, 201, 176));
        btnSave.setForeground(Color.WHITE);
        add(btnSave, gbc);

        // Acción del botón
        btnSave.addActionListener(e -> saveAddress());

        setVisible(true);
    }

    private void loadDepartments() {
        String selectedProvince = (String) cmbProvince.getSelectedItem();
        if (selectedProvince != null) {
            int provinceId = controller.getProvinceId(selectedProvince);
            cmbDepartment.removeAllItems();
            List<String> departments = controller.loadDepartments(provinceId);
            for (String department : departments) {
                cmbDepartment.addItem(department);
            }
            cmbDepartment.revalidate();
            cmbDepartment.repaint();
        }
    }

    private void loadCities() {
        String selectedDepartment = (String) cmbDepartment.getSelectedItem();
        if (selectedDepartment != null) {
            int departmentId = controller.getDepartmentId(selectedDepartment);
            cmbCity.removeAllItems();
            List<String> cities = controller.loadCities(departmentId);
            for (String city : cities) {
                cmbCity.addItem(city);
            }
            cmbCity.revalidate();
            cmbCity.repaint();
        }
    }

    private void saveAddress() {
        String province = (String) cmbProvince.getSelectedItem();
        String department = (String) cmbDepartment.getSelectedItem();
        String city = (String) cmbCity.getSelectedItem();
        String street = txtStreet.getText().trim();
        String number = txtNumber.getText().trim();
        String dpto = txtDpto.getText().trim();
        String floor = txtFloor.getText().trim();
    
        if (street.isEmpty() || province == null || department == null || city == null) {
            JOptionPane.showMessageDialog(this, "Todos los campos obligatorios deben estar completos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        int provinceId = controller.getProvinceId(province);
        int departmentId = controller.getDepartmentId(department);
        int cityId = controller.getCityId(city);
    
        ResultDataBase result = controller.saveCustomerAddress(idCustomer, provinceId, departmentId, cityId, street, number, dpto, floor);
    
        JOptionPane.showMessageDialog(this, result.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
    
        if (result.getSuccess()) {
            SwingUtilities.invokeLater(() -> {
                new InterestView(idCustomer).setVisible(true);
                dispose(); // Cierra la ventana actual
            });
        }
    }
    
}
