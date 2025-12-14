package modelo;

import java.util.Objects;

public class Producto {

    private int idProducto;
    private String nombre;
    private TipoMarca marca;
    private int valorUnitario;
    private int stock;
    private int stockMinimo;

    // CONSTRUCTOR PRINCIPAL ACTUALIZADO
    public Producto(int id, String nombre, TipoMarca marca, int valor, int stock, int stockMinimo) {
        this.idProducto = id;
        this.nombre = nombre;
        this.marca = marca;
        this.valorUnitario = valor;
        this.stock = stock;
        this.stockMinimo = stockMinimo;
    }

    public Producto(int idProducto) {
        this.idProducto = idProducto;
        this.nombre = "Desconocido";
        this.marca = TipoMarca.GENERICO;
        this.valorUnitario = 0;
        this.stock = 0;
        this.stockMinimo = 0;
    }

    public boolean esStockCritico() {
        return stock <= stockMinimo;
    }

    public void restarStock(int cantidad) {
        this.stock -= cantidad;
    }

    public void aumentarStock(int cantidad) {
        this.stock += cantidad;
    }

    public int getIdProducto() { return idProducto; }

    public String getNombre() { return nombre; } // Nuevo getter

    public TipoMarca getMarca() { return marca; }

    public int obtenerPrecio() { return valorUnitario; }

    public int getStock() { return stock; }

    public int getStockMinimo() { return stockMinimo; }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setValorUnitario(int valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public void setMarca(TipoMarca marca) {this.marca = marca;}

    public void setNombre(String nombre) {this.nombre = nombre;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Producto)) return false;
        Producto producto = (Producto) o;
        return idProducto == producto.idProducto;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProducto);
    }

    @Override
    public String toString() {
        return idProducto + " - " + nombre + " ($" + valorUnitario + ")";
    }
}
