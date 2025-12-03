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


    public Pedido(int idPedido, String rutCliente, int montoTotal, Map<Integer, Integer> mapaCantidades) {
        this.idPedido = idPedido;
        this.fecha = new Date();
        this.montoTotal = montoTotal;
        this.productos = new LinkedHashMap<>();
        this.cliente = new Distribuidor(rutCliente, "DESCONOCIDO", "SIN DIRECCIÓN", "SIN TIPO");

        for (Map.Entry<Integer, Integer> entry : mapaCantidades.entrySet()) {
            Producto temporal = new Producto(entry.getKey());   // nuevo constructor en Producto
            productos.put(temporal, entry.getValue());
        }
    }

    public void agregarProducto(Producto p, int cantidad) {
        productos.put(p, productos.getOrDefault(p, cantidad) + cantidad);
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
