/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package congreso.Dominio;

import congreso.Dominio.Conversores.ConversorHora;
import java.io.Serializable;
import java.time.LocalTime;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(schema = "cg", name = "acciones")
@SequenceGenerator(schema = "cg",sequenceName = "acciones_id_seq",name = "Accion_seq_id",allocationSize = 1)
@NamedQueries({
        @NamedQuery(name = "Accion.findAll",query = "SELECT a FROM Accion a ")
})
public class Accion implements Serializable {
    private static final long serialVersionUID=1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Personal_seq_id")
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
    @Convert(converter = ConversorHora.class)
    @Column(name = "hora_registro")
    private LocalTime horaRegistro;
    @Convert(converter = ConversorHora.class)
    @Column(name = "hora_breakam")
    private LocalTime horaBreakAM;
    @Convert(converter = ConversorHora.class)
    @Column(name = "hora_almuerzo")
    private LocalTime horaAlmuerzo;
    @Convert(converter = ConversorHora.class)
    @Column(name = "hora_breakpm")
    private LocalTime horaBreakPM;

    public Accion() {
    }

    public Accion(Integer registro, Integer breakAM, Integer almuerzo, Integer breakPM, Integer email, LocalTime horaRegistro, LocalTime horaBreakAM, LocalTime horaAlmuerzo, LocalTime horaBreakPM) {
        this.registro = registro;
        this.breakAM = breakAM;
        this.almuerzo = almuerzo;
        this.breakPM = breakPM;
        this.email = email;
        this.horaRegistro = horaRegistro;
        this.horaBreakAM = horaBreakAM;
        this.horaAlmuerzo = horaAlmuerzo;
        this.horaBreakPM = horaBreakPM;
    }

    public Accion(Long id, Integer registro, Integer breakAM, Integer almuerzo, Integer breakPM, Integer email, LocalTime horaRegistro, LocalTime horaBreakAM, LocalTime horaAlmuerzo, LocalTime horaBreakPM) {
        this.id = id;
        this.registro = registro;
        this.breakAM = breakAM;
        this.almuerzo = almuerzo;
        this.breakPM = breakPM;
        this.email = email;
        this.horaRegistro = horaRegistro;
        this.horaBreakAM = horaBreakAM;
        this.horaAlmuerzo = horaAlmuerzo;
        this.horaBreakPM = horaBreakPM;
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

    public LocalTime getHoraRegistro() {
        return horaRegistro;
    }

    public void setHoraRegistro(LocalTime horaRegistro) {
        this.horaRegistro = horaRegistro;
    }

    public LocalTime getHoraBreakAM() {
        return horaBreakAM;
    }

    public void setHoraBreakAM(LocalTime horaBreakAM) {
        this.horaBreakAM = horaBreakAM;
    }

    public LocalTime getHoraAlmuerzo() {
        return horaAlmuerzo;
    }

    public void setHoraAlmuerzo(LocalTime horaAlmuerzo) {
        this.horaAlmuerzo = horaAlmuerzo;
    }

    public LocalTime getHoraBreakPM() {
        return horaBreakPM;
    }

    public void setHoraBreakPM(LocalTime horaBreakPM) {
        this.horaBreakPM = horaBreakPM;
    }

    

    @Override
    public String toString() {
        return "Accion{" + "id=" + id + ", registro=" + registro + ", breakAM=" + breakAM + ", almuerzo=" + almuerzo + ", breakPM=" + breakPM + ", email=" + email + ", horaRegistro=" + horaRegistro + ", horaBreakAM=" + horaBreakAM + ", horaAlmuerzo=" + horaAlmuerzo + ", horaBreakPM=" + horaBreakPM + '}';
    }
    
    
}
