package vista;

import controlador.ControladorSistemaVentas;
import modelo.Distribuidor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class VerCliente extends JFrame {
    private JPanel panel1;
    private JTable tableCliente;
    private JTextField textFieldRut;
    private JButton editarButton;
    private JButton cancelarButton;
    private JButton eliminarClienteButton;
    private JButton buscarButton;
    private final ControladorSistemaVentas controlVentas;
    private DefaultTableModel modeloTabla;

    public VerCliente(ControladorSistemaVentas control) {
        this.controlVentas = control;

        setContentPane(panel1);
        setTitle("Listado de Clientes");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(700, 600);
        setLocationRelativeTo(null);
        configurarTabla();
        cargarDatosTabla();
        initListeners();
    }

    private void initListeners() {
        cancelarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {dispose();}
        });
        eliminarClienteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarCliente();
            }
        });
        editarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editarCliente();
            }
        });

        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarClientePorRut();
            }
        });

        textFieldRut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarClientePorRut();
            }
        });
    }

    private void buscarClientePorRut() {
        String rutBuscado = textFieldRut.getText().trim();

        if (rutBuscado.isEmpty()) {
            cargarDatosTabla();
            return;
        }

        Distribuidor clienteEncontrado = controlVentas.buscarCliente(rutBuscado);

        modeloTabla.setRowCount(0);

        if (clienteEncontrado != null) {
            Object[] fila = {
                    clienteEncontrado.getRut(),
                    clienteEncontrado.getNombre(),
                    clienteEncontrado.getDireccion(),
                    clienteEncontrado.getTipo()
            };
            modeloTabla.addRow(fila);
        } else {

            JOptionPane.showMessageDialog(this,
                    "No se encontró ningún cliente con el RUT: " + rutBuscado,
                    "Resultado de búsqueda",
                    JOptionPane.INFORMATION_MESSAGE);

            cargarDatosTabla();
        }
    }

    private void eliminarCliente() {
        int fila = tableCliente.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente para eliminar.");
            return;
        }

        String rut = (String) modeloTabla.getValueAt(fila, 0);

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Eliminar cliente con RUT " + rut + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean exito = controlVentas.eliminarCliente(rut);
            if (exito) {
                cargarDatosTabla();
                JOptionPane.showMessageDialog(this, "Cliente eliminado.");
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar.");
            }
        }
    }

    private void editarCliente() {
        int fila = tableCliente.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Primero seleccione un cliente de la tabla.");
            return;
        }

        String rutSeleccionado = (String) tableCliente.getValueAt(fila, 0);

        Distribuidor clienteEncontrado = controlVentas.buscarCliente(rutSeleccionado);

        if (clienteEncontrado != null) {
            EditarCliente ventanaEdicion = new EditarCliente(controlVentas, clienteEncontrado);
            ventanaEdicion.setVisible(true);

            ventanaEdicion.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                    cargarDatosTabla();
                }
            });

        } else {
            JOptionPane.showMessageDialog(this, "Error crítico: No se encuentra el cliente en memoria.");
        }
    }

    private void configurarTabla() {
        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modeloTabla.addColumn("RUT");
        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("Dirección");
        modeloTabla.addColumn("Tipo");

        tableCliente.setModel(modeloTabla);
    }

    public void cargarDatosTabla() {
        modeloTabla.setRowCount(0);

        List<Distribuidor> lista = controlVentas.obtenerClientes();

        if (lista != null) {
            for (Distribuidor d : lista) {
                Object[] fila = {
                        d.getRut(),
                        d.getNombre(),
                        d.getDireccion(),
                        d.getTipo()
                };
                modeloTabla.addRow(fila);
            }
        }
    }
}