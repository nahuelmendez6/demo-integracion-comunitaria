package com.integracioncomunitaria.view;

import com.integracioncomunitaria.controller.PetitionController;
import com.integracioncomunitaria.controller.PostulationController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ProviderPetitionView extends JFrame {
    private PetitionController petitionController;
    private PostulationController postulationController;
    private JTable petitionTable;
    private JComboBox<String> typeFilterComboBox;
    private int providerId;
    private List<String[]> typePetitions;

    public ProviderPetitionView(int providerId) {
        this.providerId = providerId;
        this.petitionController = new PetitionController();
        this.postulationController = new PostulationController();

        setTitle("Peticiones Disponibles");
        setSize(700, 400);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Panel superior con filtro
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());
        topPanel.add(new JLabel("Filtrar por tipo de petición:"));

        typeFilterComboBox = new JComboBox<>();
        loadPetitionTypes();
        typeFilterComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTableData();
            }
        });
        topPanel.add(typeFilterComboBox);

        add(topPanel, BorderLayout.NORTH);

        // Tabla de peticiones
        String[] columnNames = {"ID", "Tipo", "Descripción", "Desde", "Hasta", "Ciudad"};
        petitionTable = new JTable(new String[0][0], columnNames);
        JScrollPane scrollPane = new JScrollPane(petitionTable);
        add(scrollPane, BorderLayout.CENTER);

        // Botón de postulación
        JButton applyButton = new JButton("Postularse");
        applyButton.addActionListener(e -> applyToPetition());
        add(applyButton, BorderLayout.SOUTH);

        updateTableData();
    }

    private void loadPetitionTypes() {
        typePetitions = petitionController.getTypePetitions();
        typeFilterComboBox.addItem("Todos"); // Opción para mostrar todas las peticiones
        for (String[] type : typePetitions) {
            typeFilterComboBox.addItem(type[1]);
        }
    }

    private void updateTableData() {
        String selectedType = (String) typeFilterComboBox.getSelectedItem();
        int typeId = -1;
        
        for (String[] type : typePetitions) {
            if (type[1].equals(selectedType)) {
                typeId = Integer.parseInt(type[0]);
                break;
            }
        }

        List<String[]> petitions;
        if (selectedType.equals("Todos")) {
            petitions = petitionController.getAvailablePetitions(providerId);
        } else {
            petitions = petitionController.getAvailablePetitionsByType(providerId, typeId);
        }

        String[][] data = petitions.toArray(new String[0][]);
        String[] columnNames = {"ID", "Tipo", "Descripción", "Desde", "Hasta", "Ciudad"};
        petitionTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
    }

    private void applyToPetition() {
        int selectedRow = petitionTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una petición", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int petitionId = Integer.parseInt((String) petitionTable.getValueAt(selectedRow, 0));
        String proposal = JOptionPane.showInputDialog(this, "Ingrese su propuesta:");
        String costStr = JOptionPane.showInputDialog(this, "Ingrese el costo:");

        if (proposal == null || proposal.trim().isEmpty() || costStr == null || costStr.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar propuesta y costo", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int cost = Integer.parseInt(costStr);
        boolean success = postulationController.applyToPetition(petitionId, providerId, proposal, cost);

        if (success) {
            JOptionPane.showMessageDialog(this, "Postulación exitosa", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Error en la postulación", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProviderPetitionView(1).setVisible(true));
    }
}
