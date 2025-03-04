package com.integracioncomunitaria.view;

import com.integracioncomunitaria.controller.PetitionController;
import com.integracioncomunitaria.controller.PostulationController;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ProviderPetitionView extends JFrame {
    private PetitionController petitionController;
    private PostulationController postulationController;
    private JTable petitionTable;
    private int providerId;

    public ProviderPetitionView(int providerId) {
        this.providerId = providerId;
        this.petitionController = new PetitionController();
        this.postulationController = new PostulationController();

        setTitle("Peticiones Disponibles");
        setSize(700, 400);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] columnNames = {"ID", "Tipo", "Descripción", "Desde", "Hasta", "Ciudad"};
        List<String[]> petitions = petitionController.getAvailablePetitions(providerId);
        String[][] data = petitions.toArray(new String[0][]);
        
        petitionTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(petitionTable);
        add(scrollPane, BorderLayout.CENTER);

        JButton applyButton = new JButton("Postularse");
        applyButton.addActionListener(e -> applyToPetition());
        add(applyButton, BorderLayout.SOUTH);
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
