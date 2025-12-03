package modelo;

public class Producto {
    private int idProducto;
    private String nombre;
    private int valorUnitario;
    private int stock;
    private int stockMinimo;

    public Producto(int id, String nombre, int valor, int stock, int stockMinimo) {
        this.idProducto = id;
        this.nombre = nombre;
        this.valorUnitario = valor;
        this.stock = stock;
        this.stockMinimo = stockMinimo;
    }

    public boolean esStockCritico() {
        return stock <= stockMinimo;
    }

    public int obtenerPrecio() { return valorUnitario; }

    public void actualizarStock(int cantidad) {
        this.stock -= cantidad;
    }

    public int getIdProducto() { return idProducto; }
    public String getNombre() { return nombre; }
    public int getStock() { return stock; }
}