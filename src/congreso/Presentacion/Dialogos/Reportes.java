/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package congreso.Presentacion.Dialogos;

import congreso.Dominio.Congreso;
import congreso.Dominio.EstudianteCongreso;
import congreso.Dominio.PersonalCongreso;
import congreso.Dominio.Tipo;
import congreso.Infraestructura.EstudianteCongresoI;
import congreso.Infraestructura.PersonalCongresoI;
import congreso.Infraestructura.TipoI;
import congreso.Utilidades.ExportarEstudiantes;
import congreso.Utilidades.ExportarPersonal;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author anton
 */
public class Reportes extends java.awt.Dialog {

    /**
     * Creates new form Reportes
     */
    Integer tipo;
    TipoI ti = new TipoI();
    EstudianteCongresoI eci = new EstudianteCongresoI();
    PersonalCongresoI pci = new PersonalCongresoI();
    List<Tipo> listadoTipos;
    Map<String, Object> map = new HashMap<>();
    List<EstudianteCongreso> listadoEstudiantes;
    List<PersonalCongreso> listadoPersonal;
    Congreso congreso;
    String regional;

    public Reportes(Congreso congreso,java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        init();
        this.congreso = congreso;
        map.put("congreso", congreso);      
        map.put("idCongreso", congreso.getId());
    }

    void init() {
        tipo = 1;
        cargarItems();
        evaluarRegional(cbox2.getSelectedIndex());
        btnGenerar.addActionListener(l -> {
            if (tipo == 1) {
                if (cbox2.getSelectedIndex() == 0) {
                    try {
                        listadoEstudiantes = eci.listadoEstudiantes.apply(congreso.getId());
                        map.put("listado", listadoEstudiantes);
                        ExportarEstudiantes exportarEstudiantes = new ExportarEstudiantes();
                        exportarEstudiantes.Exportar(map);
                        JOptionPane.showMessageDialog(null, "Reporte Generado");
                    } catch (IOException ex) {
                        Logger.getLogger(Reportes.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    try {
                        listadoEstudiantes = eci.listadoEstudiantesPorRegional.apply(map);
                        map.put("listado", listadoEstudiantes);
                        ExportarEstudiantes exportarEstudiantes = new ExportarEstudiantes();
                        exportarEstudiantes.Exportar(map);
                        JOptionPane.showMessageDialog(null, "Reporte Generado");
                    } catch (IOException ex) {
                        Logger.getLogger(Reportes.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } else {
                if (cbox2.getSelectedIndex() == 0) {
                    try {
                        listadoPersonal = pci.listadoPersonal.apply(map);
                        map.put("listado", listadoPersonal);
                        map.put("idTipo", cbox2.getSelectedIndex());
                        map.put("tipo", cbox2.getSelectedItem());
                        ExportarPersonal ep = new ExportarPersonal();
                        ep.Exportar(map);
                        JOptionPane.showMessageDialog(null, "Reporte Generado");
                    } catch (IOException ex) {
                        Logger.getLogger(Reportes.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    try {
                        listadoPersonal = pci.listadoPersonalPorTipo.apply(map);
                        map.put("listado", listadoPersonal);
                        ExportarPersonal ep = new ExportarPersonal();
                        ep.Exportar(map);
                        JOptionPane.showMessageDialog(null, "Reporte Generado");
                    } catch (IOException ex) {
                        Logger.getLogger(Reportes.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

    }

    void cargarItems() {
        if (cbox1.getSelectedIndex() == 0) {
            tipo = 1;
            lblTipo.setText("Regional");
            cbox2.removeAllItems();
            cbox2.addItem("Todas");
            cbox2.addItem("San Salvador");
            cbox2.addItem("San Miguel");
            cbox2.addItem("Chalatenango");
            cbox2.addItem("Sonsonate");
        } else {
            tipo = 2;
            lblTipo.setText("Tipo");
            listadoTipos = ti.listadoTipos.get();
            cbox2.removeAllItems();
            cbox2.addItem("Todos");
            listadoTipos.stream().forEach(lt -> {
                cbox2.addItem(lt.getNombre());
            });
        }
        evaluarRegional(cbox2.getSelectedIndex() + 1);

    }

    void evaluarRegional(Integer item) {
        if (item == 1) {
            map.put("tipoEstadistica", 1);
        } else {
            map.put("tipoEstadistica", 2);
        }
        if (tipo == 1) {
            switch (item) {
                case 1: {
                    map.put("regional", "Todas");
                }
                break;
                case 2: {
                    map.put("regional", "SS");
                }
                break;
                case 3: {
                    map.put("regional", "SM");
                }
                break;
                case 4: {
                    map.put("regional", "CH");
                }
                break;
                case 5: {
                    map.put("regional", "SO");
                }
                break;
            }
        } else {
            map.put("idTipo", cbox2.getSelectedIndex());
            map.put("tipo", cbox2.getSelectedItem());
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelGradient1 = new PanelGradient.PanelGradient();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        cbox1 = new javax.swing.JComboBox<>();
        btnGenerar = new rojeru_san.RSButtonRiple();
        cbox2 = new javax.swing.JComboBox<>();
        lblTipo = new javax.swing.JLabel();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        panelGradient1.setideEndColor(new java.awt.Color(0, 204, 102));
        panelGradient1.setideStartColor(new java.awt.Color(0, 153, 153));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/congreso/img/logo.png"))); // NOI18N
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        jLabel2.setFont(new java.awt.Font("Arial", 1, 28)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("REPORTES");

        jPanel1.setOpaque(false);

        jPanel2.setOpaque(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 46, Short.MAX_VALUE)
        );

        jPanel3.setOpaque(false);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 43, Short.MAX_VALUE)
        );

        cbox1.setFont(new java.awt.Font("sansserif", 1, 20)); // NOI18N
        cbox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Estudiantes", "Personal" }));
        cbox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbox1ItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(259, 259, 259)
                .addComponent(cbox1, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(473, 473, 473)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(35, Short.MAX_VALUE)
                .addComponent(cbox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );

        btnGenerar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 2, true));
        btnGenerar.setText("Generar");
        btnGenerar.setFont(new java.awt.Font("Cambria Math", 1, 18)); // NOI18N

        cbox2.setFont(new java.awt.Font("sansserif", 1, 20)); // NOI18N
        cbox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Estudiantes", "Personal" }));
        cbox2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbox2ItemStateChanged(evt);
            }
        });

        lblTipo.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        lblTipo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTipo.setText("jLabel3");

        javax.swing.GroupLayout panelGradient1Layout = new javax.swing.GroupLayout(panelGradient1);
        panelGradient1.setLayout(panelGradient1Layout);
        panelGradient1Layout.setHorizontalGroup(
            panelGradient1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(panelGradient1Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(panelGradient1Layout.createSequentialGroup()
                .addGap(392, 392, 392)
                .addComponent(btnGenerar, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelGradient1Layout.createSequentialGroup()
                .addGroup(panelGradient1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelGradient1Layout.createSequentialGroup()
                        .addGap(0, 265, Short.MAX_VALUE)
                        .addComponent(cbox2, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(249, 249, 249))
                    .addComponent(lblTipo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelGradient1Layout.setVerticalGroup(
            panelGradient1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGradient1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(66, 66, 66)
                .addComponent(btnGenerar, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(54, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 940, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(panelGradient1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 471, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(panelGradient1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Closes the dialog
     */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeDialog

    private void cbox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbox1ItemStateChanged
        // TODO add your handling code here:
        cargarItems();


    }//GEN-LAST:event_cbox1ItemStateChanged

    private void cbox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbox2ItemStateChanged
        // TODO add your handling code here:
        evaluarRegional(cbox2.getSelectedIndex() + 1);


    }//GEN-LAST:event_cbox2ItemStateChanged



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private rojeru_san.RSButtonRiple btnGenerar;
    private javax.swing.JComboBox<String> cbox1;
    private javax.swing.JComboBox<String> cbox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel lblTipo;
    private PanelGradient.PanelGradient panelGradient1;
    // End of variables declaration//GEN-END:variables
}
