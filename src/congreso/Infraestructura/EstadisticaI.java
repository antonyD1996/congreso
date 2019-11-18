/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package congreso.Infraestructura;

import congreso.Dominio.Estadistica;
import java.util.List;
import java.util.function.BiConsumer;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author anton
 */
public class EstadisticaI {
    
    public BiConsumer<JTable, List<Estadistica>> cargarTabla=(tabla, listado)->{
        DefaultTableModel model=new DefaultTableModel();
        model.addColumn("");
        model.addColumn("Esperados");
        model.addColumn("Registrados");
        model.addColumn("BreakAM");
        model.addColumn("Almuerzos");
        model.addColumn("BreakPM");

        listado.stream().forEach(p->{
            model.addRow(new Object[]{
                    p.getNombre(),
                    p.getEsperados(),
                    p.getRegistrados(),
                    p.getBreakAM(),
                    p.getAlmuerzos(),
                    p.getBreakPM()
            });
        });
        tabla.setModel(model);
    };
    public BiConsumer<JTable,List<Estadistica>> actualizarDatos= (tabla,listado)->{
        cargarTabla.accept(tabla, listado);
//        TableColumn columna, n;
//            columna= tabla.getColumnModel().getColumn(0);
//            columna.setMaxWidth(0);
//            columna.setMinWidth(0);
//            columna.setPreferredWidth(0);
//            n= tabla.getColumnModel().getColumn(1);
//            n.setMaxWidth(100);
//            n.setMinWidth(100);
//            n.setPreferredWidth(100);
            tabla.doLayout();
    };
    
}
