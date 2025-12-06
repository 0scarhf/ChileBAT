package vista;

import controlador.ControladorInventario;
import controlador.ControladorSistemaVentas;
import modelo.*;

import java.util.List;
import java.util.Scanner;

public class UISistema {

    private Scanner sc = new Scanner(System.in);
    private ControladorInventario controlInv;
    private ControladorSistemaVentas controlVentas;

    public UISistema(ControladorInventario inv, ControladorSistemaVentas ventas) {
        this.controlInv = inv;
        this.controlVentas = ventas;
    }

    public void iniciar() {
        boolean ejecutando = true;

        while (ejecutando) {
            mostrarMenu();

            int opcion = leerEntero();

            switch (opcion) {
                case 1 -> flujoVenta();
                case 2 -> flujoIngresarStock();
                case 3 -> mostrarInventario();
                case 4 -> mostrarClientes();
                case 5 -> {
                    System.out.println("Saliendo del sistema BAT Chile...");
                    // Guardar datos antes de salir
                    controlInv.guardarInventario();
                    System.out.println("Datos guardados correctamente.");
                    ejecutando = false;
                }
                default -> System.out.println("Opción inválida.");
            }
        }

        // Cerrar Scanner al salir
        sc.close();
    }

    private void mostrarMenu() {
        System.out.println("\n===== BAT CHILE - SISTEMA =====");
        System.out.println("1. Realizar Venta");
        System.out.println("2. Ingresar Stock");
        System.out.println("3. Ver Inventario");
        System.out.println("4. Ver Clientes");
        System.out.println("5. Salir");
        System.out.print("Seleccione una opción: ");
    }

    private int leerEntero() {
        try {
            return Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
            return -1;
        }
    }


    private void mostrarInventario() {
        List<Producto> lista = controlInv.obtenerInventario();

        System.out.println("\n=== INVENTARIO ===");
        for (Producto p : lista) {
            System.out.println(
                    "[" + p.getIdProducto() + "] " +
                            p.getNombre() +
                            " | Precio: $" + p.obtenerPrecio() +
                            " | Stock: " + p.getStock()
            );
        }
    }

    private void flujoIngresarStock() {
        mostrarInventario();
        System.out.print("\nID Producto: ");
        int id = leerEntero();

        System.out.print("Cantidad a ingresar: ");
        int cantidad = leerEntero();

        boolean ok = controlInv.ingresarStock(id, cantidad);

        if (ok)
            System.out.println("Stock actualizado correctamente.");
        else
            System.out.println("ERROR: No se pudo ingresar el stock.");
    }


    private void mostrarClientes() {
        List<Distribuidor> lista = controlVentas.obtenerClientes();

        System.out.println("\n=== CLIENTES ===");
        for (Distribuidor d : lista) {
            System.out.println(
                    "[" + d.getRut().trim() + "] " +
                            d.getNombre() +
                            " | " + d.getDireccion() +
                            " | Tipo: " + d.getTipo()
            );
        }
    }


    private void flujoVenta() {

        mostrarClientes();
        System.out.print("\nIngrese RUT del Cliente: ");
        String rutCliente = sc.nextLine().trim();

        Distribuidor cliente = controlVentas.buscarCliente(rutCliente);

        if (cliente == null) {
            System.out.println("ERROR: Cliente no existe.");
            return;
        }

        Pedido pedido = controlVentas.iniciarPedido(cliente);

        boolean agregando = true;
        while (agregando) {

            mostrarInventario();
            System.out.print("\nID Producto (0 para terminar): ");
            int idProd = leerEntero();

            if (idProd == 0) break;

            System.out.print("Cantidad: ");
            int cantidad = leerEntero();

            boolean agregado = controlVentas.agregarProducto(pedido, idProd, cantidad);

            if (!agregado) {
                System.out.println("ERROR: Producto no existe o stock insuficiente.");
                continue;
            }

            if (controlVentas.esStockCritico(idProd)) {
                System.out.println("ALERTA: Stock crítico.");
            }
        }

        if (pedido.getProductos().isEmpty()) {
            System.out.println("Venta cancelada.");
            return;
        }

        System.out.print("Factura (F) o Boleta (B): ");
        String tipo = sc.nextLine().toUpperCase();

        TipoDocumento tipoDoc = tipo.startsWith("F")
                ? TipoDocumento.FACTURA
                : TipoDocumento.BOLETA;

        Comprobante comprobante = controlVentas.finalizarPedido(pedido, tipoDoc);

        System.out.println("\n=== COMPROBANTE ===");
        System.out.println(comprobante.generarReporte());
    }
}