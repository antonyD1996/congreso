/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package congreso.Dominio;

import java.io.Serializable;
import java.util.Objects;
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
@Table(schema = "cg", name = "personal")
@SequenceGenerator(schema = "cg",sequenceName = "personal_id_seq",name = "Personal_seq_id",allocationSize = 1)
@NamedQueries({
        @NamedQuery(name = "Personal.findAll",query = "SELECT p FROM Personal p")
})
public class Personal implements Serializable {
    private static final long serialVersionUID=1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Personal_seq_id")
    @Column(name = "id")
    private Long id;
    @NotNull
    @Column(name = "nombre")
    private String nombre;
    @NotNull
    @Column(name = "email")
    private String email;
    @ManyToOne(targetEntity = Tipo.class)
    @JoinColumn(name = "idtipo")
    private Tipo datosTipo;

    public Personal() {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Tipo getDatosTipo() {
        return datosTipo;
    }

    public void setDatosTipo(Tipo datosTipo) {
        this.datosTipo = datosTipo;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
        hash = 97 * hash + Objects.hashCode(this.datosTipo);
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
        final Personal other = (Personal) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.datosTipo, other.datosTipo)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Personal{" + "id=" + id + ", nombre=" + nombre + ", datosTipo=" + datosTipo + '}';
    }
    
    
}
