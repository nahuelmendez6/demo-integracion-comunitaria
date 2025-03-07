package com.integracioncomunitaria.view;

import com.integracioncomunitaria.controller.ProviderController;
import com.integracioncomunitaria.controller.OfferController;
import com.integracioncomunitaria.controller.PortfolioController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class ProviderView extends JFrame {
    private int customerId;
    private ProviderController providerController;
    private OfferController offerController;
    private PortfolioController portfolioController;

    public ProviderView(int customerId) {
        this.customerId = customerId;
        this.providerController = new ProviderController();
        this.offerController = new OfferController();
        this.portfolioController = new PortfolioController();

        setTitle("Proveedores Disponibles");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        List<String[]> providers = providerController.getFilteredProviders(customerId);
        if (providers.isEmpty()) {
            panel.add(new JLabel("No se encontraron proveedores disponibles."));
        } else {
            for (String[] provider : providers) {
                panel.add(createProviderPanel(provider));
            }
        }

        add(new JScrollPane(panel), BorderLayout.CENTER);
    }

    private JPanel createProviderPanel(String[] provider) {
        JPanel providerPanel = new JPanel();
        providerPanel.setLayout(new BoxLayout(providerPanel, BoxLayout.Y_AXIS));
        providerPanel.setBorder(BorderFactory.createTitledBorder(provider[1]));
        providerPanel.setBackground(Color.LIGHT_GRAY);
        providerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Tabla con datos del proveedor
        String[] columnNames = {"Nombre", "Categoría", "Profesión"};
        Object[][] data = {{provider[1], provider[2], provider[3]}};
        JTable providerTable = new JTable(new DefaultTableModel(data, columnNames));
        providerTable.setRowHeight(20);
        providerTable.setEnabled(false);
        providerPanel.add(new JScrollPane(providerTable));

        // Panel para los botones con GridLayout compacto
        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 3, 3));
        JButton offersButton = new JButton("Ver Ofertas");
        JButton portfolioButton = new JButton("Ver Portfolio");
        JButton contactButton = new JButton("Contactar");
        JButton gradeButton = new JButton("Ver Calificación");
        JButton availabilityButton = new JButton("Disponibilidad");
        JButton inventoryButton = new JButton("Ver Inventario");

        int providerId = Integer.parseInt(provider[0]);

        offersButton.addActionListener(e -> showOffers(providerId));
        portfolioButton.addActionListener(e -> showPortfolio(providerId));
        contactButton.addActionListener(e -> contactProvider(providerId));
        gradeButton.addActionListener(e -> showGradeProvider(providerId));
        availabilityButton.addActionListener(e -> showAvailability(providerId));
        inventoryButton.addActionListener(e -> showInventory(providerId));

        buttonPanel.add(offersButton);
        buttonPanel.add(portfolioButton);
        buttonPanel.add(contactButton);
        buttonPanel.add(gradeButton);
        buttonPanel.add(availabilityButton);
        buttonPanel.add(inventoryButton);
        providerPanel.add(buttonPanel);

        return providerPanel;
    }

    private void showOffers(int providerId) {
        List<Map<String, Object>> offers = offerController.getOffersByProvider(providerId);
        if (offers.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay ofertas disponibles.", "Ofertas", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        StringBuilder offersText = new StringBuilder("Ofertas disponibles:\n\n");
        for (Map<String, Object> offer : offers) {
            offersText.append("🔹 ").append(offer.get("name"))
                      .append("\n   Tipo: ").append(offer.get("type_offer"))
                      .append("\n   Apertura: ").append(offer.get("date_open"))
                      .append("\n   Cierre: ").append(offer.get("date_close"))
                      .append("\n   Descripción: ").append(offer.get("description"))
                      .append("\n\n");
        }
        JOptionPane.showMessageDialog(this, offersText.toString(), "Ofertas del Proveedor", JOptionPane.INFORMATION_MESSAGE);
    }


    private void showPortfolio(int providerId) {
        List<Map<String, Object>> portfolios = portfolioController.getPortfoliosByProvider(providerId);

        if (portfolios.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay portfolios disponibles para este proveedor.", "Portfolio del Proveedor", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Crear ventana para mostrar los portfolios
        JDialog portfolioDialog = new JDialog(this, "Portfolio del Proveedor", true);
        portfolioDialog.setSize(700, 500);
        portfolioDialog.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        for (Map<String, Object> portfolio : portfolios) {
            JPanel portfolioPanel = new JPanel(new BorderLayout());
            portfolioPanel.setBorder(BorderFactory.createTitledBorder("📂 " + portfolio.get("name")));

            JTextArea descriptionArea = new JTextArea("Fecha de creación: " + portfolio.get("date_create") + "\n" +
                                                    "Descripción: " + portfolio.get("description"));
            descriptionArea.setEditable(false);
            portfolioPanel.add(descriptionArea, BorderLayout.NORTH);

            List<Map<String, Object>> attachments = portfolioController.getAttachmentsByPortfolio((int) portfolio.get("id_portfolio"));

            if (!attachments.isEmpty()) {
                String[] columnNames = {"Nombre del Archivo", "Ruta", "Abrir"};
                Object[][] data = new Object[attachments.size()][3];

                for (int i = 0; i < attachments.size(); i++) {
                    data[i][0] = attachments.get(i).get("name");
                    data[i][1] = attachments.get(i).get("path");
                    data[i][2] = "Abrir";
                }

                DefaultTableModel model = new DefaultTableModel(data, columnNames) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return column == 2; // Solo permitir interacción en la columna "Abrir"
                    }
                };

                JTable attachmentTable = new JTable(model);
                attachmentTable.getColumn("Abrir").setCellRenderer(new ButtonRenderer());
                attachmentTable.getColumn("Abrir").setCellEditor(new ImageViewerButtonEditor(new JCheckBox(), attachments));

                portfolioPanel.add(new JScrollPane(attachmentTable), BorderLayout.CENTER);
            } else {
                portfolioPanel.add(new JLabel("📎 No hay archivos adjuntos."), BorderLayout.CENTER);
            }

            mainPanel.add(portfolioPanel);
    }

    portfolioDialog.add(new JScrollPane(mainPanel), BorderLayout.CENTER);
    portfolioDialog.setVisible(true);
}

    private void contactProvider(int providerId) {
        new ContactProviderDialog(this, providerId, customerId).setVisible(true);
    }

    private void showGradeProvider(int providerId) {
        new ProviderRatingDialog(this, providerId).setVisible(true);
    }

    private void showAvailability(int providerId) {
        new ProviderAvailabilityView(this, providerId).setVisible(true);
    }

    private void showInventory(int providerId) {
        new ProviderInventoryView(providerId).setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProviderView(1).setVisible(true));
    }
}
