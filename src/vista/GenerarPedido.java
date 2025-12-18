package vista;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import controlador.ControladorSistemaVentas;
import modelo.*;

public class GenerarPedido extends JFrame {

    // Componentes
    private JPanel panel1;
    private JTextField textFieldRutCliente;
    private JTextField textFieldNombreCliente;

    // CAMBIO: Ahora es ComboBox
    private JComboBox<String> comboBoxID;

    private JComboBox<String> comboBoxMarca;
    private JComboBox<String> comboBoxNombreProducto;
    private JTextField textFieldPrecio;
    private JTextField textFieldStockDisponible;
    private JComboBox<TipoDocumento> comboBoxTipoComprobante;
    private JButton emitirComprobanteButton;
    private JButton cancelarButton;
    private JTextField textFieldVentaStock;
    private JComboBox comboBoxMedioDePago;

    private final ControladorSistemaVentas control;
    private final Pedido pedidoActual;
    private List<Producto> listaGlobalProductos;
    private List<Producto> listaFiltradaProductos;

    private boolean isProgrammaticUpdate = false;
    private boolean isFiltering = false;

    public GenerarPedido(ControladorSistemaVentas control, Pedido pedido) {
        this.control = control;
        this.pedidoActual = pedido;

        setContentPane(panel1);
        setTitle("Realizar Venta");
        setSize(600, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        cargarDatosCliente();
        inicializarInventario();
        configurarCombosIniciales();

        // Configuramos autocompletado para el nombre
        configurarAutocompletadoNombre();

        // Hacemos que el ID sea editable (para poder escribir)
        comboBoxID.setEditable(true);

        initListeners();
    }

    private String obtenerTextoMarca(Producto p) {
        if (p.getMarca() == null) return "Sin Marca";
        return convertirMarcaATexto(p.getMarca());
    }

    private String convertirMarcaATexto(TipoMarca marca) {
        switch (marca) {
            case PALL_MALL: return "Pall Mall";
            case LUCKY_STRIKE: return "Lucky Strike";
            case GOLD_LEAF: return "Gold Leaf";
            case DUNHILL: return "Dunhill";
            case KENT: return "Kent";
            case CAMEL: return "Camel";
            case OCB: return "OCB";
            case GENERICO: return "Genérico";
            default: return marca.toString();
        }
    }

    private void cargarDatosCliente() {
        Distribuidor cliente = pedidoActual.getCliente();
        textFieldRutCliente.setText(cliente.getRut());
        textFieldNombreCliente.setText(cliente.getNombre());

        textFieldRutCliente.setEditable(false);
        textFieldNombreCliente.setEditable(false);
        textFieldPrecio.setEditable(false);
        textFieldStockDisponible.setEditable(false);
    }

    private void inicializarInventario() {
        listaGlobalProductos = control.obtenerTodosLosProductos();
        listaFiltradaProductos = new ArrayList<>(listaGlobalProductos);
    }

    private void configurarCombosIniciales() {
        comboBoxTipoComprobante.removeAllItems();
        for (TipoDocumento tipo : TipoDocumento.values()) {
            comboBoxTipoComprobante.addItem(tipo);
        }

        comboBoxMedioDePago.removeAllItems();
        for (TipoMedioPago mp : TipoMedioPago.values()) {
            comboBoxMedioDePago.addItem(mp);
        }

        comboBoxMarca.removeAllItems();
        comboBoxMarca.addItem("- Todas -");
        for (TipoMarca m : TipoMarca.values()) {
            comboBoxMarca.addItem(convertirMarcaATexto(m));
        }

        actualizarListasDesplegables(listaGlobalProductos);
    }

    private void actualizarListasDesplegables(List<Producto> lista) {
        isProgrammaticUpdate = true;
        //Nombres
        comboBoxNombreProducto.removeAllItems();
        comboBoxNombreProducto.addItem("- Seleccione Producto -");

        //IDs
        comboBoxID.removeAllItems();
        comboBoxID.addItem("- ID -"); // Opción por defecto

        for (Producto p : lista) {
            comboBoxNombreProducto.addItem(p.getNombre());
            comboBoxID.addItem(String.valueOf(p.getIdProducto()));
        }

        isProgrammaticUpdate = false;
    }

    private void initListeners() {
        cancelarButton.addActionListener(e -> dispose());
        comboBoxMarca.addActionListener(e -> {
            if (isProgrammaticUpdate) return;
            filtrarPorMarca();
        });
        comboBoxID.addActionListener(e -> {
            if (isProgrammaticUpdate) return;
            buscarPorSeleccionDeID();
        });
        comboBoxNombreProducto.addActionListener(e -> {
            if (isProgrammaticUpdate || isFiltering) return;
            rellenarDatosDesdeNombre();
        });
        emitirComprobanteButton.addActionListener(e -> finalizarVenta());
    }

    private void filtrarPorMarca() {
        String marcaSel = (String) comboBoxMarca.getSelectedItem();

        if (marcaSel == null || marcaSel.equals("- Todas -")) {
            listaFiltradaProductos = new ArrayList<>(listaGlobalProductos);
        } else {
            listaFiltradaProductos = new ArrayList<>();
            for (Producto p : listaGlobalProductos) {
                if (obtenerTextoMarca(p).equals(marcaSel)) {
                    listaFiltradaProductos.add(p);
                }
            }
        }
        actualizarListasDesplegables(listaFiltradaProductos);
        limpiarCamposProducto();
    }

    private void buscarPorSeleccionDeID() {
        // Obtenemos lo que está escrito o seleccionado
        Object item = comboBoxID.getSelectedItem();
        if (item == null || item.equals("- ID -")) {
            limpiarCamposProducto();
            return;
        }

        String textoID = item.toString().trim();
        if (textoID.isEmpty()) return;

        try {
            int idBuscado = Integer.parseInt(textoID);
            Producto encontrado = null;

            // Buscamos en la Global para poder cambiar la marca si es necesario
            for (Producto p : listaGlobalProductos) {
                if (p.getIdProducto() == idBuscado) {
                    encontrado = p;
                    break;
                }
            }

            if (encontrado != null) {
                isProgrammaticUpdate = true;

                // 1. Ajustar Marca si es diferente
                String marcaBonita = obtenerTextoMarca(encontrado);
                if (!marcaBonita.equals(comboBoxMarca.getSelectedItem())) {
                    if (encontrado.getMarca() != null) {
                        comboBoxMarca.setSelectedItem(marcaBonita);
                    } else {
                        comboBoxMarca.setSelectedIndex(0);
                    }
                    // Forzamos el refiltrado de las listas
                    filtrarPorMarca();
                    isProgrammaticUpdate = true; // Volvemos a bloquear tras filtrar
                }

                // 2. Seleccionar ID (por si se filtró y se perdió la selección visual)
                comboBoxID.setSelectedItem(String.valueOf(encontrado.getIdProducto()));

                // 3. Seleccionar Nombre
                comboBoxNombreProducto.setSelectedItem(encontrado.getNombre());

                // 4. Llenar campos
                textFieldPrecio.setText(String.valueOf(encontrado.obtenerPrecio()));
                textFieldStockDisponible.setText(String.valueOf(encontrado.getStock()));

                // 5. Foco a cantidad
                textFieldVentaStock.requestFocus();

                isProgrammaticUpdate = false;

            } else {
                JOptionPane.showMessageDialog(this, "ID no encontrado en el sistema.");
                // Si no existe, limpiamos
                isProgrammaticUpdate = true;
                textFieldPrecio.setText("");
                textFieldStockDisponible.setText("");
                textFieldVentaStock.setText("");
                comboBoxNombreProducto.setSelectedItem("- Seleccione Producto -");
                isProgrammaticUpdate = false;
            }

        } catch (NumberFormatException e) {
            // Si escribió letras en el ID
            // No mostramos error invasivo, solo limpiamos o ignoramos
        }
    }

    private void rellenarDatosDesdeNombre() {
        String nombreSel = (String) comboBoxNombreProducto.getSelectedItem();

        if (nombreSel == null || nombreSel.equals("- Seleccione Producto -")) {
            limpiarCamposProducto();
            return;
        }

        for (Producto p : listaFiltradaProductos) {
            if (p.getNombre().equals(nombreSel)) {
                isProgrammaticUpdate = true;

                // Sincronizamos el ID
                comboBoxID.setSelectedItem(String.valueOf(p.getIdProducto()));

                textFieldPrecio.setText(String.valueOf(p.obtenerPrecio()));
                textFieldStockDisponible.setText(String.valueOf(p.getStock()));

                // Ajustamos marca visualmente si hiciera falta (opcional)
                comboBoxMarca.setSelectedItem(obtenerTextoMarca(p));

                textFieldVentaStock.requestFocus();
                isProgrammaticUpdate = false;
                return;
            }
        }
    }

    private void limpiarCamposProducto() {
        if (isProgrammaticUpdate) return;
        isProgrammaticUpdate = true;
        // No borramos el ID ni el Nombre aquí para no entorpecer la escritura
        textFieldPrecio.setText("");
        textFieldStockDisponible.setText("");
        textFieldVentaStock.setText("");
        isProgrammaticUpdate = false;
    }

    private void configurarAutocompletadoNombre() {
        comboBoxNombreProducto.setEditable(true);
        JTextComponent editor = (JTextComponent) comboBoxNombreProducto.getEditor().getEditorComponent();

        editor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_UP ||
                        e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_ESCAPE) return;

                SwingUtilities.invokeLater(() -> {
                    isFiltering = true;
                    String texto = editor.getText();
                    List<String> coincidencias = new ArrayList<>();

                    for (Producto p : listaFiltradaProductos) {
                        if (p.getNombre().toUpperCase().contains(texto.toUpperCase())) {
                            coincidencias.add(p.getNombre());
                        }
                    }

                    if (!coincidencias.isEmpty()) {
                        comboBoxNombreProducto.setModel(new DefaultComboBoxModel<>(coincidencias.toArray(new String[0])));
                        comboBoxNombreProducto.setSelectedItem(texto);
                        comboBoxNombreProducto.showPopup();
                    } else {
                        comboBoxNombreProducto.hidePopup();
                    }
                    isFiltering = false;
                });
            }
        });
    }

    private void finalizarVenta() {
        Object itemID = comboBoxID.getSelectedItem();
        String idTxt = (itemID != null) ? itemID.toString() : "";

        if (idTxt.isEmpty() || idTxt.equals("- ID -")) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un producto válido.");
            return;
        }

        String cantidadTxt = textFieldVentaStock.getText().trim();

        if (cantidadTxt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar la cantidad.", "Faltan datos", JOptionPane.WARNING_MESSAGE);
            textFieldVentaStock.requestFocus();
            return;
        }

        try {
            int idProd = Integer.parseInt(idTxt);
            int cantidad = Integer.parseInt(cantidadTxt);

            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a 0.");
                return;
            }

            boolean agregado = control.agregarProducto(pedidoActual, idProd, cantidad);

            if (agregado) {
                TipoDocumento tipoDoc = (TipoDocumento) comboBoxTipoComprobante.getSelectedItem();
                TipoMedioPago medioPago = (TipoMedioPago) comboBoxMedioDePago.getSelectedItem();
                Comprobante comprobante = control.finalizarPedido(pedidoActual, tipoDoc, medioPago);

                EmitirComprobante ticketVisual = new EmitirComprobante(this, comprobante);
                ticketVisual.setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo realizar la venta (Stock insuficiente).");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Datos numéricos inválidos.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}