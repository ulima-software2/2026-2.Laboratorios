package pe.edu.ulima.is2.todo.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Todo {

    private Long id;
    private String titulo;
    private String descripcion;
    private Prioridad prioridad;
    private LocalDate fechaLimite;
    private boolean completado;
    private LocalDateTime creadoEn;

    public Todo() {
    }

    public Todo(String titulo, String descripcion, Prioridad prioridad, LocalDate fechaLimite) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.prioridad = prioridad;
        this.fechaLimite = fechaLimite;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Prioridad getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(Prioridad prioridad) {
        this.prioridad = prioridad;
    }

    public LocalDate getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(LocalDate fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public boolean isCompletado() {
        return completado;
    }

    public void setCompletado(boolean completado) {
        this.completado = completado;
    }

    public LocalDateTime getCreadoEn() {
        return creadoEn;
    }

    public void setCreadoEn(LocalDateTime creadoEn) {
        this.creadoEn = creadoEn;
    }

    @Override
    public String toString() {
        return "Todo{id=" + id + ", titulo='" + titulo + "', prioridad=" + prioridad
                + ", fechaLimite=" + fechaLimite + ", completado=" + completado + "}";
    }
}
