package persistencia;

import modelo.*;
import java.io.*;
import java.util.*;

public class PersistenciaDatos {

    private static final String PATH_PRODUCTOS = "src/productos";
    private static final String PATH_CLIENTES = "src/clientes";
    private static final String PATH_VENTAS = "src/ventas";
    private static final String PATH_COMPROBANTES = "src/comprobantes";

    public static List<Distribuidor> cargarClientes() {
        List<Distribuidor> lista = new ArrayList<>();

        try (Scanner sc = new Scanner(new File(PATH_CLIENTES), "UTF-8")) {

            sc.useDelimiter("[,\\r\\n]+");

            while (sc.hasNext()) {
                String rut = sc.next().trim();
                String nombre = sc.next().trim();
                String direccion = sc.next().trim();
                String tipo = sc.next().trim();

                lista.add(new Distribuidor(rut, nombre, direccion, tipo));
            }

        } catch (Exception e) {
            System.out.println("Error cargando clientes: " + e.getMessage());
        }

        return lista;
    }


    public static List<Producto> cargarProductos() {
        List<Producto> lista = new ArrayList<>();

        try (Scanner sc = new Scanner(new File(PATH_PRODUCTOS))) {

            sc.useDelimiter("[;\\r\\n]+");

            while (sc.hasNext()) {
                int id = sc.nextInt();
                String nombre = sc.next().trim();
                int precio = sc.nextInt();
                int stock = sc.nextInt();
                int stockMin = sc.nextInt();

                lista.add(new Producto(id, nombre, precio, stock, stockMin));
            }

        } catch (Exception e) {
            System.out.println("Error cargando productos: " + e.getMessage());
        }

        return lista;
    }


    public static List<Pedido> cargarVentas() {
        List<Pedido> ventas = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(PATH_VENTAS))) {

            String linea;
            while ((linea = br.readLine()) != null) {

                // Formato: id*rut*monto*id:cant,id:cant
                String[] partes = linea.split("\\*");

                int idPedido = Integer.parseInt(partes[0]);
                String rutCliente = partes[1];
                int montoTotal = Integer.parseInt(partes[2]);

                String productosStr = partes[3];
                String[] pares = productosStr.split(",");

                Map<Integer, Integer> mapa = new LinkedHashMap<>();

                for (String par : pares) {
                    String[] info = par.split(":");
                    int idProd = Integer.parseInt(info[0]);
                    int cantidad = Integer.parseInt(info[1]);
                    mapa.put(idProd, cantidad);
                }

                ventas.add(new Pedido(idPedido, rutCliente, montoTotal, mapa));
            }

        } catch (Exception e) {
            System.out.println("Error cargando ventas: " + e.getMessage());
        }

        return ventas;
    }


    public static void guardarProductos(List<Producto> lista) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(PATH_PRODUCTOS))) {

            for (Producto p : lista) {
                pw.println(
                        p.getIdProducto() + ";" +
                                p.getNombre() + ";" +
                                p.obtenerPrecio() + ";" +
                                p.getStock() + ";" +
                                p.getStockMinimo()
                );
            }

        } catch (Exception e) {
            System.out.println("Error guardando productos.");
        }
    }


    public static void guardarVenta(Pedido p) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(PATH_VENTAS, true))) {

            StringBuilder productos = new StringBuilder();

            for (Map.Entry<Producto, Integer> entry : p.getProductos().entrySet()) {
                Producto pr = entry.getKey();
                int cant = entry.getValue();

                productos.append(pr.getIdProducto())
                        .append(":")
                        .append(cant)
                        .append(",");
            }


            if (productos.length() > 0)
                productos.deleteCharAt(productos.length() - 1);

            pw.println(
                    p.getIdPedido() + "*" +
                            p.getCliente().getRut() + "*" +
                            p.getMontoTotal() + "*" +
                            productos
            );

        } catch (Exception e) {
            System.out.println("Error guardando venta.");
        }
    }


    public static void guardarComprobante(Comprobante c) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(PATH_COMPROBANTES, true))) {

            pw.println(c.generarReporte());
            pw.println("========================================");

        } catch (Exception e) {
            System.out.println("Error guardando comprobante.");
        }
    }
}
