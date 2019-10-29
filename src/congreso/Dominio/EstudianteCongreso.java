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
@Table(schema = "cg", name = "estudiante_congreso")
@SequenceGenerator(schema = "cg",sequenceName = "estudiante_congreso_id_seq",name = "Estudiante_Congreso_seq_id",allocationSize = 1)
@NamedQueries({
        @NamedQuery(name = "EstudianteCongreso.findAll",query = "SELECT ec FROM EstudianteCongreso ec where ec.datosEstudiante.datosCongreso.id = :idCongreso"),
        @NamedQuery(name = "EstudianteCongreso.findByCodigo",query = "SELECT ec.id FROM EstudianteCongreso ec where ec.datosEstudiante.codigo = :codigo and ec.datosEstudiante.datosCongreso.id = :idCongreso"),
        @NamedQuery(name = "EstudianteCongreso.EmailFaltantes",query = "SELECT ec FROM EstudianteCongreso ec where ec.datosEstudiante.datosCongreso.id =:idCongreso and ec.email=0")
        
})
public class EstudianteCongreso implements Serializable {
    private static final long serialVersionUID=1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Estudiante_seq_id")
    @Column(name = "id")
    private Long id;
    @NotNull
    @Column(name = "registro")
    private Integer registro;
    @NotNull
    @Column(name = "breakam")
    private Integer breakAM;
    @NotNull
    @Column(name = "almuerzo")
    private Integer almuerzo;
    @NotNull
    @Column(name = "breakpm")
    private Integer breakPM;
    @NotNull
    @Column(name = "email")
    private Integer email;
    
    @ManyToOne(targetEntity = Estudiante.class, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "idestudiante")
    private Estudiante datosEstudiante;

    public EstudianteCongreso() {
    }

    public EstudianteCongreso(Integer registro, Integer breakAM, Integer almuerzo, Integer breakPM,Integer email, Estudiante datosEstudiante) {
        this.registro = registro;
        this.breakAM = breakAM;
        this.almuerzo = almuerzo;
        this.breakPM = breakPM;
        this.email = email;
        this.datosEstudiante = datosEstudiante;
    }

    public EstudianteCongreso(Long id, Integer registro, Integer breakAM, Integer almuerzo, Integer breakPM, Integer email, Estudiante datosEstudiante) {
        this.id = id;
        this.registro = registro;
        this.breakAM = breakAM;
        this.almuerzo = almuerzo;
        this.breakPM = breakPM;
        this.email = email;
        this.datosEstudiante = datosEstudiante;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRegistro() {
        return registro;
    }

    public void setRegistro(Integer registro) {
        this.registro = registro;
    }

    public Integer getBreakAM() {
        return breakAM;
    }

    public void setBreakAM(Integer breakAM) {
        this.breakAM = breakAM;
    }

    public Integer getAlmuerzo() {
        return almuerzo;
    }

    public void setAlmuerzo(Integer almuerzo) {
        this.almuerzo = almuerzo;
    }

    public Integer getBreakPM() {
        return breakPM;
    }

    public void setBreakPM(Integer breakPM) {
        this.breakPM = breakPM;
    }

    public Integer getEmail() {
        return email;
    }

    public void setEmail(Integer email) {
        this.email = email;
    }
    

    public Estudiante getDatosEstudiante() {
        return datosEstudiante;
    }

    public void setDatosEstudiante(Estudiante datosEstudiante) {
        this.datosEstudiante = datosEstudiante;
    }

    @Override
    public int hashCode() {
        int hash = 7;
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
        final EstudianteCongreso other = (EstudianteCongreso) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.datosEstudiante, other.datosEstudiante)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "EstudianteCongreso{" + "id=" + id + ", registro=" + registro + ", breakAM=" + breakAM + ", almuerzo=" + almuerzo + ", breakPM=" + breakPM + ", email=" + email + ", datosEstudiante=" + datosEstudiante + '}';
    }

    
    
}
