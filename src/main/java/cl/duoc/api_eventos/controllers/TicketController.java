package cl.duoc.api_eventos.controllers;

import cl.duoc.api_eventos.services.S3Service;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private S3Service s3Service;

    @PostMapping("/generar")
    public ResponseEntity<String> generarTicket(
            @RequestParam("eventoId") String eventoId,
            @RequestParam("usuarioId") String usuarioId,
            @RequestParam("asiento") String asiento) {
        try {
            String ticketId = UUID.randomUUID().toString();
            String nombreArchivo = ticketId + ".pdf";
            
            String efsPath = System.getProperty("java.io.tmpdir") + File.separator + "efs";
            File directory = new File(efsPath);
            if (!directory.exists()) {
                directory.mkdirs(); 
            }
            String rutaCompleta = efsPath + File.separator + nombreArchivo;

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(rutaCompleta));
            document.open();
            document.add(new Paragraph("TICKET DE RESERVA"));
            document.add(new Paragraph("ID Evento: " + eventoId));
            document.add(new Paragraph("Usuario: " + usuarioId));
            document.add(new Paragraph("Asiento Asignado: " + asiento));
            document.add(new Paragraph("Ticket ID: " + ticketId));
            document.close();

            Path path = Paths.get(rutaCompleta);
            byte[] pdfBytes = Files.readAllBytes(path);
            String resultadoS3 = s3Service.subirTicket(eventoId, usuarioId, nombreArchivo, pdfBytes);

            return ResponseEntity.ok("Ticket generado en EFS y " + resultadoS3);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al generar o subir el ticket: " + e.getMessage());
        }
    }

    @PostMapping("/subir/{ticketId}")
    public ResponseEntity<String> subirTicket(
            @PathVariable String ticketId,
            @RequestParam("eventoId") String eventoId,
            @RequestParam("usuarioId") String usuarioId,
            @RequestParam("file") MultipartFile file) {
        try {
            String nombreArchivo = ticketId + ".pdf";
            String resultado = s3Service.subirTicket(eventoId, usuarioId, nombreArchivo, file.getBytes());
            return ResponseEntity.ok(resultado);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al leer el archivo: " + e.getMessage());
        }
    }

    @GetMapping("/descargar")
    public ResponseEntity<byte[]> descargarTicket(
            @RequestParam("eventoId") String eventoId,
            @RequestParam("usuarioId") String usuarioId,
            @RequestParam("ticketId") String ticketId) {
        String nombreArchivo = ticketId + ".pdf";
        byte[] archivoBytes = s3Service.descargarTicket(eventoId, usuarioId, nombreArchivo);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", nombreArchivo);

        return new ResponseEntity<>(archivoBytes, headers, HttpStatus.OK);
    }

    @PutMapping("/{ticketId}")
    public ResponseEntity<String> modificarTicket(
            @PathVariable String ticketId,
            @RequestParam("eventoId") String eventoId,
            @RequestParam("usuarioId") String usuarioId,
            @RequestParam("nuevoAsiento") String nuevoAsiento) {
        try {
            String nombreArchivo = ticketId + ".pdf";
            String efsPath = System.getProperty("java.io.tmpdir") + File.separator + "efs";
            String rutaCompleta = efsPath + File.separator + nombreArchivo;
            
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(rutaCompleta));
            document.open();
            document.add(new Paragraph("TICKET DE RESERVA - MODIFICADO"));
            document.add(new Paragraph("ID Evento: " + eventoId));
            document.add(new Paragraph("Usuario: " + usuarioId));
            document.add(new Paragraph("NUEVO Asiento Asignado: " + nuevoAsiento));
            document.add(new Paragraph("Ticket ID: " + ticketId));
            document.close();

            byte[] pdfBytes = Files.readAllBytes(Paths.get(rutaCompleta));
            String resultadoS3 = s3Service.subirTicket(eventoId, usuarioId, nombreArchivo, pdfBytes); 

            return ResponseEntity.ok("Ticket modificado y " + resultadoS3);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{ticketId}")
    public ResponseEntity<String> eliminarTicket(
            @PathVariable String ticketId,
            @RequestParam("eventoId") String eventoId,
            @RequestParam("usuarioId") String usuarioId) {
        String nombreArchivo = ticketId + ".pdf";
        String resultado = s3Service.eliminarTicket(eventoId, usuarioId, nombreArchivo);
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/reportes")
    public ResponseEntity reportarEstadisticas(@RequestParam("eventoId") String eventoId) {
        Map<String, Object> reporte = new HashMap<>();
        reporte.put("eventoId", eventoId);
        reporte.put("totalReservas", 150);
        reporte.put("ticketsGenerados", 145);
        reporte.put("estado", "Activo");
        return ResponseEntity.ok(reporte);
    }
}