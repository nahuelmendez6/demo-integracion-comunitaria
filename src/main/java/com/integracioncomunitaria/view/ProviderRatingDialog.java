package com.integracioncomunitaria.view;

import javax.swing.*;
import java.awt.*;

import com.integracioncomunitaria.controller.ProviderController;

public class ProviderRatingDialog extends JDialog {

    private ProviderController providerController;

    public ProviderRatingDialog(Frame parent, int providerId) {

        super(parent, "Puntuación del Proveedor", true);

        this.providerController = new ProviderController();

        
        setLayout(new BorderLayout());
        setSize(300, 150);
        setLocationRelativeTo(parent);

        double averageRating = providerController.getProviderAverageRating(providerId);

        JLabel ratingLabel = new JLabel("Promedio de puntuación: " + String.format("%.2f", averageRating));
        ratingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        ratingLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        add(ratingLabel, BorderLayout.CENTER);

        JButton closeButton = new JButton("Cerrar");
        closeButton.addActionListener(e -> dispose());
        add(closeButton, BorderLayout.SOUTH);
    }
}
