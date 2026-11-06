package cl.duoc.api_eventos.controllers;

import cl.duoc.api_eventos.entities.Evento;
import cl.duoc.api_eventos.services.EventoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/eventos")
public class EventoController {

    private final EventoService eventoService;

    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    // Endpoint que devuelve la lista de eventos disponibles
    @GetMapping
    public ResponseEntity<List<Evento>> obtenerEventos() {
        return ResponseEntity.ok(eventoService.obtenerEventos());
    }

    // Endpoint para agregar nuevos eventos
    @PostMapping
    public ResponseEntity<Evento> crearEvento(@RequestBody Evento evento) {
        Evento nuevoEvento = eventoService.crearEvento(evento);
        return new ResponseEntity<>(nuevoEvento, HttpStatus.CREATED);
    }
}