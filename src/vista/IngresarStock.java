package vista;

import controlador.ControladorInventario;
import modelo.Producto;
import modelo.TipoMarca;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class IngresarStock extends JFrame {
    private ControladorInventario controlInv;
    private JPanel panel1;
    private JButton INGRESARButton;
    private JButton CANCELARButton;
    private JComboBox<String> comboBox1Marca;
    private JComboBox<String> comboBox2Producto;
    private JTextField textField1ID;
    private JLabel IDLabel;
    private JButton NUEVOPRODUCTOButton;
    private JTextField textField1Cantidad;
    private JLabel Titulo;
    private Producto productoSeleccionado = null;

    public IngresarStock(ControladorInventario controlInv) {
        this.controlInv = controlInv;
        setContentPane(panel1);
        setSize(450, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        Titulo.setText("INGRESAR STOCK DE PRODUCTOS");
        setTitle("Ingreso de Stock");
        cargarMarcasEnCombo();

        //EVENTOS
        comboBox1Marca.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarProductosDeLaMarca();
            }
        });

        comboBox2Producto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarIDDelProducto();
            }
        });

        INGRESARButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarStock();
            }
        });

        CANCELARButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        textField1ID.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarYSeleccionarPorID();
            }
        });

        NUEVOPRODUCTOButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NuevoProducto ventanaCrear = new NuevoProducto(controlInv);
                ventanaCrear.setVisible(true);
                dispose();
            }
        });

        setVisible(true);
    }

    private void buscarYSeleccionarPorID() {
        String textoID = textField1ID.getText().trim();
        if (textoID.isEmpty()) {
            return;
        }

        try {
            int idBuscado = Integer.parseInt(textoID);
            Producto p = controlInv.buscarProductoPorID(idBuscado);

            if (p != null) {
                productoSeleccionado = p;
                String nombreMarca = obtenerTextoMarca(p.getMarca());
                comboBox1Marca.setSelectedItem(nombreMarca);
                comboBox2Producto.setSelectedItem(p.getNombre());
                textField1Cantidad.requestFocus();

            } else {
                JOptionPane.showMessageDialog(this,
                        "ID Inválida: No existe un producto con el ID " + idBuscado,
                        "Error de Búsqueda",
                        JOptionPane.WARNING_MESSAGE);
                textField1ID.selectAll();
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número entero.", "Formato Incorrecto", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarMarcasEnCombo() {
        comboBox1Marca.removeAllItems();
        comboBox1Marca.addItem("-- Seleccione una marca --");

        for (TipoMarca marca : TipoMarca.values()) {
            comboBox1Marca.addItem(obtenerTextoMarca(marca));
        }
    }

    private void cargarProductosDeLaMarca() {
        comboBox2Producto.removeAllItems();
        textField1ID.setText("");
        productoSeleccionado = null;

        String marcaSeleccionada = (String) comboBox1Marca.getSelectedItem();

        if (marcaSeleccionada == null || marcaSeleccionada.equals("Seleccione una marca...")) {
            return;
        }


        List<Producto> lista = controlInv.obtenerInventario();
        for (Producto p : lista) {
            if (obtenerTextoMarca(p.getMarca()).equals(marcaSeleccionada)) comboBox2Producto.addItem(p.getNombre());
        }
    }

    private void mostrarIDDelProducto() {
        String nombreProducto = (String) comboBox2Producto.getSelectedItem();

        if (nombreProducto == null) return;

        List<Producto> lista = controlInv.obtenerInventario();
        for (Producto p : lista) {
            if (p.getNombre().equals(nombreProducto)) {
                productoSeleccionado = p; // ¡Lo encontramos!
                textField1ID.setText(String.valueOf(p.getIdProducto()));
                return;
            }
        }
    }

    private void guardarStock() {
        if (productoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Por favor seleccione un producto.");
            return;
        }

        String textoCantidad = textField1Cantidad.getText();
        if (textoCantidad.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese una cantidad.");
            return;
        }

        try {
            int cantidadASumar = Integer.parseInt(textoCantidad);

            if (cantidadASumar <= 0) {
                JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a 0.");
                return;
            }

            int nuevoStock = productoSeleccionado.getStock() + cantidadASumar;
            productoSeleccionado.setStock(nuevoStock);

            controlInv.actualizarProducto(productoSeleccionado);

            JOptionPane.showMessageDialog(this, "Stock actualizado exitosamente.\nNuevo stock: " + nuevoStock);

            textField1Cantidad.setText("");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser un número entero.");
        }
    }

    private String obtenerTextoMarca(TipoMarca marca) {
        if (marca == null) return "";

        switch (marca) {
            case PALL_MALL:   return "Pall Mall";
            case LUCKY_STRIKE: return "Lucky Strike";
            case GOLD_LEAF:   return "Gold Leaf";
            case DUNHILL:     return "Dunhill";
            case KENT:        return "Kent";
            case CAMEL:       return "Camel";
            case OCB:         return "OCB";
            case GENERICO:    return "Genérico";
            default:          return marca.toString();
        }
    }

}
