package cl.duoc.api_eventos.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private static List<Ticket> listaTickets = new ArrayList<>();
    private static Long contadorId = 1L;

    @GetMapping
    public ResponseEntity<List<Ticket>> listarTickets() {
        return ResponseEntity.ok(listaTickets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ticket> obtenerTicketPorId(@PathVariable Long id) {
        Optional<Ticket> ticket = listaTickets.stream().filter(t -> t.getId().equals(id)).findFirst();
        return ticket.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Ticket> crearTicket(@RequestBody Ticket ticketRequest) {
        ticketRequest.setId(contadorId++);
        listaTickets.add(ticketRequest);
        return ResponseEntity.ok(ticketRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ticket> actualizarTicket(@PathVariable Long id, @RequestBody Ticket ticketRequest) {
        for (Ticket t : listaTickets) {
            if (t.getId().equals(id)) {
                t.setEvento(ticketRequest.getEvento());
                t.setCantidad(ticketRequest.getCantidad());
                t.setPrecioUnitario(ticketRequest.getPrecioUnitario());
                t.setTipoEntrada(ticketRequest.getTipoEntrada());
                t.setCliente(ticketRequest.getCliente());
                return ResponseEntity.ok(t);
            }
        }
        return ResponseEntity.notFound().build();
    }

    // 5. ELIMINAR TICKET - Restringido exclusivamente para el rol ADMIN
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") 
    public ResponseEntity<Void> eliminarTicket(@PathVariable Long id) {
        boolean removido = listaTickets.removeIf(t -> t.getId().equals(id));
        if (removido) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    public static class Ticket {
        private Long id;
        private String evento;
        private Integer cantidad;
        private Double precioUnitario;
        private String tipoEntrada;
        private String cliente;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getEvento() { return evento; }
        public void setEvento(String evento) { this.evento = evento; }
        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
        public Double getPrecioUnitario() { return precioUnitario; }
        public void setPrecioUnitario(Double precioUnitario) { this.precioUnitario = precioUnitario; }
        public String getTipoEntrada() { return tipoEntrada; }
        public void setTipoEntrada(String tipoEntrada) { this.tipoEntrada = tipoEntrada; }
        public String getCliente() { return cliente; }
        public void setCliente(String cliente) { this.cliente = cliente; }
    }
}