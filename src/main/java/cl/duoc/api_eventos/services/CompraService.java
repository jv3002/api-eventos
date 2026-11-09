package cl.duoc.api_eventos.services;

import cl.duoc.api_eventos.entities.Compra;
import cl.duoc.api_eventos.entities.Evento;
import cl.duoc.api_eventos.repositories.CompraRepository;
import cl.duoc.api_eventos.repositories.EventoRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CompraService {

    private final CompraRepository compraRepository;
    private final EventoRepository eventoRepository;

    public CompraService(CompraRepository compraRepository, EventoRepository eventoRepository) {
        this.compraRepository = compraRepository;
        this.eventoRepository = eventoRepository;
    }

    // 1. Procesar compra (Generación de ticket, cálculo y S3 simulado/integrado)
    public Compra procesarCompra(Long eventoId, Integer cantidad) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));

        Compra compra = new Compra();
        compra.setEvento(evento);
        compra.setCantidadEntradas(cantidad);
        compra.setPrecioUnitario(evento.getPrecio());
        compra.setPrecioTotal(evento.getPrecio() * cantidad);

        // Aquí se ejecuta tu lógica existente de PDF / S3
        return compraRepository.save(compra);
    }

    // 2. Obtener tickets / compras por ID de evento (Para descarga o consulta)
    public List<Compra> obtenerPorEvento(Long eventoId) {
        return compraRepository.findByEventoId(eventoId);
    }

    // 3. Modificar detalles del ticket (Ej: cambiar cantidad)
    public Compra actualizarCompra(Long id, Integer nuevaCantidad) {
        Compra compra = compraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada"));
        
        compra.setCantidadEntradas(nuevaCantidad);
        compra.setPrecioTotal(compra.getPrecioUnitario() * nuevaCantidad);
        
        return compraRepository.save(compra);
    }

    // 4. Eliminar ticket específico
    public void eliminarCompra(Long id) {
        if (!compraRepository.existsById(id)) {
            throw new RuntimeException("Compra no encontrada para eliminar");
        }
        compraRepository.deleteById(id);
    }

    // 5. Reportar estadísticas de ventas y reservas por evento
    public Map<String, Object> obtenerEstadisticasPorEvento(Long eventoId) {
        List<Compra> compras = compraRepository.findByEventoId(eventoId);

        int totalEntradasVendidas = compras.stream()
                .mapToInt(Compra::getCantidadEntradas)
                .sum();

        double recaudacionTotal = compras.stream()
                .mapToDouble(Compra::getPrecioTotal)
                .sum();

        Map<String, Object> estadisticas = new HashMap<>();
        estadisticas.put("eventoId", eventoId);
        estadisticas.put("totalReservas", compras.size());
        estadisticas.put("totalEntradasVendidas", totalEntradasVendidas);
        estadisticas.put("recaudacionTotal", recaudacionTotal);

        return estadisticas;
    }
}