package pe.edu.ulima.is2.todo.model;

import java.util.List;
import java.util.Optional;

public interface TodoRepository {

    /** Inserta el TODO y devuelve el mismo objeto con el id asignado. */
    Todo guardar(Todo todo);

    void actualizar(Todo todo);

    void eliminar(long id);

    Optional<Todo> buscarPorId(long id);

    List<Todo> listarTodos();
}
