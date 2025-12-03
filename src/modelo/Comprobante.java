package modelo;

import java.util.Date;

public class Comprobante {
    private int idComprobante;
    private Date fechaEmision;
    private Pedido pedidoAsociado;
    private TipoDocumento tipoDocumento;

    // Constructor actualizado
    public Comprobante(int id, Pedido pedido, TipoDocumento tipo) {
        this.idComprobante = id;
        this.fechaEmision = new Date();
        this.pedidoAsociado = pedido;
        this.tipoDocumento = tipo;
    }

    public String generarReporte() {
        StringBuilder sb = new StringBuilder();
        // .name() convierte el ENUM a texto automáticamente
        sb.append("=== BAT CHILE - ").append(tipoDocumento.name()).append(" #").append(idComprobante).append(" ===\n");
        sb.append("Fecha: ").append(fechaEmision).append("\n");
        sb.append("------------------------------\n");
        for (Producto p : pedidoAsociado.getProductos()) {
            sb.append(p.getNombre()).append("\t\t$").append(p.obtenerPrecio()).append("\n");
        }
        sb.append("------------------------------\n");
        sb.append("TOTAL A PAGAR: $").append(pedidoAsociado.getMontoTotal()).append("\n");
        return sb.toString();
    }
}
