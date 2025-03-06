package com.integracioncomunitaria.view;

import com.integracioncomunitaria.controller.PostulationController;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CustomerPostulationView extends JFrame {
    private PostulationController postulationController;
    private JTable postulationTable;
    private int customerId;

    public CustomerPostulationView(int customerId) {
        this.customerId = customerId;
        this.postulationController = new PostulationController();

        setTitle("Postulaciones a Mis Peticiones");
        setSize(700, 400);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Panel para la tabla
        String[] columnNames = {"ID", "ID Petición", "Proveedor", "Propuesta", "Costo", "Estado"};
        List<String[]> postulations = postulationController.getPostulationsByCustomer(customerId);
        String[][] data = postulations.toArray(new String[0][]);

        postulationTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(postulationTable);
        add(scrollPane, BorderLayout.CENTER);

        // Botón para aceptar postulación
        JButton acceptButton = new JButton("Aceptar Postulación");
        acceptButton.addActionListener(e -> acceptPostulation());
        add(acceptButton, BorderLayout.NORTH);

        // Boton para cancelar postulacion
        JButton cancelButton = new JButton("Cancelar postulación");
        cancelButton.addActionListener(e -> cancelPostulation());
        add(cancelButton, BorderLayout.SOUTH);
    }

    private void acceptPostulation() {
        int selectedRow = postulationTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una postulación", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int postulationId = Integer.parseInt((String) postulationTable.getValueAt(selectedRow, 0));
        int petitionId = Integer.parseInt((String) postulationTable.getValueAt(selectedRow, 1));

        boolean success = postulationController.acceptPostulation(postulationId, petitionId);

        if (success) {
            JOptionPane.showMessageDialog(this, "Postulación aceptada con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();  // Cerrar la ventana después de aceptar
        } else {
            JOptionPane.showMessageDialog(this, "Error al aceptar la postulación", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void cancelPostulation() {
        int selectedRow = postulationTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una postulación", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int postulationId = Integer.parseInt((String) postulationTable.getValueAt(selectedRow, 0));
        int petitionId = Integer.parseInt((String) postulationTable.getValueAt(selectedRow, 1));

        boolean success = postulationController.cancelPostulation(postulationId, petitionId);

        if (success) {
            JOptionPane.showMessageDialog(this, "Postulación aceptada con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();  // Cerrar la ventana después de aceptar
        } else {
            JOptionPane.showMessageDialog(this, "Error al aceptar la postulación", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void finishPostulation() {
        int selectedRow = postulationTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una postulación", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int postulationId = Integer.parseInt((String) postulationTable.getValueAt(selectedRow, 0));
        int petitionId = Integer.parseInt((String) postulationTable.getValueAt(selectedRow, 1));

        boolean success = postulationController.finishPostulation(postulationId, petitionId);

        if (success) {
            JOptionPane.showMessageDialog(this, "Postulación aceptada con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();  // Cerrar la ventana después de aceptar
        } else {
            JOptionPane.showMessageDialog(this, "Error al aceptar la postulación", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CustomerPostulationView(1).setVisible(true));
    }
}
