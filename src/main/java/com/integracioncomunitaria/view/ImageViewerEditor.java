package com.integracioncomunitaria.view;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.Map;

class ImageViewerButtonEditor extends DefaultCellEditor {
    private JButton button;
    private String filePath;
    private boolean clicked;
    private List<Map<String, Object>> attachments;

    public ImageViewerButtonEditor(JCheckBox checkBox, List<Map<String, Object>> attachments) {
        super(checkBox);
        this.attachments = attachments;

        button = new JButton("Abrir");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clicked = true;
                showImage(filePath);
                fireEditingStopped();
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        filePath = (String) table.getValueAt(row, 1);
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

    private void showImage(String filePath) {
        if (!isImageFile(filePath)) {
            JOptionPane.showMessageDialog(null, "No es un archivo de imagen.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        File file = new File(filePath);
        if (!file.exists()) {
            JOptionPane.showMessageDialog(null, "El archivo no existe: " + filePath, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ImageIcon imageIcon = new ImageIcon(filePath);
        Image image = imageIcon.getImage().getScaledInstance(600, 400, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(image);

        JDialog imageDialog = new JDialog();
        imageDialog.setTitle("Visualización de Imagen");
        imageDialog.setSize(650, 450);
        imageDialog.setLayout(new BorderLayout());

        JLabel imageLabel = new JLabel(scaledIcon);
        JScrollPane scrollPane = new JScrollPane(imageLabel);

        imageDialog.add(scrollPane, BorderLayout.CENTER);
        imageDialog.setLocationRelativeTo(null);
        imageDialog.setVisible(true);
    }

    private boolean isImageFile(String filePath) {
        String lowerCasePath = filePath.toLowerCase();
        return lowerCasePath.endsWith(".jpg") || lowerCasePath.endsWith(".jpeg") ||
               lowerCasePath.endsWith(".png") || lowerCasePath.endsWith(".gif");
    }
}
