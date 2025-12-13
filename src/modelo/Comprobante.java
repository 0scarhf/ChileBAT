package modelo;

import java.util.Date;
import java.util.Map;

public class Comprobante {

    private int idComprobante;
    private Date fechaEmision;
    private Pedido pedidoAsociado;
    private TipoDocumento tipoDocumento;

    public Comprobante(int id, Pedido pedido, TipoDocumento tipo) {
        this.idComprobante = id;
        this.fechaEmision = new Date();
        this.pedidoAsociado = pedido;
        this.tipoDocumento = tipo;
    }

    public String generarReporte() {
        StringBuilder sb = new StringBuilder();
        // Usamos "\n" para saltos de línea y formateo simple.
        sb.append("=== BAT CHILE - ").append(tipoDocumento.name()).append(" #").append(idComprobante).append(" ===\n");
        sb.append("Fecha: ").append(fechaEmision).append("\n");
        // *Corrección* Se asume que getCliente() y getRut() no devuelven null.
        sb.append("Cliente: ").append(pedidoAsociado.getCliente().getNombre()).append("\n");
        sb.append("RUT: ").append(pedidoAsociado.getCliente().getRut()).append("\n");
        sb.append("----------------------------------------------------\n");
        // Usamos String.format para alinear, mejor que solo \t
        sb.append(String.format("%-25s %5s %8s %10s\n", "Producto", "Cant", "P.Unit", "Subtotal"));
        sb.append("----------------------------------------------------\n");

        for (Map.Entry<Producto, Integer> entry : pedidoAsociado.getProductos().entrySet()) {
            Producto p = entry.getKey();
            int cant = entry.getValue();
            int precioUnit = p.obtenerPrecio();
            int subtotal = precioUnit * cant;

            sb.append(String.format("%-25s %5d %8d %10d\n", 
                                    p.getMarca(), cant, precioUnit, subtotal));
        }

        sb.append("----------------------------------------------------\n");
        sb.append("TOTAL A PAGAR: $").append(pedidoAsociado.getMontoTotal()).append("\n");

        return sb.toString();
    }
    
    // *Importante*: Necesitas Getters si el Controlador o la Vista quieren acceder a los datos.
    public int getIdComprobante() { return idComprobante; }
    public TipoDocumento getTipoDocumento() { return tipoDocumento; }
}
