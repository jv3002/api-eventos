package cl.duoc.api_eventos.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    @PostMapping
    public ResponseEntity<TicketResponse> crearTicket(@RequestBody TicketRequest ticketRequest) {
        // Aquí puedes procesar o guardar el ticket recibido
        String mensaje = "Ticket creado exitosamente para el evento: " + ticketRequest.getEvento();
        return ResponseEntity.ok(new TicketResponse(mensaje, true));
    }

    // DTOs para la petición y respuesta de tickets
    public static class TicketRequest {
        private String evento;
        private Integer cantidad;
        private Double precioUnitario;
        private String tipoEntrada;
        private String cliente;

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

    public static class TicketResponse {
        private String mensaje;
        private boolean exito;

        public TicketResponse(String mensaje, boolean exito) {
            this.mensaje = mensaje;
            this.exito = exito;
        }

        public String getMensaje() { return mensaje; }
        public boolean isExito() { return exito; }
    }
}