package controlador;

import modelo.*;
import persistencia.PersistenciaDatos;
import java.util.List;
import java.util.*;

public class ControladorSistemaVentas {
    private final ControladorInventario inventarioCtrl;
    private final List<Distribuidor> clientes;
    private final Vendedor vendedor;
    private final List<Pedido> ventasHistoricas; // Historial de ventas cargado

    // ============
    // CONSTRUCTOR 
    // ============


    public ControladorSistemaVentas(ControladorInventario inventarioCtrl) {
        this.inventarioCtrl = inventarioCtrl;
        this.clientes = PersistenciaDatos.cargarClientes();
        this.vendedor = new Vendedor(1, "Vendedor Principal");
        this.ventasHistoricas = PersistenciaDatos.cargarVentas(inventarioCtrl);
    }

    // ====================
    // MÉTODOS DE CONSULTA
    // ====================

    public List<Distribuidor> obtenerClientes() {
        return clientes;
    }

    public List<Pedido> obtenerVentasHistoricas() {
        return ventasHistoricas;
    }

    public Distribuidor buscarCliente(String rut) {
        rut = rut.trim();
        for (Distribuidor c : clientes) {
            if (c.getRut().trim().equalsIgnoreCase(rut)) {
                return c;
            }
        }
        return null;
    }
    
    public boolean esStockCritico(int idProducto) {
        Producto p = inventarioCtrl.buscarProducto(idProducto);
        // Delega la verificación al ControladorInventario
        return p != null && inventarioCtrl.esStockCritico(p); 
    }

    // ===============
    // FLUJO DE VENTA
    // ===============

    public Pedido iniciarPedido(Distribuidor cliente) {
        return new Pedido(generarIdPedido(), cliente);
    }

    public boolean agregarProducto(Pedido pedido, int idProducto, int cantidad) {

        Producto p = inventarioCtrl.buscarProducto(idProducto);

        // 1. Validación de Pre-condiciones
        if (p == null || cantidad <= 0 || p.getStock() < cantidad) {
            return false;
        }

        // 2. Modificación del estado del sistema (Delegación de Descuento de Stock)
        // CRÍTICO: La función descontarStock YA llama a guardarInventario()
        inventarioCtrl.descontarStock(p, cantidad); 
        
        // 3. Modificación del estado del Pedido
        pedido.agregarProducto(p, cantidad);

        return true;
    }
    
    public boolean agregarProducto(Pedido pedido, int idProducto) {
        return agregarProducto(pedido, idProducto, 1);
    }

    public Comprobante finalizarPedido(Pedido pedido, TipoDocumento tipo) {

        // 1. Creación del objeto Comprobante
        Comprobante comprobante = new Comprobante(
            pedido.getIdPedido() + 5000, // ID simple de comprobante
            pedido,
            tipo
        );

        // 2. Actualización del estado en memoria
        this.ventasHistoricas.add(pedido); 
        
        // 3. Persistencia
        PersistenciaDatos.guardarVenta(pedido);
        inventarioCtrl.guardarInventario(); 
        PersistenciaDatos.guardarComprobante(comprobante);

        return comprobante;
    }

    // =================
    // MÉTODOS EXTRAS
    // =================

    private int generarIdPedido() {
        return (int)(System.currentTimeMillis() % 100000); 
    }
}