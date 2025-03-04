package com.integracioncomunitaria.view;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.Map;

class ButtonEditor extends DefaultCellEditor {
    private JButton button;
    private String path;
    private boolean clicked;
    private List<Map<String, Object>> attachments;

    public ButtonEditor(JCheckBox checkBox, List<Map<String, Object>> attachments) {
        super(checkBox);
        this.attachments = attachments;

        button = new JButton("Abrir");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clicked = true;
                openFile(path);
                fireEditingStopped();
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        path = (String) table.getValueAt(row, 1);
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return "Abrir";
    }

    @Override
    public boolean stopCellEditing() {
        clicked = false;
        return super.stopCellEditing();
    }

    private void openFile(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                Desktop.getDesktop().open(file);
            } else {
                JOptionPane.showMessageDialog(null, "El archivo no existe: " + filePath, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "No se pudo abrir el archivo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

