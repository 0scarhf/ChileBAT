package modelo;

import java.util.*;

public class Distribuidor {

    private String rut;
    private String nombre;
    private String direccion;
    private String tipo;
    

    public Distribuidor(String rut, String nombre, String direccion, String tipo) {
        this.rut = (rut == null ? "" : rut.trim());
        this.nombre = (nombre == null ? "" : nombre.trim());
        this.direccion = (direccion == null ? "" : direccion.trim());
        this.tipo = (tipo == null ? "" : tipo.trim());
    }

    public String getRut() {
        return rut;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre.trim();
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion.trim();
    }

    public void setTipo(String tipo) {
        this.tipo = tipo.trim();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Distribuidor)) return false;
        Distribuidor that = (Distribuidor) o;
        return rut.equalsIgnoreCase(that.rut);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rut.toLowerCase());
    }

    @Override
    public String toString() {
        return nombre + " (" + rut + ") - " + tipo;
    }
    
    private static final String NOMBRE_ARCHIVO = "clientes.txt"; 
    
    public static Distribuidor buscarPorRutEnArchivo(String rutBuscado) {

        String rutNormalizado = rutBuscado.trim();

        // Usamos UnsafeReader para manejar la lectura de archivos.
        // El código dentro del lambda es donde se realiza la búsqueda.
        return UnsafeReader.readAndProcessFile(NOMBRE_ARCHIVO, (br) -> {
            String linea;

            while ((linea = br.readLine()) != null) {

                if (linea.trim().isEmpty()) { continue; }

                String[] campos = linea.split(",");
                if (campos.length < 4) { continue; }
                String rutEnArchivo = campos[0].trim();

                // 1. COMPARACIÓN DEL RUT
                if (rutEnArchivo.equalsIgnoreCase(rutNormalizado)) {

                    // 2. Coincidencia: Devolver el objeto
                    String nombre = campos[1].trim();
                    String direccion = campos[2].trim();
                    String tipo = campos[3].trim();

                    return new Distribuidor(rutEnArchivo, nombre, direccion, tipo);
                }
            }
            // Si el bucle termina sin encontrar nada
            return null; 
        });
    }
}
