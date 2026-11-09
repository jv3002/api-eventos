package cl.duoc.api_eventos.controllers;

import cl.duoc.api_eventos.models.Ticket;
import cl.duoc.api_eventos.repositories.TicketRepository;
import cl.duoc.api_eventos.services.PdfService;
import cl.duoc.api_eventos.services.S3Service;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketRepository ticketRepository;
    private final PdfService pdfService;
    private final S3Service s3Service;

    // Inyección del repositorio JPA y los servicios de PDF y S3
    public TicketController(TicketRepository ticketRepository, PdfService pdfService, S3Service s3Service) {
        this.ticketRepository = ticketRepository;
        this.pdfService = pdfService;
        this.s3Service = s3Service;
    }

    // 1. OBTENER TODOS LOS TICKETS
    @GetMapping
    public ResponseEntity<List<Ticket>> listarTickets() {
        List<Ticket> tickets = ticketRepository.findAll();
        return ResponseEntity.ok(tickets);
    }

    // 2. OBTENER TICKET POR ID
    @GetMapping("/{id}")
    public ResponseEntity<Ticket> obtenerTicketPorId(@PathVariable Long id) {
        Optional<Ticket> ticket = ticketRepository.findById(id);
        return ticket.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 3. CREAR TICKET + GENERAR PDF + SUBIR A S3
    @PostMapping
    public ResponseEntity<String> crearTicket(@RequestBody Ticket ticketRequest) {
        try {
            // 1. Guardamos el ticket en MySQL para obtener el ID real
            Ticket nuevoTicket = ticketRepository.save(ticketRequest);

            // 2. Generamos el PDF en memoria usando los datos del ticket
            byte[] pdfBytes = pdfService.generarTicketPdf(nuevoTicket);

            // 3. Subimos el PDF a AWS S3
            // Reemplazamos los espacios del evento por guiones bajos para crear una carpeta limpia en S3
            String carpetaEvento = nuevoTicket.getEvento().replaceAll("\\s+","_");
            String nombreArchivo = "ticket_" + nuevoTicket.getId() + ".pdf";
            
            String s3Response = s3Service.subirTicket(carpetaEvento, nuevoTicket.getCliente(), nombreArchivo, pdfBytes);

            // 4. Retornamos un mensaje indicando el éxito de la base de datos y de S3
            return ResponseEntity.ok("Venta exitosa. Ticket MySQL #" + nuevoTicket.getId() + " creado. " + s3Response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error procesando la venta: " + e.getMessage());
        }
    }

    // 4. ACTUALIZAR TICKET
    @PutMapping("/{id}")
    public ResponseEntity<Ticket> actualizarTicket(@PathVariable Long id, @RequestBody Ticket ticketRequest) {
        Optional<Ticket> ticketOpt = ticketRepository.findById(id);
        if (ticketOpt.isPresent()) {
            Ticket t = ticketOpt.get();
            t.setEvento(ticketRequest.getEvento());
            t.setCantidad(ticketRequest.getCantidad());
            t.setPrecioUnitario(ticketRequest.getPrecioUnitario());
            t.setTipoEntrada(ticketRequest.getTipoEntrada());
            t.setCliente(ticketRequest.getCliente());
            
            Ticket ticketActualizado = ticketRepository.save(t);
            return ResponseEntity.ok(ticketActualizado);
        }
        return ResponseEntity.notFound().build();
    }

    // 5. ELIMINAR TICKET - Restringido exclusivamente para el rol ADMIN
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarTicket(@PathVariable Long id) {
        if (ticketRepository.existsById(id)) {
            ticketRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}