package vista;

import modelo.Producto;
import modelo.TipoMarca;

import javax.swing.*;

import java.awt.*;

public class EditarInventario extends JDialog {
    private JPanel panel1;
    private JButton aceptarButton;
    private JButton cancelarButton;
    private JTextField IDTextField;
    private JTextField NombreTextField;
    private JComboBox<TipoMarca> MarcaComboBox;
    private JTextField PrecioTextField;
    private JTextField StockTextField;
    private JTextField StockMinimoTextField;
    private JLabel Titulo;
    private JPanel MainPanel;

    private Producto productoAEditar;
    private boolean guardado = false;

    public EditarInventario(Frame owner, Producto producto) {
        super(owner, "Editar Producto", true);
        this.productoAEditar = producto;

        setContentPane(MainPanel);
        setSize(500, 450);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        configurarComponentes();
        cargarDatosDelProducto();
        configurarEventos();
    }

    private void configurarComponentes() {
        IDTextField.setEditable(false);
        IDTextField.setFocusable(false);

        MarcaComboBox.setModel(new DefaultComboBoxModel<>(TipoMarca.values()));

        MarcaComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (value instanceof TipoMarca) {
                    setText(obtenerTextoMarca((TipoMarca) value));
                }
                return this;
            }
        });
    }

    private void cargarDatosDelProducto() {
        if (productoAEditar != null) {
            IDTextField.setText(String.valueOf(productoAEditar.getIdProducto()));
            NombreTextField.setText(productoAEditar.getNombre());
            PrecioTextField.setText(String.valueOf(productoAEditar.obtenerPrecio()));
            StockTextField.setText(String.valueOf(productoAEditar.getStock()));
            StockMinimoTextField.setText(String.valueOf(productoAEditar.getStockMinimo()));
            MarcaComboBox.setSelectedItem(productoAEditar.getMarca());
        }
    }

    private void configurarEventos() {
        cancelarButton.addActionListener(e -> dispose());
        aceptarButton.addActionListener(e -> onAceptar());
    }

    private void onAceptar() {
        if (!validarCampos()) return;

        try {
            productoAEditar.setNombre(NombreTextField.getText());
            productoAEditar.setMarca((TipoMarca) MarcaComboBox.getSelectedItem());
            productoAEditar.setValorUnitario(Integer.parseInt(PrecioTextField.getText()));
            productoAEditar.setStock(Integer.parseInt(StockTextField.getText()));
            productoAEditar.setStockMinimo(Integer.parseInt(StockMinimoTextField.getText()));

            guardado = true;
            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error en números: " + ex.getMessage(),
                    "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validarCampos() {
        if (NombreTextField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio.");
            return false;
        }
        return true;
    }

    public boolean isGuardado() {
        return guardado;
    }

    private String obtenerTextoMarca(TipoMarca m) {
        if (m == null) return "Sin Marca";

        switch (m) {
            case PALL_MALL:     return "Pall Mall";
            case LUCKY_STRIKE:  return "Lucky Strike";
            case GOLD_LEAF:     return "Gold Leaf";
            case DUNHILL:       return "Dunhill";
            case KENT:          return "Kent";
            case CAMEL:         return "Camel";
            case OCB:           return "OCB";
            case GENERICO:      return "Genérico";
            default:            return m.toString();
        }
    }

}
