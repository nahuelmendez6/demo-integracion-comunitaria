package com.integracioncomunitaria.view;

import com.integracioncomunitaria.controller.PetitionController;
import com.integracioncomunitaria.controller.PostulationController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PetitionView extends JFrame {
    private PetitionController petitionController;
    private PostulationController postulationController;
    private JComboBox<String> typePetitionComboBox;
    private JTextArea descriptionTextArea;
    private JSpinner dateSinceSpinner, dateUntilSpinner;
    private int customerId;
    private int providerId;
    private JTable petitionsTable;
    private DefaultTableModel tableModel;

    public PetitionView(int customerId, int providerId) {
        this.customerId = customerId;
        this.providerId = providerId;
        this.petitionController = new PetitionController();
        this.postulationController = new PostulationController();

        setTitle("Crear y Ver Peticiones");
        setSize(800, 600);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(5, 2, 10, 10));

        // Tipo de petición
        formPanel.add(new JLabel("Tipo de Petición:"));
        typePetitionComboBox = new JComboBox<>();
        loadTypePetitions();
        formPanel.add(typePetitionComboBox);

        // Descripción
        formPanel.add(new JLabel("Descripción:"));
        descriptionTextArea = new JTextArea(3, 20);
        formPanel.add(new JScrollPane(descriptionTextArea));

        // Fechas
        formPanel.add(new JLabel("Fecha Desde:"));
        dateSinceSpinner = new JSpinner(new SpinnerDateModel());
        formPanel.add(dateSinceSpinner);

        formPanel.add(new JLabel("Fecha Hasta:"));
        dateUntilSpinner = new JSpinner(new SpinnerDateModel());
        formPanel.add(dateUntilSpinner);

        // Botón para enviar petición
        JButton submitButton = new JButton("Crear Petición");
        submitButton.addActionListener(e -> submitPetition());

        // Panel para tabla de peticiones
        JPanel tablePanel = new JPanel(new BorderLayout());
        String[] columnNames = {"ID", "Tipo", "Descripción", "Fecha Desde", "Fecha Hasta", "Acción"};
        tableModel = new DefaultTableModel(columnNames, 0);
        petitionsTable = new JTable(tableModel);
        petitionsTable.getColumn("Acción").setCellRenderer(new ButtonRenderer());
        petitionsTable.getColumn("Acción").setCellEditor(new ButtonEditor(new JCheckBox()));

        tablePanel.add(new JScrollPane(petitionsTable), BorderLayout.CENTER);
        loadPetitions();

        add(formPanel, BorderLayout.NORTH);
        add(submitButton, BorderLayout.SOUTH);
        add(tablePanel, BorderLayout.CENTER);
    }

    private void loadTypePetitions() {
        List<String[]> typePetitions = petitionController.getTypePetitions();
        for (String[] type : typePetitions) {
            typePetitionComboBox.addItem(type[1] + " (ID: " + type[0] + ")");
        }
    }

    private void loadPetitions() {
        List<String[]> petitions = petitionController.getPetitionsByCustomer(customerId);
        tableModel.setRowCount(0);

        for (String[] petition : petitions) {
            Object[] rowData = new Object[]{
                    petition[0],
                    petition[1],
                    petition[2],
                    petition[3],
                    petition[4],
                    "Postularse"
            };
            tableModel.addRow(rowData);
        }
    }

    private void submitPetition() {
        String selectedType = (String) typePetitionComboBox.getSelectedItem();
        if (selectedType == null || selectedType.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un tipo de petición", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int idTypePetition = Integer.parseInt(selectedType.replaceAll("\\D+", ""));
        String description = descriptionTextArea.getText().trim();
        java.sql.Date dateSince = new java.sql.Date(((java.util.Date) dateSinceSpinner.getValue()).getTime());
        java.sql.Date dateUntil = new java.sql.Date(((java.util.Date) dateUntilSpinner.getValue()).getTime());

        if (description.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese una descripción", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = petitionController.createPetition(idTypePetition, description, dateSince, dateUntil, customerId);
        if (success) {
            JOptionPane.showMessageDialog(this, "Petición creada con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            loadPetitions();
        } else {
            JOptionPane.showMessageDialog(this, "Error al crear la petición", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Renderizador de botón en la tabla
    class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setText("Postularse");
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    // Editor de botón en la tabla
    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private int petitionId;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton("Postularse");
            button.addActionListener(e -> postulateToPetition());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            petitionId = Integer.parseInt(table.getValueAt(row, 0).toString());
            return button;
        }

        private void postulateToPetition() {
            String proposal = JOptionPane.showInputDialog("Ingrese su propuesta:");
            if (proposal == null || proposal.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Debe ingresar una propuesta válida.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String costStr = JOptionPane.showInputDialog("Ingrese el costo:");
            int cost;
            try {
                cost = Integer.parseInt(costStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "El costo debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean success = postulationController.applyToPetition(petitionId, providerId, proposal, cost);
            if (success) {
                JOptionPane.showMessageDialog(null, "Postulación realizada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Error al postularse.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PetitionView(1, 2).setVisible(true));
    }
}
