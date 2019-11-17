/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package congreso.Dominio;

import java.io.Serializable;
import java.util.Objects;
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
@Table(schema = "cg", name = "personal_congreso")
@SequenceGenerator(schema = "cg", sequenceName = "personal_congreso_id_seq", name = "Personal_Congreso_seq_id", allocationSize = 1)
@NamedQueries({
    @NamedQuery(name = "PersonalCongreso.findAll", query = "SELECT ec FROM PersonalCongreso ec where ec.datosCongreso.id = :idCongreso order by ec.datosAccion.registro DESC"),
    @NamedQuery(name = "PersonalCongreso.findPorTipo", query = "SELECT ec FROM PersonalCongreso ec where ec.datosCongreso.id = :idCongreso and ec.datosPersonal.datosTipo.id = :idTipo order by ec.datosAccion.registro DESC"),
    @NamedQuery(name = "PersonalCongreso.findByUUID", query = "SELECT ec FROM PersonalCongreso ec where ec.uuid = :uuid and ec.datosCongreso.id = :idCongreso"),
    @NamedQuery(name = "PersonalCongreso.findEmailFaltantes", query = "SELECT ec FROM PersonalCongreso ec where ec.datosCongreso.id =:idCongreso and ec.datosAccion.email=0"),
    @NamedQuery(name = "PersonalCongreso.findEmailFaltantesTotal", query = "SELECT COUNT(ec.id) FROM PersonalCongreso ec where ec.datosCongreso.id =:idCongreso and ec.datosAccion.email=0"),
    @NamedQuery(name = "PersonalCongreso.findTotal", query = "SELECT COUNT(ec.id) FROM PersonalCongreso ec where ec.datosCongreso.id =:idCongreso"),
    @NamedQuery(name = "PersonalCongreso.findRegistrados", query = "SELECT COUNT(ec.id) FROM PersonalCongreso ec where ec.datosCongreso.id =:idCongreso and ec.datosAccion.registro=1"),
    @NamedQuery(name = "PersonalCongreso.findBreakAM", query = "SELECT COUNT(ec.id) FROM PersonalCongreso ec where ec.datosCongreso.id =:idCongreso and ec.datosAccion.breakAM=1"),
    @NamedQuery(name = "PersonalCongreso.findAlmuerzo", query = "SELECT COUNT(ec.id) FROM PersonalCongreso ec where ec.datosCongreso.id =:idCongreso and ec.datosAccion.almuerzo=1"),
    @NamedQuery(name = "PersonalCongreso.findBreakPM", query = "SELECT COUNT(ec.id) FROM PersonalCongreso ec where ec.datosCongreso.id =:idCongreso and ec.datosAccion.breakPM=1"),
    @NamedQuery(name = "PersonalCongreso.findTotalPorTipo", query = "SELECT COUNT(ec.id) FROM PersonalCongreso ec where ec.datosCongreso.id =:idCongreso and ec.datosPersonal.datosTipo.id=:idTipo"),
    @NamedQuery(name = "PersonalCongreso.findRegistradosPorTipo", query = "SELECT COUNT(ec.id) FROM PersonalCongreso ec where ec.datosCongreso.id =:idCongreso and ec.datosAccion.registro=1 and ec.datosPersonal.datosTipo.id=:idTipo"),
    @NamedQuery(name = "PersonalCongreso.findBreakAMPorTipo", query = "SELECT COUNT(ec.id) FROM PersonalCongreso ec where ec.datosCongreso.id =:idCongreso and ec.datosAccion.breakAM=1 and ec.datosPersonal.datosTipo.id=:idTipo"),
    @NamedQuery(name = "PersonalCongreso.findAlmuerzoPorTipo", query = "SELECT COUNT(ec.id) FROM PersonalCongreso ec where ec.datosCongreso.id =:idCongreso and ec.datosAccion.almuerzo=1 and ec.datosPersonal.datosTipo.id=:idTipo"),
    @NamedQuery(name = "PersonalCongreso.findBreakPMPorTipo", query = "SELECT COUNT(ec.id) FROM PersonalCongreso ec where ec.datosCongreso.id =:idCongreso and ec.datosAccion.breakPM=1 and ec.datosPersonal.datosTipo.id=:idTipo"),
})
public class PersonalCongreso implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Estudiante_seq_id")
    @Column(name = "id")
    private Long id;
    @NotNull
    @Column(name = "uuid")
    private String uuid;

    @ManyToOne(targetEntity = Personal.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "idpersonal")
    private Personal datosPersonal;

    @ManyToOne(targetEntity = Congreso.class)
    @JoinColumn(name = "idcongreso")
    private Congreso datosCongreso;
    
    @ManyToOne(targetEntity = Accion.class,cascade = CascadeType.ALL)
    @JoinColumn(name = "idaccion")
    private Accion datosAccion;

    public PersonalCongreso() {
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

    public Personal getDatosPersonal() {
        return datosPersonal;
    }

    public void setDatosPersonal(Personal datosPersonal) {
        this.datosPersonal = datosPersonal;
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
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PersonalCongreso other = (PersonalCongreso) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PersonalCongreso{" + "id=" + id + ", datosPersonal=" + datosPersonal + ", datosCongreso=" + datosCongreso + ", datosAccion=" + datosAccion + '}';
    }
    
    
}
