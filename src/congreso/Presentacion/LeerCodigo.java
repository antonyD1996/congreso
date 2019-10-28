/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package congreso.Presentacion;

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
import congreso.Dominio.Congreso;
import congreso.Dominio.EstudianteCongreso;
import congreso.Infraestructura.EstudianteCongresoI;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.util.List;
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
    Integer valor, estado;
    EstudianteCongresoI ei = new EstudianteCongresoI();
    List<EstudianteCongreso> listadoModel;

    public LeerCodigo(Congreso congreso, Integer valor) {

        initComponents();
        this.congreso = congreso;
        this.valor = valor;
        setLayout(new FlowLayout());
        setTitle("Lectura de QR");
        //setBackground(Color.WHITE);

        Dimension size = WebcamResolution.VGA.getSize();

        webcam = Webcam.getWebcams().get(0);
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
        //System.out.println(size);

        executor.execute(this);
    }

    void buscar(String codigo) {
        listadoModel = ei.listadoEstudiantes.apply(congreso.getId());
        EstudianteCongreso estudiant = listadoModel.stream().filter(p -> p.getDatosEstudiante().getCodigo().equals(codigo)).findFirst().orElse(null);
        if (estudiant == null) {
            llenarPanel(null, codigo);
            estado = 3;
        } else {
            switch (valor) {
                case 2: {
                    if (estudiant.getRegistro() == 0) {
                        ei.cambiarEstado.accept(estudiant, valor);
                        estado = 1;
                        llenarPanel(estudiant.getDatosEstudiante().getNombre(), codigo);
                    } else {
                        estado = 2;
                        llenarPanel(estudiant.getDatosEstudiante().getNombre(), codigo);
                    }
                }
                break;

                case 3: {
                    if (estudiant.getBreakAM() == 0) {
                        ei.cambiarEstado.accept(estudiant, valor);
                        estado = 1;
                        llenarPanel(estudiant.getDatosEstudiante().getNombre(), codigo);
                    } else {
                        estado = 2;
                        llenarPanel(estudiant.getDatosEstudiante().getNombre(), codigo);
                    }
                }
                break;

                case 4: {
                    if (estudiant.getAlmuerzo() == 0) {
                        ei.cambiarEstado.accept(estudiant, valor);
                        estado = 1;
                        llenarPanel(estudiant.getDatosEstudiante().getNombre(), codigo);
                    } else {
                        estado = 2;
                        llenarPanel(estudiant.getDatosEstudiante().getNombre(), codigo);
                    }
                }
                break;

                case 5: {
                    if (estudiant.getBreakPM() == 0) {
                        ei.cambiarEstado.accept(estudiant, valor);
                        estado = 1;
                        llenarPanel(estudiant.getDatosEstudiante().getNombre(), codigo);
                    } else {
                        estado = 2;
                        llenarPanel(estudiant.getDatosEstudiante().getNombre(), codigo);
                    }
                }
                break;

            }

        }
    }

    void LimpiarPanel() {
        lblEstudiante.setText("");
        lblIcono.setIcon(null);
        lblCodigo.setText("Escaneando...");
        lblEstado.setText("");
        lblCodigo.setVisible(true);
    }

    void llenarPanel(String nombre, String codigo) {
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
                        break;
                    case 3:
                        lblEstado.setText("EL BREAK AM YA FUE TOMADO");
                        break;
                    case 4:
                        lblEstado.setText("EL ALMUERZO YA FUE TOMADO");
                        break;
                    case 5:
                        lblEstado.setText("EL BREAK PM YA FUE TOMADO");
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

        }

    }

    @Override
    public void run() {
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
                    buscar(result.toString());
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
        lblCodigo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
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

        lblEstado.setFont(new java.awt.Font("Tahoma", 1, 22)); // NOI18N
        lblEstado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEstado.setText("REGISTRADO");
        lblEstado.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        lblEstudiante.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblEstudiante.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEstudiante.setText("ANTONY DAVID DUARTE PERLERA");
        lblEstudiante.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        lblCodigo.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblCodigo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCodigo.setText("DP0024032016");
        lblCodigo.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        javax.swing.GroupLayout pnlEstudianteLayout = new javax.swing.GroupLayout(pnlEstudiante);
        pnlEstudiante.setLayout(pnlEstudianteLayout);
        pnlEstudianteLayout.setHorizontalGroup(
            pnlEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEstudianteLayout.createSequentialGroup()
                .addGroup(pnlEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblIcono, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblEstudiante, javax.swing.GroupLayout.PREFERRED_SIZE, 870, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 870, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 1, Short.MAX_VALUE))
            .addComponent(lblEstado, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlEstudianteLayout.setVerticalGroup(
            pnlEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEstudianteLayout.createSequentialGroup()
                .addComponent(lblEstudiante, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblIcono, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblEstado, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlEstudiante, javax.swing.GroupLayout.PREFERRED_SIZE, 871, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlEstudiante, javax.swing.GroupLayout.PREFERRED_SIZE, 539, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        AdminCongreso ac = new AdminCongreso(congreso);
                ac.setLocationRelativeTo(null);
                this.setVisible(false);
                ac.setVisible(true);
                this.dispose(); 
    }//GEN-LAST:event_formWindowClosing

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblCodigo;
    private javax.swing.JLabel lblEstado;
    private javax.swing.JLabel lblEstudiante;
    private javax.swing.JLabel lblIcono;
    private javax.swing.JPanel pnlEstudiante;
    // End of variables declaration//GEN-END:variables
}
