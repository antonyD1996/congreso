/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package congreso.Dominio;

/**
 *
 * @author anton
 */
public class Estadistica {
    private String nombre;
    private Integer esperados;
    private Integer registrados;
    private Integer breakAM;
    private Integer almuerzos;
    private Integer breakPM;

    public Estadistica() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getEsperados() {
        return esperados;
    }

    public void setEsperados(Integer esperados) {
        this.esperados = esperados;
    }

    public Integer getRegistrados() {
        return registrados;
    }

    public void setRegistrados(Integer registrados) {
        this.registrados = registrados;
    }

    public Integer getBreakAM() {
        return breakAM;
    }

    public void setBreakAM(Integer breakAM) {
        this.breakAM = breakAM;
    }

    public Integer getAlmuerzos() {
        return almuerzos;
    }

    public void setAlmuerzos(Integer almuerzos) {
        this.almuerzos = almuerzos;
    }

    public Integer getBreakPM() {
        return breakPM;
    }

    public void setBreakPM(Integer breakPM) {
        this.breakPM = breakPM;
    }
    
    
    
}
