package com.integracioncomunitaria.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.integracioncomunitaria.controller.ProviderController;

public class RateProviderDialog extends JDialog {
    private JComboBox<Integer> ratingComboBox;
    private JTextArea commentArea;
    private JButton submitButton;
    private int providerId;
    private int userId;
    private ProviderController providerController;

    public RateProviderDialog(Frame parent, int providerId, int userId, ProviderController providerController) {
        super(parent, "Puntuar Proveedor", true);
        this.providerId = providerId;
        this.userId = userId;
        this.providerController = providerController;

        setLayout(new BorderLayout());
        setSize(300, 200);
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel(new GridLayout(3, 1));
        panel.add(new JLabel("Seleccione una calificación (1-5):"));

        ratingComboBox = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
        panel.add(ratingComboBox);

        panel.add(new JLabel("Comentario:"));
        commentArea = new JTextArea(3, 20);
        panel.add(new JScrollPane(commentArea));

        add(panel, BorderLayout.CENTER);

        submitButton = new JButton("Enviar Puntuación");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitRating();
            }
        });
        add(submitButton, BorderLayout.SOUTH);
    }

    private void submitRating() {
        int rating = (int) ratingComboBox.getSelectedItem();
        String comment = commentArea.getText().trim();

        if (comment.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar un comentario.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = providerController.submitRating(providerId, rating, comment, userId);
        if (success) {
            JOptionPane.showMessageDialog(this, "Puntuación registrada con éxito.");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error al registrar la puntuación.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
