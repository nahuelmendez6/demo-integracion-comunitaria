package com.integracioncomunitaria.view;

import javax.swing.*;
import java.awt.*;



import com.integracioncomunitaria.controller.AvailabilityController;;

public class ProviderAvailabilityView extends JDialog {

    private AvailabilityController availabilityController;

    public ProviderAvailabilityView(Frame parent, int providerId) {
        super(parent, "Disponibilidad del Proveedor", true);
        
        this.availabilityController = new AvailabilityController();

        setSize(400, 300);
        setLocationRelativeTo(parent);

        JTextArea availabilityText = new JTextArea();
        availabilityText.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(availabilityText);
        add(scrollPane, BorderLayout.CENTER);
        
        String availability = availabilityController.getProviderAvailability(providerId);
        availabilityText.setText(availability);
    }

}
