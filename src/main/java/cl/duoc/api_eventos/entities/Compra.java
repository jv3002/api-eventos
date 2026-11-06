package cl.duoc.api_eventos.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "compras")
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;

    @Column(name = "cantidad_entradas", nullable = false)
    private Integer cantidadEntradas;

    @Column(name = "precio_unitario", nullable = false)
    private Double precioUnitario;

    @Column(name = "precio_total", nullable = false)
    private Double precioTotal;

    public Compra() {
    }

    public Compra(Evento evento, Integer cantidadEntradas, Double precioUnitario, Double precioTotal) {
        this.evento = evento;
        this.cantidadEntradas = cantidadEntradas;
        this.precioUnitario = precioUnitario;
        this.precioTotal = precioTotal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public Integer getCantidadEntradas() {
        return cantidadEntradas;
    }

    public void setCantidadEntradas(Integer cantidadEntradas) {
        this.cantidadEntradas = cantidadEntradas;
    }

    public Double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public Double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(Double precioTotal) {
        this.precioTotal = precioTotal;
    }
}