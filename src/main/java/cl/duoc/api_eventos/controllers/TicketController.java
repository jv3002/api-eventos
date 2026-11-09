package cl.duoc.api_eventos.controllers;

import cl.duoc.api_eventos.models.Ticket;
import cl.duoc.api_eventos.repositories.TicketRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketRepository ticketRepository;

    // Inyección del repositorio JPA
    public TicketController(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
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

    // 3. CREAR TICKET
    @PostMapping
    public ResponseEntity<Ticket> crearTicket(@RequestBody Ticket ticketRequest) {
        Ticket nuevoTicket = ticketRepository.save(ticketRequest);
        return ResponseEntity.ok(nuevoTicket);
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