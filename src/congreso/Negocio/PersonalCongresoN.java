/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package congreso.Negocio;

import congreso.Dominio.PersonalCongreso;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author anton
 */
public class PersonalCongresoN {

    private static final Logger LOG = Logger.getLogger("congreso");
    EntityManager em = new ConexN().entity();

    public Function<Map<String, Object>, List<PersonalCongreso>> listadoPersonal = (map) -> {
        LOG.log(Level.INFO, "[PersonalCongresoN][INIT]->Listado de Personal");
        em.clear();
        Query query = em.createNamedQuery("PersonalCongreso.findAll").setParameter("idCongreso", map.get("idCongreso"));
        return query.getResultList();
    };
    
    public Function<Map<String, Object>, List<PersonalCongreso>> listadoPersonalporTipo = (map) -> {
        LOG.log(Level.INFO, "[PersonalCongresoN][INIT]->Listado de Personal por Tipo");
        em.clear();
        Query query = em.createNamedQuery("PersonalCongreso.findPorTipo").setParameter("idCongreso", map.get("idCongreso")).setParameter("idTipo", (long)map.get("idTipo"));
        return query.getResultList();
    };

    public Function<Map<String, Object>, Object> faltantesEmail = (map) -> {
        LOG.log(Level.INFO, "[PersonalCongresoN][INIT]->Listado de faltantes de email");
        em.clear();
        if ((int) map.get("tipoEmail") == 1) {
            Query query = em.createNamedQuery("PersonalCongreso.findEmailFaltantesTotal").setParameter("idCongreso", map.get("idCongreso"));
            return query.getSingleResult();
        } else {
            Query query = em.createNamedQuery("PersonalCongreso.findEmailFaltantes").setParameter("idCongreso", map.get("idCongreso"));
            return query.getResultList();
        }
    };
    public Function<Map<String, Object>, PersonalCongreso> obtenerPersonalPorUUID = (map) -> {
        LOG.log(Level.INFO, "[PersonalCongresoN][INIT]->Obtener Personal por UUID");
        em.clear();
        try {
            Query query = em.createNamedQuery("PersonalCongreso.findByUUID").setParameter("uuid", map.get("uuid")).setParameter("idCongreso", map.get("idCongreso"));
            return  (PersonalCongreso) query.getSingleResult();
            
        } catch (Exception e) {
            return null;
        }

    };
    public Consumer<PersonalCongreso> cambiarEstado = (pc) -> {
        LOG.log(Level.INFO, "[PersonalCongresoN][INIT]->Cambiar Estado");
        try {
            em.getTransaction().begin();
            PersonalCongreso ec = em.find(PersonalCongreso.class, pc.getId());
            ec.setDatosAccion(pc.getDatosAccion());
            em.merge(ec);
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    };
    
    public Consumer<PersonalCongreso> guardarPersona = p -> {
        LOG.log(Level.INFO, "[PersonalCongresoN][INIT]->Guardar Persona");
        try {
            em.getTransaction().begin();

            em.persist(p);

            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    };
    public Consumer<PersonalCongreso> eliminarPersona = e -> {
        LOG.log(Level.INFO, "[PersonalCongresoN][INIT]->Eliminar Persona");
        try {
            em.getTransaction().begin();
            PersonalCongreso ec = em.find(PersonalCongreso.class, e.getId());
            em.remove(ec);
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    };
    public Function<Map<String, Object>, Map<String, Object>> obtenerEstadisticas = (map) -> {
        LOG.log(Level.INFO, "[PersonalCongresoN][INIT]->Obtener Estadisticas");
        em.clear();
        Query total, registrados, breakam, almuerzo, breakpm;
        if ((int) map.get("tipoEstadistica") == 1) {
            total = em.createNamedQuery("PersonalCongreso.findTotal").setParameter("idCongreso", map.get("idcongreso"));
            registrados = em.createNamedQuery("PersonalCongreso.findRegistrados").setParameter("idCongreso", map.get("idcongreso"));
            breakam = em.createNamedQuery("PersonalCongreso.findBreakAM").setParameter("idCongreso", map.get("idcongreso"));
            almuerzo = em.createNamedQuery("PersonalCongreso.findAlmuerzo").setParameter("idCongreso", map.get("idcongreso"));
            breakpm = em.createNamedQuery("PersonalCongreso.findBreakPM").setParameter("idCongreso", map.get("idcongreso"));
        } else {
            total = em.createNamedQuery("PersonalCongreso.findTotalPorTipo").setParameter("idCongreso", map.get("idcongreso")).setParameter("idTipo", map.get("idTipo"));
            registrados = em.createNamedQuery("PersonalCongreso.findRegistradosPorTipo").setParameter("idCongreso", map.get("idcongreso")).setParameter("idTipo", map.get("idTipo"));
            breakam = em.createNamedQuery("PersonalCongreso.findBreakAMPorTipo").setParameter("idCongreso", map.get("idcongreso")).setParameter("idTipo", map.get("idTipo"));
            almuerzo = em.createNamedQuery("PersonalCongreso.findAlmuerzoPorTipo").setParameter("idCongreso", map.get("idcongreso")).setParameter("idTipo", map.get("idTipo"));
            breakpm = em.createNamedQuery("PersonalCongreso.findBreakPMPorTipo").setParameter("idCongreso", map.get("idcongreso")).setParameter("idTipo", map.get("idTipo"));

        }

        Map<String, Object> map1 = new HashMap<>();
        map1.put("total", total.getSingleResult());
        map1.put("registrados", registrados.getSingleResult());
        map1.put("breakam", breakam.getSingleResult());
        map1.put("almuerzo", almuerzo.getSingleResult());
        map1.put("breakpm", breakpm.getSingleResult());

        return map1;
    };
}
