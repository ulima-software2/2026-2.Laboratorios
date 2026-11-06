package pe.edu.ulima.is2.todo.viewmodel;

import pe.edu.ulima.is2.todo.model.FiltroTodo;
import pe.edu.ulima.is2.todo.model.PersistenciaException;
import pe.edu.ulima.is2.todo.model.Todo;
import pe.edu.ulima.is2.todo.model.TodoService;

import java.util.List;

/** Estado y comandos de la pantalla principal (lista de TODOs). */
public class TodoListViewModel {

    private final TodoService servicio;

    private final Propiedad<List<TodoItemViewModel>> items = new Propiedad<>(List.of());
    private final Propiedad<FiltroTodo> filtro = new Propiedad<>(FiltroTodo.PENDIENTES);
    private final Propiedad<String> mensaje = new Propiedad<>("");

    public TodoListViewModel(TodoService servicio) {
        this.servicio = servicio;
    }

    public Propiedad<List<TodoItemViewModel>> items() {
        return items;
    }

    public Propiedad<FiltroTodo> filtro() {
        return filtro;
    }

    public Propiedad<String> mensaje() {
        return mensaje;
    }

    public void cargar() {
        try {
            List<Todo> todos = servicio.listar(filtro.get());
            items.set(todos.stream()
                    .map(t -> TodoItemViewModel.desde(t, servicio.urgenciaDe(t)))
                    .toList());
        } catch (PersistenciaException e) {
            mensaje.set("Error: " + e.getMessage());
        }
    }

    public void siguienteFiltro() {
        filtro.set(filtro.get().siguiente());
        cargar();
        mensaje.set("Mostrando: " + filtro.get().name().toLowerCase());
    }

    public void alternarCompletado(int indice) {
        TodoItemViewModel item = itemEn(indice);
        if (item == null) {
            return;
        }
        Todo todo = servicio.alternarCompletado(item.id());
        cargar();
        mensaje.set((todo.isCompletado() ? "Completado: " : "Pendiente: ") + todo.getTitulo());
    }

    public void eliminar(int indice) {
        TodoItemViewModel item = itemEn(indice);
        if (item == null) {
            return;
        }
        servicio.eliminar(item.id());
        cargar();
        mensaje.set("Eliminado: " + item.titulo());
    }

    public TodoItemViewModel itemEn(int indice) {
        List<TodoItemViewModel> lista = items.get();
        if (indice < 0 || indice >= lista.size()) {
            mensaje.set("Seleccione un TODO");
            return null;
        }
        return lista.get(indice);
    }

    /** Crea el ViewModel del formulario; al guardar, la lista se recarga. */
    public TodoFormViewModel nuevoFormulario() {
        return new TodoFormViewModel(servicio, todo -> {
            cargar();
            mensaje.set("Agregado: " + todo.getTitulo());
        });
    }
}
