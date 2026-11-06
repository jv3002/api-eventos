package cl.duoc.api_eventos.services;

import cl.duoc.api_eventos.entities.Evento;
import cl.duoc.api_eventos.repositories.EventoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EventoService {

    private final EventoRepository eventoRepository;

    public EventoService(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    public List<Evento> obtenerEventos() {
        return eventoRepository.findAll();
    }

    public Evento crearEvento(Evento evento) {
        return eventoRepository.save(evento);
    }
}