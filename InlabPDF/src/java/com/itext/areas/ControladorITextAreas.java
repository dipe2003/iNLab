/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itext.areas;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import javax.faces.context.ExternalContext;
import modelo.muestreo.Area;

/**
 *
 * @author dipe2
 */
public class ControladorITextAreas {

    public void CrearListadoAreas(ExternalContext context, List<Area> areas, boolean horizontal) throws FileNotFoundException, IOException {
        context.responseReset();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        context.setResponseContentType("application/pdf");

        // Se crea el documento
         Document documento = null;
        if(horizontal){
            documento = new Document(PageSize.A4.rotate());
        }else{
            documento = new Document(PageSize.A4);
        }

        try {
            PdfWriter.getInstance(documento, baos).setInitialLeading(36);

            // Se abre el documento.
            documento.open();

            AgregarEncabezado(documento);

            PdfPTable tabla = new PdfPTable(4);
            AgregarEncabezados(tabla);
            LlenarDatos(tabla, areas);
            documento.add(tabla);
            documento.close();

            context.setResponseContentLength(baos.size());
            OutputStream os = context.getResponseOutputStream();
            baos.writeTo(os);
            os.flush();
            os.close();

        } catch (DocumentException ex) {
            System.out.println("Error al crear pdf: " + ex.getMessage());
        }
    }

    private void AgregarEncabezado(Document documento) throws DocumentException {
        documento.add(new Paragraph("Listado de Areas:",
                FontFactory.getFont("monospace", // fuente
                        14, // tamaño
                        Font.BOLD, // estilo
                        BaseColor.BLACK)));             // color
        LineSeparator line = new LineSeparator(1, 100, BaseColor.GREEN, Element.ALIGN_CENTER, -2);
        documento.add(line);
        documento.add(new Chunk(Chunk.NEWLINE));
    }

    private void AgregarEncabezados(PdfPTable tabla) {
        tabla.addCell(new Phrase(new Chunk("Id", FontFactory.getFont("monospace", 12, Font.BOLD, BaseColor.BLACK))));
        tabla.addCell(new Phrase(new Chunk("Nombre", FontFactory.getFont("monospace", 12, Font.BOLD, BaseColor.BLACK))));
        tabla.addCell(new Phrase(new Chunk("Es Productiva", FontFactory.getFont("monospace", 12, Font.BOLD, BaseColor.BLACK))));
        tabla.addCell(new Phrase(new Chunk("Es Vigente", FontFactory.getFont("monospace", 12, Font.BOLD, BaseColor.BLACK))));
    }

    private void LlenarDatos(PdfPTable tabla, List<Area> areas) {
        for (Area area : areas) {
            tabla.addCell(area.getId().toString());
            tabla.addCell(area.getNombre());

            if (area.isEsProductiva()) {
                tabla.addCell("Si");
            } else {
                tabla.addCell("No");
            }

            if (area.isEsVigente()) {
                tabla.addCell("Si");
            } else {
                tabla.addCell("No, desde " + area.getFechaObsoleto().toString());
            }
        }
    }
}
