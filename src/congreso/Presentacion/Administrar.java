/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package congreso.Presentacion;

import com.google.zxing.WriterException;
import congreso.Dominio.Congreso;
import congreso.Dominio.Estudiante;
import congreso.Dominio.EstudianteCongreso;
import congreso.Infraestructura.EstudianteCongresoI;
import congreso.Utilidades.QRGenerator;
import congreso.Utilidades.SendEmail;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author anton
 */
public class Administrar extends javax.swing.JFrame {

    /**
     * Creates new form Administrar
     */
    List<EstudianteCongreso> listadoModel, faltantesEmail;
    EstudianteCongresoI ei = new EstudianteCongresoI();
    Congreso congreso;

    public Administrar(Congreso congreso) throws WriterException, IOException {
        this.congreso = congreso;
        initComponents();
        init();
    }

    public void init() throws WriterException, IOException {
        mostrarEstudiantes.accept(tblEstudiantes);
        tblEstudiantes.setFillsViewportHeight(true);
        if (listadoModel == null) {
            listadoModel = new ArrayList<>();
        }
        evaluarEmails(congreso.getId());

        btnEnviar.addActionListener(l -> {
            faltantesEmail = ei.listadoFaltantesEmail.apply(congreso.getId());
            faltantesEmail.stream().forEach(fe -> {
                try {
                    QRGenerator qr = new QRGenerator();
                    File file = qr.generateQRCodeImage(fe.getDatosEstudiante().getCodigo());
                    SendEmail se = new SendEmail();
                    se.enviar(fe.getDatosEstudiante().getCodigo(), file.getAbsolutePath());
                    ei.cambiarEstado.accept(fe, 1);
                } catch (WriterException | IOException e) {
                    System.out.println(e);
                }

            });
            evaluarEmails(congreso.getId());
            mostrarEstudiantes.accept(tblEstudiantes);
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

                    List<EstudianteCongreso> lista = new ArrayList();

                    for (Row row : sheet) {
                        if (row.getRowNum() > 0) {
                            if (row.getCell(0) != null) {

                                Estudiante e = new Estudiante();
                                e.setCodigo(row.getCell(0).getStringCellValue().toLowerCase());
                                e.setNombre(row.getCell(1).getStringCellValue());
                                e.setCarrera(row.getCell(2).getStringCellValue());
                                e.setRegional(row.getCell(3).getStringCellValue());
                                e.setDatosCongreso(congreso);
                                EstudianteCongreso ec = new EstudianteCongreso();
                                ec.setDatosEstudiante(e);
                                ec.setRegistro(0);
                                ec.setBreakAM(0);
                                ec.setAlmuerzo(0);
                                ec.setBreakPM(0);
                                ec.setEmail(0);
                                lista.add(ec);
                            }
                        }
                    }
                    if (!lista.isEmpty()) {

                        ei.guardarVarios.accept(lista);
                        mostrarEstudiantes.accept(tblEstudiantes);
                    } else {
                        JOptionPane.showMessageDialog(null, "No hay datos para procesar");
                    }

                    file.close();

                } catch (Exception ex) {
                    Logger.getLogger(Administrar.class.getName()).log(Level.SEVERE, null, ex);
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

    }
    Consumer<JTable> mostrarEstudiantes = (t) -> {
        listadoModel = ei.actualizarDatos.apply(congreso.getId(), t);
    };

    public void imprimir(String texto) {
        System.out.println(texto);
    }

    public void evaluarEmails(Long id) {
        if (ei.faltantesEmail.apply(id) > 0) {
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        btnImportar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        btnRegresar = new javax.swing.JButton();
        btnEnviar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblEstudiantes = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("ADMINISTRACION DE DATOS");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        btnImportar.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnImportar.setText("Importar");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setText("Busqueda:");

        btnRegresar.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        btnRegresar.setText("<<");

        btnEnviar.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnEnviar.setText("Enviar Codigos");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(btnRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnImportar, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEnviar, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtBuscar, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnRegresar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnImportar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnEnviar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setAutoscrolls(true);

        tblEstudiantes.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        tblEstudiantes.setModel(new javax.swing.table.DefaultTableModel(
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
        tblEstudiantes.setEnabled(false);
        tblEstudiantes.setFillsViewportHeight(true);
        tblEstudiantes.setRowHeight(20);
        jScrollPane1.setViewportView(tblEstudiantes);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1157, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 765, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEnviar;
    private javax.swing.JButton btnImportar;
    private javax.swing.JButton btnRegresar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblEstudiantes;
    private javax.swing.JTextField txtBuscar;
    // End of variables declaration//GEN-END:variables
}
