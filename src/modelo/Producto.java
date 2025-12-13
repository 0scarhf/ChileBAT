package modelo;

import java.util.Objects;

public class Producto {

    private int idProducto;
    private TipoMarca marca;
    private int valorUnitario;
    private int stock;
    private int stockMinimo;


    public Producto(int id, TipoMarca marca, int valor, int stock, int stockMinimo) {
        this.idProducto = id;
        this.marca = marca;
        this.valorUnitario = valor;
        this.stock = stock;
        this.stockMinimo = stockMinimo;
    }

    public Producto(int idProducto) {
        this.idProducto = idProducto;
        this.marca = marca;
        this.valorUnitario = 0;
        this.stock = 0;
        this.stockMinimo = 0;
    }


    public boolean esStockCritico() {
        return stock <= stockMinimo;
    }

    public int obtenerPrecio() {
        return valorUnitario;
    }

    public void restarStock(int cantidad) {
        this.stock -= cantidad;
    }

    public void aumentarStock(int cantidad) {
        this.stock += cantidad;
    }


    public int getIdProducto() { return idProducto; }
    public TipoMarca getMarca() { return marca; }
    public int getStock() { return stock; }
    public int getStockMinimo() { return stockMinimo; }


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
        return idProducto + " - " + marca;
    }
}
