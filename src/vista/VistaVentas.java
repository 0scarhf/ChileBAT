package vista;

import modelo.Pedido;
import modelo.Producto;
import modelo.Comprobante;
import java.util.Scanner;
import java.util.List;

public class VistaVentas {
    private Scanner scanner;

    public VistaVentas() {
        scanner = new Scanner(System.in);
    }

    public void mostrarMenu() {
        System.out.println("\n--- BAT CHILE: GESTIÓN DE VENTAS ---");
        System.out.println("1. Iniciar Venta a Distribuidor");
        System.out.println("2. Ver Inventario (Stock en Tiempo Real)");
        System.out.println("3. Salir");
        System.out.print("Seleccione opción: ");
    }

    public int leerEntero() {
        try { return Integer.parseInt(scanner.nextLine()); }
        catch (NumberFormatException e) { return -1; }
    }

    public int pedirIdProducto() {
        System.out.print("Ingrese ID Producto (0 para finalizar): ");
        return leerEntero();
    }

    public void mostrarInventario(List<Producto> productos) {
        System.out.println("\n--- INVENTARIO DISPONIBLE ---");
        System.out.println("ID\tSTOCK\tPRECIO\tNOMBRE");
        for (Producto p : productos) {
            String alerta = p.esStockCritico() ? " [!] BAJO STOCK" : "";
            System.out.println(p.getIdProducto() + "\t" + p.getStock() + "\t$" + p.obtenerPrecio() + "\t" + p.getNombre() + alerta);
        }
    }

    //* Emision de comprobantes
    public void imprimirComprobante(Comprobante comprobante) {
        System.out.println("\n" + comprobante.generarReporte());
    }

    public void mostrarMensaje(String msg) {
        System.out.println(">> " + msg);
    }

    //* Confirmacion para generar factura
    public String solicitarTipoComprobante() {
        System.out.print("¿Generar (B)oleta o (F)actura?: ");
        return scanner.nextLine().toUpperCase();
    }
}