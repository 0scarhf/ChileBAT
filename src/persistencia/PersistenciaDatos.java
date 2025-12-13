package persistencia;

import modelo.*;
import java.io.*;
import java.util.*;
import controlador.*;

public class PersistenciaDatos {
    
    private static final String PATH_PRODUCTOS = "productos.txt";
    private static final String PATH_CLIENTES = "clientes.txt";
    private static final String PATH_VENTAS = "ventas.txt";
    private static final String PATH_COMPROBANTES = "comprobantes.txt";
    
    public static List<Distribuidor> cargarClientes() {
        List<Distribuidor> lista = new ArrayList<>();

        try {
            File archivo = new File(PATH_CLIENTES);
            if (!archivo.exists()) {
                archivo.createNewFile();
                System.out.println("Archivo de clientes no existía, se creó: " + PATH_CLIENTES);
                return lista;
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(archivo), "UTF-8"))) {

                String linea;
                int numeroLinea = 0;
                while ((linea = br.readLine()) != null) {
                    numeroLinea++;
                    linea = linea.trim();

                    // Filtrar líneas vacías y comentarios
                    if (linea.isEmpty() || linea.startsWith("//")) {
                        continue;
                    }

                    // Parsear línea: RUT,Nombre,Dirección,Tipo
                    String[] partes = linea.split(",");
                    if (partes.length >= 4) {
                        String rut = partes[0].trim();
                        String nombre = partes[1].trim();
                        String direccion = partes[2].trim();
                        String tipo = partes[3].trim();

                        if (!rut.isEmpty() && !nombre.isEmpty()) {
                            lista.add(new Distribuidor(rut, nombre, direccion, tipo));
                        } else {
                            System.out.println("Advertencia: Línea " + numeroLinea + " tiene campos vacíos, se omite.");
                        }
                    } else {
                        System.out.println("Advertencia: Línea " + numeroLinea + " tiene formato incorrecto (se esperan 4 campos separados por coma), se omite.");
                    }
                }

            }

        } catch (FileNotFoundException e) {
            System.out.println("Error: No se encontró el archivo de clientes: " + PATH_CLIENTES);
        } catch (IOException e) {
            System.out.println("Error de E/S al cargar clientes: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error inesperado cargando clientes: " + e.getMessage());
        }

        return lista;
    }


    public static List<Producto> cargarProductos() {
        List<Producto> lista = new ArrayList<>();

        try {
            File archivo = new File(PATH_PRODUCTOS);
            if (!archivo.exists()) {
                archivo.createNewFile();
                System.out.println("Archivo de productos no existía, se creó: " + PATH_PRODUCTOS);
                return lista;
            }

            try (Scanner sc = new Scanner(archivo)) {
                sc.useDelimiter("[;\\r\\n]+");

                int numeroRegistro = 0;
                while (sc.hasNext()) {
                    try {
                        numeroRegistro++;
                        int id = sc.nextInt();
                        String nombre = sc.next().trim();
                        int precio = sc.nextInt();
                        int stock = sc.nextInt();
                        int stockMin = sc.nextInt();
                        
                        String marcaBaseStr = nombre.split(" ")[0].trim();
                        TipoMarca marcaEnum = TipoMarca.valueOf(marcaBaseStr.toUpperCase());
                        
                        lista.add(new Producto(id, marcaEnum, precio, stock, stockMin));
                    } catch (NoSuchElementException e) {
                        System.out.println("Advertencia: Registro " + numeroRegistro + " tiene formato incorrecto o datos faltantes, se omite.");
                    } catch (NumberFormatException e) {
                        System.out.println("Advertencia: Registro " + numeroRegistro + " tiene valores numéricos inválidos, se omite.");
                    }
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("Error: No se encontró el archivo de productos: " + PATH_PRODUCTOS);
        } catch (IOException e) {
            System.out.println("Error de E/S al cargar productos: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error inesperado cargando productos: " + e.getMessage());
        }

        return lista;
    }


    public static List<Pedido> cargarVentas(ControladorInventario inventarioCtrl) {
        List<Pedido> ventas = new ArrayList<>();

            try {
                File archivo = new File(PATH_VENTAS);
                if (!archivo.exists()) {
                    archivo.createNewFile();
                    System.out.println("Archivo de ventas no existía, se creó: " + PATH_VENTAS);
                    return ventas;
                }

                try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {

                    String linea;
                    int numeroLinea = 0;
                    while ((linea = br.readLine()) != null) {
                        numeroLinea++;
                        linea = linea.trim();

                        // Filtrar líneas vacías y comentarios
                        if (linea.isEmpty() || linea.startsWith("//")) {
                            continue;
                        }

                        try {
                            // Formato: id*rut*monto*id:cant,id:cant
                            String[] partes = linea.split("\\*");

                            // Validar que tenga exactamente 4 partes
                            if (partes.length != 4) {
                                System.out.println("Advertencia: Línea " + numeroLinea + " tiene formato incorrecto (se esperan 4 partes separadas por *), se omite.");
                                continue;
                            }

                            int idPedido = Integer.parseInt(partes[0].trim());
                            String rutCliente = partes[1].trim();
                            int montoTotal = Integer.parseInt(partes[2].trim());

                            // ❗ SOLUCIÓN MÁS SIMPLE AL ERROR DE COMPILACIÓN: DECLARAR CLIENTE ❗
                            // Creamos un objeto Distribuidor (cliente) stub/temporal para que compile
                            Distribuidor cliente = new Distribuidor(rutCliente, "RUT SIN ENCONTRAR", "SIN DIRECCIÓN", "SIN TIPO");


                            String productosStr = partes[3].trim();
                            if (productosStr.isEmpty()) {
                                System.out.println("Advertencia: Línea " + numeroLinea + " no tiene productos, se omite.");
                                continue;
                            }

                            String[] pares = productosStr.split(",");
                            Map<Integer, Integer> mapa = new LinkedHashMap<>();

                            for (String par : pares) {
                                par = par.trim();
                                if (par.isEmpty()) continue;

                                String[] info = par.split(":");
                                if (info.length != 2) {
                                    System.out.println("Advertencia: Línea " + numeroLinea + " - formato incorrecto en producto (se espera id:cantidad): " + par);
                                    continue;
                                }

                                int idProd = Integer.parseInt(info[0].trim());
                                int cantidad = Integer.parseInt(info[1].trim());
                                mapa.put(idProd, cantidad);
                            }

                            if (!mapa.isEmpty()) {
                                // Ahora 'cliente' existe y el código compila
                                ventas.add(new Pedido(idPedido, cliente));
                            } else {
                                System.out.println("Advertencia: Línea " + numeroLinea + " no tiene productos válidos, se omite.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Advertencia: Línea " + numeroLinea + " tiene valores numéricos inválidos, se omite: " + e.getMessage());
                        } catch (Exception e) {
                            System.out.println("Advertencia: Línea " + numeroLinea + " tiene error al procesar, se omite: " + e.getMessage());
                        }
                    }

                }

            } catch (FileNotFoundException e) {
                System.out.println("Error: No se encontró el archivo de ventas: " + PATH_VENTAS);
            } catch (IOException e) {
                System.out.println("Error de E/S al cargar ventas: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Error inesperado cargando ventas: " + e.getMessage());
            }

            return ventas;
    }


    public static void guardarProductos(List<Producto> lista) {
        try {
            File archivo = new File(PATH_PRODUCTOS);
            if (!archivo.exists()) {
                archivo.createNewFile();
            }

            try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
                for (Producto p : lista) {
                    pw.println(
                            p.getIdProducto() + ";" +
                                    p.getMarca()+ ";" +
                                    p.obtenerPrecio() + ";" +
                                    p.getStock() + ";" +
                                    p.getStockMinimo()
                    );
                }
            }

        } catch (IOException e) {
            System.out.println("Error de E/S al guardar productos: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error inesperado guardando productos: " + e.getMessage());
        }
    }


    public static void guardarVenta(Pedido p) {
        try {
            File archivo = new File(PATH_VENTAS);
            if (!archivo.exists()) {
                archivo.createNewFile();
            }

            try (PrintWriter pw = new PrintWriter(new FileWriter(archivo, true))) {
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
            }

        } catch (IOException e) {
            System.out.println("Error de E/S al guardar venta: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error inesperado guardando venta: " + e.getMessage());
        }
    }


    public static void guardarComprobante(Comprobante c) {
        try {
            File archivo = new File(PATH_COMPROBANTES);
            if (!archivo.exists()) {
                archivo.createNewFile();
            }

            try (PrintWriter pw = new PrintWriter(new FileWriter(archivo, true))) {
                pw.println(c.generarReporte());
                pw.println("========================================");
            }

        } catch (IOException e) {
            System.out.println("Error de E/S al guardar comprobante: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error inesperado guardando comprobante: " + e.getMessage());
        }
    }
}