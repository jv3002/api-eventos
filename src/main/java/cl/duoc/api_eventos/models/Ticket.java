package cl.duoc.api_eventos.models;

import jakarta.persistence.*;

@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String evento;
    private Integer cantidad;
    private Double precioUnitario;
    private String tipoEntrada;
    private String cliente;

    // Constructores vacíos y con parámetros necesarios para JPA
    public Ticket() {}

    public Ticket(String evento, Integer cantidad, Double precioUnitario, String tipoEntrada, String cliente) {
        this.evento = evento;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.tipoEntrada = tipoEntrada;
        this.cliente = cliente;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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