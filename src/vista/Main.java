package vista;

import controlador.ControladorInventario;
import controlador.ControladorSistemaVentas;
import javax.swing.SwingUtilities;

public class Main {
    private static ControladorInventario controlInv;
    private static ControladorSistemaVentas controlVentas;

    public static void main(String[] args) {
        controlInv = new ControladorInventario();
        controlVentas = new ControladorSistemaVentas(controlInv);
        
        // GUI
        SwingUtilities.invokeLater(() -> {
            GUI menuPrincipal = new GUI(controlInv, controlVentas);
            menuPrincipal.setTitle("BAT Chile - Menú Principal");
            menuPrincipal.setLocationRelativeTo(null); 
            menuPrincipal.setVisible(true);
        });

    }
}