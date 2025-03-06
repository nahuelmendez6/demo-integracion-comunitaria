package com.integracioncomunitaria.view;

import com.integracioncomunitaria.controller.InventoryController;
import com.integracioncomunitaria.model.Inventory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

class CustomerInventoryView extends JFrame {
    private InventoryController controller;
    private JTable inventoryTable;
    private DefaultTableModel tableModel;
    private int providerId;

    public CustomerInventoryView(int providerId) {
        this.providerId = providerId;
        this.controller = new InventoryController();
        setTitle("Inventario del Proveedor");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel panel = new JPanel(new BorderLayout());
        
        tableModel = new DefaultTableModel(new String[]{"Artículo", "Cantidad", "Costo"}, 0);
        inventoryTable = new JTable(tableModel);
        
        loadInventory();
        
        panel.add(new JScrollPane(inventoryTable), BorderLayout.CENTER);
        add(panel);
    }

    public void loadInventory() {
        tableModel.setRowCount(0);
        List<Inventory> inventory = controller.getInventoryByProvider(providerId);
    
        if (inventory.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Este proveedor no tiene inventario.", "Inventario vacío", JOptionPane.INFORMATION_MESSAGE);
        } else {
            for (Inventory item : inventory) {
                tableModel.addRow(new Object[]{
                    item.getIdInventory(), 
                    item.getArticle(), 
                    item.getQuantity(), 
                    item.getCost()
                });
            }
        }
    }
}
