package modelo;

public class Vendedor extends Empleado {
    private String rol;

    public Vendedor(int id, String nombre) {
        super(id, nombre);
        this.rol = "Vendedor";
    }

    // Métodos específicos del diagrama UML para Vendedor
    public Pedido iniciarVenta(int idPedidoNuevo) {
        // Lógica de negocio: Un vendedor crea un pedido
        return new Pedido(idPedidoNuevo);
    }
}