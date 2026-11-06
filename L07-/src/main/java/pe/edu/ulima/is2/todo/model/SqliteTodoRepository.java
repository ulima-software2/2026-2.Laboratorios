package pe.edu.ulima.is2.todo.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio de TODOs sobre SQLite. Mantiene una única conexión abierta,
 * lo que permite usar también una base de datos en memoria (útil para pruebas).
 */
public class SqliteTodoRepository implements TodoRepository, AutoCloseable {

    private static final String ESQUEMA = """
            CREATE TABLE IF NOT EXISTS todos (
                id           INTEGER PRIMARY KEY AUTOINCREMENT,
                titulo       TEXT    NOT NULL,
                descripcion  TEXT    NOT NULL DEFAULT '',
                prioridad    TEXT    NOT NULL,
                fecha_limite TEXT,
                completado   INTEGER NOT NULL DEFAULT 0,
                creado_en    TEXT    NOT NULL
            )
            """;

    private final Connection conexion;

    /**
     * @param rutaArchivo ruta del archivo .db, o ":memory:" para una base en memoria
     */
    public SqliteTodoRepository(String rutaArchivo) {
        try {
            conexion = DriverManager.getConnection("jdbc:sqlite:" + rutaArchivo);
            try (Statement st = conexion.createStatement()) {
                st.execute(ESQUEMA);
            }
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo abrir la base de datos " + rutaArchivo, e);
        }
    }

    public static SqliteTodoRepository enMemoria() {
        return new SqliteTodoRepository(":memory:");
    }

    @Override
    public Todo guardar(Todo todo) {
        String sql = "INSERT INTO todos (titulo, descripcion, prioridad, fecha_limite, completado, creado_en) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (todo.getCreadoEn() == null) {
                todo.setCreadoEn(LocalDateTime.now());
            }
            asignarParametros(ps, todo);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    todo.setId(rs.getLong(1));
                }
            }
            return todo;
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo guardar el TODO", e);
        }
    }

    @Override
    public void actualizar(Todo todo) {
        String sql = "UPDATE todos SET titulo = ?, descripcion = ?, prioridad = ?, fecha_limite = ?, "
                + "completado = ?, creado_en = ? WHERE id = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            asignarParametros(ps, todo);
            ps.setLong(7, todo.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo actualizar el TODO " + todo.getId(), e);
        }
    }

    @Override
    public void eliminar(long id) {
        try (PreparedStatement ps = conexion.prepareStatement("DELETE FROM todos WHERE id = ?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo eliminar el TODO " + id, e);
        }
    }

    @Override
    public Optional<Todo> buscarPorId(long id) {
        try (PreparedStatement ps = conexion.prepareStatement("SELECT * FROM todos WHERE id = ?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo buscar el TODO " + id, e);
        }
    }

    @Override
    public List<Todo> listarTodos() {
        List<Todo> todos = new ArrayList<>();
        try (Statement st = conexion.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM todos ORDER BY id")) {
            while (rs.next()) {
                todos.add(mapear(rs));
            }
            return todos;
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo listar los TODOs", e);
        }
    }

    @Override
    public void close() {
        try {
            conexion.close();
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo cerrar la base de datos", e);
        }
    }

    private void asignarParametros(PreparedStatement ps, Todo todo) throws SQLException {
        ps.setString(1, todo.getTitulo());
        ps.setString(2, todo.getDescripcion() == null ? "" : todo.getDescripcion());
        ps.setString(3, todo.getPrioridad().name());
        ps.setString(4, todo.getFechaLimite() == null ? null : todo.getFechaLimite().toString());
        ps.setInt(5, todo.isCompletado() ? 1 : 0);
        ps.setString(6, todo.getCreadoEn().toString());
    }

    private Todo mapear(ResultSet rs) throws SQLException {
        Todo todo = new Todo();
        todo.setId(rs.getLong("id"));
        todo.setTitulo(rs.getString("titulo"));
        todo.setDescripcion(rs.getString("descripcion"));
        todo.setPrioridad(Prioridad.valueOf(rs.getString("prioridad")));
        String fecha = rs.getString("fecha_limite");
        todo.setFechaLimite(fecha == null ? null : LocalDate.parse(fecha));
        todo.setCompletado(rs.getInt("completado") == 1);
        todo.setCreadoEn(LocalDateTime.parse(rs.getString("creado_en")));
        return todo;
    }
}
