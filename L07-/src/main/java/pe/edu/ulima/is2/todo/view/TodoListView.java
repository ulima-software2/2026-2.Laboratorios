package pe.edu.ulima.is2.todo.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Borders;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.gui2.WindowListenerAdapter;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.gui2.table.Table;
import com.googlecode.lanterna.gui2.table.TableModel;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import pe.edu.ulima.is2.todo.viewmodel.TodoItemViewModel;
import pe.edu.ulima.is2.todo.viewmodel.TodoListViewModel;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Vista principal. Solo dibuja componentes, se enlaza a las propiedades del
 * ViewModel y le delega cada acción del usuario; no contiene lógica de negocio.
 */
public class TodoListView {

    private static final String[] COLUMNAS = {"Hecho", "Título", "Prioridad", "Fecha límite", "Urgencia"};

    private final WindowBasedTextGUI gui;
    private final TodoListViewModel viewModel;
    private final BasicWindow ventana = new BasicWindow("Gestor de TODOs");
    private final Table<String> tabla = new Table<>(COLUMNAS);
    private final Label etiquetaFiltro = new Label("");
    private final Label etiquetaMensaje = new Label("");

    public TodoListView(WindowBasedTextGUI gui, TodoListViewModel viewModel) {
        this.gui = gui;
        this.viewModel = viewModel;
        construir();
        enlazar();
    }

    /** Muestra la ventana y bloquea hasta que el usuario sale. */
    public void mostrar() {
        viewModel.cargar();
        gui.addWindowAndWait(ventana);
    }

    private void construir() {
        ventana.setHints(Set.of(Window.Hint.FULL_SCREEN));

        tabla.setPreferredSize(new TerminalSize(80, 15));
        tabla.setSelectAction(this::alternarCompletado);

        Panel botones = new Panel(new LinearLayout(Direction.HORIZONTAL));
        botones.addComponent(new Button("Agregar (a)", this::agregar));
        botones.addComponent(new Button("Completar (c)", this::alternarCompletado));
        botones.addComponent(new Button("Eliminar (e)", this::eliminar));
        botones.addComponent(new Button("Filtro (f)", viewModel::siguienteFiltro));
        botones.addComponent(new Button("Salir (q)", ventana::close));

        etiquetaMensaje.setForegroundColor(TextColor.ANSI.YELLOW_BRIGHT);

        Panel principal = new Panel(new LinearLayout(Direction.VERTICAL));
        principal.addComponent(etiquetaFiltro);
        principal.addComponent(tabla.withBorder(Borders.singleLine()));
        principal.addComponent(botones);
        principal.addComponent(etiquetaMensaje);
        ventana.setComponent(principal);

        ventana.addWindowListener(new WindowListenerAdapter() {
            @Override
            public void onUnhandledInput(Window basePane, KeyStroke tecla, AtomicBoolean manejado) {
                if (tecla.getKeyType() != KeyType.Character) {
                    return;
                }
                manejado.set(true);
                switch (Character.toLowerCase(tecla.getCharacter())) {
                    case 'a' -> agregar();
                    case 'c' -> alternarCompletado();
                    case 'e' -> eliminar();
                    case 'f' -> viewModel.siguienteFiltro();
                    case 'q' -> ventana.close();
                    default -> manejado.set(false);
                }
            }
        });
    }

    private void enlazar() {
        viewModel.items().observar(this::mostrarItems);
        viewModel.filtro().observar(f -> etiquetaFiltro.setText(
                "Filtro: " + f.name() + "   |   Enter/c: completar   a: agregar   e: eliminar   f: filtro   q: salir"));
        viewModel.mensaje().observar(etiquetaMensaje::setText);
    }

    private void mostrarItems(List<TodoItemViewModel> items) {
        int seleccionAnterior = tabla.getSelectedRow();
        TableModel<String> modelo = new TableModel<>(COLUMNAS);
        for (TodoItemViewModel item : items) {
            modelo.addRow(item.estado(), item.titulo(), item.prioridad(), item.fechaLimite(), item.urgencia());
        }
        tabla.setTableModel(modelo);
        if (!items.isEmpty()) {
            tabla.setSelectedRow(Math.max(0, Math.min(seleccionAnterior, items.size() - 1)));
        }
    }

    private int filaSeleccionada() {
        return viewModel.items().get().isEmpty() ? -1 : tabla.getSelectedRow();
    }

    private void agregar() {
        new TodoFormView(gui, viewModel.nuevoFormulario()).mostrar();
    }

    private void alternarCompletado() {
        viewModel.alternarCompletado(filaSeleccionada());
    }

    private void eliminar() {
        TodoItemViewModel item = viewModel.itemEn(filaSeleccionada());
        if (item == null) {
            return;
        }
        MessageDialogButton respuesta = MessageDialog.showMessageDialog(gui, "Eliminar",
                "¿Eliminar \"" + item.titulo() + "\"?", MessageDialogButton.Yes, MessageDialogButton.No);
        if (respuesta == MessageDialogButton.Yes) {
            viewModel.eliminar(filaSeleccionada());
        }
    }
}
