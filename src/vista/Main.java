package vista;

import controlador.ControladorInventario;
import controlador.ControladorSistemaVentas;

public class Main {

    public static void main(String[] args) {

        ControladorInventario controlInv = new ControladorInventario();
        ControladorSistemaVentas controlVentas = new ControladorSistemaVentas(controlInv);

        UISistema ui = new UISistema(controlInv, controlVentas);

        ui.iniciar();
    }
}
