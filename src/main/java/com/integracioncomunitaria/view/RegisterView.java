package com.integracioncomunitaria.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterView extends JFrame {
    private JTextField txtUsername, txtEmail;
    private JPasswordField txtPassword;
    private JButton btnRegister, btnBack;

    public RegisterView() {
        setTitle("Integración Comunitaria - Registro");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la ventana
        setLayout(new GridBagLayout()); // Usar GridBagLayout para mayor control

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Espaciado entre componentes
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campo Nombre de Usuario
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblUsername = new JLabel("Nombre de Usuario:");
        lblUsername.setFont(new Font("Arial", Font.PLAIN, 14));
        add(lblUsername, gbc);

        gbc.gridy = 1;
        txtUsername = new JTextField(20); // Tamaño reducido
        txtUsername.setBorder(BorderFactory.createTitledBorder("Nombre de Usuario"));
        add(txtUsername, gbc);

        // Campo Contraseña
        gbc.gridy = 2;
        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        add(lblPassword, gbc);

        gbc.gridy = 3;
        txtPassword = new JPasswordField(20); // Tamaño reducido
        txtPassword.setBorder(BorderFactory.createTitledBorder("Contraseña"));
        add(txtPassword, gbc);

        // Campo Correo Electrónico
        gbc.gridy = 4;
        JLabel lblEmail = new JLabel("Correo Electrónico:");
        lblEmail.setFont(new Font("Arial", Font.PLAIN, 14));
        add(lblEmail, gbc);

        gbc.gridy = 5;
        txtEmail = new JTextField(20); // Tamaño reducido
        txtEmail.setBorder(BorderFactory.createTitledBorder("Correo Electrónico"));
        add(txtEmail, gbc);

        // Botón Registrar
        gbc.gridy = 6;
        btnRegister = new JButton("Registrar");
        btnRegister.setFont(new Font("Arial", Font.PLAIN, 16));
        btnRegister.setBackground(new Color(72, 201, 176)); // Color verde
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFocusPainted(false);
        btnRegister.setPreferredSize(new Dimension(150, 30)); // Tamaño reducido
        add(btnRegister, gbc);

        // Botón Volver
        gbc.gridy = 7;
        btnBack = new JButton("Volver");
        btnBack.setFont(new Font("Arial", Font.PLAIN, 16));
        btnBack.setBackground(new Color(231, 76, 60)); // Color rojo
        btnBack.setForeground(Color.WHITE);
        btnBack.setFocusPainted(false);
        btnBack.setPreferredSize(new Dimension(150, 30)); // Tamaño reducido
        add(btnBack, gbc);

        // Acción para registrar
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Aquí iría la lógica para registrar el usuario
                JOptionPane.showMessageDialog(null, "Usuario registrado con éxito");
                new MainView().setVisible(true);
                dispose();
            }
        });

        // Acción para volver
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MainView().setVisible(true);
                dispose();
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegisterView().setVisible(true));
    }
}