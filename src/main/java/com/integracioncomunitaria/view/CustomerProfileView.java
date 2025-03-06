package com.integracioncomunitaria.view;

import javax.swing.*;
import java.awt.*;
import com.integracioncomunitaria.controller.CustomerProfileController;

public class CustomerProfileView extends JFrame {
    private JTextField txtName, txtLastName, txtEmail, txtStreet, txtNumber, txtDpto, txtFloor;
    private JComboBox<String> cbCountry, cbProvince, cbDepartment, cbCity;
    private JList<String> categoryList;
    private DefaultListModel<String> categoryListModel;
    private JButton btnSave;
    private int customerId;
    private CustomerProfileController controller;
    
    public CustomerProfileView(int customerId) {
        this.customerId = customerId;
        this.controller = new CustomerProfileController(this);
        
        setTitle("Perfil del Cliente");
        setSize(500, 600);
        setLayout(new GridLayout(10, 2));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Campos de usuario
        txtName = new JTextField();
        txtLastName = new JTextField();
        txtEmail = new JTextField();
        
        // Campos de dirección
        cbCountry = new JComboBox<>();
        cbProvince = new JComboBox<>();
        cbDepartment = new JComboBox<>();
        cbCity = new JComboBox<>();
        txtStreet = new JTextField();
        txtNumber = new JTextField();
        txtDpto = new JTextField();
        txtFloor = new JTextField();
        
        // Categorías de interés
        categoryListModel = new DefaultListModel<>();
        categoryList = new JList<>(categoryListModel);
        
        btnSave = new JButton("Guardar Cambios");
        btnSave.addActionListener(e -> controller.saveProfile());
        
        // Agregar componentes a la ventana
        add(new JLabel("Nombre:"));
        add(txtName);
        add(new JLabel("Apellido:"));
        add(txtLastName);
        add(new JLabel("Email:"));
        add(txtEmail);
        add(new JLabel("País:"));
        add(cbCountry);
        add(new JLabel("Provincia:"));
        add(cbProvince);
        add(new JLabel("Departamento:"));
        add(cbDepartment);
        add(new JLabel("Ciudad:"));
        add(cbCity);
        add(new JLabel("Calle:"));
        add(txtStreet);
        add(new JLabel("Número:"));
        add(txtNumber);
        add(new JLabel("Dpto:"));
        add(txtDpto);
        add(new JLabel("Piso:"));
        add(txtFloor);
        add(new JLabel("Categorías de Interés:"));
        add(new JScrollPane(categoryList));
        add(btnSave);
        
        // Cargar los datos del perfil
        controller.loadProfileData(customerId);
    }
    
    public JTextField getTxtName() { return txtName; }
    public JTextField getTxtLastName() { return txtLastName; }
    public JTextField getTxtEmail() { return txtEmail; }
    public JTextField getTxtStreet() { return txtStreet; }
    public JTextField getTxtNumber() { return txtNumber; }
    public JTextField getTxtDpto() { return txtDpto; }
    public JTextField getTxtFloor() { return txtFloor; }
    public JComboBox<String> getCbCountry() { return cbCountry; }
    public JComboBox<String> getCbProvince() { return cbProvince; }
    public JComboBox<String> getCbDepartment() { return cbDepartment; }
    public JComboBox<String> getCbCity() { return cbCity; }
    public DefaultListModel<String> getCategoryListModel() { return categoryListModel; }
}
