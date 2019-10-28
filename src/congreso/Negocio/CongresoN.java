/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package congreso.Negocio;

import congreso.Dominio.Congreso;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author anton
 */
public class CongresoN {
    private static final Logger LOG = Logger.getLogger("congreso");
    EntityManager em = new ConexN().entity();

    public Supplier<List<Congreso>> listadoCongresos = () -> {
        LOG.log(Level.INFO, "[CongresoN][INIT]->Listado de Congresos");
        em.clear();
        Query query = em.createNamedQuery("Congreso.findAll");
        List<Congreso> listado = query.getResultList();
        return listado;
    };

    public Consumer<Congreso> guardarCongreso = e -> {
        LOG.log(Level.INFO, "[CongresoN][INIT]->Guardar Congreso");
        try {
            em.getTransaction().begin();
            em.persist(e);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    };
    
}
