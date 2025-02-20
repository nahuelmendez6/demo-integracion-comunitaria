package com.integracioncomunitaria.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OfferView extends JFrame {
    private JTable offerTable;
    private DefaultTableModel tableModel;
    private JButton btnCreateOffer;
    private JButton btnDeleteOffer;
    private JButton btnDeactivateOffer;

    // Datos hardcodeados de ofertas
    private List<Offer> offers = new ArrayList<>();

    public OfferView() {
        setTitle("Gestión de Ofertas");
        setSize(1200, 600); // Tamaño adecuado para la tabla
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cierra solo esta ventana
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Hardcodeo de ofertas de ejemplo
        offers.add(new Offer(1, 1, "Oferta 1", "2023-10-01 10:00", "2023-10-10 18:00", "Descripción de la Oferta 1", 1, 1));
        offers.add(new Offer(2, 2, "Oferta 2", "2023-10-02 09:00", "2023-10-12 17:00", "Descripción de la Oferta 2", 1, 1));

        // Crear la tabla
        String[] columnNames = {"ID", "Tipo de Oferta", "Nombre", "Fecha Apertura", "Fecha Cierre", "Descripción", "Acciones"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer que la tabla no sea editable
            }
        };
        offerTable = new JTable(tableModel);
        offerTable.setRowHeight(30); // Ajustar altura de las filas
        JScrollPane scrollPane = new JScrollPane(offerTable);
        add(scrollPane, BorderLayout.CENTER);

        // Actualizar la tabla con los datos hardcodeados
        updateOfferTable();

        // Panel de botones
        JPanel buttonPanel = new JPanel();
        btnCreateOffer = new JButton("Crear Oferta");
        btnCreateOffer.setBackground(new Color(72, 201, 176));
        btnCreateOffer.setForeground(Color.WHITE);
        btnCreateOffer.setFocusPainted(false);
        btnCreateOffer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showCreateOfferDialog();
            }
        });

        btnDeleteOffer = new JButton("Eliminar Oferta");
        btnDeleteOffer.setBackground(new Color(231, 76, 60));
        btnDeleteOffer.setForeground(Color.WHITE);
        btnDeleteOffer.setFocusPainted(false);
        btnDeleteOffer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteOffer();
            }
        });

        btnDeactivateOffer = new JButton("Desactivar Oferta");
        btnDeactivateOffer.setBackground(new Color(241, 196, 15));
        btnDeactivateOffer.setForeground(Color.WHITE);
        btnDeactivateOffer.setFocusPainted(false);
        btnDeactivateOffer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deactivateOffer();
            }
        });

        buttonPanel.add(btnCreateOffer);
        buttonPanel.add(btnDeleteOffer);
        buttonPanel.add(btnDeactivateOffer);

        // Agregar el panel de botones al sur
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Método para actualizar la tabla de ofertas
    private void updateOfferTable() {
        tableModel.setRowCount(0); // Limpiar la tabla
        for (Offer offer : offers) {
            Object[] rowData = {
                    offer.getId(),
                    offer.getTypeOffer(),
                    offer.getName(),
                    offer.getDateOpen(),
                    offer.getDateClose(),
                    offer.getDescription(),
                    "Acciones" // Columna para acciones (eliminar/desactivar)
            };
            tableModel.addRow(rowData);
        }
    }

    // Método para mostrar el diálogo de creación de oferta
    private void showCreateOfferDialog() {
        JTextField txtName = new JTextField();
        JComboBox<String> cmbTypeOffer = new JComboBox<>(new String[]{"Tipo 1", "Tipo 2", "Tipo 3"});
        JTextField txtDateOpen = new JTextField(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
        JTextField txtDateClose = new JTextField(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
        JTextArea txtDescription = new JTextArea(5, 20);
        JScrollPane scrollPane = new JScrollPane(txtDescription);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Nombre de la Oferta:"));
        panel.add(txtName);
        panel.add(new JLabel("Tipo de Oferta:"));
        panel.add(cmbTypeOffer);
        panel.add(new JLabel("Fecha de Apertura (yyyy-MM-dd HH:mm):"));
        panel.add(txtDateOpen);
        panel.add(new JLabel("Fecha de Cierre (yyyy-MM-dd HH:mm):"));
        panel.add(txtDateClose);
        panel.add(new JLabel("Descripción:"));
        panel.add(scrollPane);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Crear Nueva Oferta",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String name = txtName.getText().trim();
            String typeOffer = (String) cmbTypeOffer.getSelectedItem();
            String dateOpen = txtDateOpen.getText().trim();
            String dateClose = txtDateClose.getText().trim();
            String description = txtDescription.getText().trim();

            if (!name.isEmpty() && !dateOpen.isEmpty() && !dateClose.isEmpty()) {
                // Hardcodeo de la nueva oferta
                int newId = offers.size() + 1; // Simula un ID autoincremental
                offers.add(new Offer(newId, cmbTypeOffer.getSelectedIndex() + 1, name, dateOpen, dateClose, description, 1, 1));
                updateOfferTable(); // Actualiza la tabla
                JOptionPane.showMessageDialog(this, "Oferta creada con éxito.");
            } else {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
            }
        }
    }

    // Método para eliminar una oferta
    private void deleteOffer() {
        int selectedRow = offerTable.getSelectedRow();
        if (selectedRow != -1) {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "¿Estás seguro de eliminar esta oferta?",
                    "Eliminar Oferta",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                offers.remove(selectedRow); // Eliminar la oferta de la lista
                updateOfferTable(); // Actualizar la tabla
                JOptionPane.showMessageDialog(this, "Oferta eliminada con éxito.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona una oferta para eliminar.");
        }
    }

    // Método para desactivar una oferta
    private void deactivateOffer() {
        int selectedRow = offerTable.getSelectedRow();
        if (selectedRow != -1) {
            Offer selectedOffer = offers.get(selectedRow);
            selectedOffer.setDateClose(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date())); // Desactivar = cerrar la oferta
            updateOfferTable(); // Actualizar la tabla
            JOptionPane.showMessageDialog(this, "Oferta desactivada con éxito.");
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona una oferta para desactivar.");
        }
    }

    // Clase interna para representar una oferta
    private class Offer {
        private int id;
        private int typeOffer;
        private String name;
        private String dateOpen;
        private String dateClose;
        private String description;
        private int idUserCreate;
        private int idUserUpdate;

        public Offer(int id, int typeOffer, String name, String dateOpen, String dateClose, String description, int idUserCreate, int idUserUpdate) {
            this.id = id;
            this.typeOffer = typeOffer;
            this.name = name;
            this.dateOpen = dateOpen;
            this.dateClose = dateClose;
            this.description = description;
            this.idUserCreate = idUserCreate;
            this.idUserUpdate = idUserUpdate;
        }

        public int getId() {
            return id;
        }

        public int getTypeOffer() {
            return typeOffer;
        }

        public String getName() {
            return name;
        }

        public String getDateOpen() {
            return dateOpen;
        }

        public String getDateClose() {
            return dateClose;
        }

        public String getDescription() {
            return description;
        }

        public void setDateClose(String dateClose) {
            this.dateClose = dateClose;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new OfferView().setVisible(true);
        });
    }
}