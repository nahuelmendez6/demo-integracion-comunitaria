package com.integracioncomunitaria.view;

import com.integracioncomunitaria.controller.PetitionController;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PetitionView extends JFrame {
    private PetitionController petitionController;
    private JComboBox<String> typePetitionComboBox;
    private JTextArea descriptionTextArea;
    private JSpinner dateSinceSpinner, dateUntilSpinner;
    private int customerId;
    private JTable petitionsTable;

    public PetitionView(int customerId) {
        this.customerId = customerId;
        this.petitionController = new PetitionController();

        setTitle("Crear y Ver Peticiones");
        setSize(700, 500);
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

        // Botón para enviar
        JButton submitButton = new JButton("Crear Petición");
        submitButton.addActionListener(e -> submitPetition());

        // Tabla para mostrar las peticiones del cliente
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        String[] columnNames = {"ID", "Tipo", "Descripción", "Fecha Desde", "Fecha Hasta"};
        petitionsTable = new JTable();
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
        String[][] data = new String[petitions.size()][5];

        for (int i = 0; i < petitions.size(); i++) {
            data[i] = petitions.get(i);
        }

        String[] columnNames = {"ID", "Tipo", "Descripción", "Fecha Desde", "Fecha Hasta"};
        petitionsTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
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
            loadPetitions();  // Actualiza la tabla con la nueva petición
        } else {
            JOptionPane.showMessageDialog(this, "Error al crear la petición", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PetitionView(1).setVisible(true));
    }
}
