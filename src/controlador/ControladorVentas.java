package controlador;

import modelo.Producto;
import modelo.Pedido;
import modelo.Vendedor;
import modelo.Distribuidor; // Importar
import modelo.Comprobante;
import modelo.TipoDocumento;
import vista.VistaVentas;
import java.util.List;

public class ControladorVentas {
    private Vendedor vendedorModel;
    private List<Producto> inventarioModel;
    private VistaVentas vista;
    private Distribuidor clienteActual; // Guardamos al cliente de la sesión

    public ControladorVentas(Vendedor vendedor, List<Producto> inventario, VistaVentas vista, Distribuidor cliente) {
        this.vendedorModel = vendedor;
        this.inventarioModel = inventario;
        this.vista = vista;
        this.clienteActual = cliente;
    }

    public void iniciar() {
        boolean ejecutando = true;
        while (ejecutando) {
            vista.mostrarMenu();
            int opcion = vista.leerEntero();

            switch (opcion) {
                case 1:
                    realizarVenta();
                    break;
                case 2:
                    vista.mostrarInventario(inventarioModel);
                    break;
                case 3:
                    ejecutando = false;
                    vista.mostrarMensaje("Cerrando sistema BAT Chile...");
                    break;
                default:
                    vista.mostrarMensaje("Opción incorrecta.");
            }
        }
    }

    private void realizarVenta() {
        Pedido nuevoPedido = vendedorModel.iniciarVenta((int)(System.currentTimeMillis() % 10000), clienteActual);
        vista.mostrarMensaje("Pedido iniciado para cliente: " + clienteActual.getNombre());
        boolean agregando = true;
        while(agregando) {
            vista.mostrarInventario(inventarioModel);
            int idSeleccionado = vista.pedirIdProducto();

            if (idSeleccionado == 0) break;

            Producto prod = buscarProducto(idSeleccionado);

            if (prod != null) {
                if (prod.getStock() > 0) {
                    nuevoPedido.agregarProducto(prod);
                    prod.actualizarStock(1);
                    vista.mostrarMensaje("Agregado: " + prod.getNombre());

                    if (prod.esStockCritico()) {
                        vista.mostrarMensaje("ALERTA: El producto " + prod.getNombre() + " ha llegado a su stock mínimo.");
                    }
                } else {
                    vista.mostrarMensaje("ERROR: No se puede vender. Stock agotado.");
                }
            } else {
                vista.mostrarMensaje("Producto no encontrado.");
            }
        }

        if (!nuevoPedido.getProductos().isEmpty()) {
            finalizarVenta(nuevoPedido);
        } else {
            vista.mostrarMensaje("Venta cancelada.");
        }
    }

    private void finalizarVenta(Pedido p) {
        String tipoInput = vista.solicitarTipoComprobante();
        TipoDocumento tipoSeleccionado;

        if (tipoInput.startsWith("F")) {
            tipoSeleccionado = TipoDocumento.FACTURA;
        } else {
            tipoSeleccionado = TipoDocumento.BOLETA;
        }

        Comprobante comprobante = new Comprobante(
                p.getIdPedido() + 5000,
                p,
                tipoSeleccionado
        );

        vista.imprimirComprobante(comprobante);
    }

    private Producto buscarProducto(int id) {
        for (Producto p : inventarioModel) {
            if (p.getIdProducto() == id) return p;
        }
        return null;
    }
}