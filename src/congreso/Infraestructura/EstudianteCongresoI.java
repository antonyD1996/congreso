/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package congreso.Infraestructura;

import congreso.Dominio.EstudianteCongreso;
import congreso.Negocio.EstudianteCongresoN;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
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

    List<EstudianteCongreso> listadoModel;

    EstudianteCongresoN en = new EstudianteCongresoN();

    public Function<Long, List<EstudianteCongreso>> listadoEstudiantes = (idCongreso) -> {
        return en.listadoEstudiantes.apply(idCongreso);
    };
    public Function<Long, Integer> faltantesEmail = (idCongreso) -> {
        return en.faltantesEmail.apply(idCongreso);
    };

    public Function<Long, List<EstudianteCongreso>> listadoFaltantesEmail = (idCongreso) -> {
        return en.listadoFaltantesEmail.apply(idCongreso);
    };
    
    public Function<String, EstudianteCongreso> obtenerEstudiantePorCodigo = (codigo) -> {
        return en.obtenerEstudiantePorCodigo.apply(codigo);
    };

    public Consumer<EstudianteCongreso> guardarEstudiante = (e) -> {
        new EstudianteCongresoN().guardarEstudiante.accept(e);
    };
    public BiConsumer<EstudianteCongreso, Integer> cambiarEstado = (e, valor) -> {
        new EstudianteCongresoN().cambiarEstado.accept(e, valor);
    };
    public Consumer<List<EstudianteCongreso>> guardarVarios = (le) -> {
        new EstudianteCongresoN().guardarVarios.accept(le);
    };

    public BiConsumer<JTable, List<EstudianteCongreso>> cargarTabla = (tabla, listado) -> {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("No.");
        model.addColumn("Codigo");
        model.addColumn("Nombre");
        model.addColumn("Correo");
        model.addColumn("Carrera");
        model.addColumn("Regional");
        model.addColumn("Email");
        model.addColumn("Registro");
        model.addColumn("Break AM");
        model.addColumn("Almuerzo");
        model.addColumn("Break PM");

        listado.stream().forEach(p -> {
            model.addRow(new Object[]{
                p.getId(),
                model.getRowCount() + 1,
                p.getDatosEstudiante().getCodigo(),
                p.getDatosEstudiante().getNombre(),
                (p.getDatosEstudiante().getCodigo() + "@unab.edu.sv"),
                p.getDatosEstudiante().getCarrera(),
                p.getDatosEstudiante().getRegional(),
                evaluar(p.getEmail()),
                evaluar(p.getRegistro()),
                evaluar(p.getBreakAM()),
                evaluar(p.getAlmuerzo()),
                evaluar(p.getBreakPM())
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
    public BiFunction<Long, JTable, List<EstudianteCongreso>> actualizarDatos = (idCongreso, tabla) -> {
        listadoModel = en.listadoEstudiantes.apply(idCongreso);
        cargarTabla.accept(tabla, listadoModel);
        TableColumn columna;
        columna = tabla.getColumnModel().getColumn(0);
        columna.setMaxWidth(0);
        columna.setMinWidth(0);
        columna.setPreferredWidth(0);
        tabla.doLayout();
        return listadoModel;
    };

}
