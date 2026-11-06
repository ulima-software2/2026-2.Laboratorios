package pe.edu.ulima.is2.todo.viewmodel;

import pe.edu.ulima.is2.todo.model.NivelUrgencia;
import pe.edu.ulima.is2.todo.model.Todo;

/** Datos de un TODO ya formateados para mostrarse en una fila de la lista. */
public record TodoItemViewModel(
        long id,
        String estado,
        String titulo,
        String prioridad,
        String fechaLimite,
        String urgencia) {

    public static TodoItemViewModel desde(Todo todo, NivelUrgencia urgencia) {
        return new TodoItemViewModel(
                todo.getId(),
                todo.isCompletado() ? "[x]" : "[ ]",
                todo.getTitulo(),
                todo.getPrioridad().name(),
                todo.getFechaLimite() == null ? "-" : todo.getFechaLimite().toString(),
                urgencia == NivelUrgencia.NINGUNA ? "-" : urgencia.name());
    }
}
