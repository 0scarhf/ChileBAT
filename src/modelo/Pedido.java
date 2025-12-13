package modelo;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class Pedido {

    private int idPedido;
    private Date fecha;
    private int montoTotal;

    private Map<Producto, Integer> productos;

    private Distribuidor cliente;

    public Pedido(int idPedido, Distribuidor cliente) {
        this.idPedido = idPedido;
        this.cliente = cliente;
        this.fecha = new Date();
        this.productos = new LinkedHashMap<>();
        this.montoTotal = 0;
    }

    public void agregarProducto(Producto p, int cantidad) {
        // Si el producto ya existe, obtener su cantidad actual y sumar la nueva
        // Si no existe, usar 0 como base y sumar la cantidad
        int cantidadActual = productos.getOrDefault(p, 0);
        productos.put(p, cantidadActual + cantidad);
        calcularMonto();
    }


    private void calcularMonto() {
        montoTotal = 0;
        for (Map.Entry<Producto, Integer> entry : productos.entrySet()) {
            Producto prod = entry.getKey();
            int cant = entry.getValue();
            montoTotal += prod.obtenerPrecio() * cant;
        }
    }

    public int getIdPedido() { return idPedido; }

    public Date getFecha() { return fecha; }

    public Distribuidor getCliente() { return cliente; }

    public int getMontoTotal() { return montoTotal; }

    public Map<Producto, Integer> getProductos() { return productos; }

    public int getCantidad(Producto p) { return productos.getOrDefault(p, 0); }
}
