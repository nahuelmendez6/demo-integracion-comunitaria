package com.integracioncomunitaria.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class PortfolioView extends JFrame {
    private JTable portfolioTable;
    private DefaultTableModel tableModel;
    private JButton btnCreatePortfolio;
    private JButton btnViewAttachments;

    // Datos hardcodeados de portfolios
    private List<Portfolio> portfolios = new ArrayList<>();

    public PortfolioView() {
        setTitle("Gestión de Portfolios");
        setSize(1000, 600); // Aumentamos el tamaño para la tabla
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cierra solo esta ventana
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Hardcodeo de portfolios de ejemplo
        portfolios.add(new Portfolio(1, "Portfolio 1", "Descripción del Portfolio 1", "2023-10-01"));
        portfolios.add(new Portfolio(2, "Portfolio 2", "Descripción del Portfolio 2", "2023-10-02"));

        // Crear la tabla
        String[] columnNames = {"Nombre", "Descripción", "Fecha de Creación", "Archivos Adjuntos"};
        tableModel = new DefaultTableModel(columnNames, 0);
        portfolioTable = new JTable(tableModel);
        portfolioTable.setRowHeight(30); // Ajustar altura de las filas
        JScrollPane scrollPane = new JScrollPane(portfolioTable);
        add(scrollPane, BorderLayout.CENTER);

        // Actualizar la tabla con los datos hardcodeados
        updatePortfolioTable();

        // Panel de botones
        JPanel buttonPanel = new JPanel();
        btnCreatePortfolio = new JButton("Crear Portfolio");
        btnCreatePortfolio.setBackground(new Color(72, 201, 176));
        btnCreatePortfolio.setForeground(Color.WHITE);
        btnCreatePortfolio.setFocusPainted(false);
        btnCreatePortfolio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showCreatePortfolioDialog();
            }
        });

        btnViewAttachments = new JButton("Ver Archivos Adjuntos");
        btnViewAttachments.setBackground(new Color(52, 152, 219));
        btnViewAttachments.setForeground(Color.WHITE);
        btnViewAttachments.setFocusPainted(false);
        btnViewAttachments.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewAttachments();
            }
        });

        buttonPanel.add(btnCreatePortfolio);
        buttonPanel.add(btnViewAttachments);

        // Agregar el panel de botones al sur
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Método para actualizar la tabla de portfolios
    private void updatePortfolioTable() {
        tableModel.setRowCount(0); // Limpiar la tabla
        for (Portfolio portfolio : portfolios) {
            Object[] rowData = {
                    portfolio.getName(),
                    portfolio.getDescription(),
                    portfolio.getDateCreate(),
                    "Ver Adjuntos" // Botón para ver archivos adjuntos
            };
            tableModel.addRow(rowData);
        }
    }

    // Método para mostrar el diálogo de creación de portfolio
    private void showCreatePortfolioDialog() {
        JTextField txtName = new JTextField();
        JTextArea txtDescription = new JTextArea(5, 20);
        JScrollPane scrollPane = new JScrollPane(txtDescription);

        // Campo para adjuntar archivos
        JButton btnAttachFile = new JButton("Adjuntar Archivo");
        btnAttachFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                    JOptionPane.showMessageDialog(null, "Archivo seleccionado: " + filePath);
                    // Aquí puedes guardar la ruta del archivo o procesarlo
                }
            }
        });

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Nombre del Portfolio:"));
        panel.add(txtName);
        panel.add(new JLabel("Descripción:"));
        panel.add(scrollPane);
        panel.add(new JLabel("Adjuntar Archivo:"));
        panel.add(btnAttachFile);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Crear Nuevo Portfolio",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String name = txtName.getText().trim();
            String description = txtDescription.getText().trim();

            if (!name.isEmpty()) {
                // Hardcodeo del nuevo portfolio
                int newId = portfolios.size() + 1; // Simula un ID autoincremental
                portfolios.add(new Portfolio(newId, name, description, "2023-10-03")); // Fecha hardcodeada
                updatePortfolioTable(); // Actualiza la tabla
                JOptionPane.showMessageDialog(this, "Portfolio creado con éxito.");
            } else {
                JOptionPane.showMessageDialog(this, "El nombre del portfolio es obligatorio.");
            }
        }
    }

    // Método para ver los archivos adjuntos
    private void viewAttachments() {
        int selectedRow = portfolioTable.getSelectedRow();
        if (selectedRow != -1) {
            Portfolio selectedPortfolio = portfolios.get(selectedRow);
            JOptionPane.showMessageDialog(
                    this,
                    "Archivos adjuntos para: " + selectedPortfolio.getName(),
                    "Archivos Adjuntos",
                    JOptionPane.INFORMATION_MESSAGE
            );
            // Aquí puedes implementar la lógica para mostrar los archivos adjuntos
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona un portfolio para ver sus archivos adjuntos.");
        }
    }

    // Clase interna para representar un portfolio
    private class Portfolio {
        private int id;
        private String name;
        private String description;
        private String dateCreate;

        public Portfolio(int id, String name, String description, String dateCreate) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.dateCreate = dateCreate;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public String getDateCreate() {
            return dateCreate;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PortfolioView().setVisible(true);
        });
    }
}