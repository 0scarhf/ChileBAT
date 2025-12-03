package modelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Pedido {
    private int idPedido;
    private Date fecha;
    private int montoTotal;
    private List<Producto> productos;
    private Distribuidor cliente;

    public Pedido(int idPedido, Distribuidor cliente) {
        this.idPedido = idPedido;
        this.cliente = cliente; // <-- Asignamos al cliente
        this.fecha = new Date();
        this.productos = new ArrayList<>();
        this.montoTotal = 0;
    }

    public void agregarProducto(Producto p) {
        productos.add(p);
        calcularMonto();
    }

    public void calcularMonto() {
        montoTotal = 0;
        for (Producto p : productos) {
            montoTotal += p.obtenerPrecio();
        }
    }

    public Distribuidor getCliente() { return cliente; }
    public int getMontoTotal() { return montoTotal; }
    public List<Producto> getProductos() { return productos; }
    public int getIdPedido() { return idPedido; }
}