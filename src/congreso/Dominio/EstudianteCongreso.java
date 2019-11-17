/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package congreso.Dominio;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author anton
 */
@Entity
@Table(schema = "cg", name = "estudiante_congreso")
@SequenceGenerator(schema = "cg", sequenceName = "estudiante_congreso_id_seq", name = "Estudiante_Congreso_seq_id", allocationSize = 1)
@NamedQueries({
    @NamedQuery(name = "EstudianteCongreso.findAll", query = "SELECT ec FROM EstudianteCongreso ec where ec.datosCongreso.id = :idCongreso order by ec.datosAccion.registro DESC"),
    @NamedQuery(name = "EstudianteCongreso.findPorRegional", query = "SELECT ec FROM EstudianteCongreso ec where ec.datosCongreso.id = :idCongreso and ec.datosEstudiante.regional = :regional order by ec.datosAccion.registro DESC"),
    @NamedQuery(name = "EstudianteCongreso.findByUUID", query = "SELECT ec FROM EstudianteCongreso ec where ec.uuid = :uuid and ec.datosCongreso.id = :idCongreso"),
    @NamedQuery(name = "EstudianteCongreso.findByCodigo", query = "SELECT ec FROM EstudianteCongreso ec where ec.datosEstudiante.codigo = :codigo and ec.datosCongreso.id = :idCongreso"),
    @NamedQuery(name = "EstudianteCongreso.findEmailFaltantes", query = "SELECT ec FROM EstudianteCongreso ec where ec.datosCongreso.id =:idCongreso and ec.datosAccion.email=0"),
    @NamedQuery(name = "EstudianteCongreso.findPendientes", query = "SELECT ec FROM EstudianteCongreso ec where ec.datosCongreso.id =:idCongreso and ec.abono<65"),
    @NamedQuery(name = "EstudianteCongreso.findEmailFaltantesTotal", query = "SELECT COUNT(ec.id) FROM EstudianteCongreso ec where ec.datosCongreso.id =:idCongreso and ec.datosAccion.email=0"),
    @NamedQuery(name = "EstudianteCongreso.findTotal", query = "SELECT COUNT(ec.id) FROM EstudianteCongreso ec where ec.datosCongreso.id =:idCongreso"),
    @NamedQuery(name = "EstudianteCongreso.findRegistrados", query = "SELECT COUNT(ec.id) FROM EstudianteCongreso ec where ec.datosCongreso.id =:idCongreso and ec.datosAccion.registro=1"),
    @NamedQuery(name = "EstudianteCongreso.findBreakAM", query = "SELECT COUNT(ec.id) FROM EstudianteCongreso ec where ec.datosCongreso.id =:idCongreso and ec.datosAccion.breakAM=1"),
    @NamedQuery(name = "EstudianteCongreso.findAlmuerzo", query = "SELECT COUNT(ec.id) FROM EstudianteCongreso ec where ec.datosCongreso.id =:idCongreso and ec.datosAccion.almuerzo=1"),
    @NamedQuery(name = "EstudianteCongreso.findBreakPM", query = "SELECT COUNT(ec.id) FROM EstudianteCongreso ec where ec.datosCongreso.id =:idCongreso and ec.datosAccion.breakPM=1"),
    @NamedQuery(name = "EstudianteCongreso.findTotalPorRegional", query = "SELECT COUNT(ec.id) FROM EstudianteCongreso ec where ec.datosCongreso.id =:idCongreso and ec.datosEstudiante.regional=:regional"),
    @NamedQuery(name = "EstudianteCongreso.findRegistradosPorRegional", query = "SELECT COUNT(ec.id) FROM EstudianteCongreso ec where ec.datosCongreso.id =:idCongreso and ec.datosAccion.registro=1 and ec.datosEstudiante.regional=:regional"),
    @NamedQuery(name = "EstudianteCongreso.findBreakAMPorRegional", query = "SELECT COUNT(ec.id) FROM EstudianteCongreso ec where ec.datosCongreso.id =:idCongreso and ec.datosAccion.breakAM=1 and ec.datosEstudiante.regional=:regional"),
    @NamedQuery(name = "EstudianteCongreso.findAlmuerzoPorRegional", query = "SELECT COUNT(ec.id) FROM EstudianteCongreso ec where ec.datosCongreso.id =:idCongreso and ec.datosAccion.almuerzo=1 and ec.datosEstudiante.regional=:regional"),
    @NamedQuery(name = "EstudianteCongreso.findBreakPMPorRegional", query = "SELECT COUNT(ec.id) FROM EstudianteCongreso ec where ec.datosCongreso.id =:idCongreso and ec.datosAccion.breakPM=1 and ec.datosEstudiante.regional=:regional"),
})
public class EstudianteCongreso implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Estudiante_seq_id")
    @Column(name = "id")
    private Long id;
    @NotNull
    @Column(name = "uuid")
    private String uuid;
    @Column(name = "abono")
    private Integer abono;

    @ManyToOne(targetEntity = Estudiante.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "idestudiante")
    private Estudiante datosEstudiante;

    @ManyToOne(targetEntity = Congreso.class)
    @JoinColumn(name = "idcongreso")
    private Congreso datosCongreso;
    
    @ManyToOne(targetEntity = Accion.class,cascade = CascadeType.ALL)
    @JoinColumn(name = "idaccion")
    private Accion datosAccion;

    public EstudianteCongreso() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    
    

    public Integer getAbono() {
        return abono;
    }

    public void setAbono(Integer abono) {
        this.abono = abono;
    }

    public Estudiante getDatosEstudiante() {
        return datosEstudiante;
    }

    public void setDatosEstudiante(Estudiante datosEstudiante) {
        this.datosEstudiante = datosEstudiante;
    }

    public Congreso getDatosCongreso() {
        return datosCongreso;
    }

    public void setDatosCongreso(Congreso datosCongreso) {
        this.datosCongreso = datosCongreso;
    }

    public Accion getDatosAccion() {
        return datosAccion;
    }

    public void setDatosAccion(Accion datosAccion) {
        this.datosAccion = datosAccion;
    }

    @Override
    public String toString() {
        return "EstudianteCongreso{" + "id=" + id + ", uuid=" + uuid + ", abono=" + abono + ", datosEstudiante=" + datosEstudiante + ", datosCongreso=" + datosCongreso + ", datosAccion=" + datosAccion + '}';
    }

    

  

}
