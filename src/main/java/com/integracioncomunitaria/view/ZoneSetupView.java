package com.integracioncomunitaria.view;

import com.integracioncomunitaria.controller.ZoneController;
import com.integracioncomunitaria.controller.AddressController;
import com.integracioncomunitaria.database.ResultDataBase;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.ArrayList;

public class ZoneSetupView extends JFrame {
    private int providerId;
    private JComboBox<String> cmbProvince;
    private JPanel departmentPanel;
    private JPanel cityPanel;
    private JButton btnSave;
    private ZoneController controller;
    private AddressController addressController;
    private Map<String, List<JCheckBox>> departmentCityMap = new HashMap<>();
    private Map<String, List<JCheckBox>> provinceDepartmentMap = new HashMap<>();

    public ZoneSetupView(int providerId) {
        this.providerId = providerId;
        controller = new ZoneController();
        addressController = new AddressController();

        setTitle("Configuración de Zona de Trabajo");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel provincePanel = new JPanel();
        provincePanel.add(new JLabel("Selecciona una Provincia:"));
        cmbProvince = new JComboBox<>(addressController.loadProvinces());
        provincePanel.add(cmbProvince);

        departmentPanel = new JPanel();
        departmentPanel.setLayout(new GridLayout(0, 1));

        cityPanel = new JPanel();
        cityPanel.setLayout(new GridLayout(0, 1));

        btnSave = new JButton("Guardar Zona");
        btnSave.addActionListener(e -> saveZone());

        add(provincePanel, BorderLayout.NORTH);
        add(new JScrollPane(departmentPanel), BorderLayout.WEST);
        add(new JScrollPane(cityPanel), BorderLayout.CENTER);
        add(btnSave, BorderLayout.SOUTH);

        cmbProvince.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                updateDepartmentCheckboxes((String) e.getItem());
            }
        });

        updateDepartmentCheckboxes((String) cmbProvince.getSelectedItem());
    }

    private void updateDepartmentCheckboxes(String province) {
        departmentPanel.removeAll();
        departmentCityMap.clear();
        int id_province = addressController.getProvinceId(province);
        

        List<String> departments = addressController.loadDepartments(id_province);
        List<JCheckBox> departmentCheckBoxes = new ArrayList<>();

        for (String department : departments) {
            JCheckBox checkBox = new JCheckBox(department);
            checkBox.addItemListener(e -> {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    updateCityCheckboxes(department);
                } else {
                    clearCityCheckboxes(department);
                }
            });
            departmentCheckBoxes.add(checkBox);
            departmentPanel.add(checkBox);
        }
        
        provinceDepartmentMap.put(province, departmentCheckBoxes);
        departmentPanel.revalidate();
        departmentPanel.repaint();
    }

    

    private void updateCityCheckboxes(String department) {

        int id_departament = addressController.getDepartmentId(department);

        List<String> cities = addressController.loadCities(id_departament);
        List<JCheckBox> checkBoxes = new ArrayList<>();

        for (String city : cities) {
            JCheckBox checkBox = new JCheckBox(city);
            checkBoxes.add(checkBox);
            cityPanel.add(checkBox);
        }

        departmentCityMap.put(department, checkBoxes);
        cityPanel.revalidate();
        cityPanel.repaint();
    }

    private void clearCityCheckboxes(String department) {
        List<JCheckBox> checkBoxes = departmentCityMap.remove(department);
        if (checkBoxes != null) {
            for (JCheckBox checkBox : checkBoxes) {
                cityPanel.remove(checkBox);
            }
        }
        cityPanel.revalidate();
        cityPanel.repaint();
    }

    private void saveZone() {
        //String zoneName = JOptionPane.showInputDialog(this, "Nombre de la Zona:", "Definir Zona", JOptionPane.PLAIN_MESSAGE);

        //if (zoneName == null || zoneName.trim().isEmpty()) {
        //    JOptionPane.showMessageDialog(this, "Debe ingresar un nombre para la zona.", "Error", JOptionPane.ERROR_MESSAGE);
        //    return;
        //}

        int idZone = controller.createZone(providerId);
        if (idZone == -1) {
            JOptionPane.showMessageDialog(this, "Error al crear la zona.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Integer> cityIds = new ArrayList<>();
        for (List<JCheckBox> checkBoxes : departmentCityMap.values()) {
            for (JCheckBox checkBox : checkBoxes) {
                if (checkBox.isSelected()) {
                    int cityId = controller.getCityIdByName(checkBox.getText());
                    if (cityId != -1) {
                        cityIds.add(cityId);
                    }
                }
            }
        }

        if (cityIds.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar al menos una ciudad.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ResultDataBase result = controller.saveZoneCities(idZone, cityIds);
        JOptionPane.showMessageDialog(this, result.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);

        if (result.getSuccess()) {
            new MainMenuView(providerId).setVisible(true);
            dispose();
        }
    }
}
