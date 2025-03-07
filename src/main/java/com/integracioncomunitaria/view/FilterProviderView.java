package com.integracioncomunitaria.view;

import com.integracioncomunitaria.controller.ProviderController;
import com.integracioncomunitaria.controller.OfferController;
import com.integracioncomunitaria.controller.PortfolioController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Map;

public class FilterProviderView extends JFrame {
    private int customerId;
    private ProviderController providerController;
    private OfferController offerController;
    private PortfolioController portfolioController;
    private JComboBox<String> categoryComboBox;
    private JComboBox<String> professionComboBox;
    private JPanel providerPanel;

    public FilterProviderView(int customerId) {
        this.customerId = customerId;
        this.providerController = new ProviderController();
        this.offerController = new OfferController();
        this.portfolioController = new PortfolioController();

        setTitle("Proveedores Disponibles");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        categoryComboBox = new JComboBox<>(providerController.getCategories().toArray(new String[0]));
        professionComboBox = new JComboBox<>(providerController.getProfessions().toArray(new String[0]));
        JButton filterCategoryButton = new JButton("Filtrar por Categoría");
        JButton filterProfessionButton = new JButton("Filtrar por Profesión");

        filterPanel.add(new JLabel("Categoría:"));
        filterPanel.add(categoryComboBox);
        filterPanel.add(filterCategoryButton);
        filterPanel.add(new JLabel("Profesión:"));
        filterPanel.add(professionComboBox);
        filterPanel.add(filterProfessionButton);

        add(filterPanel, BorderLayout.NORTH);

        providerPanel = new JPanel();
        providerPanel.setLayout(new BoxLayout(providerPanel, BoxLayout.Y_AXIS));
        add(new JScrollPane(providerPanel), BorderLayout.CENTER);

        filterCategoryButton.addActionListener(e -> loadProvidersByCategory());
        filterProfessionButton.addActionListener(e -> loadProvidersByProfession());

        loadProviders();
    }

    private void loadProviders() {
        providerPanel.removeAll();
        List<String[]> providers = providerController.getAllProviders();
        displayProviders(providers);
    }

    private void loadProvidersByCategory() {
        providerPanel.removeAll();
        String selectedCategory = (String) categoryComboBox.getSelectedItem();
        List<String[]> providers = providerController.getProvidersByCategory(selectedCategory);
        displayProviders(providers);
    }

    private void loadProvidersByProfession() {
        providerPanel.removeAll();
        String selectedProfession = (String) professionComboBox.getSelectedItem();
        List<String[]> providers = providerController.getProvidersByProfession(selectedProfession);
        displayProviders(providers);
    }

    private void displayProviders(List<String[]> providers) {
        providerPanel.removeAll();
        providerPanel.setLayout(new GridLayout(0, 1, 5, 5));

        if (providers.isEmpty()) {
            providerPanel.add(new JLabel("No se encontraron proveedores disponibles."));
        } else {
            for (String[] provider : providers) {
                providerPanel.add(createProviderPanel(provider));
            }
        }
        providerPanel.revalidate();
        providerPanel.repaint();
    }

    private JPanel createProviderPanel(String[] provider) {
        JPanel providerPanel = new JPanel(new BorderLayout());
        providerPanel.setBorder(BorderFactory.createTitledBorder(provider[1]));
        providerPanel.setBackground(Color.LIGHT_GRAY);
        
        JPanel contentPanel = new JPanel(new BorderLayout());
        
        String[] columnNames = {"Nombre", "Categoría", "Profesión"};
        Object[][] data = {{provider[1], provider[2], provider[3]}};
        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
        JTable providerTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        providerTable.setRowHeight(25);
        providerTable.setPreferredScrollableViewportSize(new Dimension(300, 30));
        contentPanel.add(new JScrollPane(providerTable), BorderLayout.NORTH);
        
        JPanel buttonPanel = new JPanel(new GridLayout(1, 6, 5, 5));
        JButton offersButton = new JButton("Ver Ofertas");
        JButton portfolioButton = new JButton("Ver Portfolio");
        JButton contactButton = new JButton("Contactar");
        JButton gradeButton = new JButton("Ver calificación");
        JButton availavilityButton = new JButton("Disponibilidad");
        JButton providerInventory = new JButton("Ver inventario");

        int providerId = Integer.parseInt(provider[0]);
        offersButton.addActionListener(e -> showOffers(providerId));
        portfolioButton.addActionListener(e -> showPortfolio(providerId));
        contactButton.addActionListener(e -> contactProvider(providerId));
        gradeButton.addActionListener(e -> showGradeProvider(providerId));
        availavilityButton.addActionListener(e -> showAvailability(providerId));
        providerInventory.addActionListener(e -> showInventory(providerId));

        buttonPanel.add(offersButton);
        buttonPanel.add(portfolioButton);
        buttonPanel.add(contactButton);
        buttonPanel.add(gradeButton);
        buttonPanel.add(availavilityButton);
        buttonPanel.add(providerInventory);

        contentPanel.add(buttonPanel, BorderLayout.CENTER);
        providerPanel.add(contentPanel, BorderLayout.CENTER);
        
        return providerPanel;
    }

    private void showOffers(int providerId) {
        List<Map<String, Object>> offers = offerController.getOffersByProvider(providerId);
        if (offers.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay ofertas disponibles para este proveedor.", "Ofertas del Proveedor", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        StringBuilder offersText = new StringBuilder("Ofertas disponibles:\n\n");
        for (Map<String, Object> offer : offers) {
            offersText.append("🔹 ").append(offer.get("name")).append("\n")
                    .append("   Tipo: ").append(offer.get("type_offer")).append("\n")
                    .append("   Apertura: ").append(offer.get("date_open")).append("\n")
                    .append("   Cierre: ").append(offer.get("date_close")).append("\n")
                    .append("   Descripción: ").append(offer.get("description")).append("\n\n");
        }
        JOptionPane.showMessageDialog(this, offersText.toString(), "Ofertas del Proveedor", JOptionPane.INFORMATION_MESSAGE);
    }


    private void showPortfolio(int providerId) {
        List<Map<String, Object>> portfolios = portfolioController.getPortfoliosByProvider(providerId);
        if (portfolios.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay portfolios disponibles para este proveedor.", "Portfolio del Proveedor", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void contactProvider(int providerId) {
        ContactProviderDialog dialog = new ContactProviderDialog(this, providerId, customerId);
        dialog.setVisible(true);
    }

    private void showGradeProvider(int providerId) {
        ProviderRatingDialog dialog = new ProviderRatingDialog(this, providerId);
        dialog.setVisible(true);
    }

    private void showAvailability(int providerId) {
        ProviderAvailabilityView dialog = new ProviderAvailabilityView(this, providerId);
        dialog.setVisible(true);
    }

    private void showInventory(int providerId) {
        new ProviderInventoryView(providerId).setVisible(true);
    }
}
