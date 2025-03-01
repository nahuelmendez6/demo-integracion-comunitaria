package com.integracioncomunitaria.view;

import com.integracioncomunitaria.controller.InterestController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class InterestView extends JFrame {
    private int customerId;
    private int userId;
    private InterestController controller;
    private List<JCheckBox> checkBoxes;
    
    public InterestView(int customerId) {
        this.customerId = customerId;
        this.controller = new InterestController();
        this.checkBoxes = new ArrayList<>();

        setTitle("Selecciona tus intereses");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Obtener categorías y mostrar checkboxes
        List<String[]> categories = controller.getAllCategories();
        for (String[] category : categories) {
            JCheckBox checkBox = new JCheckBox(category[1]);
            checkBox.setActionCommand(category[0]);  // Guarda el id de la categoría
            checkBoxes.add(checkBox);
            panel.add(checkBox);
        }

        JButton saveButton = new JButton("Guardar Intereses");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveInterests();
            }
        });

        add(new JScrollPane(panel), BorderLayout.CENTER);
        add(saveButton, BorderLayout.SOUTH);
    }

    private void saveInterests() {
        List<Integer> selectedCategories = new ArrayList<>();
        for (JCheckBox checkBox : checkBoxes) {
            if (checkBox.isSelected()) {
                selectedCategories.add(Integer.parseInt(checkBox.getActionCommand()));
            }
        }

        boolean success = controller.saveCustomerInterests(customerId, selectedCategories, userId);
        
        
        if (success) {
            JOptionPane.showMessageDialog(this, "Intereses guardados con éxito.");
            SwingUtilities.invokeLater(() -> {
                new MainCustomerMenuView(customerId).setVisible(true);
                dispose(); // Cierra la ventana actual
            });
        } else {
            JOptionPane.showMessageDialog(this, "Error al guardar intereses.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //public static void main(String[] args) {
    //    SwingUtilities.invokeLater(() -> new InterestView(1, 1).setVisible(true)); // Prueba con id_customer = 1 y id_user = 1
    //}
}
