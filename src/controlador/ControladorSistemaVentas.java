package controlador;

import modelo.*;
import persistencia.PersistenciaDatos;
import java.util.List;

public class ControladorSistemaVentas {
    private final ControladorInventario inventarioCtrl;
    private final List<Distribuidor> clientes;
    private final Vendedor vendedor;
    private final List<Pedido> ventasHistoricas;


    public ControladorSistemaVentas(ControladorInventario inventarioCtrl) {
        this.inventarioCtrl = inventarioCtrl;
        this.clientes = PersistenciaDatos.cargarClientes();
        this.vendedor = new Vendedor(1, "Vendedor Principal");
        this.ventasHistoricas = PersistenciaDatos.cargarVentas(inventarioCtrl);
    }

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
        Producto p = inventarioCtrl.buscarProductoPorID(idProducto);
        // Delega la verificación al ControladorInventario
        return p != null && inventarioCtrl.esStockCritico(p); 
    }

    public Pedido iniciarPedido(Distribuidor cliente) {
        return new Pedido(generarIdPedido(), cliente);
    }

    public boolean agregarProducto(Pedido pedido, int idProducto, int cantidad) {

        Producto p = inventarioCtrl.buscarProductoPorID(idProducto);

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

    private int generarIdPedido() {
        return (int)(System.currentTimeMillis() % 100000); 
    }

    public boolean eliminarCliente(String rut) {
        Distribuidor clienteAborrar = buscarCliente(rut);
        if (clienteAborrar != null) {
            clientes.remove(clienteAborrar);
            persistencia.PersistenciaDatos.guardarClientes(clientes);
            return true;
        }
        return false;
    }

    public boolean actualizarCliente() {
        try {
            persistencia.PersistenciaDatos.guardarClientes(clientes);
            return true;
        } catch (Exception e) {
            System.out.println("Error al actualizar cliente: " + e.getMessage());
            return false;
        }
    }

    public boolean crearNuevoCliente(String rut, String nombre, String direccion, String tipo) {
        if (buscarCliente(rut) != null) {
            return false;
        }
        Distribuidor nuevoCliente = new Distribuidor(rut, nombre, direccion, tipo);
        this.clientes.add(nuevoCliente);
        persistencia.PersistenciaDatos.guardarClientes(this.clientes);

        return true;
    }

    public java.util.List<modelo.Producto> obtenerTodosLosProductos() {
        return inventarioCtrl.obtenerInventario();
    }
}