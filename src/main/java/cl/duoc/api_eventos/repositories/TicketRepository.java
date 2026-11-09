package cl.duoc.api_eventos.repositories;

import cl.duoc.api_eventos.models.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    // Spring Data JPA provee automáticamente findAll(), findById(), save() y deleteById()
}