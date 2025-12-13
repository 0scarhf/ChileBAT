// Archivo: vista.Main.java (FINALMENTE CORREGIDO)
package vista;

import controlador.ControladorInventario;
import controlador.ControladorSistemaVentas;
import javax.swing.SwingUtilities;

public class Main {

    private static ControladorInventario controlInv;
    private static ControladorSistemaVentas controlVentas;

    public static void main(String[] args) {

        // 1. Inicialización de Controladores (Lógica de Negocio)
        controlInv = new ControladorInventario();
        controlVentas = new ControladorSistemaVentas(controlInv);
        
        // 2. Arranque de la GUI
        SwingUtilities.invokeLater(() -> {
            
            // CRÍTICO: Llamamos a la clase GUI e inyectamos los controladores.
            GUI menuPrincipal = new GUI(controlInv, controlVentas); 
            
            menuPrincipal.setTitle("BAT Chile - Menú Principal");
            menuPrincipal.setLocationRelativeTo(null); 
            menuPrincipal.setVisible(true);
        });
        
        // NOTA: El hook de guardado se ha movido al WindowListener de la clase GUI.
    }
}