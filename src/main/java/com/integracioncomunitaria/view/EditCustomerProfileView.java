package com.integracioncomunitaria.view;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.integracioncomunitaria.controller.CustomerController;
import com.integracioncomunitaria.controller.ProfileController;
import java.util.List;

public class EditCustomerProfileView extends JFrame {
    private CustomerController customerController;
    private ProfileController profileController;
    private int customerId;
    private JTextField nameField, lastNameField, emailField;
    private JComboBox<String> categoryBox, professionBox;

    public EditCustomerProfileView(int customerId) {
        this.customerController = new CustomerController();
        this.profileController = new ProfileController();
        this.customerId = customerId;

        setTitle("Editar Perfil del Proveedor");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(6, 2));

        // Obtener datos actuales del proveedor
        List<String[]> providerData = customerController.getCustomerData(customerId);
        if (providerData.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontró información del cliente", "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        String[] data = providerData.get(0);

        panel.add(new JLabel("Nombre:"));
        nameField = new JTextField(data[1]);
        panel.add(nameField);

        panel.add(new JLabel("Apellido:"));
        lastNameField = new JTextField(data[2]);
        panel.add(lastNameField);

        panel.add(new JLabel("Email:"));
        emailField = new JTextField(data[3]);
        panel.add(emailField);

        JButton saveButton = new JButton("Guardar Cambios");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateCustomerProfile();
            }
        });

        add(panel, BorderLayout.CENTER);
        add(saveButton, BorderLayout.SOUTH);
    }

    private void updateCustomerProfile() {
        String name = nameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();


        boolean success = customerController.updateCustomerProfile(customerId, name, lastName, email);

        if (success) {
            JOptionPane.showMessageDialog(this, "Perfil actualizado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error al actualizar el perfil.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
