package vista;

import javax.swing.*;
import controlador.*;
import modelo.Producto;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.util.List;

// 1. Extendemos de JFrame para que sea una ventana
public class GUI1 extends JFrame {

    // Componentes del GUI Designer (IntelliJ)
    private JPanel panel1;
    private JButton salirButton;
    private JButton REALIZARVENTAButton;
    private JButton VERINVENTARIOButton;
    private JButton VERCLIENTESButton;
    private JButton INGRESARSTOCKButton;
    private JTextArea txtAlertas;
    private JLabel lblLogo;

    private ControladorInventario controlInv;
    private ControladorSistemaVentas controlVentas;

    public GUI1(ControladorInventario inv, ControladorSistemaVentas ventas) {
        this.controlInv = inv;
        this.controlVentas = ventas;
        setContentPane(panel1);
        setTitle("Sistema de Ventas - ChileBAT");
        setSize(550, 650);
        setMinimumSize(new java.awt.Dimension(500, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        cargarAlertasStock();
        this.addWindowFocusListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowGainedFocus(java.awt.event.WindowEvent e) {
                cargarAlertasStock();
            }
        });
        initListeners();
    }

    private void cargarAlertasStock() {
        txtAlertas.setText("");
        txtAlertas.append("=== ESTADO DEL INVENTARIO ===\n\n");

        List<Producto> inventario = controlInv.obtenerInventario();
        boolean hayProblemas = false;

        for (Producto p : inventario) {
            if (controlInv.esStockCritico(p)) {
                txtAlertas.append("⚠ ALERTA: " + p.getNombre() + "\n");
                txtAlertas.append("   Stock: " + p.getStock() + " (Mínimo: " + p.getStockMinimo() + ")\n");
                txtAlertas.append("   ID: " + p.getIdProducto() + "\n");
                txtAlertas.append("-----------------------------\n");
                hayProblemas = true;
            }
        }

        if (hayProblemas) {
            txtAlertas.setForeground(Color.RED);
        } else {
            txtAlertas.append("✔ Todo el inventario está correcto.");
            txtAlertas.setForeground(new Color(0, 100, 0)); // Verde
        }
    }

    private void initListeners() {
        REALIZARVENTAButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IniciarVenta v = new IniciarVenta(controlVentas);
                v.setVisible(true);
                v.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                        cargarAlertasStock();
                    }
                });
            }
        });

        INGRESARSTOCKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IngresarStock v = new IngresarStock(controlInv);
                v.setVisible(true);
                v.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                        cargarAlertasStock();
                    }
                });
            }
        });

        VERINVENTARIOButton.addActionListener(e -> {
            VerInventario v = new VerInventario(controlInv);
            v.setVisible(true);
        });

        VERCLIENTESButton.addActionListener(e -> {
            VerCliente v = new VerCliente(controlVentas);
            v.setVisible(true);
        });

        salirButton.addActionListener(e -> System.exit(0));
    }

}
