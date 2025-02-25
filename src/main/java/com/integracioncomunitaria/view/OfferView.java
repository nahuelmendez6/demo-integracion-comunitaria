package com.integracioncomunitaria.view;

import com.integracioncomunitaria.controller.OfferController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class OfferView extends JFrame {
    private JTable offerTable;
    private DefaultTableModel tableModel;
    private JButton btnCreateOffer;
    private JButton btnDeleteOffer;
    private JButton btnDeactivateOffer;
    private OfferController offerController;
    private int providerId;

    public OfferView(int providerId) {
        this.providerId = providerId;
        offerController = new OfferController();

        setTitle("Gestión de Ofertas");
        setSize(1200, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Crear la tabla
        String[] columnNames = {"ID", "Tipo de Oferta", "Nombre", "Fecha Apertura", "Fecha Cierre", "Descripción"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        offerTable = new JTable(tableModel);
        offerTable.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(offerTable);
        add(scrollPane, BorderLayout.CENTER);

        updateOfferTable();

        JPanel buttonPanel = new JPanel();
        btnCreateOffer = new JButton("Crear Oferta");
        btnCreateOffer.addActionListener(e -> showCreateOfferDialog());

        btnDeleteOffer = new JButton("Eliminar Oferta");
        //btnDeleteOffer.addActionListener(e -> deleteOffer());

        btnDeactivateOffer = new JButton("Desactivar Oferta");
        //btnDeactivateOffer.addActionListener(e -> deactivateOffer());

        buttonPanel.add(btnCreateOffer);
        buttonPanel.add(btnDeleteOffer);
        buttonPanel.add(btnDeactivateOffer);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void updateOfferTable() {
        tableModel.setRowCount(0);
        List<Map<String, Object>> offers = offerController.getOffersByProvider(providerId);
        for (Map<String, Object> offer : offers) {
            Object[] rowData = {
                    offer.get("id_offer"),
                    offer.get("type_offer"),
                    offer.get("name"),
                    offer.get("date_open"),
                    offer.get("date_close"),
                    offer.get("description")
            };
            tableModel.addRow(rowData);
        }
    }

    private void showCreateOfferDialog() {
        JTextField nameField = new JTextField();
        JTextField dateOpenField = new JTextField();
        JTextField dateCloseField = new JTextField();
        JTextField descriptionField = new JTextField();

        JComboBox<String> offerTypeBox = new JComboBox<>();
        List<Map<String, Object>> offerTypes = offerController.getOfferTypes();
        for (Map<String, Object> type : offerTypes) {
            offerTypeBox.addItem(type.get("nombre").toString());
        }

        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.add(new JLabel("Tipo de Oferta:"));
        panel.add(offerTypeBox);
        panel.add(new JLabel("Nombre:"));
        panel.add(nameField);
        panel.add(new JLabel("Fecha de Apertura (YYYY-MM-DD HH:MM:SS):"));
        panel.add(dateOpenField);
        panel.add(new JLabel("Fecha de Cierre (YYYY-MM-DD HH:MM:SS):"));
        panel.add(dateCloseField);
        panel.add(new JLabel("Descripción:"));
        panel.add(descriptionField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Crear Nueva Oferta", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String dateOpen = dateOpenField.getText();
            String dateClose = dateCloseField.getText();
            String description = descriptionField.getText();
            int selectedIndex = offerTypeBox.getSelectedIndex();
            int idTypeOffer = (int) offerTypes.get(selectedIndex).get("id_type_offer");

            boolean success = offerController.createOffer(idTypeOffer, name, dateOpen, dateClose, description, providerId);
            if (success) {
                JOptionPane.showMessageDialog(this, "Oferta creada exitosamente.");
                updateOfferTable();
            } else {
                JOptionPane.showMessageDialog(this, "Error al crear la oferta.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
