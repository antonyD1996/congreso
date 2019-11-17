/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package congreso.Negocio;

import congreso.Dominio.Tipo;
import java.util.List;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author anton
 */
public class TipoN {
    private static final Logger LOG = Logger.getLogger("congreso");
    EntityManager em = new ConexN().entity();

    public Supplier<List<Tipo>> listadoTipos = () -> {
        LOG.log(Level.INFO, "[CongresoN][INIT]->Listado de Tipos");
        em.clear();
        Query query = em.createNamedQuery("Tipo.findAll");
        return query.getResultList();        
    };
    
}
