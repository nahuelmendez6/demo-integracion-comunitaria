package com.integracioncomunitaria.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import repository.ProviderRepository;
import repository.UserRepository;

public class ProviderView extends JFrame {
    private int userId; // ID del usuario
    private ProviderRepository providerService; // Servicio para manejar proveedores
    private UserRepository userRepository; // Repositorio para manejar usuarios
    private JTable table; // Tabla para mostrar los datos
    private DefaultTableModel tableModel; // Modelo de la tabla

    public ProviderView(int userId) {
        this.userId = userId;

        // Inicialización de repositorios
        providerService = new ProviderRepository();
        userRepository = new UserRepository();

        // Configuración de la ventana
        setTitle("Perfil del Proveedor");
        setSize(600, 400);
        setLocationRelativeTo(null); // Centra la ventana
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cierra solo esta ventana al salir

        // Inicializar componentes
        initializeComponents();
    }

    private void initializeComponents() {
        String providerCategory = providerService.getProviderCategory(userId); // Obtener la categoría del proveedor

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Modelo de tabla
        tableModel = new DefaultTableModel(new String[]{"Atributo", "Valor", "Acciones"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Hacer que solo la columna de acciones sea editable
                return column == 2;
            }
        };

        // Agregar datos iniciales
        tableModel.addRow(new Object[]{"Categoría del Proveedor", providerCategory != null ? providerCategory : "No asignada", "Editar"});

        // Crear la tabla
        table = new JTable(tableModel);

        // Renderizador y editor para la columna de acciones
        table.getColumn("Acciones").setCellRenderer(new ButtonRenderer());
        table.getColumn("Acciones").setCellEditor(new ButtonEditor(new JCheckBox(), this, providerService, userRepository, userId));

        // Configuración de la tabla
        table.setRowHeight(30);

        // Agregar la tabla a un panel con scroll
        JScrollPane scrollPane = new JScrollPane(table);

        // Botón para volver al menú principal
        JButton backButton = new JButton("Volver al Menú Principal");
        backButton.addActionListener(e -> dispose()); // Cierra esta ventana

        // Agregar componentes al panel principal
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(backButton, BorderLayout.SOUTH);

        // Agregar el panel principal a la ventana
        add(mainPanel);
    }

    // Clase para renderizar los botones en la tabla
    private static class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setText("Editar");
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    // Clase para manejar la edición desde los botones en la tabla
    private static class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private ProviderView parent;
        private ProviderRepository providerService;
        private UserRepository userRepository;
        private int userId;

        public ButtonEditor(JCheckBox checkBox, ProviderView parent, ProviderRepository providerService, UserRepository userRepository, int userId) {
            super(checkBox);
            this.parent = parent;
            this.providerService = providerService;
            this.userRepository = userRepository;
            this.userId = userId;

            button = new JButton("Editar");
            button.addActionListener(e -> {
                int row = parent.table.getSelectedRow();
                String attribute = (String) parent.tableModel.getValueAt(row, 0);

                if ("Categoría del Proveedor".equals(attribute)) {
                    // Crear un ComboBox con las categorías
                    JComboBox<String> cmbCategory = new JComboBox<>(userRepository.getCategories().toArray(new String[0]));
                    cmbCategory.setSelectedItem(parent.tableModel.getValueAt(row, 1));

                    // Mostrar un cuadro de diálogo para que el usuario seleccione una categoría
                    int option = JOptionPane.showConfirmDialog(parent, cmbCategory, "Seleccionar Categoría", JOptionPane.OK_CANCEL_OPTION);

                    if (option == JOptionPane.OK_OPTION) {
                        String newCategory = (String) cmbCategory.getSelectedItem();
                        // Actualizar la tabla
                        parent.tableModel.setValueAt(newCategory, row, 1);

                        // Obtener el ID de la categoría seleccionada y actualizar en la base de datos
                        int categoryId = providerService.getCategoryId(newCategory);
                        providerService.updateProviderCategory(userId, categoryId);
                    }
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return button;
        }
    }
}
