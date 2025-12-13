package vista;
import controlador.*;
import modelo.*;
import javax.swing.JOptionPane;

public class IngresarStock extends javax.swing.JFrame {
    
    private final ControladorInventario controlInv;
    
    public IngresarStock(ControladorInventario controlInv) {
        this.controlInv = controlInv; 
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        
        // Cargar las Marcas en el ComboBox
        MarcajComboBox.removeAllItems();
        for (TipoMarca marca : TipoMarca.values()) {
            MarcajComboBox.addItem(marca.name());
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        IDProductoTextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        CantidadAIngresarTextField = new javax.swing.JTextField();
        BotonIngresar = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        MarcajComboBox = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("INGRESAR STOCK");

        jLabel2.setText("ID Producto:");

        IDProductoTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IDProductoTextFieldActionPerformed(evt);
            }
        });

        jLabel3.setText("Cantidad a Ingresar:");

        BotonIngresar.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.default.focusedBorderColor"));
        BotonIngresar.setText("INGRESAR");
        BotonIngresar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BotonIngresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonIngresarActionPerformed(evt);
            }
        });

        jLabel4.setText("Marca:");

        MarcajComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        MarcajComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MarcajComboBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(IDProductoTextField)
                    .addComponent(CantidadAIngresarTextField)
                    .addComponent(MarcajComboBox, 0, 199, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(129, Short.MAX_VALUE)
                .addComponent(BotonIngresar, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(129, 129, 129))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(IDProductoTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CantidadAIngresarTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(MarcajComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 55, Short.MAX_VALUE)
                .addComponent(BotonIngresar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void IDProductoTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IDProductoTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_IDProductoTextFieldActionPerformed

    private void BotonIngresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonIngresarActionPerformed
        // 1. Capturar y validar entradas de la GUI
        String idProductoStr = IDProductoTextField.getText().trim();
        String cantidadStr = CantidadAIngresarTextField.getText().trim();
        String marcaStr = (String) MarcajComboBox.getSelectedItem();

        if (idProductoStr.isEmpty() || cantidadStr.isEmpty() || marcaStr == null) {
            JOptionPane.showMessageDialog(this, "Debe ingresar ID, Cantidad y seleccionar una Marca.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int idProducto;
        int cantidad;
        try {
            idProducto = Integer.parseInt(idProductoStr);
            cantidad = Integer.parseInt(cantidadStr);
            
            if (cantidad <= 0) {
                 JOptionPane.showMessageDialog(this, "La cantidad a ingresar debe ser positiva.", "Error de Cantidad", JOptionPane.ERROR_MESSAGE);
                 return;
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID y Cantidad deben ser números válidos.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 2. Búsqueda y Validación de Existencia (DELEGACIÓN)
        Producto productoExistente = controlInv.buscarProducto(idProducto);
        
        if (productoExistente == null) {
            // Caso 1: ID no existe
            JOptionPane.showMessageDialog(this, 
                "ERROR: No existe ningún producto con el ID " + idProducto + " para reponer stock.", 
                "ID Inválido", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // 3. Validación de TipoMarca (Regla de Negocio: debe coincidir con el producto existente)
        // Obtenemos la marca del producto existente para comparar con lo que seleccionó el usuario.
        if (!productoExistente.getMarca().name().equalsIgnoreCase(marcaStr)) {
            JOptionPane.showMessageDialog(this, 
                "ERROR: La marca seleccionada (" + marcaStr + ") no coincide con la marca registrada para este ID (" + productoExistente.getMarca().name() + ").", 
                "Error de Marca", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 4. Delegación al Controlador para Ingresar Stock
        // La función ingresarStock() se encarga de aumentar el stock y guardar los datos.
        boolean ok = controlInv.ingresarStock(idProducto, cantidad);
        
        if (ok) {
            // Éxito:
            JOptionPane.showMessageDialog(this, 
                "Stock de " + productoExistente.getMarca().name() + " actualizado con éxito.\nCantidad ingresada: " + cantidad, 
                "Éxito", 
                JOptionPane.INFORMATION_MESSAGE);
            
            // Limpiar campos para la siguiente entrada
            IDProductoTextField.setText("");
            CantidadAIngresarTextField.setText("");
            
        } else {
             // Fallo Interno (poco probable si las validaciones previas pasaron)
             JOptionPane.showMessageDialog(this, "Fallo desconocido al actualizar el stock.", "Error Interno", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_BotonIngresarActionPerformed

    private void MarcajComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MarcajComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_MarcajComboBoxActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BotonIngresar;
    private javax.swing.JTextField CantidadAIngresarTextField;
    private javax.swing.JTextField IDProductoTextField;
    private javax.swing.JComboBox<String> MarcajComboBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}