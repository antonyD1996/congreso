/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package congreso.Presentacion.Dialogos;

import congreso.Dominio.Congreso;
import congreso.Dominio.Estadistica;
import congreso.Dominio.EstudianteCongreso;
import congreso.Dominio.Tipo;
import congreso.Infraestructura.EstadisticaI;
import congreso.Infraestructura.EstudianteCongresoI;
import congreso.Infraestructura.PersonalCongresoI;
import congreso.Infraestructura.TipoI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Luna
 */
public class Estadisticas extends javax.swing.JDialog {

    /**
     * Creates new form Estadisticas
     */
    EstudianteCongresoI ei = new EstudianteCongresoI();
    PersonalCongresoI pci = new PersonalCongresoI();
    List<EstudianteCongreso> listado;
    Congreso congreso;
    Map<String, Object> mapEstadisticas, mapResultado;
    EstadisticaI esi = new EstadisticaI();
    List<String> regionales = new ArrayList<String>();
    List<Tipo> tipos;
    TipoI ti = new TipoI();
    List<Estadistica> listadoEstudiantes, listadoPersonal, listadoGeneral;

    public Estadisticas(Congreso congreso, java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.congreso = congreso;
        mapEstadisticas = new HashMap<>();
        listadoEstudiantes = new ArrayList<Estadistica>();
        listadoPersonal = new ArrayList<Estadistica>();
        listadoGeneral = new ArrayList<Estadistica>();
        init();
    }

    void init() {
        mapEstadisticas.put("idcongreso", congreso.getId());
        tipos = ti.listadoTipos.get();
        regionales.add("SS");
        regionales.add("SM");
        regionales.add("CH");
        regionales.add("SO");
        cargarDatos();
        btnActualizar.addActionListener(l -> {
            cargarDatos();
        });
    }

    void cargarDatos() {
        listadoEstudiantes.clear();
        listadoPersonal.clear();
        listadoGeneral.clear();
        cargarDatosEstudiantes();
        cargarDatosPersonal();
        cargarDatosGenerales();
    }

    void cargarDatosEstudiantes() {
        
        mapEstadisticas.put("tipoEstadistica", 2);

        regionales.stream().forEach(lr -> {
            mapEstadisticas.put("regional", lr);
            mapResultado = ei.obtenerEstadisticas.apply(mapEstadisticas);
            listadoEstudiantes.add(crearObjeto(lr));
        });
        mapEstadisticas.put("tipoEstadistica", 1);
        mapResultado = ei.obtenerEstadisticas.apply(mapEstadisticas);
        listadoEstudiantes.add(crearObjeto("Total"));
        crearObjetoGeneral("Estudiantes");
        esi.actualizarDatos.accept(tblEstudiantes, listadoEstudiantes);
    }
    

    void cargarDatosPersonal() {
        
        mapEstadisticas.put("tipoEstadistica", 2);

        tipos.stream().forEach(lr -> {
            mapEstadisticas.put("idTipo", lr.getId());
            mapResultado = pci.obtenerEstadisticas.apply(mapEstadisticas);
            listadoPersonal.add(crearObjeto(lr.getNombre()));
        });
        mapEstadisticas.put("tipoEstadistica", 1);
        mapResultado = pci.obtenerEstadisticas.apply(mapEstadisticas);
        listadoPersonal.add(crearObjeto("Total"));
        crearObjetoGeneral("Personal");
        esi.actualizarDatos.accept(tblPersonal, listadoPersonal);
    }
    
    void cargarDatosGenerales() {
        Estadistica e = listadoGeneral.get(0);
        Estadistica p = listadoGeneral.get(1);
        Estadistica estadstica = new Estadistica();
        estadstica.setNombre("Total");
        estadstica.setEsperados(e.getEsperados()+p.getEsperados());
        estadstica.setRegistrados(e.getRegistrados()+p.getRegistrados());
        estadstica.setBreakAM(e.getBreakAM()+p.getBreakAM());
        estadstica.setAlmuerzos(e.getAlmuerzos()+p.getAlmuerzos());
        estadstica.setBreakPM(e.getBreakPM()+p.getBreakPM());
        listadoGeneral.add(estadstica);
        esi.actualizarDatos.accept(tblGeneral, listadoGeneral);
    }
    
    void crearObjetoGeneral(String nombre) {
        listadoGeneral.add(crearObjeto(nombre));
    }

    Estadistica crearObjeto(String lr) {
        Estadistica estadstica = new Estadistica();
        estadstica.setNombre(evaluarRegional(lr));
        estadstica.setEsperados(Integer.valueOf(String.valueOf(mapResultado.get("total"))));
        estadstica.setRegistrados(Integer.valueOf(String.valueOf(mapResultado.get("registrados"))));
        estadstica.setBreakAM(Integer.valueOf(String.valueOf(mapResultado.get("breakam"))));
        estadstica.setAlmuerzos(Integer.valueOf(String.valueOf(mapResultado.get("almuerzo"))));
        estadstica.setBreakPM(Integer.valueOf(String.valueOf(mapResultado.get("breakpm"))));
        return estadstica;
    }

    String evaluarRegional(String r) {
        String re = r;
        if (r.equals("SS")) {
            re = "San Salvador";
        }
        if (r.equals("SM")) {
            re = "San Miguel";
        }
        if (r.equals("CH")) {
            re = "Chalatenango";
        }
        if (r.equals("SO")) {
            re = "Sonsonate";
        }
        return re;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelGradient1 = new PanelGradient.PanelGradient();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        btnActualizar = new rojeru_san.RSButtonRiple();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblEstudiantes = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblPersonal = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblGeneral = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        panelGradient1.setideEndColor(new java.awt.Color(0, 204, 102));
        panelGradient1.setideStartColor(new java.awt.Color(0, 153, 153));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/congreso/img/logo.png"))); // NOI18N

        javax.swing.GroupLayout panelGradient1Layout = new javax.swing.GroupLayout(panelGradient1);
        panelGradient1.setLayout(panelGradient1Layout);
        panelGradient1Layout.setHorizontalGroup(
            panelGradient1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1108, Short.MAX_VALUE)
        );
        panelGradient1Layout.setVerticalGroup(
            panelGradient1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGradient1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        btnActualizar.setText("Actualizar");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 2, true), "Estudiantes", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.ABOVE_TOP, new java.awt.Font("sansserif", 0, 18))); // NOI18N

        tblEstudiantes.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        tblEstudiantes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Regional", "Esperados", "Registrados", "Break AM", "Almuerzos", "Break PM"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tblEstudiantes);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 2, true), "Personal", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.ABOVE_TOP, new java.awt.Font("sansserif", 0, 18), new java.awt.Color(0, 0, 0))); // NOI18N

        tblPersonal.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        tblPersonal.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Regional", "Esperados", "Registrados", "Break AM", "Almuerzos", "Break PM"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tblPersonal);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 2, true), "General", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.ABOVE_TOP, new java.awt.Font("sansserif", 0, 18))); // NOI18N

        tblGeneral.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Regional", "Esperados", "Registrados", "Break AM", "Almuerzos", "Break PM"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane5.setViewportView(tblGeneral);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 1084, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38))
        );

        jLabel2.setFont(new java.awt.Font("Calibri Light", 1, 22)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("ESTADÍSTICAS");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnActualizar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelGradient1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelGradient1, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private rojeru_san.RSButtonRiple btnActualizar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane5;
    private PanelGradient.PanelGradient panelGradient1;
    private javax.swing.JTable tblEstudiantes;
    private javax.swing.JTable tblGeneral;
    private javax.swing.JTable tblPersonal;
    // End of variables declaration//GEN-END:variables
}
