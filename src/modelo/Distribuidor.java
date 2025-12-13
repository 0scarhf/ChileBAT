package modelo;

import java.util.*;

public class Distribuidor {

    private String rut;
    private String nombre;
    private String direccion;
    private String tipo;
    

    public Distribuidor(String rut, String nombre, String direccion, String tipo) {
        this.rut = rut;
        this.nombre = nombre;
        this.direccion = direccion;
        this.tipo = tipo;
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
}
