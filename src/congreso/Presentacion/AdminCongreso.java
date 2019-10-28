/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package congreso.Presentacion;

import com.google.zxing.WriterException;
import congreso.Dominio.Congreso;
import congreso.Infraestructura.EstudianteCongresoI;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author anton
 */
public class AdminCongreso extends javax.swing.JFrame {

    /**
     * Creates new form Inicio
     */
    EstudianteCongresoI ei = new EstudianteCongresoI();
    Congreso congreso;

    public AdminCongreso(Congreso congreso) {
        this.congreso = congreso;
        initComponents();
        init();
    }

    public void init() {
        lblNombreCongreso.setText(congreso.getNombre().toUpperCase());
        mostrarEstudiantes();

    }

    void cargarLector(Integer valor) {
        new LeerCodigo(congreso, valor);
        this.setVisible(false);
        this.dispose();
    }

    public void mostrarEstudiantes() {

        btnRecepcion.addActionListener(l -> {
            cargarLector(2);
        });
        btnBreakAM.addActionListener(l -> {
            cargarLector(3);
        });
        btnAlmuerzo.addActionListener(l -> {
            cargarLector(4);
        });
        btnBreakPM.addActionListener(l -> {
            cargarLector(5);
        });
        btnAdmin.addActionListener(l -> {
            try {
                Administrar admin = new Administrar(congreso);
                admin.setLocationRelativeTo(null);
                admin.setVisible(true);
                this.dispose();
            } catch (WriterException ex) {
                Logger.getLogger(AdminCongreso.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(AdminCongreso.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        lblNombreCongreso = new javax.swing.JLabel();
        btnAlmuerzo = new javax.swing.JButton();
        btnRecepcion = new javax.swing.JButton();
        btnBreakAM = new javax.swing.JButton();
        btnBreakPM = new javax.swing.JButton();
        btnAdmin = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setAutoscrolls(true);

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204), 2));

        lblNombreCongreso.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblNombreCongreso.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNombreCongreso.setText("ADMINISTRACION DE CONGRESOS");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblNombreCongreso, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblNombreCongreso, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        btnAlmuerzo.setBackground(new java.awt.Color(82, 164, 247));
        btnAlmuerzo.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        btnAlmuerzo.setText("ALMUERZO");
        btnAlmuerzo.setFocusable(false);

        btnRecepcion.setBackground(new java.awt.Color(82, 164, 247));
        btnRecepcion.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        btnRecepcion.setText("RECEPCION");
        btnRecepcion.setFocusable(false);
        btnRecepcion.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRecepcion.setOpaque(false);

        btnBreakAM.setBackground(new java.awt.Color(82, 164, 247));
        btnBreakAM.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        btnBreakAM.setText("BREAK AM");
        btnBreakAM.setFocusable(false);

        btnBreakPM.setBackground(new java.awt.Color(82, 164, 247));
        btnBreakPM.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        btnBreakPM.setText("BREAK PM");
        btnBreakPM.setFocusable(false);

        btnAdmin.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnAdmin.setText("ADMINISTRAR");
        btnAdmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdminActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnRecepcion, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnBreakAM, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnBreakPM, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnAdmin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnAlmuerzo, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(34, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRecepcion, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                    .addComponent(btnBreakAM, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                    .addComponent(btnAlmuerzo, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnBreakPM, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE)
                    .addComponent(btnAdmin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(305, 305, 305))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdminActionPerformed
        // TODO add your handling code here:


    }//GEN-LAST:event_btnAdminActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdmin;
    private javax.swing.JButton btnAlmuerzo;
    private javax.swing.JButton btnBreakAM;
    private javax.swing.JButton btnBreakPM;
    private javax.swing.JButton btnRecepcion;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblNombreCongreso;
    // End of variables declaration//GEN-END:variables
}
