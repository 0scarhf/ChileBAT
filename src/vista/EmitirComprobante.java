package vista;

import modelo.Comprobante;
import modelo.Pedido;
import modelo.Producto;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class EmitirComprobante extends JDialog {
    private JTextArea text;
    private JPanel panelPrincipal;
    private JButton btnImprimir;
    private JButton btnCerrar;

    public EmitirComprobante(Window owner, Comprobante comprobante) {
        super(owner, "Comprobante de Venta", ModalityType.APPLICATION_MODAL);

        setContentPane(panelPrincipal);
        setSize(400, 650);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        if (text != null) {
            text.setFont(new Font("Monospaced", Font.BOLD, 12));
            text.setText(generarDiseñoTicket(comprobante));
            text.setCaretPosition(0); // Ir al inicio
        }
        initListeners();
    }

    private void initListeners() {
        if (btnCerrar != null) {
            btnCerrar.addActionListener(e -> dispose());
        }

        if (btnImprimir != null) {
            btnImprimir.addActionListener(e -> {
                JOptionPane.showMessageDialog(this,
                        "Imprimiendo comprobante...\nGuardado en sistema.",
                        "Impresión", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            });
        }
    }

    private String generarDiseñoTicket(Comprobante c) {
        StringBuilder sb = new StringBuilder();
        Pedido p = c.getPedido();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        sb.append("=======================================\n");
        sb.append("      BRITISH AMERICAN TOBACCO         \n");
        sb.append("          CHILE S.A.                   \n");
        sb.append("=======================================\n\n");

        sb.append("DOCUMENTO: ").append(c.getTipo()).append("\n");
        sb.append("FOLIO    : ").append(c.getIdComprobante()).append("\n");
        sb.append("FECHA    : ").append(sdf.format(new Date())).append("\n");
        sb.append("---------------------------------------\n");

        if (p.getCliente() != null) {
            sb.append("CLIENTE  : ").append(p.getCliente().getNombre()).append("\n");
            sb.append("RUT      : ").append(p.getCliente().getRut()).append("\n");
        }

        sb.append("---------------------------------------\n");
        sb.append(String.format("%-18s %3s %14s\n", "ITEM", "CANT", "TOTAL"));
        sb.append("---------------------------------------\n");

        for (Map.Entry<Producto, Integer> entry : p.getProductos().entrySet()) {
            Producto prod = entry.getKey();
            int cantidad = entry.getValue();
            int totalLinea = prod.obtenerPrecio() * cantidad;

            String nombre = prod.getNombre();
            if (nombre.length() > 18) nombre = nombre.substring(0, 17) + ".";

            sb.append(String.format("%-18s %3d  $%12d\n", nombre, cantidad, totalLinea));
        }

        sb.append("---------------------------------------\n");

        int total = p.getMontoTotal();
        int neto = (int) Math.round(total / 1.19);
        int iva = total - neto;

        sb.append(String.format("NETO : $%29d\n", neto));
        sb.append(String.format("IVA  : $%29d\n", iva));
        sb.append(String.format("TOTAL: $%29d\n", total));

        sb.append("\n=======================================\n");
        sb.append("       ¡GRACIAS POR SU COMPRA!         \n");
        sb.append("=======================================\n");

        return sb.toString();
    }
}