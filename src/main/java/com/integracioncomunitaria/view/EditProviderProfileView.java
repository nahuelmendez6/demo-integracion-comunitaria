package com.integracioncomunitaria.view;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.integracioncomunitaria.controller.ProviderController;
import com.integracioncomunitaria.controller.ProfileController;
import java.util.List;

public class EditProviderProfileView extends JFrame {
    private ProviderController providerController;
    private ProfileController profileController;
    private int providerId;
    private JTextField nameField, lastNameField, emailField;
    private JComboBox<String> categoryBox, professionBox;

    public EditProviderProfileView(int providerId) {
        this.providerController = new ProviderController();
        this.profileController = new ProfileController();
        this.providerId = providerId;

        setTitle("Editar Perfil del Proveedor");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(6, 2));

        // Obtener datos actuales del proveedor
        List<String[]> providerData = providerController.getProviderData(providerId);
        if (providerData.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontró información del proveedor", "Error", JOptionPane.ERROR_MESSAGE);
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

        panel.add(new JLabel("Categoría:"));
        categoryBox = new JComboBox<>(providerController.getCategories().toArray(new String[0]));
        categoryBox.setSelectedItem(data[4]);
        panel.add(categoryBox);

        panel.add(new JLabel("Profesión:"));
        professionBox = new JComboBox<>(providerController.getProfessions().toArray(new String[0]));
        professionBox.setSelectedItem(data[5]);
        panel.add(professionBox);

        JButton saveButton = new JButton("Guardar Cambios");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateProviderProfile();
            }
        });

        add(panel, BorderLayout.CENTER);
        add(saveButton, BorderLayout.SOUTH);
    }

    private void updateProviderProfile() {
        String name = nameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String category = (String) categoryBox.getSelectedItem();
        String profession = (String) professionBox.getSelectedItem();

        int id_category = profileController.getCategoryId(category);
        int id_profession = profileController.getProfessionId(profession);

        boolean success = providerController.updateProviderProfile(providerId, name, lastName, email, id_category, id_profession);

        if (success) {
            JOptionPane.showMessageDialog(this, "Perfil actualizado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error al actualizar el perfil.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
