package vista;

import controlador.ControladorInventario;
import modelo.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VerInventario extends javax.swing.JFrame {
    private JPanel panel1;
    private JButton editarButton;
    private JButton cancelarButton;
    private JTable InventarioTabla;
    private JComboBox<String> cmbFiltroMarca;
    private JTextField BuscarPorIDjTextField;
    private JButton BuscarBoton;
    private JPanel mainPanel;
    private JButton eliminarProductoButton;

    private final ControladorInventario controlInv;
    private TableRowSorter<DefaultTableModel> sorter;

    public VerInventario(ControladorInventario controlInv) {
        this.controlInv = controlInv;
        setContentPane(mainPanel);
        setTitle("Listado de Inventario");
        setSize(700, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        inicializarTabla();
        configurarFiltroMarca();
        configurarEventos();

    }

    private void configurarEventos() {
        BuscarBoton.addActionListener(e -> filtrarTablaPorID());
        BuscarPorIDjTextField.addActionListener(e -> filtrarTablaPorID());
        cmbFiltroMarca.addActionListener(e -> filtrarPorMarca());
        cancelarButton.addActionListener(e -> dispose());
        editarButton.addActionListener(e -> abrirDialogoEditar());
        eliminarProductoButton.addActionListener(e -> eliminarProductoSeleccionado());
    }

    private void eliminarProductoSeleccionado() {
        int filaSeleccionada = InventarioTabla.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, seleccione un producto de la tabla para eliminarlo.",
                    "Ningún producto seleccionado",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int filaModelo = InventarioTabla.convertRowIndexToModel(filaSeleccionada);
        int idProducto = (int) InventarioTabla.getModel().getValueAt(filaModelo, 0);
        String nombreProducto = (String) InventarioTabla.getModel().getValueAt(filaModelo, 1);

        int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea eliminar el producto: " + nombreProducto + "?\nEsta acción no se puede deshacer.",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (respuesta == JOptionPane.YES_OPTION) {
            boolean exito = controlInv.eliminarProducto(idProducto);

            if (exito) {
                cargarDatosEnTabla((DefaultTableModel) InventarioTabla.getModel());
                JOptionPane.showMessageDialog(this, "Producto eliminado correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "Error al intentar eliminar el producto.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void abrirDialogoEditar() {
        int filaSeleccionada = InventarioTabla.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Debes seleccionar un producto de la tabla para editarlo.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int filaModelo = InventarioTabla.convertRowIndexToModel(filaSeleccionada);
        int idProducto = (int) InventarioTabla.getModel().getValueAt(filaModelo, 0);
        Producto producto = controlInv.buscarProductoPorID(idProducto);

        if (producto != null) {
            EditarInventario dialog = new EditarInventario(this, producto);
            dialog.setVisible(true);

            if (dialog.isGuardado()) {
                controlInv.actualizarProducto(producto);
                cargarDatosEnTabla((DefaultTableModel) InventarioTabla.getModel());

                JOptionPane.showMessageDialog(this, "Producto actualizado correctamente.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Error: No se encontró el producto con ID " + idProducto);
        }
    }

    private void inicializarTabla() {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"ID", "Nombre", "Marca", "Precio", "Stock", "Stock Mínimo"}, 0
        ) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 3) return Double.class;
                if (columnIndex == 0 || columnIndex == 4 || columnIndex == 5) return Integer.class;
                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        InventarioTabla.setModel(model);
        cargarDatosEnTabla(model);
        sorter = new TableRowSorter<>(model);
        InventarioTabla.setRowSorter(sorter);
        configurarEstilosTabla();
    }

    private void cargarDatosEnTabla(DefaultTableModel model) {
        model.setRowCount(0);

        List<Producto> lista = controlInv.obtenerInventario();
        for (Producto p : lista) {
            model.addRow(new Object[]{
                    p.getIdProducto(),
                    p.getNombre(),
                    obtenerTextoMarca(p),
                    p.obtenerPrecio(),
                    p.getStock(),
                    p.getStockMinimo()
            });
        }
    }

    private void configurarEstilosTabla() {
        // Anchos de columna
        InventarioTabla.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        InventarioTabla.getColumnModel().getColumn(1).setPreferredWidth(200); // Nombre
        InventarioTabla.getColumnModel().getColumn(2).setPreferredWidth(100); // Marca

        // Centrar columnas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        InventarioTabla.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        InventarioTabla.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        InventarioTabla.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);

        // Formato de Precio
        DefaultTableCellRenderer precioRenderer = new DefaultTableCellRenderer() {
            @Override
            protected void setValue(Object value) {
                if (value instanceof Number) {
                    setText("$ " + value.toString());
                } else {
                    super.setValue(value);
                }
            }
        };
        precioRenderer.setHorizontalAlignment(JLabel.CENTER);
        InventarioTabla.getColumnModel().getColumn(3).setCellRenderer(precioRenderer);
    }

    private void configurarFiltroMarca() {
        cmbFiltroMarca.removeAllItems();
        cmbFiltroMarca.addItem("Todas las marcas");

        Set<String> marcasUnicas = new HashSet<>();
        List<Producto> lista = controlInv.obtenerInventario();

        for (Producto p : lista) {
            String nombreMarca = obtenerTextoMarca(p);
            if (!nombreMarca.isEmpty()) {
                marcasUnicas.add(nombreMarca);
            }
        }

        for (String marca : marcasUnicas) {
            cmbFiltroMarca.addItem(marca);
        }
    }

    private void filtrarPorMarca() {
        String marcaSeleccionada = (String) cmbFiltroMarca.getSelectedItem();
        BuscarPorIDjTextField.setText("");

        if (marcaSeleccionada == null || marcaSeleccionada.equals("Todas las marcas")) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("^" + marcaSeleccionada + "$", 2));
        }
    }

    private void filtrarTablaPorID() {
        String texto = BuscarPorIDjTextField.getText().trim();
        cmbFiltroMarca.setSelectedIndex(0);

        if (texto.length() == 0) {
            sorter.setRowFilter(null);
        } else {
            try {
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto, 0));
            } catch (java.util.regex.PatternSyntaxException e) {
                sorter.setRowFilter(null);
            }
        }
    }

    private String obtenerTextoMarca(Producto p) {
        if (p.getMarca() == null) {
            return "Sin Marca";
        }

        switch (p.getMarca()) {
            case PALL_MALL:
                return "Pall Mall";

            case LUCKY_STRIKE:
                return "Lucky Strike";

            case GOLD_LEAF:
                return "Gold Leaf";

            case DUNHILL:
                return "Dunhill";

            case KENT:
                return "Kent";

            case CAMEL:
                return "Camel";

            case OCB:
                return "OCB";

            case GENERICO:
                return "Genérico";

            default:
                return p.getMarca().toString();
        }
    }
}
