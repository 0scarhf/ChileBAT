package modelo;

import java.util.Objects;

public abstract class Empleado {

    protected int idEmpleado;
    protected String nombre;

    public Empleado(int idEmpleado, String nombre) {
        this.idEmpleado = idEmpleado;
        this.nombre = nombre == null ? "" : nombre.trim();
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre.trim();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Empleado)) return false;
        Empleado empleado = (Empleado) o;
        return idEmpleado == empleado.idEmpleado;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEmpleado);
    }

    @Override
    public String toString() {
        return nombre + " (ID: " + idEmpleado + ")";
    }
}
