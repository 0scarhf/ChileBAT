package vista;

import controlador.ControladorInventario;
import modelo.Producto;
import modelo.TipoMarca;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NuevoProducto extends JFrame {
    private JPanel panel1;
    private JButton INGRESARPRODUCTOButton;
    private JButton CANCELARButton;
    private JComboBox comboBox1Marca;
    private JTextField textField1Nombre;
    private JTextField textField2ID;
    private JTextField textField3Precio;
    private JTextField textField1Cantidad;

    private ControladorInventario controlInv;
    public NuevoProducto(ControladorInventario controlInv) {
        this.controlInv = controlInv;
        setContentPane(panel1);
        setTitle("Crear Nuevo Producto");
        setSize(500, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        cargarMarcas();

        //EVENTOS

        //INGRESAR
        INGRESARPRODUCTOButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarNuevoProducto();
            }
        });

        //CANCELAR
        CANCELARButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }

    private void cargarMarcas() {
        comboBox1Marca.removeAllItems();
        comboBox1Marca.addItem("Seleccione una marca...");

        for (TipoMarca marca : TipoMarca.values()) {
            if (marca != TipoMarca.GENERICO) {
                comboBox1Marca.addItem(obtenerTextoMarca(marca));
            }
        }
    }

    private void guardarNuevoProducto() {
        String nombre = textField1Nombre.getText().trim();
        String idTexto = textField2ID.getText().trim();
        String precioTexto = textField3Precio.getText().trim();
        String cantidadTexto = textField1Cantidad.getText().trim(); // <--- NUEVO
        String marcaTexto = (String) comboBox1Marca.getSelectedItem();

        if (nombre.isEmpty() || idTexto.isEmpty() || precioTexto.isEmpty() || cantidadTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor complete todos los campos.");
            return;
        }

        if (marcaTexto == null || marcaTexto.equals("Seleccione una marca...")) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una marca.");
            return;
        }

        if (controlInv.existeProductoConNombre(nombre)) {
            JOptionPane.showMessageDialog(this, "El nombre '" + nombre + "' ya existe.", "Nombre Duplicado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int id = Integer.parseInt(idTexto);
            int precio = Integer.parseInt(precioTexto);
            int cantidadInicial = Integer.parseInt(cantidadTexto); // <--- NUEVO

            if (precio < 0) {
                JOptionPane.showMessageDialog(this, "El precio no puede ser negativo.");
                return;
            }
            if (cantidadInicial < 0) {
                JOptionPane.showMessageDialog(this, "La cantidad inicial no puede ser negativa.");
                return;
            }

            if (controlInv.buscarProducto(id) != null) {
                JOptionPane.showMessageDialog(this, "Ya existe un producto con el ID " + id, "ID Duplicado", JOptionPane.ERROR_MESSAGE);
                return;
            }

            TipoMarca marcaEnum = buscarEnumPorTexto(marcaTexto);
            Producto nuevoProducto = new Producto(id, nombre, marcaEnum, precio, cantidadInicial, 5);
            controlInv.actualizarProducto(nuevoProducto);
            JOptionPane.showMessageDialog(this, "Producto creado exitosamente con stock inicial: " + cantidadInicial);
            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID, Precio y Cantidad deben ser números enteros.");
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

    private TipoMarca buscarEnumPorTexto(String textoBonito) {
        for (TipoMarca marca : TipoMarca.values()) {
            if (obtenerTextoMarca(marca).equals(textoBonito)) {
                return marca;
            }
        }
        return TipoMarca.GENERICO;
    }
}
