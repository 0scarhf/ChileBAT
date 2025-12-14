package controlador;

import modelo.Producto;
import persistencia.PersistenciaDatos;
import java.util.*; // Importación corregida a * para incluir Map, List, LinkedHashMap, ArrayList

public class ControladorInventario {

    private Map<Integer, Producto> inventario;

    public ControladorInventario() {
        this.inventario = new LinkedHashMap<>();
        
        // 1. Delegación (Lectura O(n))
        List<Producto> listaCargada = PersistenciaDatos.cargarProductos(); 

        // 2. Gestión (Almacenamiento eficiente O(1))
        for (Producto p : listaCargada) {
            this.inventario.put(p.getIdProducto(), p);
        }
    }

    private boolean validarProductoYCantidad(Producto producto, int cantidad) {
        if (producto == null || cantidad <= 0) {
            return false;
        }
        return true;
    }

    //METODO PARA VERIFICAR NOMBRE DUPLICADO
    public boolean existeProductoConNombre(String nombreBuscado) {
        for (Producto p : inventario.values()) {
            if (p.getNombre().equalsIgnoreCase(nombreBuscado)) {
                return true;
            }
        }
        return false; // No existe
    }

    //METODO PARA IngresarStock (GUI)
    public void actualizarProducto(Producto p) {
        inventario.put(p.getIdProducto(), p);
        guardarInventario();
    }

    // OBTENER INVENTARIO
    public List<Producto> obtenerInventario() {
        return new ArrayList<>(inventario.values());
    }

    // BUSCAR PRODUCTO POR ID (Eficiencia O(1))
    public Producto buscarProductoPorID(int idProducto) {
        return inventario.get(idProducto);
    }

    public boolean eliminarProducto(int idProducto) {
        if (inventario.containsKey(idProducto)) {
            inventario.remove(idProducto);
            guardarInventario();
            return true;
        }
        return false;
    }
    
    // METODO GUARDAR INVENTARIO ACTUALIZADO (Delegación de Responsabilidad)
    public void guardarInventario() {
        // La responsabilidad de la E/S se delega, manteniendo el controlador limpio.
        PersistenciaDatos.guardarProductos(new ArrayList<>(inventario.values()));
    }

    // DESCONTAR STOCK (VENTA)
    public boolean descontarStock(Producto producto, int cantidad) {
        // 1. Validar producto y cantidad
        if (!validarProductoYCantidad(producto, cantidad)) {
            return false;
        }

        // 2. Validar stock
        if (producto.getStock() < cantidad) {
            return false;
        }

        // 3. Modificación del estado del objeto y persistencia
        producto.restarStock(cantidad); 
        guardarInventario();
        return true;
    }

    // INGRESAR STOCK MANUAL
    public boolean ingresarStock(int idProducto, int cantidad) {
        Producto p = buscarProductoPorID(idProducto);

        // 1. Validar producto y cantidad (Reutilizando la lógica)
        if (!validarProductoYCantidad(p, cantidad)) {
            return false;
        }

        // 2. Modificación del estado del objeto y persistencia
        p.aumentarStock(cantidad); 
        guardarInventario();
        return true;
    }

    // STOCK CRÍTICO (El objeto Producto define la regla de negocio)
    public boolean esStockCritico(Producto p) {
        // Delegamos la lógica de la regla de negocio al objeto Producto (Cohesión)
        return p != null && p.esStockCritico(); 
    }
}