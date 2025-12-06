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

        sb.append("=== BAT CHILE - ")
                .append(tipoDocumento.name())
                .append(" #")
                .append(idComprobante)
                .append(" ===\n");

        sb.append("Fecha: ").append(fechaEmision).append("\n");
        sb.append("Cliente: ").append(pedidoAsociado.getCliente().getNombre()).append("\n");
        sb.append("RUT: ").append(pedidoAsociado.getCliente().getRut()).append("\n");
        sb.append("----------------------------------------------\n");
        sb.append("Producto\tCant\tP.Unit\tSubtotal\n");
        sb.append("----------------------------------------------\n");

        for (Map.Entry<Producto, Integer> entry : pedidoAsociado.getProductos().entrySet()) {

            Producto p = entry.getKey();
            int cant = entry.getValue();
            int precioUnit = p.obtenerPrecio();
            int subtotal = precioUnit * cant;

            sb.append(p.getNombre()).append("\t")
                    .append(cant).append("\t$")
                    .append(precioUnit).append("\t$")
                    .append(subtotal).append("\n");
        }

        sb.append("----------------------------------------------\n");
        sb.append("TOTAL A PAGAR: $").append(pedidoAsociado.getMontoTotal()).append("\n");

        return sb.toString();
    }
}
