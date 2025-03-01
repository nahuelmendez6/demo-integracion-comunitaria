package com.integracioncomunitaria.view;

import com.integracioncomunitaria.controller.ProviderController;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ProviderView extends JFrame {
    private int customerId;
    private ProviderController controller;

    public ProviderView(int customerId) {
        this.customerId = customerId;
        this.controller = new ProviderController();

        setTitle("Proveedores Disponibles");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        List<String[]> providers = controller.getFilteredProviders(customerId);
        if (providers.isEmpty()) {
            panel.add(new JLabel("No se encontraron proveedores disponibles."));
        } else {
            for (String[] provider : providers) {
                JPanel providerPanel = new JPanel(new GridLayout(3, 2, 5, 5));
                providerPanel.setBorder(BorderFactory.createTitledBorder(provider[1])); // Nombre del proveedor
                providerPanel.setBackground(Color.LIGHT_GRAY);
                providerPanel.setOpaque(true);
                providerPanel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder(provider[1]),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));

                JLabel categoryLabel = new JLabel("Categoría: " + provider[2]); // Nombre de la categoría
                JLabel professionLabel = new JLabel("Profesión: " + provider[3]); // Nombre de la profesión
                JLabel offerLabel = new JLabel("Oferta: " + provider[4]); // Oferta del proveedor

                JButton portfolioButton = new JButton("Ver Portfolio");
                portfolioButton.addActionListener(e -> openPortfolio(provider[0]));

                JButton contactButton = new JButton("Contactar");
                contactButton.addActionListener(e -> contactProvider(provider[0]));

                providerPanel.add(categoryLabel);
                providerPanel.add(professionLabel);
                providerPanel.add(offerLabel);
                providerPanel.add(portfolioButton);
                providerPanel.add(contactButton);

                panel.add(providerPanel);
            }
        }

        add(new JScrollPane(panel), BorderLayout.CENTER);
    }

    private void openPortfolio(String providerId) {
        JOptionPane.showMessageDialog(this, "Mostrando portfolio del proveedor " + providerId);
    }

    private void contactProvider(String providerId) {
        JOptionPane.showMessageDialog(this, "Contactando al proveedor " + providerId);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProviderView(1).setVisible(true)); // Prueba con id_customer = 1
    }
}
