package modelo;

public abstract class Empleado {
    protected int idEmpleado;
    protected String nombre;

    public Empleado(int id, String nombre) {
        this.idEmpleado = id;
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}