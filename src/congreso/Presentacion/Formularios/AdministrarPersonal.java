package congreso.Presentacion.Formularios;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Luna
 */
import com.google.zxing.WriterException;
import congreso.Dominio.Accion;
import congreso.Dominio.Congreso;
import congreso.Dominio.Personal;
import congreso.Dominio.PersonalCongreso;
import congreso.Dominio.Tipo;
import congreso.Infraestructura.PersonalCongresoI;
import congreso.Infraestructura.TipoI;
import congreso.Presentacion.Dialogos.Importar;
import congreso.Presentacion.Dialogos.RegistrarPersonal;
import congreso.Utilidades.QRGenerator;
import congreso.Utilidades.SendEmail;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class AdministrarPersonal extends javax.swing.JFrame {

    /**
     * Creates new form AdministrarEstudiantes
     */
    List<PersonalCongreso> listadoModel, faltantesEmail, lista, busqueda;
    PersonalCongresoI pci = new PersonalCongresoI();
    Congreso congreso;
    Long ID;
    int contador = 1;
    List<Integer> datos;
    Map<String, Object> map = new HashMap<>();
    PersonalCongreso persona = null;
    Integer pendientes;
    Accion accion;
    List<Tipo> listadoTipos;
    TipoI ti = new TipoI();

    public AdministrarPersonal(Congreso congreso) throws WriterException, IOException, Exception {
        this.congreso = congreso;
        initComponents();
        init();
    }

    public void init() throws WriterException, IOException, Exception {
        lista = new ArrayList<>();
        busqueda = new ArrayList<>();
        listadoTipos = ti.listadoTipos.get();
        cboxTipo.addItem("Todos");
        listadoTipos.stream().forEach(lt->{
            cboxTipo.addItem(lt.getNombre());
        });
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        map.put("idCongreso", congreso.getId());
        map.put("tabla", tblPersonal);
        mostrarDatos();
        tblPersonal.setFillsViewportHeight(true);
        if (listadoModel == null) {
            listadoModel = new ArrayList<>();
        }
        evaluarEmails(congreso.getId());

        btnEnviar.addActionListener(l -> {
            map.put("tipoEmail", 2);
            faltantesEmail = (List<PersonalCongreso>) pci.faltantesEmail.apply(map);
            contador = 1;
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            String usuario = "";
            String contra = "";
            Authenticator authentication = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(usuario, contra);
                }
            };
            Session session = Session.getInstance(props, authentication);
            boolean fallo = false;

            for (PersonalCongreso pc : faltantesEmail) {
                try {
                    QRGenerator qr = new QRGenerator();
                    File file = qr.generateQRCodeImage(pc.getUuid());
                    SendEmail se = new SendEmail();
                    se.enviar(congreso.getNombre(), pc.getDatosPersonal().getEmail(), pc.getDatosPersonal().getNombre(), file.getAbsolutePath(), session);
                    accion = pc.getDatosAccion();
                    accion.setEmail(1);
                    pc.setDatosAccion(accion);
                    pci.cambiarEstado.accept(pc);
                    System.out.println("Enviando email " + contador++ + " de " + faltantesEmail.size() + " a " + pc.getDatosPersonal().getNombre());
                } catch (WriterException | IOException e) {
                    fallo = true;
                    System.out.println(e);
                }
                if (fallo) {
                    break;

                }
            }

            evaluarEmails(congreso.getId());
            mostrarDatos();
        });

        btnNuevo.addActionListener(l -> {
            RegistrarPersonal rp = new RegistrarPersonal(new java.awt.Frame(), true);
            rp.setLocationRelativeTo(null);
            rp.setVisible(true);

            if (rp.persona != null) {
                Accion ac = new Accion();
                PersonalCongreso pc = new PersonalCongreso();
                pc.setUuid("P" + UUID.randomUUID().toString());
                ac.setRegistro(0);
                ac.setBreakAM(0);
                ac.setAlmuerzo(0);
                ac.setBreakPM(0);
                ac.setEmail(0);
                pc.setDatosAccion(ac);
                pc.setDatosPersonal(rp.persona);
                pc.setDatosCongreso(congreso);
                pci.guardarPersona.accept(pc);
                mostrarDatos();
            }
            evaluarEmails(congreso.getId());
        });
        btnImportar.addActionListener(l -> {
            Importar imp = new Importar(new java.awt.Frame(), true);
            imp.setLocationRelativeTo(imp);
            imp.setVisible(true);

            if (imp.archivoUrl != null) {

                try {

                    FileInputStream file = new FileInputStream(new File(imp.archivoUrl));
                    XSSFWorkbook workbook = new XSSFWorkbook(file);
                    XSSFSheet sheet = workbook.getSheetAt(0);

                    for (Row row : sheet) {
                        if (row.getRowNum() > 0) {
                            if (row.getCell(0) != null) {
                                Personal e = new Personal();
                                Tipo tipo = new Tipo();
                                Accion ac = new Accion();
                                e.setEmail(row.getCell(0).getStringCellValue().toLowerCase());
                                e.setNombre(row.getCell(1).getStringCellValue());
                                tipo.setId(Long.valueOf(row.getCell(2).getStringCellValue()));
                                e.setDatosTipo(tipo);
                                PersonalCongreso ec = new PersonalCongreso();
                                ec.setDatosPersonal(e);
                                ec.setDatosCongreso(congreso);
                                ec.setUuid("P" + UUID.randomUUID().toString());
                                ac.setRegistro(0);
                                ac.setBreakAM(0);
                                ac.setAlmuerzo(0);
                                ac.setBreakPM(0);
                                ac.setEmail(0);
                                ec.setDatosAccion(ac);
                                lista.add(ec);
                            }
                        }

                    }
                    if (!lista.isEmpty()) {
                        contador = 1;
                        lista.stream().forEach(ec -> {
                            pci.guardarPersona.accept(ec);
                            System.out.println("Importando " + contador++ + " de " + lista.size());
                        });
                        JOptionPane.showMessageDialog(null, "Datos importados");
                    } else {
                        JOptionPane.showMessageDialog(null, "No hay datos para procesar");
                    }

                    file.close();
                    lista.clear();
                    mostrarDatos();
                } catch (IOException ex) {
                    Logger.getLogger(AdministrarPersonal.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

            evaluarEmails(congreso.getId());

        });

        btnRegresar.addActionListener(l -> {
            AdminCongreso ac = new AdminCongreso(congreso);
            ac.setLocationRelativeTo(null);
            ac.setVisible(true);
            this.setVisible(false);
            this.dispose();

        });
        btnEliminar.addActionListener(l -> {
            
            pci.eliminarPersona.accept(persona);
            mostrarDatos();
        });
       
    }

    void mostrarDatos() {
        listadoModel = pci.listadoPersonal.apply(map);
        if(!listadoModel.isEmpty()){
        map.put("listado", listadoModel);
        pci.actualizarDatos.accept(map);
        
        }
        
    }

    public void evaluarEmails(Long id) {
        map.put("tipoEmail", 1);
        
        if ((long)pci.faltantesEmail.apply(map) > 0 ) {
            btnEnviar.setEnabled(true);
        } else {
            btnEnviar.setEnabled(false);
        }
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
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        btnEnviar = new rojeru_san.RSButtonRiple();
        btnImportar = new rojeru_san.RSButtonRiple();
        btnRegresar = new rojeru_san.RSButtonRiple();
        btnEliminar = new rojeru_san.RSButtonRiple();
        btnNuevo = new rojeru_san.RSButtonRiple();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        cboxTipo = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPersonal = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        panelGradient1.setideEndColor(new java.awt.Color(0, 204, 102));
        panelGradient1.setideStartColor(new java.awt.Color(0, 153, 153));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/congreso/img/logo.png"))); // NOI18N
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        jLabel2.setFont(new java.awt.Font("Cambria Math", 1, 28)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Administración del Personal");

        jPanel1.setOpaque(false);

        btnEnviar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 2, true));
        btnEnviar.setText("Enviar Correos");
        btnEnviar.setFont(new java.awt.Font("Cambria Math", 1, 18)); // NOI18N

        btnImportar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 2, true));
        btnImportar.setText("Importar");
        btnImportar.setFont(new java.awt.Font("Cambria Math", 1, 18)); // NOI18N

        btnRegresar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 2, true));
        btnRegresar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/congreso/img/atras.png"))); // NOI18N
        btnRegresar.setFont(new java.awt.Font("Cambria Math", 1, 18)); // NOI18N

        btnEliminar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 2, true));
        btnEliminar.setText("Eliminar");
        btnEliminar.setFont(new java.awt.Font("Cambria Math", 1, 18)); // NOI18N

        btnNuevo.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 2, true));
        btnNuevo.setText("Nuevo");
        btnNuevo.setFont(new java.awt.Font("Cambria Math", 1, 18)); // NOI18N

        jPanel2.setOpaque(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
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

        cboxTipo.setFont(new java.awt.Font("sansserif", 0, 22)); // NOI18N
        cboxTipo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboxTipoItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnImportar, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEnviar, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(136, 136, 136)
                .addComponent(cboxTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(101, 101, 101)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cboxTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnRegresar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnImportar, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnEnviar, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelGradient1Layout = new javax.swing.GroupLayout(panelGradient1);
        panelGradient1.setLayout(panelGradient1Layout);
        panelGradient1Layout.setHorizontalGroup(
            panelGradient1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelGradient1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(panelGradient1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 1078, Short.MAX_VALUE)))
        );
        panelGradient1Layout.setVerticalGroup(
            panelGradient1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGradient1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tblPersonal.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        tblPersonal.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblPersonal.setRowHeight(20);
        tblPersonal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPersonalMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblPersonal);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addComponent(panelGradient1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelGradient1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 467, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblPersonalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPersonalMouseClicked
        // TODO add your handling code here:
        try {
            int i = tblPersonal.getSelectedRow();
            ID = Long.valueOf(tblPersonal.getValueAt(i, 0).toString());
            persona.setId(ID);
        } catch (ArrayIndexOutOfBoundsException e1) {
        }
    }//GEN-LAST:event_tblPersonalMouseClicked

    private void cboxTipoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboxTipoItemStateChanged
        // TODO add your handling code here:
        if (cboxTipo.getSelectedIndex() == 0) {
            mostrarDatos();
        } else {
            listadoModel = pci.listadoPersonalPorTipo.apply(map);
            map.put("listado", listadoModel);
            pci.actualizarDatos.accept(map);
        }
    }//GEN-LAST:event_cboxTipoItemStateChanged

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private rojeru_san.RSButtonRiple btnEliminar;
    private rojeru_san.RSButtonRiple btnEnviar;
    private rojeru_san.RSButtonRiple btnImportar;
    private rojeru_san.RSButtonRiple btnNuevo;
    private rojeru_san.RSButtonRiple btnRegresar;
    private javax.swing.JComboBox<String> cboxTipo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private PanelGradient.PanelGradient panelGradient1;
    private javax.swing.JTable tblPersonal;
    // End of variables declaration//GEN-END:variables
}
