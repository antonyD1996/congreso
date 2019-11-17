/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package congreso.Infraestructura;
import congreso.Dominio.Congreso;
import congreso.Negocio.CongresoN;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author anton
 */
public class CongresoI {
    
    DateTimeFormatter dtf=DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    CongresoN cn = new CongresoN();
    
    public Supplier<List<Congreso>> listadoCongresos= cn.listadoCongresos::get;
    
    public Consumer<Congreso> guardarCongreso= (e) -> {
        new CongresoN().guardarCongreso.accept(e);
    };
    public BiConsumer<JTable, List<Congreso>> cargarTabla=(tabla, listado)->{
        DefaultTableModel model=new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("No.");
        model.addColumn("Nombre");
        model.addColumn("fecha");

        listado.stream().forEach(p->{
            model.addRow(new Object[]{
                    p.getId(),
                    model.getRowCount()+1,
                    p.getNombre(),
                    p.getFecha().format(dtf)
            });
        });
        tabla.setModel(model);
    };
    public Function<JTable,List<Congreso>> actualizarDatos= tabla->{
        cargarTabla.accept(tabla, cn.listadoCongresos.get());
        TableColumn columna, n;
            columna= tabla.getColumnModel().getColumn(0);
            columna.setMaxWidth(0);
            columna.setMinWidth(0);
            columna.setPreferredWidth(0);
            n= tabla.getColumnModel().getColumn(1);
            n.setMaxWidth(100);
            n.setMinWidth(100);
            n.setPreferredWidth(100);
            tabla.doLayout();
        return cn.listadoCongresos.get();
    };
}
