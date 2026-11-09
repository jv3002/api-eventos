package cl.duoc.api_eventos.repositories;

import cl.duoc.api_eventos.entities.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {
    
    // Método personalizado requerido por el servicio para listar y calcular estadísticas por evento
    List<Compra> findByEventoId(Long eventoId);

}