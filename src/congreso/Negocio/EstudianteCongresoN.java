/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package congreso.Negocio;

import congreso.Dominio.EstudianteCongreso;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
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

    public Function<Long, List<EstudianteCongreso>> listadoEstudiantes = (idCongreso) -> {
        LOG.log(Level.INFO, "[EstudianteCongresoN][INIT]->Listado de Estudiantes");
        em.clear();
        Query query = em.createNamedQuery("EstudianteCongreso.findAll").setParameter("idCongreso", idCongreso);
        List<EstudianteCongreso> listado = query.getResultList();
        return listado;
    };
    public Function<Long, Integer> faltantesEmail = (idCongreso) -> {
        LOG.log(Level.INFO, "[EstudianteCongresoN][INIT]->Cantidad de faltantes de email");
        em.clear();
        Query query = em.createNamedQuery("EstudianteCongreso.EmailFaltantes").setParameter("idCongreso", idCongreso);
        return query.getResultList().size();
    };
    public Function<Long, List<EstudianteCongreso>> listadoFaltantesEmail = (idCongreso) -> {
        LOG.log(Level.INFO, "[EstudianteCongresoN][INIT]->Listado de faltantes de email");
        em.clear();
        Query query = em.createNamedQuery("EstudianteCongreso.EmailFaltantes").setParameter("idCongreso", idCongreso);
        List<EstudianteCongreso> listado = query.getResultList();
        return listado;
    };

    public Function<String, EstudianteCongreso> obtenerEstudiantePorCodigo = (codigo) -> {
        LOG.log(Level.INFO, "[EstudianteCongresoN][INIT]->Obtener estudiante por codigo");
        Query query = em.createNamedQuery("EstudianteCongreso.findByCodigo").setParameter("codigo", codigo);
        Long idCongreso = (Long) query.getSingleResult();
        EstudianteCongreso estudiante = new EstudianteCongreso();
        estudiante.setId(idCongreso);
        if (estudiante != null) {
            EstudianteCongreso ec = em.find(EstudianteCongreso.class, estudiante.getId());
            return ec;
        } else {
            return null;
        }
    };

    public Consumer<EstudianteCongreso> guardarEstudiante = e -> {
        LOG.log(Level.INFO, "[EstudianteCongresoN][INIT]->Guardar Estudiante");
        try {
            em.getTransaction().begin();
            em.persist(e);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    };
    public BiConsumer<EstudianteCongreso, Integer> cambiarEstado = (e, v) -> {
        LOG.log(Level.INFO, "[EstudianteCongresoN][INIT]->Cambiar Estado");
        try {
            em.getTransaction().begin();
            EstudianteCongreso ec = em.find(EstudianteCongreso.class, e.getId());
            switch (v) {
                case 1:
                    ec.setEmail(1);
                    break;
                case 2:
                    ec.setRegistro(1);
                    break;
                case 3:
                    ec.setBreakAM(1);
                    break;
                case 4:
                    ec.setAlmuerzo(1);
                    break;
                case 5:
                    ec.setBreakPM(1);
                    break;
            }
            em.merge(ec);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    };
    public Function<List<EstudianteCongreso>, List<EstudianteCongreso>> guardarVarios = (lista) -> {
        LOG.log(Level.INFO, "[EstudianteCongresoN][INIT]->Guardar Varios");
        try {
            List<EstudianteCongreso> listado = new ArrayList<>();

            lista.stream().forEach(l -> {
                EstudianteCongreso estudiante = obtenerEstudiantePorCodigo.apply(l.getDatosEstudiante().getCodigo());
                if (guardarEstudiante == null) {
                    em.getTransaction().begin();
                    em.persist(l);
                    em.getTransaction().commit();

                } else {

                    listado.add(l);
                }
            });

            if (listado.isEmpty()) {
                return null;
            } else {
                return listado;
            }
        } finally {
            em.close();
        }
    };

}
