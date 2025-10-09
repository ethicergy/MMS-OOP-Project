package com.mms.util;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.io.image.ImageDataFactory;
import com.mms.models.Movie;
import com.mms.models.Showtime;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class PDFGenerator {
    
    public static void generateTicketPDF(String filePath, Movie movie, Showtime showtime, 
                                       List<String> seats, double totalPrice, BufferedImage qrImage) throws Exception {
        
        PdfWriter writer = new PdfWriter(new FileOutputStream(filePath));
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        
        // Fonts
        PdfFont titleFont = PdfFontFactory.createFont();
        PdfFont headerFont = PdfFontFactory.createFont();
        PdfFont normalFont = PdfFontFactory.createFont();
        
        // Header
        Paragraph title = new Paragraph("MOVIE MANAGEMENT SYSTEM")
                .setFont(titleFont)
                .setFontSize(24)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(10);
        document.add(title);
        
        Paragraph subtitle = new Paragraph("E-TICKET")
                .setFont(headerFont)
                .setFontSize(18)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(20);
        document.add(subtitle);
        
        // Ticket details table
        Table ticketTable = new Table(UnitValue.createPercentArray(new float[]{30, 70}))
                .setWidth(UnitValue.createPercentValue(100))
                .setMarginBottom(20);
        
        // Add ticket information
        addTableRow(ticketTable, "Movie Title:", movie.getTitle(), headerFont, normalFont);
        addTableRow(ticketTable, "Genre:", movie.getGenre(), headerFont, normalFont);
        addTableRow(ticketTable, "Language:", movie.getLanguage(), headerFont, normalFont);
        addTableRow(ticketTable, "Duration:", movie.getDuration() + " minutes", headerFont, normalFont);
        addTableRow(ticketTable, "Certificate:", movie.getCertificate(), headerFont, normalFont);
        addTableRow(ticketTable, "Show Date:", showtime.getDate().toString(), headerFont, normalFont);
        addTableRow(ticketTable, "Show Time:", showtime.getTime().toString(), headerFont, normalFont);
        addTableRow(ticketTable, "Screen:", "Screen " + showtime.getScreenNumber(), headerFont, normalFont);
        addTableRow(ticketTable, "Seats:", String.join(", ", seats), headerFont, normalFont);
        addTableRow(ticketTable, "Total Amount:", "₹" + String.format("%.2f", totalPrice), headerFont, normalFont);
        
        document.add(ticketTable);
        
        // QR Code section
        if (qrImage != null) {
            Paragraph qrTitle = new Paragraph("QR Code for Digital Verification")
                    .setFont(headerFont)
                    .setFontSize(14)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(20)
                    .setMarginBottom(10);
            document.add(qrTitle);
            
            // Convert BufferedImage to byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(qrImage, "PNG", baos);
            byte[] imageBytes = baos.toByteArray();
            
            // Add QR image to PDF
            Image qrPdfImage = new Image(ImageDataFactory.create(imageBytes))
                    .setWidth(150)
                    .setHeight(150)
                    .setHorizontalAlignment(com.itextpdf.layout.properties.HorizontalAlignment.CENTER);
            document.add(qrPdfImage);
        }
        
        // Footer information
        Paragraph footer = new Paragraph("\n\nTicket generated on: " + 
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")))
                .setFont(normalFont)
                .setFontSize(10)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(30);
        document.add(footer);
        
        Paragraph terms = new Paragraph("Terms & Conditions: Please arrive 15 minutes before show time. " +
                "No outside food allowed. Ticket is non-refundable.")
                .setFont(normalFont)
                .setFontSize(8)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(10)
                .setFontColor(ColorConstants.GRAY);
        document.add(terms);
        
        document.close();
    }
    
    private static void addTableRow(Table table, String label, String value, PdfFont labelFont, PdfFont valueFont) {
        Cell labelCell = new Cell()
                .add(new Paragraph(label).setFont(labelFont).setBold().setFontSize(12))
                .setBorder(null)
                .setPaddingBottom(8);
        
        Cell valueCell = new Cell()
                .add(new Paragraph(value).setFont(valueFont).setFontSize(12))
                .setBorder(null)
                .setPaddingBottom(8);
        
        table.addCell(labelCell);
        table.addCell(valueCell);
    }
}