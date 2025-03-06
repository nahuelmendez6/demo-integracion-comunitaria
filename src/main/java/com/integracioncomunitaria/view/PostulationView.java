package com.integracioncomunitaria.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import com.integracioncomunitaria.controller.PostulationController;
import com.integracioncomunitaria.controller.ProviderController;

public class PostulationView extends JFrame {
    private JTable approvedTable, canceledTable, finishedTable;
    private DefaultTableModel approvedModel, canceledModel, finishedModel;
    private int customerId;
    private PostulationController postulationController;
    private ProviderController providerController;

    public PostulationView(int customerId) {
        this.customerId = customerId;
        this.postulationController = new PostulationController();
        this.providerController = new ProviderController();
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
            } else if (state.equalsIgnoreCase("Terminada con éxito")) {
                finishedModel.addRow(new Object[]{id, provider, proposal, cost, state});
            }
        }
    }

    private void finalizePostulation() {
        int selectedRow = approvedTable.getSelectedRow();
        if (selectedRow != -1) {
            String postulationId = (String) approvedModel.getValueAt(selectedRow, 0);
            int idpostulation = Integer.parseInt(postulationId);
            int id_petition = postulationController.getIdPetitionByPostulation(idpostulation);
            postulationController.finishPostulation(idpostulation, id_petition);
            JOptionPane.showMessageDialog(this, "Postulación finalizada con éxito.");
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una postulación para finalizar.");
        }
    }

    private void rateProvider() {
        int selectedRow = finishedTable.getSelectedRow();
        if (selectedRow != -1) {
            String idprovider =  (String) finishedModel.getValueAt(selectedRow, 0);// Asume que la primera columna tiene el id_provider
            String providerName = (String) finishedModel.getValueAt(selectedRow, 1);
            int providerId = Integer.parseInt(idprovider);
            ProviderController providerController = new ProviderController();
            new RateProviderDialog(this, providerId, customerId, providerController).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un proveedor para puntuar.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PostulationView(1).setVisible(true));
    }
}
