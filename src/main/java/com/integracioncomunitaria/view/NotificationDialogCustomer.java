package com.integracioncomunitaria.view;

import com.integracioncomunitaria.controller.NotificationController;
import com.integracioncomunitaria.controller.ProviderController;
import com.integracioncomunitaria.controller.CustomerController;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class NotificationDialogCustomer extends JDialog {
    private int customerId;
    private NotificationController notificationController;
    private ProviderController providerController;
    private CustomerController customerController;
    private JPanel notificationPanel;

    public NotificationDialogCustomer(Frame parent, int customerId) {
        super(parent, "Notificaciones", true);
        this.customerId = customerId;
        this.notificationController = new NotificationController();
        this.providerController = new ProviderController();
        this.customerController = new CustomerController();


        setSize(700, 400); // Aumentado el tamaño horizontalmente
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // Panel para las notificaciones
        notificationPanel = new JPanel();
        notificationPanel.setLayout(new BoxLayout(notificationPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(notificationPanel);
        add(scrollPane, BorderLayout.CENTER);

        loadNotifications();
    }

    private void loadNotifications() {
        notificationPanel.removeAll();
        List<Map<String, Object>> notifications = notificationController.getNotifications(customerId, "provider");

        for (Map<String, Object> notification : notifications) {
            JPanel panel = createNotificationPanel(notification);
            notificationPanel.add(panel);
        }

        notificationPanel.revalidate();
        notificationPanel.repaint();
    }

    private JPanel createNotificationPanel(Map<String, Object> notification) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setPreferredSize(new Dimension(650, 80)); // Definir tamaño del panel

        // Contenido de la notificación
        String message = notification.get("message").toString();
        String senderIdName = notification.get("id_user_create").toString(); // Asegúrate de incluir esto en la consulta SQL
        int sender_id = Integer.parseInt(senderIdName);
        String senderName = "Usuario desconocido";

        if ((notification.get("type").toString()).equals("customer")) {
            senderName = customerController.getCustomerName(sender_id);
            //JLabel lblSender = new JLabel("De: " + senderName);

        } else if ((notification.get("type").toString()).equals("provider")) {
            senderName = providerController.getProviderName(sender_id);
            //JLabel lblSender = new JLabel("De: " + senderName);
        }

        String date = notification.get("date_create").toString();
        JLabel lblSender = new JLabel("De: " + senderName);
        JLabel lblDate = new JLabel("Fecha: " + date);
        JTextArea txtMessage = new JTextArea(message);
        txtMessage.setWrapStyleWord(true);
        txtMessage.setLineWrap(true);
        txtMessage.setEditable(false);
        JScrollPane messageScroll = new JScrollPane(txtMessage);

        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.add(lblSender, BorderLayout.NORTH);
        infoPanel.add(messageScroll, BorderLayout.CENTER);
        infoPanel.add(lblDate, BorderLayout.SOUTH);

        // Botones de acción
        JButton btnReply = new JButton("Responder");
        JButton btnMarkAsRead = new JButton("Marcar como leído");

        btnReply.addActionListener(e -> replyToNotification(notification));
        btnMarkAsRead.addActionListener(e -> markAsRead(notification));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnReply);
        buttonPanel.add(btnMarkAsRead);

        panel.add(infoPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void replyToNotification(Map<String, Object> notification) {
        String replyMessage = JOptionPane.showInputDialog(this, "Escribe tu respuesta:", "Responder Notificación", JOptionPane.PLAIN_MESSAGE);
        if (replyMessage != null && !replyMessage.trim().isEmpty()) {
            notificationController.sendReply(customerId, (int) notification.get("id_notification"), replyMessage);
            JOptionPane.showMessageDialog(this, "Respuesta enviada con éxito.");
        }
    }

    private void markAsRead(Map<String, Object> notification) {
        int notificationId = (int) notification.get("id_notification");
        notificationController.markAsRead(notificationId);
        loadNotifications();
    }
}


