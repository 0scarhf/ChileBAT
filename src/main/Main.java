package main;

import modelo.Distribuidor; // Importamos la nueva clase
import modelo.Producto;
import modelo.Vendedor;
import vista.VistaVentas;
import controlador.ControladorVentas;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // 1. Simular Base de Datos (Inventario)
        List<Producto> inventario = new ArrayList<>();
        inventario.add(new Producto(101, "Pall Mall Azules", 4800, 50, 10));
        inventario.add(new Producto(102, "Dunhill International", 6500, 20, 5));
        inventario.add(new Producto(103, "Kent Silver Neo", 5200, 0, 10));
        inventario.add(new Producto(200, "Filtros OCB Slim", 1500, 100, 20));
        Vendedor miVendedor = new Vendedor(1, "Roberto Gomez");
        Distribuidor miCliente = new Distribuidor(99, "Botillería El Cielo", "76.123.456-K", "Av. Siempre Viva 123");
        VistaVentas miVista = new VistaVentas();
        ControladorVentas controlador = new ControladorVentas(miVendedor, inventario, miVista, miCliente);
        controlador.iniciar();
    }
}