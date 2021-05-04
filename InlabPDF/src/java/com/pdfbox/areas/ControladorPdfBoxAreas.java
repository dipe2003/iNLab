/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdfbox.areas;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import javax.faces.context.ExternalContext;
import modelo.muestreo.Area;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

/**
 *
 * @author dipe2
 */
public class ControladorPdfBoxAreas {

    public void CrearListadoAreas(ExternalContext context, List<Area> areas) throws FileNotFoundException, IOException {
        context.responseReset();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        context.setResponseContentType("application/pdf");

        // Se crea el documento
        PDDocument documento = new PDDocument();

        try {
            PDPage pagina = new PDPage();

            PDPageContentStream contentStream = new PDPageContentStream(documento, pagina);
            contentStream.setLeading(14.5f);

            AgregarEncabezado(contentStream);

//            PdfPTable tabla = new PdfPTable(4);
//            AgregarEncabezados(tabla);
//            LlenarDatos(tabla, areas);
//            documento.add(tabla);
            contentStream.close();
            documento.save(baos);
            documento.close();

            context.setResponseContentLength(baos.size());
            OutputStream os = context.getResponseOutputStream();
            baos.writeTo(os);
            os.flush();
            os.close();

        } catch (IOException ex) {
            System.out.println("Error al crear pdf: " + ex.getMessage());
        }
    }

    private void AgregarEncabezado(PDPageContentStream documento) throws IOException {
        String texto = "Listado de Areas - PDFBOX";
        documento.setFont(PDType1Font.COURIER_BOLD, 14);
        documento.beginText();
        documento.showText(texto);
        documento.newLine();
        documento.endText();
    }

//    private void AgregarEncabezados(PdfPTable tabla) {
//        tabla.addCell(new Phrase(new Chunk("Id", FontFactory.getFont("monospace", 12, Font.BOLD, BaseColor.BLACK))));
//        tabla.addCell(new Phrase(new Chunk("Nombre", FontFactory.getFont("monospace", 12, Font.BOLD, BaseColor.BLACK))));
//        tabla.addCell(new Phrase(new Chunk("Es Productiva", FontFactory.getFont("monospace", 12, Font.BOLD, BaseColor.BLACK))));
//        tabla.addCell(new Phrase(new Chunk("Es Vigente", FontFactory.getFont("monospace", 12, Font.BOLD, BaseColor.BLACK))));
//    }
//
//    private void LlenarDatos(PdfPTable tabla, List<Area> areas) {
//        for (Area area : areas) {
//            tabla.addCell(area.getId().toString());
//            tabla.addCell(area.getNombre());
//
//            if (area.isEsProductiva()) {
//                tabla.addCell("Si");
//            } else {
//                tabla.addCell("No");
//            }
//
//            if (area.EsVigente()) {
//                tabla.addCell("Si");
//            } else {
//                tabla.addCell("No, desde " + area.getFechaObsoleto().toString());
//            }
//        }
}
