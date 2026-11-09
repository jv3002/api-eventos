package cl.duoc.api_eventos;

import cl.duoc.api_eventos.entities.Evento; // Ajusta el paquete de tu entidad Evento si es necesario
import cl.duoc.api_eventos.repositories.EventoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApiEventosApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiEventosApplication.class, args);
    }

	@Bean
	public CommandLineRunner initData(EventoRepository eventoRepository) {
    	return args -> {
        	Evento evento = new Evento();
        	evento.setId(1L);
        	evento.setNombre("Conferencia Tech 2026");
        	eventoRepository.save(evento);
        	System.out.println("--> Evento con ID 1 creado automáticamente.");
    	};
	}
}