package controlador;

import modelo.*;
import persistencia.PersistenciaDatos;

import java.util.List;

public class ControladorSistemaVentas {

    private List<Distribuidor> clientes;
    private Vendedor vendedor;
    private ControladorInventario inventarioCtrl;

    public ControladorSistemaVentas(ControladorInventario inventarioCtrl) {
        this.inventarioCtrl = inventarioCtrl;
        this.clientes = PersistenciaDatos.cargarClientes();
        this.vendedor = new Vendedor(1, "Vendedor Principal");
    }

    public List<Distribuidor> obtenerClientes() {
        return clientes;
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

    public Pedido iniciarPedido(Distribuidor cliente) {
        return new Pedido(generarIdPedido(), cliente);
    }

    public boolean agregarProducto(Pedido pedido, int idProducto, int cantidad) {

        Producto p = inventarioCtrl.buscarProducto(idProducto);

        if (p == null || cantidad <= 0)
            return false;

        if (p.getStock() < cantidad)
            return false;

        inventarioCtrl.descontarStock(p, cantidad);
        pedido.agregarProducto(p, cantidad);

        return true;
    }

    public boolean agregarProducto(Pedido pedido, int idProducto) {
        return agregarProducto(pedido, idProducto, 1);
    }

    public boolean esStockCritico(int idProducto) {
        Producto p = inventarioCtrl.buscarProducto(idProducto);
        return p != null && inventarioCtrl.esStockCritico(p);
    }

    public Comprobante finalizarPedido(Pedido pedido, TipoDocumento tipo) {

        Comprobante comprobante = new Comprobante(
                pedido.getIdPedido() + 5000,
                pedido,
                tipo
        );

        PersistenciaDatos.guardarVenta(pedido);
        inventarioCtrl.guardarInventario();
        PersistenciaDatos.guardarComprobante(comprobante);

        return comprobante;
    }

    private int generarIdPedido() {
        return (int)(System.currentTimeMillis() % 100000);
    }
}
