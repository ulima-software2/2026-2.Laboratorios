package pe.edu.ulima.is2.todo.model;

/**
 * Se lanza cuando los datos ingresados para un TODO no cumplen las reglas de negocio.
 * Indica qué campo falló para que la vista pueda mostrar el error.
 */
public class ValidacionException extends RuntimeException {

    private final String campo;

    public ValidacionException(String campo, String mensaje) {
        super(mensaje);
        this.campo = campo;
    }

    public String getCampo() {
        return campo;
    }
}
