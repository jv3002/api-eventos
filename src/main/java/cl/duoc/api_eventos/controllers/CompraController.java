package cl.duoc.api_eventos.controllers;

import cl.duoc.api_eventos.entities.Compra;
import cl.duoc.api_eventos.services.CompraService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/compras")
public class CompraController {

    private final CompraService compraService;

    public CompraController(CompraService compraService) {
        this.compraService = compraService;
    }

    // Endpoint para la compra de entradas
    @PostMapping
    public ResponseEntity<Compra> procesarCompra(@RequestBody CompraRequest request) {
        Compra resumenCompra = compraService.procesarCompra(request.getEventoId(), request.getCantidad());
        return new ResponseEntity<>(resumenCompra, HttpStatus.CREATED);
    }

    // Clase auxiliar para recibir el JSON de la petición en Postman
    public static class CompraRequest {
        private Long eventoId;
        private Integer cantidad;

        public Long getEventoId() { return eventoId; }
        public void setEventoId(Long eventoId) { this.eventoId = eventoId; }
        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    }
}