package cl.duoc.api_eventos.controllers;

import cl.duoc.api_eventos.entities.Compra;
import cl.duoc.api_eventos.services.CompraService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/compras")
public class CompraController {

    private final CompraService compraService;

    public CompraController(CompraService compraService) {
        this.compraService = compraService;
    }

    @PostMapping
    public ResponseEntity<Compra> procesarCompra(@RequestBody CompraRequest request) {
        Compra resumenCompra = compraService.procesarCompra(request.getEventoId(), request.getCantidad());
        return new ResponseEntity<>(resumenCompra, HttpStatus.CREATED);
    }

    @GetMapping("/evento/{eventoId}")
    public ResponseEntity<List<Compra>> obtenerTicketsPorEvento(@PathVariable Long eventoId) {
        List<Compra> tickets = compraService.obtenerPorEvento(eventoId);
        return ResponseEntity.ok(tickets);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Compra> actualizarCompra(@PathVariable Long id, @RequestBody CompraRequest request) {
        Compra actualizada = compraService.actualizarCompra(id, request.getCantidad());
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCompra(@PathVariable Long id) {
        compraService.eliminarCompra(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/evento/{eventoId}/estadisticas")
    public ResponseEntity obtenerEstadisticasEvento(@PathVariable Long eventoId) {
        Map<String, Object> stats = compraService.obtenerEstadisticasPorEvento(eventoId);
        return ResponseEntity.ok(stats);
    }

    public static class CompraRequest {
        private Long eventoId;
        private Integer cantidad;

        public Long getEventoId() { return eventoId; }
        public void setEventoId(Long eventoId) { this.eventoId = eventoId; }
        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    }
}