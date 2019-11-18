/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package congreso.Infraestructura;

import congreso.Dominio.PersonalCongreso;
import congreso.Negocio.PersonalCongresoN;
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
public class PersonalCongresoI {
    PersonalCongresoN pcn = new PersonalCongresoN();
    
    public Function<Map<String, Object>, List<PersonalCongreso>> listadoPersonal = (map) -> {
        return pcn.listadoPersonal.apply(map);
    };
    
    public Function<Map<String, Object>, List<PersonalCongreso>> listadoPersonalPorTipo = (map) -> {
        return pcn.listadoPersonalporTipo.apply(map);
    };
    
    public Function<Map<String, Object>, Map<String, Object>> obtenerEstadisticas = (map) -> {
        return pcn.obtenerEstadisticas.apply(map);
    };
    public Function<Map<String, Object>, PersonalCongreso> obtenerPersonalPorUUID = (map) -> {
        return pcn.obtenerPersonalPorUUID.apply(map);
    };
    
    public Function<Map<String, Object>, Object> faltantesEmail = (map) -> {
        return pcn.faltantesEmail.apply(map);
    };
    public Consumer<PersonalCongreso> cambiarEstado = (p) -> {
        new PersonalCongresoN().cambiarEstado.accept(p);
    };
     public Consumer<PersonalCongreso> guardarPersona = (p) -> {
        new PersonalCongresoN().guardarPersona.accept(p);
    };
     public Consumer<PersonalCongreso> eliminarPersona = (e) -> {
        new PersonalCongresoN().eliminarPersona.accept(e);
    };
     
     public BiConsumer<JTable, List<PersonalCongreso>> cargarTabla = (tabla, listado) -> {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("No.");
        model.addColumn("Nombre");
        model.addColumn("Correo");
        model.addColumn("Tipo");
        model.addColumn("Registro");
        model.addColumn("Break AM");
        model.addColumn("Almuerzo");
        model.addColumn("Break PM");

        listado.stream().forEach(p -> {
            model.addRow(new Object[]{
                p.getId(),
                model.getRowCount() + 1,
                p.getDatosPersonal().getNombre(),
                p.getDatosPersonal().getEmail(),
                p.getDatosPersonal().getDatosTipo().getNombre(),
                evaluar(p.getDatosAccion().getEmail()),
                evaluar(p.getDatosAccion().getRegistro()),
                evaluar(p.getDatosAccion().getBreakAM()),
                evaluar(p.getDatosAccion().getAlmuerzo()),
                evaluar(p.getDatosAccion().getBreakPM()),
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
        cargarTabla.accept(tabla, (List<PersonalCongreso>) map.get("listado"));
        TableColumn ID, numero, tipo, breakam, almuerzo, breakpm, registro;
        int valor = 60;
        ID = tabla.getColumnModel().getColumn(0);
        ID.setMaxWidth(0);
        ID.setMinWidth(0);
        ID.setPreferredWidth(0);

        numero = tabla.getColumnModel().getColumn(1);
        numero.setMaxWidth(60);
        numero.setMinWidth(60);
        numero.setPreferredWidth(60);
        
        tipo = tabla.getColumnModel().getColumn(4);
        tipo.setMaxWidth(60*3-20);
        tipo.setMinWidth(60*3-20);
        tipo.setPreferredWidth(60*3-20);

        registro = tabla.getColumnModel().getColumn(5);
        registro.setMaxWidth(valor);
        registro.setMinWidth(valor);
        registro.setPreferredWidth(valor);

        breakam = tabla.getColumnModel().getColumn(6);
        breakam.setMaxWidth(valor + 10);
        breakam.setMinWidth(valor + 10);
        breakam.setPreferredWidth(valor + 10);

        almuerzo = tabla.getColumnModel().getColumn(7);
        almuerzo.setMaxWidth(valor + 10);
        almuerzo.setMinWidth(valor + 10);
        almuerzo.setPreferredWidth(valor + 10);
        
        breakpm = tabla.getColumnModel().getColumn(8);
        breakpm.setMaxWidth(valor + 10);
        breakpm.setMinWidth(valor + 10);
        breakpm.setPreferredWidth(valor + 10);

        tabla.doLayout();
    };
}
