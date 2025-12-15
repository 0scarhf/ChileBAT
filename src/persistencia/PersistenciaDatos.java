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
        File archivo = new File(PATH_PRODUCTOS);

        // 1. Verificación inicial
        if (!archivo.exists()) {
            try {
                archivo.createNewFile();
                System.out.println("Archivo no existía, se creó: " + PATH_PRODUCTOS);
            } catch (IOException e) {
                System.out.println("Error creando archivo vacío: " + e.getMessage());
            }
            return lista;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            int numeroRegistro = 0;

            while ((linea = br.readLine()) != null) {
                numeroRegistro++;
                if (linea.trim().isEmpty()) continue;

                try {
                    String[] partes = linea.split(";");

                    if (partes.length < 5) {
                        System.out.println("Advertencia: Línea " + numeroRegistro + " incompleta. Se omite.");
                        continue;
                    }

                    int id = Integer.parseInt(partes[0].trim());
                    String nombre = partes[1].trim();
                    int precio = Integer.parseInt(partes[2].trim());
                    int stock = Integer.parseInt(partes[3].trim());
                    int stockMin = Integer.parseInt(partes[4].trim());
                    TipoMarca marcaEnum = determinarMarca(nombre);
                    lista.add(new Producto(id, nombre, marcaEnum, precio, stock, stockMin));

                } catch (NumberFormatException e) {
                    System.out.println("Error de formato numérico en línea " + numeroRegistro + ": " + linea);
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: Marca desconocida o datos inválidos en línea " + numeroRegistro + " (" + e.getMessage() + ")");
                }
            }
        } catch (IOException e) {
            System.out.println("Error leyendo el archivo: " + e.getMessage());
        }

        return lista;
    }

    //! Metodo auxiliar para manejar marcas (Pall Mall, Lucky Strike)
    private static TipoMarca determinarMarca(String nombreProducto) {
        if(nombreProducto == null || nombreProducto.isEmpty()) return TipoMarca.GENERICO ;
        String textoMayuscula = nombreProducto.toUpperCase();

        if (textoMayuscula.contains("PALL MAN")) return TipoMarca.PALL_MALL;
        if (textoMayuscula.contains("FILTROS")) return TipoMarca.OCB;
        for (TipoMarca marca : TipoMarca.values()) {
            if (marca == TipoMarca.GENERICO) continue;

            String nombreMarca = marca.name().replace("_", " ");

            if (textoMayuscula.contains(nombreMarca)) {
                return marca;
            }
        }

        if (textoMayuscula.contains("LUCKY")) return TipoMarca.LUCKY_STRIKE;
        if (textoMayuscula.contains("GOLD")) return TipoMarca.GOLD_LEAF;

        return TipoMarca.GENERICO;
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
                            p.getNombre()+ ";" +
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

    public static void guardarClientes(java.util.List<modelo.Distribuidor> clientes) {
        try {
            java.io.File archivo = new java.io.File("clientes.txt");
            try (java.io.PrintWriter pw = new java.io.PrintWriter(new java.io.FileWriter(archivo))) {
                for (modelo.Distribuidor d : clientes) {
                    pw.println(
                            d.getRut() + "," +
                                    d.getNombre() + "," +
                                    d.getDireccion() + "," +
                                    d.getTipo()
                    );
                }
            }
        } catch (java.io.IOException e) {
            System.out.println("Error al guardar clientes: " + e.getMessage());
        }
    }
}