package com.integracioncomunitaria.view;
import com.integracioncomunitaria.model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainCustomerMenuView extends JFrame {
    
    private int customerId;
    
    public MainCustomerMenuView(int customerId) {
        this.customerId = customerId;
        setTitle("Menú Principal");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel de bienvenida
        JPanel welcomePanel = new JPanel();
        welcomePanel.setBackground(new Color(72, 201, 176)); // Color de fondo
        JLabel lblWelcome = new JLabel("Bienvenido al Menú Principal", SwingConstants.CENTER);
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 24));
        lblWelcome.setForeground(Color.WHITE); // Texto en blanco
        welcomePanel.add(lblWelcome);
        add(welcomePanel, BorderLayout.NORTH);

        // Panel de botones de funciones
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 2, 10, 10)); // 3 filas, 2 columnas
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Márgenes

        // Botón: Configurar Perfil
        JButton btnConfigureProfile = createMenuButton("Configurar Perfil", new Color(46, 204, 113));
        btnConfigureProfile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Acceso a Configurar Perfil");
                // Aquí puedes redirigir a la pantalla de Configuración de Perfil
            }
        });
        buttonPanel.add(btnConfigureProfile);

        // Botón: Gestionar Dirección
        JButton btnManageAddress = createMenuButton("Gestionar Dirección", new Color(155, 89, 182));
        btnManageAddress.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Acceso a Gestionar Dirección");
                // Aquí puedes redirigir a la pantalla de Gestión de Dirección
            }
        });
        buttonPanel.add(btnManageAddress);

        // Botón: Publicar Ofertas
        JButton btnPublishOffers = createMenuButton("Publicar Petición", new Color(241, 196, 15));
        btnPublishOffers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //new OfferView(providerId).setVisible(true);
            }
        });
        buttonPanel.add(btnPublishOffers);

        // Botón: Ver Ofertas
        JButton btnViewOffers = createMenuButton("Ver Ofertas", new Color(230, 126, 34));
        btnViewOffers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ProviderView(customerId).setVisible(true);;
            }
        });
        buttonPanel.add(btnViewOffers);

        // Botón: Ver Peticiones
        JButton btnViewRequests = createMenuButton("Filtrar ofertas", new Color(231, 76, 60));
        btnViewRequests.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Acceso a Ver Ofertas");
                // Aquí puedes redirigir a la pantalla de Ver Peticiones
            }
        });
        buttonPanel.add(btnViewRequests);

        // Agregar el panel de botones al centro
        add(buttonPanel, BorderLayout.CENTER);

        // Panel inferior (opcional: botón de salir o cerrar sesión)
        JPanel bottomPanel = new JPanel();
        JButton btnLogout = new JButton("Cerrar Sesión");
        btnLogout.setBackground(new Color(231, 76, 60));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Sesión cerrada");
                dispose(); // Cierra la ventana actual
               // new LoginView().setVisible(true); // Redirige a la pantalla de login
            }
        });
        bottomPanel.add(btnLogout);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // Método para crear botones con un estilo uniforme
    private JButton createMenuButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(150, 50)); // Tamaño preferido
        return button;
    }

    /*
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainMenuView().setVisible(true);
        });
    }
     */
}