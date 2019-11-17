/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package congreso.Negocio;

import congreso.Dominio.EstudianteCongreso;
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
public class EstudianteCongresoN {

    private static final Logger LOG = Logger.getLogger("congreso");
    EntityManager em = new ConexN().entity();

    public Function<Map<String, Object>, Map<String, Object>> obtenerEstadisticas = (map) -> {
        LOG.log(Level.INFO, "[EstudianteCongresoN][INIT]->Obtener Estadisticas");
        em.clear();
        Query total, registrados, breakam, almuerzo, breakpm;
        if ((int) map.get("tipoEstadistica") == 1) {
            total = em.createNamedQuery("EstudianteCongreso.findTotal").setParameter("idCongreso", map.get("idcongreso"));
            registrados = em.createNamedQuery("EstudianteCongreso.findRegistrados").setParameter("idCongreso", map.get("idcongreso"));
            breakam = em.createNamedQuery("EstudianteCongreso.findBreakAM").setParameter("idCongreso", map.get("idcongreso"));
            almuerzo = em.createNamedQuery("EstudianteCongreso.findAlmuerzo").setParameter("idCongreso", map.get("idcongreso"));
            breakpm = em.createNamedQuery("EstudianteCongreso.findBreakPM").setParameter("idCongreso", map.get("idcongreso"));
        } else {
            total = em.createNamedQuery("EstudianteCongreso.findTotalPorRegional").setParameter("idCongreso", map.get("idcongreso")).setParameter("regional", map.get("regional"));
            registrados = em.createNamedQuery("EstudianteCongreso.findRegistradosPorRegional").setParameter("idCongreso", map.get("idcongreso")).setParameter("regional", map.get("regional"));
            breakam = em.createNamedQuery("EstudianteCongreso.findBreakAMPorRegional").setParameter("idCongreso", map.get("idcongreso")).setParameter("regional", map.get("regional"));
            almuerzo = em.createNamedQuery("EstudianteCongreso.findAlmuerzoPorRegional").setParameter("idCongreso", map.get("idcongreso")).setParameter("regional", map.get("regional"));
            breakpm = em.createNamedQuery("EstudianteCongreso.findBreakPMPorRegional").setParameter("idCongreso", map.get("idcongreso")).setParameter("regional", map.get("regional"));

        }

        Map<String, Object> map1 = new HashMap<>();
        map1.put("total", total.getSingleResult());
        map1.put("registrados", registrados.getSingleResult());
        map1.put("breakam", breakam.getSingleResult());
        map1.put("almuerzo", almuerzo.getSingleResult());
        map1.put("breakpm", breakpm.getSingleResult());

        return map1;
    };
    public Function<Map<String, Object>, List<EstudianteCongreso>> listadoEstudiantesPaginado = (map) -> {
        LOG.log(Level.INFO, "[EstudianteCongresoN][INIT]->Listado de Estudiantes");
        em.clear();
        Query query = em.createNamedQuery("EstudianteCongreso.findAll").setParameter("idCongreso", map.get("idCongreso"));
        int pagina = (int) map.get("pagina");
        query.setFirstResult((pagina - 1) * 20);
        query.setMaxResults(20);
        List<EstudianteCongreso> listado = query.getResultList();
        return listado;
    };

    public Function<Map<String, Object>, List<EstudianteCongreso>> listadoEstudiantesPorRegionalPaginado = (map) -> {
        LOG.log(Level.INFO, "[EstudianteCongresoN][INIT]->Listado de Estudiantes");
        em.clear();
        Query query = em.createNamedQuery("EstudianteCongreso.findPorRegional").setParameter("idCongreso", map.get("idCongreso")).setParameter("regional", map.get("regional"));
        int pagina = (int) map.get("pagina");
        query.setFirstResult((pagina - 1) * 20);
        query.setMaxResults(20);
        List<EstudianteCongreso> listado = query.getResultList();
        return listado;
    };

    public Function<Long, List<EstudianteCongreso>> listadoEstudiantesTodos = (idCongreso) -> {
        LOG.log(Level.INFO, "[EstudianteCongresoN][INIT]->Listado de Todos Estudiantes");
        em.clear();
        Query query = em.createNamedQuery("EstudianteCongreso.findAll").setParameter("idCongreso", idCongreso);
        List<EstudianteCongreso> listado = query.getResultList();
        return listado;
    };

    public Function<Map<String, Object>, List<EstudianteCongreso>> listadoEstudiantesPorRegional = (map) -> {
        LOG.log(Level.INFO, "[EstudianteCongresoN][INIT]->Listado de Todos Estudiantes po Regional");
        em.clear();
        Query query = em.createNamedQuery("EstudianteCongreso.findPorRegional").setParameter("idCongreso", map.get("idCongreso")).setParameter("regional", map.get("regional"));
        List<EstudianteCongreso> listado = query.getResultList();
        return listado;
    };
    public Function<Long, List<EstudianteCongreso>> estudiantePendientes = (idCongreso) -> {
        LOG.log(Level.INFO, "[EstudianteCongresoN][INIT]->Listado de EstudiantePendiente");
        em.clear();
        Query query = em.createNamedQuery("EstudianteCongreso.findPendientes").setParameter("idCongreso", idCongreso);
        List<EstudianteCongreso> listado = query.getResultList();
        return listado;
    };
    public Function<Long, Integer> faltantesEmail = (idCongreso) -> {
        LOG.log(Level.INFO, "[EstudianteCongresoN][INIT]->Cantidad de faltantes de email");
        em.clear();
        Query query = em.createNamedQuery("EstudianteCongreso.findEmailFaltantesTotal").setParameter("idCongreso", idCongreso);
        return Integer.valueOf(query.getSingleResult().toString());
    };
    public Function<Long, List<EstudianteCongreso>> listadoFaltantesEmail = (idCongreso) -> {
        LOG.log(Level.INFO, "[EstudianteCongresoN][INIT]->Listado de faltantes de email");
        em.clear();
        Query query = em.createNamedQuery("EstudianteCongreso.findEmailFaltantes").setParameter("idCongreso", idCongreso);
        List<EstudianteCongreso> listado = query.getResultList();
        return listado;
    };

    public Function<Map<String, Object>, EstudianteCongreso> obtenerEstudiantePorUUID = (map) -> {
        LOG.log(Level.INFO, "[EstudianteCongresoN][INIT]->Obtener estudiante por UUID");
        em.clear();
        try {
            Query query = em.createNamedQuery("EstudianteCongreso.findByUUID").setParameter("uuid", map.get("uuid")).setParameter("idCongreso", map.get("idCongreso"));
            EstudianteCongreso ec = (EstudianteCongreso) query.getSingleResult();
            return ec;
        } catch (Exception e) {
            return null;
        }

    };

    public Function<Map<String, Object>, EstudianteCongreso> obtenerEstudianteCongresoPorCodigo = (map) -> {
        LOG.log(Level.INFO, "[EstudianteCongresoN][INIT]->Obtener estudiante por codigo");
        em.clear();
        try {
            Query query = em.createNamedQuery("EstudianteCongreso.findByCodigo").setParameter("codigo", map.get("codigo")).setParameter("idCongreso", map.get("idCongreso"));
            EstudianteCongreso estudiante = (EstudianteCongreso) query.getSingleResult();

            return estudiante;
        } catch (Exception e) {
            return null;
        }
    };

    public Function<Long, EstudianteCongreso> obtenerEstudianteCongresoPorID = (ID) -> {
        LOG.log(Level.INFO, "[EstudianteCongresoN][INIT]->Obtener estudiante por ID");
        em.clear();
        try {

            EstudianteCongreso estudiante = new EstudianteCongreso();
            estudiante.setId(ID);

            EstudianteCongreso ec = em.find(EstudianteCongreso.class, estudiante.getId());

            return ec;
        } catch (Exception e) {
            return null;
        }
    };

    public Consumer<EstudianteCongreso> guardarEstudiante = e -> {
        LOG.log(Level.INFO, "[EstudianteCongresoN][INIT]->Guardar Estudiante");
        try {
            em.getTransaction().begin();

            em.persist(e);

            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    };
    public Consumer<EstudianteCongreso> abonar = e -> {
        LOG.log(Level.INFO, "[EstudianteCongresoN][INIT]->Abonar");
        try {
            em.getTransaction().begin();
            EstudianteCongreso ec = em.find(EstudianteCongreso.class, e.getId());
            if ((ec.getAbono() + e.getAbono()) <= 65) {
                ec.setAbono(ec.getAbono() + e.getAbono());
                em.merge(ec);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    };
    public Consumer<EstudianteCongreso> eliminarEstudiante = e -> {
        LOG.log(Level.INFO, "[EstudianteCongresoN][INIT]->Eliminar Estudiante");
        try {
            em.getTransaction().begin();
            EstudianteCongreso ec = em.find(EstudianteCongreso.class, e.getId());
            em.remove(ec);
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    };
    public Consumer<EstudianteCongreso> cambiarEstado = (e) -> {
        LOG.log(Level.INFO, "[EstudianteCongresoN][INIT]->Cambiar Estado");
        try {
            em.getTransaction().begin();
            EstudianteCongreso ec = em.find(EstudianteCongreso.class, e.getId());
            ec.setDatosAccion(e.getDatosAccion());
            em.merge(ec);
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    };

}
