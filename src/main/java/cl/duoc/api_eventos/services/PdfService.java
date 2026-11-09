package cl.duoc.api_eventos.services;

import cl.duoc.api_eventos.models.Ticket;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PdfService {

    public byte[] generarTicketPdf(Ticket ticket) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, BaseColor.BLACK);
            Paragraph titulo = new Paragraph("TICKET DE ENTRADA", fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);
            
            document.add(Chunk.NEWLINE);

            Font fontTexto = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.DARK_GRAY);
            document.add(new Paragraph("ID de Transacción: #" + ticket.getId(), fontTexto));
            document.add(new Paragraph("Evento: " + ticket.getEvento(), fontTexto));
            document.add(new Paragraph("Cliente: " + ticket.getCliente(), fontTexto));
            document.add(new Paragraph("Tipo de Entrada: " + ticket.getTipoEntrada(), fontTexto));
            document.add(new Paragraph("Cantidad: " + ticket.getCantidad(), fontTexto));
            document.add(new Paragraph("Precio Unitario: $" + ticket.getPrecioUnitario(), fontTexto));
            
            double total = ticket.getCantidad() * ticket.getPrecioUnitario();
            
            document.add(Chunk.NEWLINE);
            Font fontTotal = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.RED);
            document.add(new Paragraph("TOTAL PAGADO: $" + total, fontTotal));

            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return out.toByteArray();
    }
}