package com.integracioncomunitaria.view;

import com.integracioncomunitaria.controller.PortfolioController;
import com.integracioncomunitaria.controller.ProfileController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.Map;

public class PortfolioView extends JFrame {
    private JTable portfolioTable;
    private DefaultTableModel tableModel;
    private JButton btnCreatePortfolio;
    private JButton btnAttachFile;
    private JButton btnViewAttachments;
    private PortfolioController portfolioController;
    private ProfileController profileController;
    private int providerId;

    public PortfolioView(int providerId) {
        this.providerId = providerId;
        this.portfolioController = new PortfolioController();
        this.profileController = new ProfileController();

        setTitle("Gestión de Portfolios");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Crear la tabla
        String[] columnNames = {"ID", "Nombre", "Descripción", "Fecha de Creación"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        portfolioTable = new JTable(tableModel);
        portfolioTable.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(portfolioTable);
        add(scrollPane, BorderLayout.CENTER);

        updatePortfolioTable();

        JPanel buttonPanel = new JPanel();
        btnCreatePortfolio = new JButton("Crear Portfolio");
        btnCreatePortfolio.addActionListener(e -> showCreatePortfolioDialog());

        btnAttachFile = new JButton("Adjuntar Archivo");
        btnAttachFile.addActionListener(e -> attachFileToPortfolio());

        btnViewAttachments = new JButton("Ver Adjuntos");
        btnViewAttachments.addActionListener(e -> viewAttachments());

        buttonPanel.add(btnCreatePortfolio);
        buttonPanel.add(btnAttachFile);
        buttonPanel.add(btnViewAttachments);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void updatePortfolioTable() {
        tableModel.setRowCount(0);
        List<Map<String, Object>> portfolios = portfolioController.getPortfoliosByProvider(providerId);
        for (Map<String, Object> portfolio : portfolios) {
            Object[] rowData = {
                    portfolio.get("id_portfolio"),
                    portfolio.get("name"),
                    portfolio.get("description"),
                    portfolio.get("date_create")
            };
            tableModel.addRow(rowData);
        }
    }

    private void showCreatePortfolioDialog() {
        JTextField nameField = new JTextField();
        JTextArea descriptionArea = new JTextArea(5, 20);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Nombre del Portfolio:"));
        panel.add(nameField);
        panel.add(new JLabel("Descripción:"));
        panel.add(scrollPane);

        int result = JOptionPane.showConfirmDialog(
                this, panel, "Crear Nuevo Portfolio", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String description = descriptionArea.getText().trim();

            if (!name.isEmpty()) {
                boolean success = portfolioController.createPortfolio(providerId,name, description);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Portfolio creado con éxito.");
                    updatePortfolioTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al crear el portfolio.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "El nombre del portfolio es obligatorio.");
            }
        }
    }

    private void attachFileToPortfolio() {
        int selectedRow = portfolioTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un portfolio para adjuntar un archivo.");
            return;
        }
    
        // Obtener el ID del portfolio seleccionado (ajusta según cómo guardes los datos en la tabla)
        int portfolioId = (int) tableModel.getValueAt(selectedRow, 0); 
    
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String fileName = selectedFile.getName();
            String filePath = selectedFile.getAbsolutePath();
        
            int userId = profileController.getUserByProviderId(providerId);

            boolean success = portfolioController.addAttachmentToPortfolio(portfolioId, fileName, filePath, userId);
            if (success) {
                JOptionPane.showMessageDialog(this, "Archivo adjuntado exitosamente.");
            } else {
                JOptionPane.showMessageDialog(this, "Error al adjuntar el archivo.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    

    private void viewAttachments() {

        int selectedRow = portfolioTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un portfolio para adjuntar un archivo.");
            return;
        }

        int portfolioId = (int) tableModel.getValueAt(selectedRow, 0); 

        List<Map<String, Object>> attachments = portfolioController.getAttachmentsByPortfolio(portfolioId);
    
        if (attachments.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay archivos adjuntos en este portfolio.");
            return;
        }
    
        // Crear ventana de visualización
        JDialog attachmentDialog = new JDialog(this, "Archivos Adjuntos", true);
        attachmentDialog.setSize(600, 400);
        attachmentDialog.setLocationRelativeTo(this);
        attachmentDialog.setLayout(new BorderLayout());
    
        // Lista de archivos adjuntos
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> fileList = new JList<>(listModel);
    
        // Panel para mostrar la imagen
        JLabel imageLabel = new JLabel("", SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(400, 300));
    
        for (Map<String, Object> attachment : attachments) {
            listModel.addElement(attachment.get("name").toString());
        }
    
        // Evento para mostrar imagen si es un archivo de imagen
        fileList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedIndex = fileList.getSelectedIndex();
                if (selectedIndex != -1) {
                    String filePath = attachments.get(selectedIndex).get("path").toString();
                    if (isImageFile(filePath)) {
                        ImageIcon icon = new ImageIcon(filePath);
                        Image image = icon.getImage().getScaledInstance(400, 300, Image.SCALE_SMOOTH);
                        imageLabel.setIcon(new ImageIcon(image));
                    } else {
                        imageLabel.setIcon(null);
                        JOptionPane.showMessageDialog(this, "No es una imagen: " + filePath);
                    }
                }
            }
        });
    
        // Agregar componentes al diálogo
        attachmentDialog.add(new JScrollPane(fileList), BorderLayout.WEST);
        attachmentDialog.add(imageLabel, BorderLayout.CENTER);
        attachmentDialog.setVisible(true);
    }
    
    // Método auxiliar para verificar si el archivo es una imagen
    private boolean isImageFile(String filePath) {
        String[] imageExtensions = {".jpg", ".jpeg", ".png", ".gif", ".bmp"};
        for (String ext : imageExtensions) {
            if (filePath.toLowerCase().endsWith(ext)) {
                return true;
            }
        }
        return false;
    }
    
    
}
