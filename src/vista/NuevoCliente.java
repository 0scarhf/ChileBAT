package vista;

import controlador.ControladorSistemaVentas;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NuevoCliente extends JFrame {

    // Componentes (Asegúrate de que coincidan con el "Field Name" en el .form)
    private JPanel panel1;
    private JTextField textFieldNombre;     // Antes textField1
    private JTextField textFieldRut;        // Antes textField2
    private JTextField textFieldDireccion;  // Antes textField3
    private JComboBox<String> comboBoxTipo; // Antes comboBox1
    private JButton crearNuevoClienteButton;
    private JButton cancelarButton;

    private final ControladorSistemaVentas control;

    public NuevoCliente(ControladorSistemaVentas control) {
        this.control = control;

        // Configuración de la ventana
        setContentPane(panel1);
        setTitle("Ingresar Nuevo Cliente");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Cargar tipos en el ComboBox
        cargarComboTipos();

        // Activar botones
        initListeners();
    }

    private void cargarComboTipos() {
        comboBoxTipo.addItem("Distribuidor");
        comboBoxTipo.addItem("Mayorista");
        comboBoxTipo.addItem("Minorista");
        comboBoxTipo.addItem("Kiosco");
    }

    private void initListeners() {
        // --- BOTÓN CANCELAR ---
        cancelarButton.addActionListener(e -> dispose());

        // --- BOTÓN CREAR ---
        crearNuevoClienteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarCliente();
            }
        });
    }

    private void guardarCliente() {
        // 1. Obtener datos de los campos
        String nombre = textFieldNombre.getText().trim();
        String rut = textFieldRut.getText().trim();
        String direccion = textFieldDireccion.getText().trim();
        String tipo = (String) comboBoxTipo.getSelectedItem();

        // 2. Validaciones básicas (Campos vacíos)
        if (nombre.isEmpty() || rut.isEmpty() || direccion.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Todos los campos son obligatorios.",
                    "Error de Validación",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 3. Validación de formato de RUT (Opcional, pero recomendada)
        if (rut.length() < 8 || !rut.contains("-")) {
            JOptionPane.showMessageDialog(this,
                    "El formato del RUT parece incorrecto (ej: 12345678-9)",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            // No hacemos return aquí, dejamos pasar si quieres ser flexible
        }

        // 4. Llamar al controlador para guardar
        boolean exito = control.crearNuevoCliente(rut, nombre, direccion, tipo);

        if (exito) {
            JOptionPane.showMessageDialog(this, "¡Cliente creado exitosamente!");
            // Limpiar campos por si quiere crear otro
            textFieldNombre.setText("");
            textFieldRut.setText("");
            textFieldDireccion.setText("");
            // O cerrar la ventana directamente:
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Error: El RUT ingresado ya existe en el sistema.",
                    "Error al guardar",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}