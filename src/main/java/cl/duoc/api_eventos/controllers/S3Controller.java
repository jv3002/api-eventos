package cl.duoc.api_eventos.controllers;

import cl.duoc.api_eventos.services.S3Service;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/s3/tickets")
public class S3Controller {

    private final S3Service s3Service;

    public S3Controller(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    // 1. SUBIR TICKET (PDF)
    @PostMapping("/subir")
    public ResponseEntity<String> subirTicketS3(
            @RequestParam("eventoId") String eventoId,
            @RequestParam("usuarioId") String usuarioId,
            @RequestParam("file") MultipartFile file) {
        try {
            // Pasamos los bytes del archivo como lo pide tu servicio
            String resultado = s3Service.subirTicket(eventoId, usuarioId, file.getOriginalFilename(), file.getBytes());
            return ResponseEntity.ok(resultado);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error al procesar el archivo: " + e.getMessage());
        }
    }

    // 2. DESCARGAR TICKET (PDF)
    @GetMapping("/descargar")
    public ResponseEntity<byte[]> descargarTicketS3(
            @RequestParam("eventoId") String eventoId,
            @RequestParam("usuarioId") String usuarioId,
            @RequestParam("nombreArchivo") String nombreArchivo) {
        try {
            byte[] archivoBytes = s3Service.descargarTicket(eventoId, usuarioId, nombreArchivo);
            
            // Configuramos los headers para que el navegador/Postman entienda que es un PDF descargable
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", nombreArchivo);
            
            return new ResponseEntity<>(archivoBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // 3. ELIMINAR TICKET - Restringido solo a ADMIN
    @DeleteMapping("/eliminar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> eliminarTicketS3(
            @RequestParam("eventoId") String eventoId,
            @RequestParam("usuarioId") String usuarioId,
            @RequestParam("nombreArchivo") String nombreArchivo) {
        try {
            String resultado = s3Service.eliminarTicket(eventoId, usuarioId, nombreArchivo);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al eliminar en S3: " + e.getMessage());
        }
    }
}