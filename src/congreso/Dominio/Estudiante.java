/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package congreso.Dominio;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *
 * @author anton
 */
@Entity
@Table(schema = "cg", name = "estudiantes")
@SequenceGenerator(schema = "cg",sequenceName = "estudiantes_id_seq",name = "Estudiante_seq_id",allocationSize = 1)
@NamedQueries({
        @NamedQuery(name = "Estudiante.findByCodigo",query = "SELECT e FROM Estudiante e where e.codigo = :codigo")
        
})
public class Estudiante implements Serializable {
     private static final long serialVersionUID=1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Estudiante_seq_id")
    @Column(name = "id")
    private Long id;
    @NotNull
    @Column(name = "nombre")
    private String nombre;
    @NotNull
    @Column(name = "codigo")
    private String codigo;
    @NotNull
    @Column(name = "carrera")
    private String carrera;
    @NotNull
    @Column(name = "regional")
    private String regional;
    
    

    public Estudiante() {
    }

    public Estudiante(String nombre, String codigo, String carrera, String regional) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.carrera = carrera;
        this.regional = regional;
    }

    public Estudiante(Long id, String nombre, String codigo, String carrera, String regional) {
        this.id = id;
        this.nombre = nombre;
        this.codigo = codigo;
        this.carrera = carrera;
        this.regional = regional;
    }
    

    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public String getRegional() {
        return regional;
    }

    public void setRegional(String regional) {
        this.regional = regional;
    }
    

    @Override
    public int hashCode() {
        int hash = 3;
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
        final Estudiante other = (Estudiante) obj;
        if (!Objects.equals(this.codigo, other.codigo)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Estudiante{" + "id=" + id + ", nombre=" + nombre + ", codigo=" + codigo + ", carrera=" + carrera + ", regional=" + regional  + '}';
    }

    
    
    
}
