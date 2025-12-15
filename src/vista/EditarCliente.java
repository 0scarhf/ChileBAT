package vista;

import javax.swing.*;
import java.awt.event.*;
import controlador.ControladorSistemaVentas;
import modelo.Distribuidor;

public class EditarCliente extends JFrame {

    private JPanel panel1;
    private JButton aceptarButton;
    private JButton cancelarButton;
    private JTextField textFieldRut;
    private JTextField textFieldNombre;
    private JTextField textFieldDireccion;
    private JComboBox<String> comboBoxTipo;
    private final ControladorSistemaVentas controlador;
    private final Distribuidor cliente;

    public EditarCliente(ControladorSistemaVentas control, Distribuidor clienteAEditar) {
        this.controlador = control;
        this.cliente = clienteAEditar;
        setContentPane(panel1);
        setTitle("Editar Cliente: " + cliente.getNombre());
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        llenarComboTipos();
        cargarDatosEnFormulario();
        initListeners();
    }

    private void cargarDatosEnFormulario() {
        textFieldRut.setText(cliente.getRut());
        textFieldNombre.setText(cliente.getNombre());
        textFieldDireccion.setText(cliente.getDireccion());
        comboBoxTipo.setSelectedItem(cliente.getTipo());
        //textFieldRut.setEditable(false);
    }

    private void llenarComboTipos() {
        comboBoxTipo.addItem("Distribuidor");
        comboBoxTipo.addItem("Mayorista");
        comboBoxTipo.addItem("Minorista");
        comboBoxTipo.addItem("Kiosco");
    }

    private void initListeners() {
        cancelarButton.addActionListener(e -> dispose());

        aceptarButton.addActionListener(e -> guardarCambios());
    }

    private void guardarCambios() {
        String nuevoRut = textFieldRut.getText().trim();
        String nuevoNombre = textFieldNombre.getText().trim();
        String nuevaDireccion = textFieldDireccion.getText().trim();
        String nuevoTipo = (String) comboBoxTipo.getSelectedItem();

        if (nuevoRut.isEmpty() || nuevoNombre.isEmpty() || nuevaDireccion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
            return;
        }

        if (!nuevoRut.equalsIgnoreCase(cliente.getRut())) {

            Distribuidor posibleDuplicado = controlador.buscarCliente(nuevoRut);

            if (posibleDuplicado != null) {
                JOptionPane.showMessageDialog(this,
                        "Error: El RUT " + nuevoRut + " ya pertenece a otro cliente.\nNo se puede usar.",
                        "RUT Duplicado",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        if (!nuevoRut.equalsIgnoreCase(cliente.getRut())) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Estás seguro de cambiar el RUT de este cliente?\nEsto actualizará sus datos permanentemente.",
                    "Confirmar cambio de RUT",
                    JOptionPane.YES_NO_OPTION);

            if (confirm != JOptionPane.YES_OPTION) return;
        }

        cliente.setRut(nuevoRut);
        cliente.setNombre(nuevoNombre);
        cliente.setDireccion(nuevaDireccion);
        cliente.setTipo(nuevoTipo);

        boolean exito = controlador.actualizarCliente();

        if (exito) {
            JOptionPane.showMessageDialog(this, "Cliente actualizado correctamente.");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error al guardar los cambios.");
        }
    }
}