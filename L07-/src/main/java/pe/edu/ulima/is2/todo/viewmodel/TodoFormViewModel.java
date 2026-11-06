package pe.edu.ulima.is2.todo.viewmodel;

import pe.edu.ulima.is2.todo.model.Prioridad;
import pe.edu.ulima.is2.todo.model.Todo;
import pe.edu.ulima.is2.todo.model.TodoService;
import pe.edu.ulima.is2.todo.model.ValidacionException;

import java.util.Arrays;
import java.util.function.Consumer;

/** Estado y comandos del formulario para agregar un TODO. */
public class TodoFormViewModel {

    private final TodoService servicio;
    private final Consumer<Todo> alGuardar;

    private final Propiedad<String> titulo = new Propiedad<>("");
    private final Propiedad<String> descripcion = new Propiedad<>("");
    private final Propiedad<String> prioridad = new Propiedad<>(TodoService.PRIORIDAD_POR_DEFECTO.name());
    private final Propiedad<String> fechaLimite = new Propiedad<>("");
    private final Propiedad<String> error = new Propiedad<>("");

    public TodoFormViewModel(TodoService servicio, Consumer<Todo> alGuardar) {
        this.servicio = servicio;
        this.alGuardar = alGuardar;
    }

    public Propiedad<String> titulo() {
        return titulo;
    }

    public Propiedad<String> descripcion() {
        return descripcion;
    }

    public Propiedad<String> prioridad() {
        return prioridad;
    }

    public Propiedad<String> fechaLimite() {
        return fechaLimite;
    }

    public Propiedad<String> error() {
        return error;
    }

    public String[] opcionesPrioridad() {
        return Arrays.stream(Prioridad.values()).map(Enum::name).toArray(String[]::new);
    }

    /** @return true si se guardó; false si hubo un error de validación (queda en {@link #error()}). */
    public boolean guardar() {
        try {
            Todo todo = servicio.agregar(titulo.get(), descripcion.get(), prioridad.get(), fechaLimite.get());
            error.set("");
            alGuardar.accept(todo);
            return true;
        } catch (ValidacionException e) {
            error.set(e.getMessage());
            return false;
        }
    }
}
