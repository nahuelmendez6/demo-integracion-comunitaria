package com.integracioncomunitaria.view;

import com.integracioncomunitaria.controller.AuthController;
import com.integracioncomunitaria.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginView extends JFrame {
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private AuthController authController;

    public LoginView() {
        authController = new AuthController();

        setTitle("Inicio de Sesión");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // Centra la ventana en la pantalla
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);  // Espaciado entre componentes
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campo Email
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        txtEmail = new JTextField(20);
        txtEmail.setBorder(BorderFactory.createTitledBorder("Correo Electrónico"));
        add(txtEmail, gbc);

        // Campo Contraseña
        gbc.gridy = 1;
        txtPassword = new JPasswordField(20);
        txtPassword.setBorder(BorderFactory.createTitledBorder("Contraseña"));
        add(txtPassword, gbc);

        // Botón Iniciar Sesión
        gbc.gridy = 2;
        btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setBackground(new Color(72, 201, 176));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        add(btnLogin, gbc);

        // Acción al presionar el botón de Login
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });

        pack();  // Ajusta el tamaño de la ventana según los componentes
        setLocationRelativeTo(null);  // Asegura que la ventana esté centrada al abrirse
    }

    private void login() {
        String email = txtEmail.getText();
        String password = new String(txtPassword.getPassword());

        User user = authController.login(email, password);

        if (user != null) {
            JOptionPane.showMessageDialog(this, "Bienvenido " + user.getName());

            if (authController.userIsProvider(user.getIdUser())) {
                openMainMenu(authController.getProviderId(user.getIdUser()));
            } else if (authController.userIsCustomer(user.getIdUser())) {
                openMainCustomerView(authController.getCustomerId(user.getIdUser()));
            } else {
                JOptionPane.showMessageDialog(this, "No tienes un rol asignado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openMainMenu(int providerId) {
        this.dispose(); // Cierra la ventana de login
        new MainMenuView(providerId).setVisible(true);
    }

    private void openMainCustomerView(int customerId) {
        this.dispose();
        new MainCustomerMenuView(customerId).setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginView().setVisible(true));
    }
}
