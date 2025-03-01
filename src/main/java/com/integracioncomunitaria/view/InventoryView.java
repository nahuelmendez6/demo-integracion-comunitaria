package com.integracioncomunitaria.view;

import com.integracioncomunitaria.controller.InventoryController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class InventoryView extends JFrame {
    private JTable inventoryTable;
    private DefaultTableModel tableModel;
    private JButton btnAddItem;
    private InventoryController inventoryController;
    private int providerId;

    public InventoryView(int providerId) {
        this.providerId = providerId;
        inventoryController = new InventoryController();

        setTitle("Gestión de Inventario");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Definir columnas de la tabla
        String[] columnNames = {"ID", "Artículo", "Cantidad", "Costo"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        inventoryTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(inventoryTable);
        add(scrollPane, BorderLayout.CENTER);

        updateInventoryTable();

        // Panel de botones
        JPanel buttonPanel = new JPanel();
        btnAddItem = new JButton("Agregar Artículo");
        btnAddItem.addActionListener(e -> showAddItemDialog());
        buttonPanel.add(btnAddItem);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Método para actualizar la tabla con el inventario
    private void updateInventoryTable() {
        tableModel.setRowCount(0);
        List<Map<String, Object>> inventory = inventoryController.getInventoryByProvider(providerId);
        for (Map<String, Object> item : inventory) {
            Object[] rowData = {
                    item.get("id_inventory"),
                    item.get("article_name"),
                    item.get("quantity"),
                    item.get("cost")
            };
            tableModel.addRow(rowData);
        }
    }

    // Método para mostrar el diálogo de agregar artículo
    private void showAddItemDialog() {
        JTextField articleIdField = new JTextField();
        JTextField quantityField = new JTextField();
        JTextField costField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("ID del Artículo:"));
        panel.add(articleIdField);
        panel.add(new JLabel("Cantidad:"));
        panel.add(quantityField);
        panel.add(new JLabel("Costo:"));
        panel.add(costField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Agregar Nuevo Artículo", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int articleId = Integer.parseInt(articleIdField.getText());
                int quantity = Integer.parseInt(quantityField.getText());
                double cost = Double.parseDouble(costField.getText());

                // Aquí se asume que el ID del usuario es el mismo que el del proveedor por simplicidad
                boolean success = inventoryController.addInventoryItem(providerId, articleId, quantity, cost, providerId);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Artículo agregado exitosamente.");
                    updateInventoryTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al agregar el artículo.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Ingrese valores válidos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
