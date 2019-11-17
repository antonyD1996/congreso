/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package congreso.Infraestructura;
import congreso.Dominio.Tipo;
import congreso.Negocio.TipoN;
import java.util.List;
import java.util.function.Supplier;
/**
 *
 * @author anton
 */
public class TipoI {
    
    TipoN tn = new TipoN();
    
    public Supplier<List<Tipo>> listadoTipos= tn.listadoTipos::get;
    
}
