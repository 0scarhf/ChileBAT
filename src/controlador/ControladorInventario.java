package controlador;

import modelo.Producto;
import persistencia.PersistenciaDatos;

import java.util.List;

public class ControladorInventario {

    private List<Producto> inventario;

    public ControladorInventario() {
        this.inventario = PersistenciaDatos.cargarProductos();
    }
    //   OBTENER INVENTARIO
    public List<Producto> obtenerInventario() {
        return inventario;
    }
    //   BUSCAR PRODUCTO POR ID
    public Producto buscarProducto(int idProducto) {
        for (Producto p : inventario) {
            if (p.getIdProducto() == idProducto) {
                return p;
            }
        }
        return null;
    }
    //   DESCONTAR STOCK (VENTA)
    public boolean descontarStock(Producto producto, int cantidad) {
        if (producto == null || cantidad <= 0)
            return false;

        if (producto.getStock() < cantidad)
            return false;

        producto.restarStock(cantidad);   // ← CORREGIDO
        guardarInventario();
        return true;
    }
    //   INGRESAR STOCK MANUAL
    public boolean ingresarStock(int idProducto, int cantidad) {
        Producto p = buscarProducto(idProducto);

        if (p == null || cantidad <= 0)
            return false;

        p.aumentarStock(cantidad);        // ← CORREGIDO
        guardarInventario();
        return true;
    }
    //   STOCK CRÍTICO
    public boolean esStockCritico(Producto p) {
        return p.getStock() <= p.getStockMinimo();
    }
    //     GUARDAR INVENTARIO
    public void guardarInventario() {
        PersistenciaDatos.guardarProductos(inventario);
    }
}

