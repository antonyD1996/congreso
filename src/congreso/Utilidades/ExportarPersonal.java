/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package congreso.Utilidades;

import congreso.Dominio.Congreso;
import congreso.Dominio.PersonalCongreso;
import congreso.Infraestructura.PersonalCongresoI;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author anton
 */
public class ExportarPersonal {


    String[] columns1 = {"NOMBRE", "FUNCION", "ASISTIO", "BREAK AM", "ALMUERZO", "BREAK PM"};

    String[] resumenes = {"Esperados", "Registrados", "Break AM", "Almuerzo", "Break PM"};
    JFileChooser SelectArchivo = new JFileChooser();
    FileNameExtensionFilter filtro = new FileNameExtensionFilter("xls", "XLS", "xlsx", "XLSX");
    File archivo;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    Congreso congreso;
    Map<String, Object> map;
    PersonalCongresoI pci = new PersonalCongresoI();
    List<PersonalCongreso> listado;

    public void Exportar(Map<String, Object> map1) throws FileNotFoundException, IOException {

        this.congreso = (Congreso) map1.get("congreso");
        map1.put("idcongreso", congreso.getId());

        map = (int)map1.get("idTipo") == 0 ? pci.obtenerEstadisticas.apply(map1) : pci.obtenerEstadisticas.apply(map1);
        SelectArchivo.setFileFilter(filtro);
        if (SelectArchivo.showDialog(null, "Seleccionar Archivo") == JFileChooser.APPROVE_OPTION) {
            archivo = SelectArchivo.getSelectedFile();
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Personal");
            sheet.protectSheet("foodLaboratorio");
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 7));
            // Create a Font for styling header cells
            Font headerEncabezado = workbook.createFont();
            headerEncabezado.setBold(true);
            headerEncabezado.setFontHeightInPoints((short) 12);
            headerEncabezado.setColor(IndexedColors.BLACK.getIndex());

            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerFont.setColor(IndexedColors.BLACK.getIndex());

            // Create a CellStyle with the font
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);

            CellStyle encabezadoCellStyle = workbook.createCellStyle();
            encabezadoCellStyle.setFont(headerEncabezado);

            CellStyle alineadas = workbook.createCellStyle();
            alineadas.setAlignment(HorizontalAlignment.LEFT);
            alineadas.setLocked(true);

            // Fila del encabezado
            Row headerRow = sheet.createRow(0);
            Row fechaRow = sheet.createRow(1);
            Row regionalRow = sheet.createRow(2);

            Cell c = headerRow.createCell(0);
            c.setCellValue("Reporte del " + congreso.getNombre());
            c.setCellStyle(encabezadoCellStyle);

            Cell fechaCell = fechaRow.createCell(0);
            fechaCell.setCellValue("Fecha " + congreso.getFecha().format(dtf));

            Cell regionalCell = regionalRow.createCell(0);
            regionalCell.setCellValue((String) map1.get("tipo"));
            regionalCell.setCellStyle(encabezadoCellStyle);

            Row resument = sheet.createRow(4);

            Cell resumenCell = resument.createCell(0);
            resumenCell.setCellValue("RESUMEN");
            resumenCell.setCellStyle(encabezadoCellStyle);

            Row titulos = sheet.createRow(12);

            for (int i = 0; i < columns1.length; i++) {
                    Cell cell = titulos.createCell(i);
                    cell.setCellValue(columns1[i]);
                    cell.setCellStyle(headerCellStyle);
                }

            int rowNum = 13;
            listado = (List<PersonalCongreso>) map1.get("listado");

            for (PersonalCongreso personal : listado) {
                    Row row = sheet.createRow(rowNum++);

                    row.createCell(0).setCellValue(personal.getDatosPersonal().getNombre());

                    row.createCell(1).setCellValue(personal.getDatosPersonal().getDatosTipo().getNombre());

                    row.createCell(2).setCellValue(evaluarEstado(personal.getDatosAccion().getRegistro()));

                    row.createCell(3).setCellValue(evaluarEstado(personal.getDatosAccion().getBreakAM()));

                    row.createCell(4).setCellValue(evaluarEstado(personal.getDatosAccion().getAlmuerzo()));

                    row.createCell(5).setCellValue(evaluarEstado(personal.getDatosAccion().getBreakPM()));

                }
            for (int i = 0; i < columns1.length; i++) {
                    sheet.autoSizeColumn(i);
                }

            //Totales
            Row resumen = sheet.createRow(5);
            Cell total = resumen.createCell(0);
            total.setCellValue(resumenes[0] + ": ");
            Cell totalValor = resumen.createCell(1);
            totalValor.setCellValue(listado.size());
            totalValor.setCellStyle(alineadas);

            //Asistentes
            Row asistentes = sheet.createRow(6);
            Cell asistentesTitulo = asistentes.createCell(0);
            asistentesTitulo.setCellValue(resumenes[1] + ": ");
            Cell asistentesValor = asistentes.createCell(1);
            asistentesValor.setCellValue(map.get("registrados").toString());
            asistentesValor.setCellStyle(alineadas);

            //Break AM
            Row breakAM = sheet.createRow(7);
            Cell breakAMTitulo = breakAM.createCell(0);
            breakAMTitulo.setCellValue(resumenes[2] + ": ");
            Cell breakAMValor = breakAM.createCell(1);
            breakAMValor.setCellValue(map.get("breakam").toString());
            breakAMValor.setCellStyle(alineadas);

            //Almuerzo
            Row almuerzoRow = sheet.createRow(8);
            Cell almuerzoTitulo = almuerzoRow.createCell(0);
            almuerzoTitulo.setCellValue(resumenes[3] + ": ");
            Cell almuerzoValor = almuerzoRow.createCell(1);
            almuerzoValor.setCellValue(map.get("almuerzo").toString());
            almuerzoValor.setCellStyle(alineadas);

            //Break PM
            Row breakPM = sheet.createRow(9);
            Cell breakPMTitulo = breakPM.createCell(0);
            breakPMTitulo.setCellValue(resumenes[4] + ": ");
            Cell breakPMValor = breakPM.createCell(1);
            breakPMValor.setCellValue(map.get("breakpm").toString());
            breakPMValor.setCellStyle(alineadas);

            // Write the output to a file
            FileOutputStream fileOut = new FileOutputStream(archivo + ".xlsx");

            workbook.write(fileOut);
            fileOut.close();
            workbook.close();
        }

    }

    String evaluarEstado(Integer estado) {
        String valor;
        switch (estado) {
            case 1:
                valor = "SI";
                break;

            default:
                valor = "NO";
                break;
        }
        return valor;
    }

}
