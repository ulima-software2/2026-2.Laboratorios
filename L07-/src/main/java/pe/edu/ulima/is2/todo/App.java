package pe.edu.ulima.is2.todo;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.DefaultWindowManager;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import pe.edu.ulima.is2.todo.model.SqliteTodoRepository;
import pe.edu.ulima.is2.todo.model.TodoService;
import pe.edu.ulima.is2.todo.model.UrgenciaCalculator;
import pe.edu.ulima.is2.todo.view.TodoListView;
import pe.edu.ulima.is2.todo.viewmodel.TodoListViewModel;

import java.io.IOException;
import java.time.Clock;

/**
 * Punto de entrada: arma las capas Model - ViewModel - View y arranca la TUI.
 * Uso: java -jar todo-tui.jar [ruta-base-de-datos]   (por defecto: todos.db)
 */
public class App {

    public static void main(String[] args) throws IOException {
        String rutaBaseDatos = args.length > 0 ? args[0] : "todos.db";

        try (SqliteTodoRepository repositorio = new SqliteTodoRepository(rutaBaseDatos)) {
            TodoService servicio = new TodoService(repositorio, new UrgenciaCalculator(), Clock.systemDefaultZone());
            TodoListViewModel viewModel = new TodoListViewModel(servicio);

            Screen pantalla = new DefaultTerminalFactory().createScreen();
            pantalla.startScreen();
            try {
                MultiWindowTextGUI gui = new MultiWindowTextGUI(
                        pantalla, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLUE));
                new TodoListView(gui, viewModel).mostrar();
            } finally {
                pantalla.stopScreen();
            }
        }
    }
}
