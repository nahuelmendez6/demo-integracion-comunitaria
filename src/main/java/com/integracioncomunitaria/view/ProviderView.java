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
        JPanel providerPanel = new JPanel(new BorderLayout());
        providerPanel.setBorder(BorderFactory.createTitledBorder(provider[1])); // Nombre del proveedor
        providerPanel.setBackground(Color.LIGHT_GRAY);
        providerPanel.setOpaque(true);
        providerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Tabla con dos filas: (1) Datos del proveedor, (2) Botones
        String[] columnNames = {"Nombre", "Categoría", "Profesión"};
        Object[][] data = {
            {provider[1], provider[2], provider[3]},  // Primera fila: datos
            
        };

        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
        JTable providerTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // No permitir edición en las celdas
            }
        };
        providerTable.setRowHeight(30);
        providerPanel.add(new JScrollPane(providerTable), BorderLayout.NORTH);

        // Panel para los botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton offersButton = new JButton("Ver Ofertas");
        JButton portfolioButton = new JButton("Ver Portfolio");
        JButton contactButton = new JButton("Contactar");
        JButton gradeButton = new JButton("Ver calificación");
        JButton availavilityButton = new JButton("Disponibilidad");

        int providerId = Integer.parseInt(provider[0]);

        offersButton.addActionListener(e -> showOffers(providerId));
        portfolioButton.addActionListener(e -> showPortfolio(providerId));
        contactButton.addActionListener(e -> contactProvider(providerId));
        gradeButton.addActionListener(e -> showGradeProvider(providerId));
        availavilityButton.addActionListener(e -> showAvailability(providerId));

        buttonPanel.add(offersButton);
        buttonPanel.add(portfolioButton);
        buttonPanel.add(contactButton);
        buttonPanel.add(gradeButton);
        buttonPanel.add(availavilityButton);
        providerPanel.add(buttonPanel, BorderLayout.CENTER);

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProviderView(1).setVisible(true));
    }
}
