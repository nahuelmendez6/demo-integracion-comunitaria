package com.integracioncomunitaria.view;

import javax.swing.*;

import com.integracioncomunitaria.controller.UserProfileController;

import com.integracioncomunitaria.database.ResultDataBase;
import com.integracioncomunitaria.controller.UserController;
import com.integracioncomunitaria.controller.CustomerController;
import com.integracioncomunitaria.controller.AuthController;
import com.integracioncomunitaria.model.User;
import com.integracioncomunitaria.model.Customer;
import java.util.regex.Pattern;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterCustomerView extends JFrame {
    private JTextField txtName;
    private JTextField txtLastName;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private JButton btnRegister;
    private JButton btnBack;

    public RegisterCustomerView() {
        setTitle("Registro de Cliente");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campo Nombre
        gbc.gridx = 0;
        gbc.gridy = 0;
        txtName = new JTextField(20);
        txtName.setBorder(BorderFactory.createTitledBorder("Nombre del Cliente"));
        add(txtName, gbc);

        // Campo Apellido
        gbc.gridy = 1;
        txtLastName = new JTextField(20);
        txtLastName.setBorder(BorderFactory.createTitledBorder("Apellido del Cliente"));
        add(txtLastName, gbc);

        // Campo Correo Electrónico
        gbc.gridy = 2;
        txtEmail = new JTextField(20);
        txtEmail.setBorder(BorderFactory.createTitledBorder("Correo Electrónico"));
        add(txtEmail, gbc);

        // Campo Contraseña
        gbc.gridy = 3;
        txtPassword = new JPasswordField(20);
        txtPassword.setBorder(BorderFactory.createTitledBorder("Contraseña"));
        add(txtPassword, gbc);

        // Campo Confirmar Contraseña
        gbc.gridy = 4;
        txtConfirmPassword = new JPasswordField(20);
        txtConfirmPassword.setBorder(BorderFactory.createTitledBorder("Confirmar Contraseña"));
        add(txtConfirmPassword, gbc);

        // Botón Registrar
        gbc.gridy = 5;
        btnRegister = new JButton("Registrar");
        btnRegister.setBackground(new Color(72, 201, 176));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFocusPainted(false);
        add(btnRegister, gbc);

        // Botón Volver
        gbc.gridy = 6;
        btnBack = new JButton("Volver");
        btnBack.setBackground(new Color(231, 76, 60));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFocusPainted(false);
        add(btnBack, gbc);

        btnRegister.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        String name = txtName.getText().trim();
        String lastName = txtLastName.getText().trim();
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());
        String confirmPassword = new String(txtConfirmPassword.getPassword());

        if (name.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios.");
            return;
        }

        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(null, "Correo electrónico inválido.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(null, "Las contraseñas no coinciden.");
            return;
        }

        User user = new User(name, lastName, email, password);
        UserController userController = new UserController();
        ResultDataBase userResult = userController.registerUser(user, "cliente");

        if (userResult.getSuccess()) {
            int userId = (int) userResult.getObject();

            // Registrar el proveedor
            Customer customer = new Customer(name, userId);
            CustomerController customerController = new CustomerController();
            ResultDataBase customerResult = customerController.registerCustomer(customer);

            if (customerResult.getSuccess()) {
                // Registrar el perfil del usuario
                UserProfileController profileController = new UserProfileController();
                ResultDataBase profileResult = profileController.registerUserProfile(userId, email, "cliente");

                if (profileResult.getSuccess()) {
                    AuthController authController = new AuthController();
                    int idCustomer = authController.getCustomerId(userId);
                    JOptionPane.showMessageDialog(null, "Cliente registrado correctamente.");
                    new AddressCustomerSetupView(idCustomer).setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, profileResult.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(null, customerResult.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, userResult.getMessage());
        }
    }
});

        btnBack.addActionListener(e -> {
            new MainView().setVisible(true);
            dispose();
        });
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        return Pattern.matches(emailRegex, email);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new RegisterCustomerView().setVisible(true);
        });
    }
}