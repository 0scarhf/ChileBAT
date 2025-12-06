package vista;

import controlador.ControladorInventario;
import controlador.ControladorSistemaVentas;

public class Main {

    private static ControladorInventario controlInv;
    private static ControladorSistemaVentas controlVentas;

    public static void main(String[] args) {

        controlInv = new ControladorInventario();
        controlVentas = new ControladorSistemaVentas(controlInv);

        // Agregar shutdown hook para guardar datos al cerrar la aplicación
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nGuardando datos antes de salir...");
            if (controlInv != null) {
                controlInv.guardarInventario();
            }
            System.out.println("Datos guardados correctamente.");
        }));

        UISistema ui = new UISistema(controlInv, controlVentas);

        ui.iniciar();
    }
}
