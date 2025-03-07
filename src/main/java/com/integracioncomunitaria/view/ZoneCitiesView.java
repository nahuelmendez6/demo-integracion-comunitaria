package com.integracioncomunitaria.view;

import com.integracioncomunitaria.controller.ZoneController;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ZoneCitiesView extends JFrame {
    private ZoneController zoneController;
    private JList<String> cityList;
    private DefaultListModel<String> cityListModel;

    public ZoneCitiesView(int providerId) {
        this.zoneController = new ZoneController();
        setTitle("Ciudades por Zona");
        setSize(400, 300);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Panel superior con la etiqueta
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Ciudades en la Zona:"));
        add(topPanel, BorderLayout.NORTH);

        // Lista de ciudades
        cityListModel = new DefaultListModel<>();
        cityList = new JList<>(cityListModel);
        add(new JScrollPane(cityList), BorderLayout.CENTER);

        // Cargar las ciudades de la zona del proveedor
        loadCities(providerId);
    }

    private void loadCities(int providerId) {
        int idZone = zoneController.getZones(providerId);
        if (idZone != -1) {
            List<String> cities = zoneController.getCitiesByZone(idZone);
            cityListModel.clear();
            for (String city : cities) {
                cityListModel.addElement(city);
            }
        } else {
            JOptionPane.showMessageDialog(this, "El proveedor no tiene una zona asignada.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
