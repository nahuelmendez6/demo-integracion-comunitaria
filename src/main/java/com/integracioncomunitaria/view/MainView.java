package com.integracioncomunitaria.view;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainView extends JFrame {
    private JButton btnRegisterProvider; // Botón para registrarse como proveedor
    private JButton btnRegisterClient;  // Botón para registrarse como cliente
    private JButton btnLogin;           // Botón para iniciar sesión

    public MainView() {
        // Configuración principal de la ventana
        setTitle("Integración Comunitaria"); // Título de la ventana
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Abrir la ventana en pantalla completa
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Cerrar la aplicación al salir
        setLocationRelativeTo(null); // Centrar la ventana en la pantalla
        setLayout(new BorderLayout()); // Usamos BorderLayout para organizar los componentes

        // Panel de bienvenida
        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // Alineación centrada
        JLabel welcomeLabel = new JLabel("Bienvenido a Integración Comunitaria");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 30)); // Estilo de la fuente
        welcomeLabel.setForeground(new Color(45, 45, 45)); // Color del texto
        welcomePanel.add(welcomeLabel); // Añadimos el texto al panel de bienvenida

        add(welcomePanel, BorderLayout.NORTH); // Añadimos el panel en la parte superior (NORTH)

        // Panel de botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 20)); // Tres filas, una para cada botón, con espacio vertical
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100)); // Márgenes alrededor del panel

        // Botón de registro como proveedor
        btnRegisterProvider = new JButton("Registrarse como Proveedor");
        btnRegisterProvider.setFont(new Font("Arial", Font.PLAIN, 16)); // Tamaño y estilo de la fuente
        btnRegisterProvider.setBackground(new Color(72, 201, 176)); // Color de fondo del botón
        btnRegisterProvider.setForeground(Color.WHITE); // Color del texto
        btnRegisterProvider.setFocusPainted(false); // Quitar el borde de selección
        btnRegisterProvider.setPreferredSize(new Dimension(200, 50)); // Tamaño preferido del botón
        buttonPanel.add(btnRegisterProvider); // Añadimos el botón al panel

        // Botón de registro como cliente
        btnRegisterClient = new JButton("Registrarse como Cliente");
        btnRegisterClient.setFont(new Font("Arial", Font.PLAIN, 16)); // Tamaño y estilo de la fuente
        btnRegisterClient.setBackground(new Color(93, 173, 226)); // Color de fondo del botón
        btnRegisterClient.setForeground(Color.WHITE); // Color del texto
        btnRegisterClient.setFocusPainted(false); // Quitar el borde de selección
        btnRegisterClient.setPreferredSize(new Dimension(200, 50)); // Tamaño preferido del botón
        buttonPanel.add(btnRegisterClient); // Añadimos el botón al panel

        // Botón para iniciar sesión
        btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setFont(new Font("Arial", Font.PLAIN, 16)); // Tamaño y estilo de la fuente
        btnLogin.setBackground(new Color(72, 144, 226)); // Color de fondo del botón
        btnLogin.setForeground(Color.WHITE); // Color del texto
        btnLogin.setFocusPainted(false); // Quitar el borde de selección
        btnLogin.setPreferredSize(new Dimension(200, 50)); // Tamaño preferido del botón
        buttonPanel.add(btnLogin); // Añadimos el botón al panel

        add(buttonPanel, BorderLayout.CENTER); // Añadimos el panel en el centro de la ventana

        // Acción para registrarse como proveedor
        btnRegisterProvider.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Abre la vista de registro como proveedor (puedes cambiar la clase si es diferente)
                new RegisterProviderView().setVisible(true);
                dispose(); // Cierra la ventana actual
            }
        });

        //Acción para registrarse como cliente
        btnRegisterClient.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Abre la vista de registro como cliente (puedes cambiar la clase si es diferente)
                new RegisterView().setVisible(true);
                dispose(); // Cierra la ventana actual
            }
        });

        // Acción para iniciar sesión
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Abre la vista de inicio de sesión
                new LoginView().setVisible(true);
                dispose(); // Cierra la ventana actual
            }
        });
    }

    public static void main(String[] args) {
        // Inicia la aplicación en el hilo de eventos de Swing
        SwingUtilities.invokeLater(() -> new MainView().setVisible(true));
    }
}

// Clase auxiliar para los bordes redondeados de los botones
class RoundBorder implements Border {
    private int radius; // Radio de los bordes redondeados

    public RoundBorder(int radius) {
        this.radius = radius;
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(5, 5, 5, 5); // Espaciado entre el borde y el contenido
    }

    @Override
    public boolean isBorderOpaque() {
        return true; // El borde es opaco
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        g.setColor(c.getBackground()); // El color del borde coincide con el fondo del componente
        g.fillRoundRect(x, y, width - 1, height - 1, radius, radius); // Dibuja un rectángulo redondeado
    }
}
