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
import congreso.Dominio.Estudiante;
import congreso.Dominio.EstudianteCongreso;
import congreso.Infraestructura.EstudianteCongresoI;
import congreso.Presentacion.Dialogos.Abonar;
import congreso.Presentacion.Dialogos.Importar;
import congreso.Presentacion.Dialogos.RegistrarEstudiante;
import congreso.Utilidades.Datos;
import congreso.Utilidades.ExportarEstudiantes;
import congreso.Utilidades.QRGenerator;
import congreso.Utilidades.SendEmail;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class AdministrarEstudiantes extends javax.swing.JFrame {

    /**
     * Creates new form AdministrarEstudiantes
     */
    List<EstudianteCongreso> listadoModel,faltantesEmail,lista,listaExportar,busqueda;
    EstudianteCongresoI ei = new EstudianteCongresoI();
    Congreso congreso;
    Long ID;
    Accion accion;
    int contador = 1;
    List<Integer> datos;
    Integer pagina = 1;
    Integer total, ultima;
    Map<String, Object> map = new HashMap<>();
    EstudianteCongreso estudiante = null;
    Integer pendientes;
    Datos sesion = new Datos();

    public AdministrarEstudiantes(Congreso congreso) throws WriterException, IOException, Exception {
        this.congreso = congreso;
        initComponents();
        init();
    }

    public void init() throws WriterException, IOException, Exception {
        lista = new ArrayList<>();
        busqueda = new ArrayList<>();

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        map.put("idCongreso", congreso.getId());
        map.put("pagina", pagina);
        map.put("tabla", tblEstudiantes);
        mostrarDatos();
        tblEstudiantes.setFillsViewportHeight(true);
        if (listadoModel == null) {
            listadoModel = new ArrayList<>();
        }
        evaluarEmails(congreso.getId());

        btnEnviar.addActionListener(l -> {
            faltantesEmail = ei.listadoFaltantesEmail.apply(congreso.getId());
            contador = 1;
            
            boolean fallo = false;

            for (EstudianteCongreso fe : faltantesEmail) {
                try {
                    QRGenerator qr = new QRGenerator();
                    File file = qr.generateQRCodeImage(fe.getUuid());
                    SendEmail se = new SendEmail();
                    se.enviar(congreso.getNombre(),fe.getDatosEstudiante().getCodigo()+"@unab.edu.sv", fe.getDatosEstudiante().getNombre(), file.getAbsolutePath(), sesion.getDatosSesion());
                     accion = fe.getDatosAccion();
                    accion.setEmail(1);
                    fe.setDatosAccion(accion);
                    ei.cambiarEstado.accept(fe);
                    System.out.println("Enviando email " + contador++ + " de " + faltantesEmail.size() + " a " + fe.getDatosEstudiante().getNombre());
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
        btnExportar.addActionListener(l -> {

            if (cboxRegional.getSelectedIndex() == 0) {
                listaExportar = ei.listadoEstudiantes.apply(congreso.getId());
                map.put("regional", "Todas");
            } else {
                listaExportar = ei.listadoEstudiantesPorRegional.apply(map);
            }

            if (listaExportar.isEmpty()) {
                JOptionPane.showMessageDialog(null, "NO hay datos para exportar");
            } else {
                try {
                    map.put("congreso", congreso);
                    evaluarRegional(cboxRegional.getSelectedIndex());
                    map.put("listado", listaExportar);
                    ExportarEstudiantes ex = new ExportarEstudiantes();
                    ex.Exportar(map);
                    listaExportar.clear();
                } catch (IOException ex1) {
                    Logger.getLogger(AdministrarEstudiantes.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }

        });
        btnNuevo.addActionListener(l -> {
            RegistrarEstudiante re = new RegistrarEstudiante(new java.awt.Frame(), true);
            re.setLocationRelativeTo(null);
            re.setVisible(true);

            if (re.estudiante != null) {
                Accion ac = new Accion();
                EstudianteCongreso ec = new EstudianteCongreso();
                ec.setUuid("E"+UUID.randomUUID().toString());
                ac.setRegistro(0);
                ac.setBreakAM(0);
                ac.setAlmuerzo(0);
                ac.setBreakPM(0);
                ac.setEmail(0);
                ec.setDatosAccion(ac);
                ec.setAbono(re.abono);
                ec.setDatosEstudiante(re.estudiante);
                ec.setDatosCongreso(congreso);
                ei.guardarEstudiante.accept(ec);
                JOptionPane.showMessageDialog(null, "Estudiante registrado");
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
                                Estudiante e = new Estudiante();
                                Accion ac = new Accion();
                                e.setCodigo(row.getCell(0).getStringCellValue().toLowerCase());
                                e.setNombre(row.getCell(1).getStringCellValue());
                                e.setCarrera(row.getCell(2).getStringCellValue());
                                e.setRegional(row.getCell(3).getStringCellValue());
                                EstudianteCongreso ec = new EstudianteCongreso();
                                ec.setDatosEstudiante(e);
                                ec.setDatosCongreso(congreso);
                                ec.setUuid("E"+UUID.randomUUID().toString());
                                ac.setRegistro(0);
                                ac.setBreakAM(0);
                                ac.setAlmuerzo(0);
                                ac.setBreakPM(0);
                                ac.setEmail(0);
                                ec.setDatosAccion(ac);
                                ec.setAbono((int) row.getCell(4).getNumericCellValue());
                                lista.add(ec);
                            }
                        }

                    }
                    if (!lista.isEmpty()) {
                        contador = 1;
                        lista.stream().forEach(ec -> {
                            Map<String, Object> map = new HashMap<>();
                            map.put("codigo", ec.getDatosEstudiante().getCodigo());
                            map.put("idCongreso", ec.getDatosCongreso().getId());


                            estudiante = ei.obtenerEstudianteporCodigo.apply(map);
                            
                            if (estudiante == null) {
                                ei.guardarEstudiante.accept(ec);
                                System.out.println("Importando " + contador++ + " de " + lista.size());

                            } else {
                                if (estudiante.getAbono() < 65) {
                                    estudiante.setAbono(ec.getAbono());
                                    ei.abonar.accept(estudiante);
                                }

                            }
                        });
                        JOptionPane.showMessageDialog(null, "Datos importados");
                    } else {
                        JOptionPane.showMessageDialog(null, "No hay datos para procesar");
                    }

                    file.close();
                    lista.clear();
                    mostrarDatos();
                    file = null;
                } catch (IOException ex) {
                    Logger.getLogger(AdministrarEstudiantes.class.getName()).log(Level.SEVERE, null, ex);
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
            EstudianteCongreso ec = new EstudianteCongreso();

            ec.setId(ID);

            ei.eliminarEstudiante.accept(ec);
            mostrarDatos();
        });
        btnPrimero.addActionListener(l -> {
            pagina = 1;
            mostrarDatos();

        });
        btnUltimo.addActionListener(l -> {
            pagina = ultima;
            mostrarDatos();

        });
        btnSiguiente.addActionListener(l -> {
            if (pagina < ultima) {
                pagina++;
                mostrarDatos();
            }
        });
        btnAnterior.addActionListener(l -> {
            if (pagina > 1) {
                pagina--;
                mostrarDatos();
            }
        });
        btnPendientes.addActionListener(l -> {
            listadoModel = ei.estudiantesPendietes.apply(congreso.getId());
            map.put("listado", listadoModel);
            ei.actualizarDatos.accept(map);
        });
        btnReset.addActionListener(l -> {
            mostrarDatos();
        });

        btnAbonar.addActionListener(l -> {
            estudiante = ei.obtenerEstudianteporID.apply(ID);

            if (estudiante != null) {
                Abonar ad = new Abonar(this, true);
                ad.setLocationRelativeTo(null);
                ad.setVisible(true);

                if (ad.monto != null) {
                    estudiante.setAbono(ad.monto);
                    ei.abonar.accept(estudiante);
                    listadoModel = ei.estudiantesPendietes.apply(congreso.getId());
                    map.put("listado", listadoModel);
                    if (listadoModel.size() > 0) {
                        evaluarPendientes();
                        map.put("listado", listadoModel);
                        ei.actualizarDatos.accept(map);
                    } else {
                        mostrarDatos();
                    }

                }
            }
        });
    }

    void mostrarDatos() {
        map.put("pagina", pagina);

        listadoModel = ei.listadoEstudiantesPaginado.apply(map);
        map.put("listado", listadoModel);
        ei.actualizarDatos.accept(map);
        lblPrimero.setText(String.valueOf(pagina));
        evaluarPendientes();
        cambiarEstados();
    }

    void cambiarEstados() {
        if (cboxRegional.getSelectedIndex() == 0) {
            map.put("tipoEstadistica", 1);
            

        } else {
            map.put("tipoEstadistica", 2);
            pagina = 1;
        }
        total = Integer.valueOf(ei.obtenerEstadisticas.apply(map).get("total").toString());
        ultima = (int) ((total / 20) + 1);
        lblUltimo.setText(String.valueOf(ultima));
    }

    void evaluarPendientes() {
        btnPendientes.setEnabled(false);
        btnAbonar.setEnabled(false);
        pendientes = ei.estudiantesPendietes.apply(congreso.getId()).size();
        if (pendientes > 0) {
            btnPendientes.setEnabled(true);
            btnAbonar.setEnabled(true);
            btnPendientes.setText("Pendientes (" + pendientes.toString() + ")");
        }
    }

    public void evaluarEmails(Long id) {
        if (ei.faltantesEmail.apply(id) > 0) {
            btnEnviar.setEnabled(true);
        } else {
            btnEnviar.setEnabled(false);
        }
    }

    void evaluarRegional(Integer item) {
        switch (item) {
           case 1: {
                map.put("regional", "SS");
            }
            break;
            case 2: {
                map.put("regional", "SM");
            }
            break;
            case 3: {
                map.put("regional", "CH");
            }
            break;
            case 4: {
                map.put("regional", "SO");
            }
            break;
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
        btnUltimo = new rojeru_san.RSButtonRiple();
        btnSiguiente = new rojeru_san.RSButtonRiple();
        btnAnterior = new rojeru_san.RSButtonRiple();
        btnPrimero = new rojeru_san.RSButtonRiple();
        jLabel3 = new javax.swing.JLabel();
        lblPrimero = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblUltimo = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        btnExportar = new rojeru_san.RSButtonRiple();
        btnEnviar = new rojeru_san.RSButtonRiple();
        btnImportar = new rojeru_san.RSButtonRiple();
        btnRegresar = new rojeru_san.RSButtonRiple();
        btnEliminar = new rojeru_san.RSButtonRiple();
        btnNuevo = new rojeru_san.RSButtonRiple();
        btnAbonar = new rojeru_san.RSButtonRiple();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        btnReset = new rojeru_san.RSButtonRiple();
        btnPendientes = new rojeru_san.RSButtonRiple();
        jLabel5 = new javax.swing.JLabel();
        cboxRegional = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblEstudiantes = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panelGradient1.setideEndColor(new java.awt.Color(0, 204, 102));
        panelGradient1.setideStartColor(new java.awt.Color(0, 153, 153));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/congreso/img/logo.png"))); // NOI18N
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        jLabel2.setFont(new java.awt.Font("Cambria Math", 1, 28)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Administración de Estudiantes");

        btnUltimo.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 2, true));
        btnUltimo.setText(">>");
        btnUltimo.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N

        btnSiguiente.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 2, true));
        btnSiguiente.setText(">");
        btnSiguiente.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N

        btnAnterior.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 2, true));
        btnAnterior.setText("<");
        btnAnterior.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N

        btnPrimero.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 2, true));
        btnPrimero.setText("<<");
        btnPrimero.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N

        jLabel3.setFont(new java.awt.Font("sansserif", 1, 20)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Pagina ");

        lblPrimero.setFont(new java.awt.Font("sansserif", 1, 20)); // NOI18N
        lblPrimero.setForeground(new java.awt.Color(255, 255, 255));
        lblPrimero.setText("1");

        jLabel4.setFont(new java.awt.Font("sansserif", 1, 20)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("de");

        lblUltimo.setFont(new java.awt.Font("sansserif", 1, 20)); // NOI18N
        lblUltimo.setForeground(new java.awt.Color(255, 255, 255));
        lblUltimo.setText("100");

        txtBuscar.setFont(new java.awt.Font("sansserif", 0, 22)); // NOI18N
        txtBuscar.setToolTipText("");
        txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBuscarKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBuscarKeyTyped(evt);
            }
        });

        jPanel1.setOpaque(false);

        btnExportar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 2, true));
        btnExportar.setText("Exportar");
        btnExportar.setFont(new java.awt.Font("Cambria Math", 1, 18)); // NOI18N

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

        btnAbonar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 2, true));
        btnAbonar.setText("Abonar");
        btnAbonar.setFont(new java.awt.Font("Cambria Math", 1, 18)); // NOI18N

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

        btnReset.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 2, true));
        btnReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/congreso/img/reset.png"))); // NOI18N
        btnReset.setFont(new java.awt.Font("Cambria Math", 1, 18)); // NOI18N

        btnPendientes.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 2, true));
        btnPendientes.setText("Pendientes");
        btnPendientes.setFont(new java.awt.Font("Cambria Math", 1, 16)); // NOI18N

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
                .addComponent(btnExportar, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEnviar, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAbonar, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPendientes, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnRegresar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnImportar, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnEnviar, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnExportar, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnAbonar, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnPendientes, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnReset, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jLabel5.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Busqueda:");

        cboxRegional.setFont(new java.awt.Font("sansserif", 0, 18)); // NOI18N
        cboxRegional.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todas las Regionales", "San Salvador", "San Miguel", "Chalatenango", "Sonsonate" }));
        cboxRegional.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboxRegionalItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout panelGradient1Layout = new javax.swing.GroupLayout(panelGradient1);
        panelGradient1.setLayout(panelGradient1Layout);
        panelGradient1Layout.setHorizontalGroup(
            panelGradient1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(panelGradient1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelGradient1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelGradient1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblPrimero)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblUltimo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnPrimero, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAnterior, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSiguiente, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnUltimo, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboxRegional, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addGroup(panelGradient1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelGradient1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnAnterior, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(panelGradient1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnPrimero, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblPrimero)
                            .addComponent(jLabel4)
                            .addComponent(lblUltimo)
                            .addComponent(jLabel3)))
                    .addGroup(panelGradient1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(panelGradient1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnUltimo, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnSiguiente, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addComponent(cboxRegional, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

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
        tblEstudiantes.setRowHeight(20);
        tblEstudiantes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblEstudiantesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblEstudiantes);

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

    private void tblEstudiantesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblEstudiantesMouseClicked
        // TODO add your handling code here:
        try {
            int i = tblEstudiantes.getSelectedRow();
            ID = Long.valueOf(tblEstudiantes.getValueAt(i, 0).toString());

        } catch (ArrayIndexOutOfBoundsException e1) {
        }
    }//GEN-LAST:event_tblEstudiantesMouseClicked

    private void txtBuscarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarKeyTyped
        // TODO add your handling code here:
        if (!Character.isDigit(evt.getKeyChar()) && !(evt.getKeyChar() == KeyEvent.VK_BACK_SPACE) && !Character.isLetter(evt.getKeyChar())) {
            evt.consume();
        }
        if (txtBuscar.getText().length() > 11) {
            evt.consume();
        }
        if (txtBuscar.getText().length() == 0) {
            mostrarDatos();
        }
    }//GEN-LAST:event_txtBuscarKeyTyped

    private void txtBuscarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            map.put("codigo", txtBuscar.getText().toLowerCase());
            estudiante = ei.obtenerEstudianteporCodigo.apply(map);
            if (estudiante != null) {
                listadoModel.clear();
                listadoModel.add(estudiante);
                map.put("listado", listadoModel);
                ei.actualizarDatos.accept(map);
            } else {
                JOptionPane.showMessageDialog(null, "No se encontraron datos");
            }
        }
    }//GEN-LAST:event_txtBuscarKeyPressed

    private void cboxRegionalItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboxRegionalItemStateChanged
        // TODO add your handling code here:
        evaluarRegional(cboxRegional.getSelectedIndex());
        if (cboxRegional.getSelectedIndex() == 0) {
            mostrarDatos();
        } else {
            listadoModel = ei.listadoEstudiantesPorRegionalPaginado.apply(map);
            map.put("listado", listadoModel);
            ei.actualizarDatos.accept(map);
            cambiarEstados();

        }
    }//GEN-LAST:event_cboxRegionalItemStateChanged

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private rojeru_san.RSButtonRiple btnAbonar;
    private rojeru_san.RSButtonRiple btnAnterior;
    private rojeru_san.RSButtonRiple btnEliminar;
    private rojeru_san.RSButtonRiple btnEnviar;
    private rojeru_san.RSButtonRiple btnExportar;
    private rojeru_san.RSButtonRiple btnImportar;
    private rojeru_san.RSButtonRiple btnNuevo;
    private rojeru_san.RSButtonRiple btnPendientes;
    private rojeru_san.RSButtonRiple btnPrimero;
    private rojeru_san.RSButtonRiple btnRegresar;
    private rojeru_san.RSButtonRiple btnReset;
    private rojeru_san.RSButtonRiple btnSiguiente;
    private rojeru_san.RSButtonRiple btnUltimo;
    private javax.swing.JComboBox<String> cboxRegional;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblPrimero;
    private javax.swing.JLabel lblUltimo;
    private PanelGradient.PanelGradient panelGradient1;
    private javax.swing.JTable tblEstudiantes;
    private javax.swing.JTextField txtBuscar;
    // End of variables declaration//GEN-END:variables
}
