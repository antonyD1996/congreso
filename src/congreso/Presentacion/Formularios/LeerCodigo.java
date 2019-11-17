/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package congreso.Presentacion.Formularios;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import congreso.Dominio.Accion;
import congreso.Dominio.Congreso;
import congreso.Dominio.EstudianteCongreso;
import congreso.Infraestructura.EstudianteCongresoI;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import javax.swing.ImageIcon;

/**
 *
 * @author anton
 */
public class LeerCodigo extends javax.swing.JFrame implements Runnable, ThreadFactory {

    /**
     * Creates new form LeerCodigo
     */
    private static final long serialVersionUID = 6441489157408381878L;

    private Executor executor = Executors.newSingleThreadExecutor(this);

    private Webcam webcam = null;
    private WebcamPanel panel = null;

    Congreso congreso;
    Integer valor;
    Map<String, Object> map = new HashMap<>();

    int ancho = Toolkit.getDefaultToolkit().getScreenSize().width;
    Accion accion;
    String hora, minutos, segundos, ampm;
    Calendar calendario;
    Thread h1;

    public LeerCodigo(Congreso congreso, Integer valor, Integer camara) {

        initComponents();
        this.congreso = congreso;
        this.valor = valor;
        map.put("idCongreso", congreso.getId());
        setLayout(new FlowLayout());
        setTitle("Lectura de QR");
        //setBackground(Color.WHITE);

        Dimension size;
        if (ancho < 1920) {
            size = WebcamResolution.QVGA.getSize();
        } else {
            size = WebcamResolution.VGA.getSize();
        }

        webcam = Webcam.getWebcams().get(camara);

        System.out.println(Webcam.getWebcams());
        webcam.setViewSize(size);

        panel = new WebcamPanel(webcam);
        panel.setPreferredSize(size);
        panel.setFPSDisplayed(true);

        add(panel);
        add(pnlEstudiante);

        pack();
        setVisible(true);
        getContentPane().setBackground(Color.white);
        setLocationRelativeTo(null);
        LimpiarPanel();
        titulo();
        //System.out.println(size);
        h1 = new Thread(this);
        h1.start();
        executor.execute(this);

    }

    void buscarEstudiante(String uuid) {
        lblCodigo.setText("Validando...");
        EstudianteCongresoI ei = new EstudianteCongresoI();
        map.put("uuid", uuid);
        map.put("idCongreso", congreso.getId());
        EstudianteCongreso estudiant = ei.obtenerEstudiantePorUUID.apply(map);
        if (estudiant == null) {
            llenarPanel(null, null, 3, valor, null);
        } else {
            switch (valor) {
                case 2: {
                    if (estudiant.getAbono() == 65) {
                        if (estudiant.getDatosAccion().getRegistro() == 0) {
                            accion = estudiant.getDatosAccion();
                            accion.setRegistro(1);
                            accion.setHoraRegistro(LocalTime.now());
                            estudiant.setDatosAccion(accion);
                            ei.cambiarEstado.accept(estudiant);
                            llenarPanel(estudiant.getDatosEstudiante().getNombre(), estudiant.getDatosEstudiante().getCodigo(), 1, valor, null);
                        } else {
                            llenarPanel(estudiant.getDatosEstudiante().getNombre(), estudiant.getDatosEstudiante().getCodigo(), 2, valor, estudiant.getDatosAccion().getHoraRegistro());
                        }
                    } else {
                        llenarPanel(estudiant.getDatosEstudiante().getNombre(), estudiant.getDatosEstudiante().getCodigo(), 2, 6, null);
                    }

                }
                break;

                case 3: {
                    if (estudiant.getDatosAccion().getRegistro().equals(1)) {
                        if (estudiant.getDatosAccion().getBreakAM() == 0) {
                            accion = estudiant.getDatosAccion();
                            accion.setBreakAM(1);
                            accion.setHoraBreakAM(LocalTime.now());
                            estudiant.setDatosAccion(accion);
                            ei.cambiarEstado.accept(estudiant);
                            ei.cambiarEstado.accept(estudiant);
                            llenarPanel(estudiant.getDatosEstudiante().getNombre(), estudiant.getDatosEstudiante().getCodigo(), 1, valor, null);
                        } else {
                            llenarPanel(estudiant.getDatosEstudiante().getNombre(), estudiant.getDatosEstudiante().getCodigo(), 2, valor, estudiant.getDatosAccion().getHoraBreakAM());
                        }
                    } else {
                        llenarPanel(estudiant.getDatosEstudiante().getNombre(), estudiant.getDatosEstudiante().getCodigo(), 4, valor, null);
                    }

                }
                break;

                case 4: {

                    if (estudiant.getDatosAccion().getRegistro().equals(1)) {
                        if (estudiant.getDatosAccion().getAlmuerzo() == 0) {
                            accion = estudiant.getDatosAccion();
                            accion.setAlmuerzo(1);
                            accion.setHoraAlmuerzo(LocalTime.now());
                            estudiant.setDatosAccion(accion);
                            ei.cambiarEstado.accept(estudiant);
                            ei.cambiarEstado.accept(estudiant);
                            llenarPanel(estudiant.getDatosEstudiante().getNombre(), estudiant.getDatosEstudiante().getCodigo(), 1, valor, null);
                        } else {
                            llenarPanel(estudiant.getDatosEstudiante().getNombre(), estudiant.getDatosEstudiante().getCodigo(), 2, valor, estudiant.getDatosAccion().getHoraAlmuerzo());
                        }
                    } else {
                        llenarPanel(estudiant.getDatosEstudiante().getNombre(), estudiant.getDatosEstudiante().getCodigo(), 4, valor, null);
                    }

                }
                break;

                case 5: {

                    if (estudiant.getDatosAccion().getRegistro().equals(1)) {
                        if (estudiant.getDatosAccion().getBreakPM() == 0) {
                            accion = estudiant.getDatosAccion();
                            accion.setBreakPM(1);
                            accion.setHoraBreakPM(LocalTime.now());
                            estudiant.setDatosAccion(accion);
                            ei.cambiarEstado.accept(estudiant);
                            ei.cambiarEstado.accept(estudiant);
                            llenarPanel(estudiant.getDatosEstudiante().getNombre(), estudiant.getDatosEstudiante().getCodigo(), 1, valor, null);
                        } else {
                            llenarPanel(estudiant.getDatosEstudiante().getNombre(), estudiant.getDatosEstudiante().getCodigo(), 2, valor, estudiant.getDatosAccion().getHoraBreakPM());
                        }
                    } else {
                        llenarPanel(estudiant.getDatosEstudiante().getNombre(), estudiant.getDatosEstudiante().getCodigo(), 4, valor, null);
                    }

                }
                break;
            }
            estudiant = null;
        }
    }

    void LimpiarPanel() {
        lblEstudiante.setText("");
        lblIcono.setIcon(null);
        lblCodigo.setText("Escaneando...");
        lblEstado.setText("");
        lblCodigo.setVisible(true);
        lblHoraUsada.setVisible(false);
    }

    void titulo() {
        switch (valor) {
            case 2:
                lblTipo.setText("RECEPCIÓN");
                break;
            case 3:
                lblTipo.setText("BREAK AM");
                break;
            case 4:
                lblTipo.setText("ALMUERZO");
                break;
            case 5:
                lblTipo.setText("BREAK PM");
                break;
        }
    }

    void llenarPanel(String nombre, String codigo, Integer estado, Integer valor, LocalTime hora) {
        lblIcono.setVisible(true);
        switch (estado) {
            case 1: {
                lblCodigo.setVisible(true);
                lblCodigo.setText(codigo.toUpperCase());
                lblEstudiante.setVisible(true);
                lblEstudiante.setText(nombre);
                lblEstado.setText("OK");
                lblIcono.setIcon(new ImageIcon(getClass().getResource("/congreso/img/ok.png")));
            }
            break;
            case 2: {
                lblCodigo.setVisible(true);
                lblCodigo.setText(codigo.toUpperCase());
                lblEstudiante.setVisible(true);
                lblEstudiante.setText(nombre);
                switch (valor) {
                    case 2:
                        lblEstado.setText("EL ESTUDIANTE YA FUE REGISTRADO");
                        lblHoraUsada.setText("A LAS " + convertirHora(hora));
                        lblHoraUsada.setVisible(true);
                        break;
                    case 3:
                        lblEstado.setText("EL BREAK AM YA FUE TOMADO");
                        lblHoraUsada.setText("A LAS " + convertirHora(hora));
                        lblHoraUsada.setVisible(true);
                        break;
                    case 4:
                        lblEstado.setText("EL ALMUERZO YA FUE TOMADO");
                        lblHoraUsada.setText("A LAS " + convertirHora(hora));
                        lblHoraUsada.setVisible(true);
                        break;
                    case 5:
                        lblEstado.setText("EL BREAK PM YA FUE TOMADO");
                        lblHoraUsada.setText("A LAS " + convertirHora(hora));
                        lblHoraUsada.setVisible(true);
                        break;
                    case 6:
                        lblEstado.setText("PENDIENTE DE PAGO");
                        break;
                }

                lblIcono.setIcon(new ImageIcon(getClass().getResource("/congreso/img/advertencia.png")));
            }
            break;
            case 3: {
                lblEstudiante.setVisible(false);
                lblIcono.setIcon(new ImageIcon(getClass().getResource("/congreso/img/error.png")));
                lblCodigo.setVisible(false);
                lblEstado.setText("ESTUDIANTE NO REGISTRADO");
            }
            break;
            case 4: {
                lblCodigo.setVisible(true);
                lblCodigo.setText(codigo.toUpperCase());
                lblEstudiante.setVisible(true);
                lblEstudiante.setText(nombre);
                lblEstado.setText("ESTUDIANTE NO CONFIRMADO");
                lblIcono.setIcon(new ImageIcon(getClass().getResource("/congreso/img/advertencia.png")));
            }

        }

    }

    String convertirHora(LocalTime horaf) {
        String h, pmam, add;
        add = horaf.getHour()>9 ? "" : "0" ;
        if(horaf.getHour()>12){
            h= add+String.valueOf(horaf.getHour()-12);
            pmam=horaf.getHour()==24? "am":"pm";
        }else{
            h= add+String.valueOf(horaf.getHour());
            pmam = "am";
        }
        return h+":"+horaf.getMinute()+":"+horaf.getSecond()+" "+pmam;
    }

    @Override
    public void run() {
        Thread ct = Thread.currentThread();
        while (ct == h1) {
            calcula();
            lblHora.setText(calcula());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
        do {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Result result = null;
            BufferedImage image = null;

            if (webcam.isOpen()) {

                if ((image = webcam.getImage()) == null) {
                    continue;
                }

                LuminanceSource source = new BufferedImageLuminanceSource(image);
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                try {
                    result = new MultiFormatReader().decode(bitmap);
                } catch (NotFoundException e) {
                    // fall thru, it means there is no QR code in image
                }
            }

            if (result != null) {

                try {
                    buscarEstudiante(result.toString());
                    Thread.sleep(3000);
                    LimpiarPanel();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }

        } while (true);

    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, "example-runner");
        t.setDaemon(true);
        return t;
    }

    public String calcula() {
        Calendar calendario = new GregorianCalendar();
        Date fechaHoraActual = new Date();

        calendario.setTime(fechaHoraActual);
        ampm = calendario.get(Calendar.AM_PM) == Calendar.AM ? "am" : "pm";

        if (ampm.equals("PM")) {
            int h = calendario.get(Calendar.HOUR_OF_DAY) - 12;
            hora = h > 9 ? "" + h : "0" + h;
        } else {
            hora = calendario.get(Calendar.HOUR_OF_DAY) > 9 ? "" + calendario.get(Calendar.HOUR_OF_DAY) : "0" + calendario.get(Calendar.HOUR_OF_DAY);
        }
        minutos = calendario.get(Calendar.MINUTE) > 9 ? "" + calendario.get(Calendar.MINUTE) : "0" + calendario.get(Calendar.MINUTE);
        segundos = calendario.get(Calendar.SECOND) > 9 ? "" + calendario.get(Calendar.SECOND) : "0" + calendario.get(Calendar.SECOND);

        return hora + ":" + minutos + ":" + segundos + " " + ampm;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlEstudiante = new javax.swing.JPanel();
        lblIcono = new javax.swing.JLabel();
        lblEstado = new javax.swing.JLabel();
        lblEstudiante = new javax.swing.JLabel();
        lblTipo = new javax.swing.JLabel();
        lblCodigo = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        lblHora = new javax.swing.JLabel();
        lblHoraUsada = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        pnlEstudiante.setBackground(new java.awt.Color(255, 255, 255));
        pnlEstudiante.setPreferredSize(new java.awt.Dimension(900, 640));

        lblIcono.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblIcono.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblIcono.setIcon(new javax.swing.ImageIcon(getClass().getResource("/congreso/img/ok.png"))); // NOI18N
        lblIcono.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        lblEstado.setBackground(new java.awt.Color(0, 0, 0));
        lblEstado.setFont(new java.awt.Font("Tahoma", 1, 22)); // NOI18N
        lblEstado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEstado.setText("REGISTRADO");
        lblEstado.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        lblEstudiante.setBackground(new java.awt.Color(0, 0, 0));
        lblEstudiante.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblEstudiante.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEstudiante.setText("ANTONY DAVID DUARTE PERLERA");
        lblEstudiante.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        lblTipo.setBackground(new java.awt.Color(255, 255, 255));
        lblTipo.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblTipo.setForeground(new java.awt.Color(14, 55, 177));
        lblTipo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTipo.setText("REGISTRO");
        lblTipo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblTipo.setOpaque(true);
        lblTipo.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

        lblCodigo.setBackground(new java.awt.Color(0, 0, 0));
        lblCodigo.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblCodigo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCodigo.setText("DP0024032016");

        jLabel1.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Hora:");

        lblHora.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        lblHora.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblHora.setText("lblHora");

        lblHoraUsada.setBackground(new java.awt.Color(0, 0, 0));
        lblHoraUsada.setFont(new java.awt.Font("Tahoma", 1, 22)); // NOI18N
        lblHoraUsada.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHoraUsada.setText("A LAS ");
        lblHoraUsada.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        javax.swing.GroupLayout pnlEstudianteLayout = new javax.swing.GroupLayout(pnlEstudiante);
        pnlEstudiante.setLayout(pnlEstudianteLayout);
        pnlEstudianteLayout.setHorizontalGroup(
            pnlEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblIcono, javax.swing.GroupLayout.DEFAULT_SIZE, 903, Short.MAX_VALUE)
            .addGroup(pnlEstudianteLayout.createSequentialGroup()
                .addGroup(pnlEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTipo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblEstudiante, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblCodigo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(1, 1, 1))
            .addComponent(lblEstado, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlEstudianteLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(157, 157, 157))
            .addComponent(lblHoraUsada, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnlEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlEstudianteLayout.createSequentialGroup()
                    .addContainerGap(754, Short.MAX_VALUE)
                    .addComponent(lblHora, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );
        pnlEstudianteLayout.setVerticalGroup(
            pnlEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEstudianteLayout.createSequentialGroup()
                .addComponent(lblTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblEstudiante, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblIcono, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblHoraUsada, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 89, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap())
            .addGroup(pnlEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlEstudianteLayout.createSequentialGroup()
                    .addContainerGap(560, Short.MAX_VALUE)
                    .addComponent(lblHora)
                    .addGap(5, 5, 5)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlEstudiante, javax.swing.GroupLayout.DEFAULT_SIZE, 903, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlEstudiante, javax.swing.GroupLayout.DEFAULT_SIZE, 594, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        AdminCongreso ac = new AdminCongreso(congreso);
        ac.setLocationRelativeTo(null);
        this.setVisible(false);
        ac.setVisible(true);
        webcam.close();
    }//GEN-LAST:event_formWindowClosing

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblCodigo;
    private javax.swing.JLabel lblEstado;
    private javax.swing.JLabel lblEstudiante;
    private javax.swing.JLabel lblHora;
    private javax.swing.JLabel lblHoraUsada;
    private javax.swing.JLabel lblIcono;
    private javax.swing.JLabel lblTipo;
    private javax.swing.JPanel pnlEstudiante;
    // End of variables declaration//GEN-END:variables
}
