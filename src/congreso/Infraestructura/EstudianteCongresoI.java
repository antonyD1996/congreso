/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package congreso.Infraestructura;

import congreso.Dominio.EstudianteCongreso;
import congreso.Negocio.EstudianteCongresoN;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author anton
 */
public class EstudianteCongresoI {

    EstudianteCongresoN en = new EstudianteCongresoN();

    public Function<Map<String, Object>, Map<String, Object>> obtenerEstadisticas = (map) -> {
        return en.obtenerEstadisticas.apply(map);
    };
    public Function<Map<String, Object>, List<EstudianteCongreso>> listadoEstudiantesPaginado = (map) -> {
        return en.listadoEstudiantesPaginado.apply(map);
    };
    public Function<Map<String, Object>, List<EstudianteCongreso>> listadoEstudiantesPorRegionalPaginado = (map) -> {
        return en.listadoEstudiantesPorRegionalPaginado.apply(map);
    };
    public Function<Map<String, Object>, List<EstudianteCongreso>> listadoEstudiantesPorRegional = (map) -> {
        return en.listadoEstudiantesPorRegional.apply(map);
    };
    public Function<Long, Integer> faltantesEmail = (idCongreso) -> {
        return en.faltantesEmail.apply(idCongreso);
    };
    public Function<Long, List<EstudianteCongreso>> listadoEstudiantes = (idCongreso) -> {
        return en.listadoEstudiantesTodos.apply(idCongreso);
    };

    public Function<Long, List<EstudianteCongreso>> estudiantesPendietes = (idCongreso) -> {
        return en.estudiantePendientes.apply(idCongreso);
    };

    public Function<Long, List<EstudianteCongreso>> listadoFaltantesEmail = (idCongreso) -> {
        return en.listadoFaltantesEmail.apply(idCongreso);
    };

    public Function<Map<String, Object>, EstudianteCongreso> obtenerEstudiantePorUUID = (map) -> {
        return en.obtenerEstudiantePorUUID.apply(map);
    };

    public Function<Map<String, Object>, EstudianteCongreso> obtenerEstudianteporCodigo = (map) -> {
        return en.obtenerEstudianteCongresoPorCodigo.apply(map);
    };

    public Function<Long, EstudianteCongreso> obtenerEstudianteporID = (ID) -> {
        return en.obtenerEstudianteCongresoPorID.apply(ID);
    };

    public Consumer<EstudianteCongreso> abonar = (e) -> {
        new EstudianteCongresoN().abonar.accept(e);
    };

    public Consumer<EstudianteCongreso> guardarEstudiante = (e) -> {
        new EstudianteCongresoN().guardarEstudiante.accept(e);
    };
    public Consumer<EstudianteCongreso> eliminarEstudiante = (e) -> {
        new EstudianteCongresoN().eliminarEstudiante.accept(e);
    };
    public Consumer<EstudianteCongreso> cambiarEstado = (e) -> {
        new EstudianteCongresoN().cambiarEstado.accept(e);
    };

    public BiConsumer<JTable, List<EstudianteCongreso>> cargarTabla = (tabla, listado) -> {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("No.");
        model.addColumn("Codigo");
        model.addColumn("Nombre");
        model.addColumn("Carrera");
        model.addColumn("Regional");
        model.addColumn("Email");
        model.addColumn("Registro");
        model.addColumn("Break AM");
        model.addColumn("Almuerzo");
        model.addColumn("Break PM");
        model.addColumn("Abono");

        listado.stream().forEach(p -> {
            model.addRow(new Object[]{
                p.getId(),
                model.getRowCount() + 1,
                p.getDatosEstudiante().getCodigo(),
                p.getDatosEstudiante().getNombre(),
                p.getDatosEstudiante().getCarrera(),
                p.getDatosEstudiante().getRegional(),
                evaluar(p.getDatosAccion().getEmail()),
                evaluar(p.getDatosAccion().getRegistro()),
                evaluar(p.getDatosAccion().getBreakAM()),
                evaluar(p.getDatosAccion().getAlmuerzo()),
                evaluar(p.getDatosAccion().getBreakPM()),
                p.getAbono()
            });
        });
        tabla.setModel(model);
    };

    public String evaluar(Integer valor) {
        String resultado;
        switch (valor) {
            case 1:
                resultado = "SI";
                break;
            default:
                resultado = "NO";
        }
        return resultado;
    }

   
    public Consumer<Map<String, Object>> actualizarDatos = (map) -> {
        JTable tabla = (JTable) map.get("tabla");
        cargarTabla.accept(tabla, (List<EstudianteCongreso>) map.get("listado"));
        TableColumn columna, numero, regional, email, breakam, almuerzo, breakpm, registro, codigo, abono, carrera;
        int valor = 60;
        columna = tabla.getColumnModel().getColumn(0);
        columna.setMaxWidth(0);
        columna.setMinWidth(0);
        columna.setPreferredWidth(0);

        numero = tabla.getColumnModel().getColumn(1);
        numero.setMaxWidth(60);
        numero.setMinWidth(60);
        numero.setPreferredWidth(60);
        
        carrera = tabla.getColumnModel().getColumn(4);
        carrera.setMaxWidth(valor*3);
        carrera.setMinWidth(valor*3);
        carrera.setPreferredWidth(valor*3);

        regional = tabla.getColumnModel().getColumn(5);
        regional.setMaxWidth(valor);
        regional.setMinWidth(valor);
        regional.setPreferredWidth(valor);

        email = tabla.getColumnModel().getColumn(6);
        email.setMaxWidth(valor);
        email.setMinWidth(valor);
        email.setPreferredWidth(valor);

        registro = tabla.getColumnModel().getColumn(7);
        registro.setMaxWidth(valor);
        registro.setMinWidth(valor);
        registro.setPreferredWidth(valor);

        breakam = tabla.getColumnModel().getColumn(8);
        breakam.setMaxWidth(valor + 10);
        breakam.setMinWidth(valor + 10);
        breakam.setPreferredWidth(valor + 10);

        almuerzo = tabla.getColumnModel().getColumn(9);
        almuerzo.setMaxWidth(valor + 10);
        almuerzo.setMinWidth(valor + 10);
        almuerzo.setPreferredWidth(valor + 10);

        breakpm = tabla.getColumnModel().getColumn(10);
        breakpm.setMaxWidth(valor + 10);
        breakpm.setMinWidth(valor + 10);
        breakpm.setPreferredWidth(valor + 10);

        abono = tabla.getColumnModel().getColumn(11);
        abono.setMaxWidth(valor + 10);
        abono.setMinWidth(valor + 10);
        abono.setPreferredWidth(valor + 10);

        codigo = tabla.getColumnModel().getColumn(2);
        codigo.setMaxWidth(130);
        codigo.setMinWidth(130);
        codigo.setPreferredWidth(130);

        tabla.doLayout();
    };
    public BiConsumer<JTable, List<EstudianteCongreso>> mostrarCoincidencias = (tabla, lista) -> {
        cargarTabla.accept(tabla, lista);
        TableColumn columna;
        columna = tabla.getColumnModel().getColumn(0);
        columna.setMaxWidth(0);
        columna.setMinWidth(0);
        columna.setPreferredWidth(0);
        tabla.doLayout();
    };

}
