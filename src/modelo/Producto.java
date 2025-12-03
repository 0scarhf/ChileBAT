package modelo;

public class Producto {
    private int idProducto;
    private String nombre;
    private int valorUnitario;
    private int stock;

    public Producto(int id, String nombre, int valor, int stock) {
        this.idProducto = id;
        this.nombre = nombre;
        this.valorUnitario = valor;
        this.stock = stock;
    }

    public int obtenerPrecio() {
        return valorUnitario;
    }

    public void actualizarStock(int cantidad) {
        this.stock -= cantidad;
    }

    // Getters
    public int getIdProducto() { return idProducto; }
    public String getNombre() { return nombre; }
    public int getStock() { return stock; }
}