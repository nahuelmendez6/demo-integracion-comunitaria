package com.integracioncomunitaria.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.integracioncomunitaria.controller.InventoryController;

public class AddProductView extends JFrame {
    private JTextField txtArticleName, txtQuantity, txtCost;
    private JComboBox<String> cbCategory;
    private InventoryController controller;
    private int providerId;
    private ProviderInventoryView parentView;

    public AddProductView(int providerId, ProviderInventoryView parentView) {
        this.providerId = providerId;
        this.parentView = parentView;
        this.controller = new InventoryController();

        setTitle("Agregar Producto");
        setSize(350, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(5, 2));

        add(new JLabel("Nombre del artículo:"));
        txtArticleName = new JTextField();
        add(txtArticleName);

        add(new JLabel("Categoría:"));
        cbCategory = new JComboBox<>(new String[]{"Electrónica", "Ropa", "Alimentos"}); // Se pueden cargar dinámicamente
        add(cbCategory);

        add(new JLabel("Cantidad:"));
        txtQuantity = new JTextField();
        add(txtQuantity);

        add(new JLabel("Costo:"));
        txtCost = new JTextField();
        add(txtCost);

        JButton btnSave = new JButton("Guardar");
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveProduct();
            }
        });
        add(btnSave);

        JButton btnCancel = new JButton("Cancelar");
        btnCancel.addActionListener(e -> dispose());
        add(btnCancel);
    }

    private void saveProduct() {
        String articleName = txtArticleName.getText();
        int categoryId = cbCategory.getSelectedIndex() + 1; // Simulación de IDs de categorías
        int quantity = Integer.parseInt(txtQuantity.getText());
        double cost = Double.parseDouble(txtCost.getText());

        boolean success = controller.addProductToInventory(providerId, articleName, categoryId, quantity, cost, providerId);
        if (success) {
            JOptionPane.showMessageDialog(this, "Producto agregado correctamente.");
            parentView.loadInventory();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error al agregar el producto.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
