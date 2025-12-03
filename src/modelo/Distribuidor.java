package modelo;

public class Distribuidor {
    private int idDistribuidor;
    private String nombre;
    private String rut; // O dirección, según tu UML
    private String direccion;

    public Distribuidor(int id, String nombre, String rut, String direccion) {
        this.idDistribuidor = id;
        this.nombre = nombre;
        this.rut = rut;
        this.direccion = direccion;
    }

    public String getNombre() { return nombre; }
    public String getRut() { return rut; }
}