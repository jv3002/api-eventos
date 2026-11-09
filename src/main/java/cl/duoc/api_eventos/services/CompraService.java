package cl.duoc.api_eventos.services;

import cl.duoc.api_eventos.entities.Compra;
import cl.duoc.api_eventos.entities.Evento;
import cl.duoc.api_eventos.repositories.CompraRepository;
import cl.duoc.api_eventos.repositories.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompraService {

    // Al usar @Autowired(required = false) y quitar el "final", 
    // Spring Boot iniciará tu app aunque no encuentre la base de datos.
    @Autowired(required = false)
    private CompraRepository compraRepository;

    @Autowired(required = false)
    private EventoRepository eventoRepository;

    public Compra procesarCompra(Long eventoId, Integer cantidad) {
        // 1. Buscar el evento solicitado
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));

        // 2. Calcular los precios solicitados por el requerimiento
        Double precioUnitario = evento.getPrecio();
        Double precioTotal = precioUnitario * cantidad;

        // 3. Crear el registro de la compra y guardarlo
        Compra nuevaCompra = new Compra(evento, cantidad, precioUnitario, precioTotal);
        return compraRepository.save(nuevaCompra);
    }
}