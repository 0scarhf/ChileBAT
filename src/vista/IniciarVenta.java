package vista;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import controlador.ControladorSistemaVentas;
import modelo.Distribuidor;
import modelo.Pedido;

public class IniciarVenta extends JFrame {
    private JPanel panel1;
    private JButton siguienteButton;
    private JButton cancelarButton;
    private JTextField textFieldRut;
    private JComboBox<String> comboBoxNombre;
    private JTextField textFieldDireccion;
    private JComboBox<String> comboBoxTipo;
    private JButton nuevoClienteButton;

    private final ControladorSistemaVentas controlVentas;
    private List<Distribuidor> listaTodosLosClientes;

    // Banderas para controlar el flujo de eventos
    private boolean isProgrammaticUpdate = false;
    private boolean isFiltering = false;

    public IniciarVenta(ControladorSistemaVentas control) {
        this.controlVentas = control;

        setContentPane(panel1);
        setTitle("Iniciar Venta");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        cargarListas();
        initListeners();
    }

    private void cargarListas() {
        listaTodosLosClientes = controlVentas.obtenerClientes();

        // 1. Configurar Combo Tipo
        comboBoxTipo.removeAllItems();
        comboBoxTipo.addItem("- Todos -");
        comboBoxTipo.addItem("Distribuidor");
        comboBoxTipo.addItem("Mayorista");
        comboBoxTipo.addItem("Minorista");
        comboBoxTipo.addItem("Kiosco");

        // 2. Configurar Combo Nombre (Inicial)
        cargarNombresEnCombo(listaTodosLosClientes);

        // 3. ACTIVAR AUTOCOMPLETADO AQUÍ
        configurarAutocompletado(comboBoxNombre, listaTodosLosClientes);

        textFieldDireccion.setEditable(false);
    }

    // --- NUEVO MÉTODO: LÓGICA DE AUTOCOMPLETADO ---
    private void configurarAutocompletado(JComboBox<String> comboBox, List<Distribuidor> listaBase) {
        // Hacemos que se pueda escribir en el combo
        comboBox.setEditable(true);

        // Obtenemos el componente de texto interno del ComboBox
        JTextComponent editor = (JTextComponent) comboBox.getEditor().getEditorComponent();

        editor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                // Ignorar teclas de navegación (flechas, enter, escape)
                if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN ||
                        e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    return;
                }

                SwingUtilities.invokeLater(() -> {
                    isFiltering = true; // Avisamos que estamos filtrando

                    String textoEscrito = editor.getText();
                    int caretPos = editor.getCaretPosition(); // Guardamos posición del cursor

                    // 1. Filtrar la lista base
                    List<String> resultados = new ArrayList<>();
                    for (Distribuidor d : listaBase) {
                        // Búsqueda insensible a mayúsculas/minúsculas
                        if (d.getNombre().toUpperCase().contains(textoEscrito.toUpperCase())) {
                            resultados.add(d.getNombre());
                        }
                    }

                    // 2. Actualizar el modelo del combo
                    comboBox.removeAllItems();
                    if (textoEscrito.isEmpty()) {
                        // Si borró todo, mostrar mensaje por defecto
                        comboBox.addItem("- Seleccione Cliente -");
                        for(Distribuidor d : listaBase) comboBox.addItem(d.getNombre());
                    } else {
                        // Agregar coincidencias
                        for (String s : resultados) {
                            comboBox.addItem(s);
                        }
                    }

                    // 3. Restaurar el texto y el cursor (Porque removeAllItems lo borra)
                    editor.setText(textoEscrito);
                    try {
                        editor.setCaretPosition(caretPos); // Restaurar cursor
                    } catch (Exception ex) { } // Ignorar error de rango si ocurre

                    // 4. Mostrar el desplegable si hay resultados
                    if (!resultados.isEmpty() && !textoEscrito.isEmpty()) {
                        comboBox.showPopup();
                    }

                    isFiltering = false; // Terminamos de filtrar
                });
            }
        });
    }

    private void cargarNombresEnCombo(List<Distribuidor> listaParaMostrar) {
        isProgrammaticUpdate = true;
        comboBoxNombre.removeAllItems();
        comboBoxNombre.addItem("- Seleccione Cliente -");
        for (Distribuidor c : listaParaMostrar) {
            comboBoxNombre.addItem(c.getNombre());
        }
        isProgrammaticUpdate = false;
    }

    private void initListeners() {

        // --- MODIFICACIÓN IMPORTANTE EN EL LISTENER DEL COMBO ---
        comboBoxNombre.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Si estamos escribiendo (filtrando) o el sistema está actualizando, NO rellenar datos
                if (isProgrammaticUpdate || isFiltering) return;

                // Al ser editable, getSelectedItem() devuelve lo que está escrito (String)
                String nombreSeleccionado = (String) comboBoxNombre.getSelectedItem();

                // Solo rellenamos si el texto coincide exactamente con un cliente real
                if (nombreSeleccionado != null && !nombreSeleccionado.equals("- Seleccione Cliente -")) {
                    rellenarDatosPorNombre(nombreSeleccionado);
                }
            }
        });

        // ... RESTO DE TUS LISTENERS IGUAL QUE ANTES ...

        // Listener RUT
        textFieldRut.addActionListener(e -> buscarYRellenarPorRut());

        // Listener Tipo
        comboBoxTipo.addActionListener(e -> {
            if (isProgrammaticUpdate) return;
            filtrarNombresPorTipoSeleccionado();
        });

        cancelarButton.addActionListener(e -> dispose());
        siguienteButton.addActionListener(e -> procesarVenta());
        nuevoClienteButton.addActionListener(e -> {
            NuevoCliente v = new NuevoCliente(controlVentas);
            v.setVisible(true);
            v.addWindowListener(new WindowAdapter(){
                public void windowClosed(WindowEvent e){ cargarListas(); }
            });
        });
    }

    // ... TUS MÉTODOS AUXILIARES (filtrarNombresPorTipoSeleccionado, rellenarDatosPorNombre, etc.) ...
    // Asegúrate de incluir aquí los métodos que hicimos en la respuesta anterior

    private void filtrarNombresPorTipoSeleccionado() {
        String tipoSeleccionado = (String) comboBoxTipo.getSelectedItem();
        if (tipoSeleccionado == null || tipoSeleccionado.equals("- Todos -")) {
            cargarNombresEnCombo(listaTodosLosClientes);
            limpiarCamposMenosCombos();
            return;
        }
        List<Distribuidor> listaFiltrada = new ArrayList<>();
        for (Distribuidor d : listaTodosLosClientes) {
            if (d.getTipo().equalsIgnoreCase(tipoSeleccionado)) {
                listaFiltrada.add(d);
            }
        }
        cargarNombresEnCombo(listaFiltrada);
        textFieldRut.setText("");
        textFieldDireccion.setText("");
    }

    private void rellenarDatosPorNombre(String nombre) {
        for (Distribuidor c : listaTodosLosClientes) {
            if (c.getNombre().equalsIgnoreCase(nombre)) { // EqualsIgnoreCase para asegurar match
                isProgrammaticUpdate = true;
                textFieldRut.setText(c.getRut());
                textFieldDireccion.setText(c.getDireccion());
                comboBoxTipo.setSelectedItem(c.getTipo());
                isProgrammaticUpdate = false;
                return;
            }
        }
    }

    private void buscarYRellenarPorRut() {
        if (isProgrammaticUpdate) return;
        String rutIngresado = textFieldRut.getText().trim();
        if (rutIngresado.isEmpty()) return;

        Distribuidor encontrado = null;
        for (Distribuidor c : listaTodosLosClientes) {
            if (c.getRut().equalsIgnoreCase(rutIngresado)) {
                encontrado = c;
                break;
            }
        }
        if (encontrado != null) {
            isProgrammaticUpdate = true;
            // Al encontrar por RUT, ponemos el nombre en el combo (que ahora es editable)
            comboBoxNombre.setSelectedItem(encontrado.getNombre());
            textFieldDireccion.setText(encontrado.getDireccion());
            comboBoxTipo.setSelectedItem(encontrado.getTipo());
            isProgrammaticUpdate = false;
        } else {
            JOptionPane.showMessageDialog(this, "RUT no encontrado.");
        }
    }

    private void limpiarCamposMenosCombos() {
        textFieldRut.setText("");
        textFieldDireccion.setText("");
    }

    private void procesarVenta() {
        String rut = textFieldRut.getText().trim();
        Distribuidor cliente = controlVentas.buscarCliente(rut);
        if (cliente != null) {
            Pedido nuevoPedido = controlVentas.iniciarPedido(cliente);
            GenerarPedido pantallaPedido = new GenerarPedido(controlVentas, nuevoPedido);
            pantallaPedido.setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente válido.");
        }
    }
}