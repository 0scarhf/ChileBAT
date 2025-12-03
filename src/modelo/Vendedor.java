package modelo;

public class Vendedor extends Empleado {
    private String rol;

    public Vendedor(int id, String nombre) {
        super(id, nombre);
        this.rol = "Vendedor";
    }

    public Pedido iniciarVenta(int idPedidoNuevo, Distribuidor cliente) {
        return new Pedido(idPedidoNuevo, cliente);
    }
}