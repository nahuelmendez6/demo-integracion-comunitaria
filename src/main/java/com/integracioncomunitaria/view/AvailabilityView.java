package com.integracioncomunitaria.view;

import com.integracioncomunitaria.controller.AvailabilityController;
import com.integracioncomunitaria.controller.ProfileController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class AvailabilityView extends JFrame {

    private AvailabilityController controller;
    private ProfileController profileController;
    private JPanel mainPanel;
    private Map<Integer, List<JCheckBox>> checkboxesByDay; // Mapea días con sus checkboxes de horas
    private Map<Integer, String> dayNames; // Mapea ID de días con sus nombres

    private int providerId; // ID del proveedor que inicia sesión

    public AvailabilityView(int providerId) {
        this.providerId = providerId;
        this.controller = new AvailabilityController();
        this.checkboxesByDay = new HashMap<>();
        this.dayNames = new HashMap<>();

        setTitle("Seleccionar Disponibilidad");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(0, 1));

        loadDaysAndHours(); // Cargar la información de la BD

        JButton saveButton = new JButton("Guardar Disponibilidad");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveAvailability();
            }
        });

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        add(scrollPane, BorderLayout.CENTER);
        add(saveButton, BorderLayout.SOUTH);
    }

    private void loadDaysAndHours() {
        List<Map<String, Object>> days = controller.getWeekDays();

        for (Map<String, Object> day : days) {
            int dayId = (int) day.get("id_week");
            String dayName = (String) day.get("name");
            dayNames.put(dayId, dayName);

            JPanel dayPanel = new JPanel();
            dayPanel.setLayout(new BorderLayout());
            dayPanel.setBorder(BorderFactory.createTitledBorder(dayName));

            JPanel hourPanel = new JPanel();
            hourPanel.setLayout(new GridLayout(0, 2));

            List<JCheckBox> hourCheckboxes = new ArrayList<>();
            List<Map<String, Object>> hours = controller.getHoursByDay(dayId);

            for (Map<String, Object> hour : hours) {
                int hourId = (int) hour.get("id_hour");
                String hourName = (String) hour.get("name");

                JCheckBox checkBox = new JCheckBox(hourName);
                checkBox.setActionCommand(dayId + "-" + hourId);
                hourCheckboxes.add(checkBox);
                hourPanel.add(checkBox);
            }

            checkboxesByDay.put(dayId, hourCheckboxes);

            dayPanel.add(hourPanel, BorderLayout.CENTER);
            mainPanel.add(dayPanel);
        }

        revalidate();
        repaint();
    }

    private void saveAvailability() {
        boolean success = false;

        for (Map.Entry<Integer, List<JCheckBox>> entry : checkboxesByDay.entrySet()) {
            int dayId = entry.getKey();
            List<JCheckBox> hourCheckboxes = entry.getValue();

            int fromHour = -1;
            int untilHour = -1;

            for (JCheckBox checkBox : hourCheckboxes) {
                if (checkBox.isSelected()) {
                    String[] parts = checkBox.getActionCommand().split("-");
                    int hourId = Integer.parseInt(parts[1]);

                    if (fromHour == -1) {
                        fromHour = hourId;
                    }
                    untilHour = hourId;
                }
            }

            if (fromHour != -1 && untilHour != -1) {
                success = controller.saveAvailability(providerId, dayId, fromHour, untilHour, providerId);
            }
        }

        if (success) {
            JOptionPane.showMessageDialog(this, "Disponibilidad guardada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        
            //int idProvider = profileController.getProviderIdByUserId(userId);

            SwingUtilities.invokeLater(() -> {
                new AddressSetupView(providerId).setVisible(true);
                dispose(); // Cierra la ventana actual
            });
        
        } else {
            JOptionPane.showMessageDialog(this, "Error al guardar la disponibilidad.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


}
