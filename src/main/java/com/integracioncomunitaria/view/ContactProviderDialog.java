package com.integracioncomunitaria.view;

import com.integracioncomunitaria.controller.NotificationController;

import javax.swing.*;
import java.awt.*;

public class ContactProviderDialog extends JDialog {
    private int providerId;
    private int customerId;
    private NotificationController notificationController;

    public ContactProviderDialog(Frame parent, int providerId, int customerId) {
        super(parent, "Enviar mensaje al proveedor", true);
        this.providerId = providerId;
        this.customerId = customerId;
        this.notificationController = new NotificationController();

        setLayout(new BorderLayout());
        setSize(400, 250);
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Escribe tu mensaje:");
        JTextArea messageArea = new JTextArea(5, 30);
        JScrollPane scrollPane = new JScrollPane(messageArea);

        panel.add(label, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton sendButton = new JButton("Enviar");
        sendButton.addActionListener(e -> sendMessage(messageArea.getText()));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(sendButton);

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void sendMessage(String message) {
        if (message.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El mensaje no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = notificationController.sendNotification(providerId, customerId, "customer", message);
        if (success) {
            JOptionPane.showMessageDialog(this, "Mensaje enviado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error al enviar el mensaje.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
