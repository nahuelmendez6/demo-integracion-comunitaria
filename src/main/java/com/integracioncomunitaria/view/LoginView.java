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
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 1));

        JPanel panelEmail = new JPanel();
        panelEmail.add(new JLabel("Email:"));
        txtEmail = new JTextField(20);
        panelEmail.add(txtEmail);

        JPanel panelPassword = new JPanel();
        panelPassword.add(new JLabel("Contraseña:"));
        txtPassword = new JPasswordField(20);
        panelPassword.add(txtPassword);

        btnLogin = new JButton("Iniciar Sesión");
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });

        add(panelEmail);
        add(panelPassword);
        add(btnLogin);
    }

    private void login() {
        String email = txtEmail.getText();
        String password = new String(txtPassword.getPassword());

        User user = authController.login(email, password);

        if (user != null) {
            JOptionPane.showMessageDialog(this, "Bienvenido " + user.getName());

            if (authController.userIsProvider(user.getIdUser())) {
                openMainMenu(authController.getProviderId(user.getIdUser()));
            //} else if (user.isCustomer()) {
            //    openMainCustomerView(user.getCustomerId());
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

    //private void openMainCustomerView(int customerId) {
    //    this.dispose();
    //    new MainCustomerView(customerId).setVisible(true);
    //}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginView().setVisible(true));
    }
}
