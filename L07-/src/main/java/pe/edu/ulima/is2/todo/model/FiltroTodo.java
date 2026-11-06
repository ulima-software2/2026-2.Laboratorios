package pe.edu.ulima.is2.todo.model;

public enum FiltroTodo {
    PENDIENTES,
    COMPLETADOS,
    TODOS;

    public FiltroTodo siguiente() {
        return values()[(ordinal() + 1) % values().length];
    }
}
