package pe.edu.ulima.is2.todo.model;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Reglas de negocio de la aplicación. Es el punto de entrada del modelo
 * para los ViewModels.
 */
public class TodoService {

    public static final int TITULO_MIN = 3;
    public static final int TITULO_MAX = 100;
    public static final int DESCRIPCION_MAX = 500;
    public static final Prioridad PRIORIDAD_POR_DEFECTO = Prioridad.MEDIA;

    public static final String CAMPO_TITULO = "titulo";
    public static final String CAMPO_DESCRIPCION = "descripcion";
    public static final String CAMPO_PRIORIDAD = "prioridad";
    public static final String CAMPO_FECHA = "fechaLimite";

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);

    private final TodoRepository repositorio;
    private final UrgenciaCalculator calculadoraUrgencia;
    private final Clock reloj;

    public TodoService(TodoRepository repositorio, UrgenciaCalculator calculadoraUrgencia, Clock reloj) {
        this.repositorio = repositorio;
        this.calculadoraUrgencia = calculadoraUrgencia;
        this.reloj = reloj;
    }

    /**
     * Agrega un nuevo TODO pendiente. Los parámetros llegan como texto, tal como
     * los ingresa el usuario en el formulario.
     *
     * @param titulo      obligatorio, de 3 a 100 caracteres (sin contar espacios al inicio y al final),
     *                    y no puede repetirse entre los TODOs pendientes (sin distinguir mayúsculas)
     * @param descripcion opcional, máximo 500 caracteres
     * @param prioridad   ALTA, MEDIA o BAJA (sin distinguir mayúsculas); si está vacía se usa MEDIA
     * @param fechaLimite opcional, formato yyyy-MM-dd, debe ser una fecha válida igual o posterior a hoy
     * @return el TODO guardado, con su id
     * @throws ValidacionException si algún dato no cumple las reglas
     */
    public Todo agregar(String titulo, String descripcion, String prioridad, String fechaLimite) {
        String tituloValido = validarTitulo(titulo);
        String descripcionValida = validarDescripcion(descripcion);
        Prioridad prioridadValida = validarPrioridad(prioridad);
        LocalDate fechaValida = validarFecha(fechaLimite);

        Todo todo = new Todo(tituloValido, descripcionValida, prioridadValida, fechaValida);
        todo.setCreadoEn(LocalDateTime.now(reloj));
        return repositorio.guardar(todo);
    }

    /** Lista los TODOs que cumplen el filtro, ordenados por urgencia, fecha límite e id. */
    public List<Todo> listar(FiltroTodo filtro) {
        LocalDate hoy = hoy();
        Comparator<Todo> orden = Comparator
                .comparing((Todo t) -> calculadoraUrgencia.calcular(t, hoy))
                .thenComparing(Todo::getFechaLimite, Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing(Todo::getId);
        return repositorio.listarTodos().stream()
                .filter(t -> switch (filtro) {
                    case PENDIENTES -> !t.isCompletado();
                    case COMPLETADOS -> t.isCompletado();
                    case TODOS -> true;
                })
                .sorted(orden)
                .toList();
    }

    public NivelUrgencia urgenciaDe(Todo todo) {
        return calculadoraUrgencia.calcular(todo, hoy());
    }

    /** Marca el TODO como completado, o como pendiente si ya lo estaba. */
    public Todo alternarCompletado(long id) {
        Todo todo = repositorio.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe el TODO " + id));
        todo.setCompletado(!todo.isCompletado());
        repositorio.actualizar(todo);
        return todo;
    }

    public void eliminar(long id) {
        repositorio.eliminar(id);
    }

    private String validarTitulo(String titulo) {
        if (titulo == null || titulo.isBlank()) {
            throw new ValidacionException(CAMPO_TITULO, "El título es obligatorio");
        }
        String limpio = titulo.strip();
        if (limpio.length() < TITULO_MIN || limpio.length() > TITULO_MAX) {
            throw new ValidacionException(CAMPO_TITULO,
                    "El título debe tener entre " + TITULO_MIN + " y " + TITULO_MAX + " caracteres");
        }
        boolean duplicado = repositorio.listarTodos().stream()
                .anyMatch(t -> !t.isCompletado() && t.getTitulo().equalsIgnoreCase(limpio));
        if (duplicado) {
            throw new ValidacionException(CAMPO_TITULO, "Ya existe un TODO pendiente con ese título");
        }
        return limpio;
    }

    private String validarDescripcion(String descripcion) {
        if (descripcion == null) {
            return "";
        }
        String limpia = descripcion.strip();
        if (limpia.length() > DESCRIPCION_MAX) {
            throw new ValidacionException(CAMPO_DESCRIPCION,
                    "La descripción no puede superar los " + DESCRIPCION_MAX + " caracteres");
        }
        return limpia;
    }

    private Prioridad validarPrioridad(String prioridad) {
        if (prioridad == null || prioridad.isBlank()) {
            return PRIORIDAD_POR_DEFECTO;
        }
        try {
            return Prioridad.valueOf(prioridad.strip().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new ValidacionException(CAMPO_PRIORIDAD, "La prioridad debe ser ALTA, MEDIA o BAJA");
        }
    }

    private LocalDate validarFecha(String fechaLimite) {
        if (fechaLimite == null || fechaLimite.isBlank()) {
            return null;
        }
        LocalDate fecha;
        try {
            fecha = LocalDate.parse(fechaLimite.strip(), FORMATO_FECHA);
        } catch (DateTimeParseException e) {
            throw new ValidacionException(CAMPO_FECHA,
                    "La fecha límite debe ser una fecha válida con formato AAAA-MM-DD");
        }
        if (fecha.isBefore(hoy())) {
            throw new ValidacionException(CAMPO_FECHA, "La fecha límite no puede ser anterior a hoy");
        }
        return fecha;
    }

    private LocalDate hoy() {
        return LocalDate.now(reloj);
    }
}
