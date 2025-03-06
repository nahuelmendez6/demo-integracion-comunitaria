package com.integracioncomunitaria.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import com.integracioncomunitaria.controller.PostulationController;

public class PostulationView extends JFrame {
    private JTable approvedTable, canceledTable, finishedTable;
    private DefaultTableModel approvedModel, canceledModel, finishedModel;
    private int customerId;
    private PostulationController postulationController;

    public PostulationView(int customerId) {
        this.customerId = customerId;
        this.postulationController = new PostulationController();
        setTitle("Postulaciones del Cliente");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Crear los paneles con las tablas
        tabbedPane.add("Aprobadas", createApprovedPanel());
        tabbedPane.add("Canceladas", createCanceledPanel());
        tabbedPane.add("Finalizadas", createFinishedPanel());
        
        add(tabbedPane, BorderLayout.CENTER);
        loadPostulations();
    }

    private JPanel createApprovedPanel() {
        approvedModel = new DefaultTableModel(new String[]{"ID", "Proveedor", "Propuesta", "Costo", "Estado", "Acción"}, 0);
        approvedTable = new JTable(approvedModel);
        JScrollPane scrollPane = new JScrollPane(approvedTable);
        
        JButton finalizeButton = new JButton("Finalizar Postulación");
        finalizeButton.addActionListener(e -> finalizePostulation());
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(finalizeButton, BorderLayout.SOUTH);
        return panel;
    }

    private Component createCanceledPanel() {
        canceledModel = new DefaultTableModel(new String[]{"ID", "Proveedor", "Propuesta", "Costo", "Estado"}, 0);
        canceledTable = new JTable(canceledModel);
        return new JPanel(new BorderLayout()).add(new JScrollPane(canceledTable));
    }

    private JPanel createFinishedPanel() {
        finishedModel = new DefaultTableModel(new String[]{"ID", "Proveedor", "Propuesta", "Costo", "Estado", "Acción"}, 0);
        finishedTable = new JTable(finishedModel);
        JScrollPane scrollPane = new JScrollPane(finishedTable);
        
        JButton rateButton = new JButton("Puntuar Proveedor");
        rateButton.addActionListener(e -> rateProvider());
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(rateButton, BorderLayout.SOUTH);
        return panel;
    }

    private void loadPostulations() {
        List<String[]> postulations = postulationController.getPostulationsByCustomer(customerId);
        for (String[] post : postulations) {
            String id = post[0];
            String provider = post[2];
            String proposal = post[3];
            String cost = post[4];
            String state = post[5];
            
            if (state.equalsIgnoreCase("Aceptado")) {
                approvedModel.addRow(new Object[]{id, provider, proposal, cost, state});
            } else if (state.equalsIgnoreCase("Rechazado")) {
                canceledModel.addRow(new Object[]{id, provider, proposal, cost, state});
            } else if (state.equalsIgnoreCase("Pendiente")) {
                finishedModel.addRow(new Object[]{id, provider, proposal, cost, state});
            } else if (state.equalsIgnoreCase("Dado de baja")) {
                finishedModel.addRow(new Object[]{id, provider, proposal, cost, state});
            } else if (state.equalsIgnoreCase("Inventario cancelado")) {
                finishedModel.addRow(new Object[]{id, provider, proposal, cost, state});
            } else if (state.equalsIgnoreCase("Cancelada")) {
                finishedModel.addRow(new Object[]{id, provider, proposal, cost, state});
            } else if (state.equalsIgnoreCase("Terminada con éxto")) {
                finishedModel.addRow(new Object[]{id, provider, proposal, cost, state});
            }
        }
    }

    private void finalizePostulation() {
        int selectedRow = approvedTable.getSelectedRow();
        if (selectedRow != -1) {
            String postulationId = (String) approvedModel.getValueAt(selectedRow, 0);
            // Aquí iría la lógica para actualizar el estado de la postulación en la base de datos
            JOptionPane.showMessageDialog(this, "Postulación finalizada con éxito.");
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una postulación para finalizar.");
        }
    }

    private void rateProvider() {
        int selectedRow = finishedTable.getSelectedRow();
        if (selectedRow != -1) {
            String providerName = (String) finishedModel.getValueAt(selectedRow, 1);
            String rating = JOptionPane.showInputDialog("Ingrese una calificación para " + providerName + " (1-5):");
            // Aquí iría la lógica para guardar la calificación en la base de datos
            JOptionPane.showMessageDialog(this, "Calificación registrada.");
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un proveedor para puntuar.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PostulationView(1).setVisible(true));
    }
}
