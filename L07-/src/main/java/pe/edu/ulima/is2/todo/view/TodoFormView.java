package pe.edu.ulima.is2.todo.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.ComboBox;
import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import pe.edu.ulima.is2.todo.viewmodel.Propiedad;
import pe.edu.ulima.is2.todo.viewmodel.TodoFormViewModel;

import java.util.Set;

/** Formulario modal para agregar un TODO, enlazado a un {@link TodoFormViewModel}. */
public class TodoFormView {

    private final WindowBasedTextGUI gui;
    private final TodoFormViewModel viewModel;
    private final BasicWindow ventana = new BasicWindow("Nuevo TODO");

    public TodoFormView(WindowBasedTextGUI gui, TodoFormViewModel viewModel) {
        this.gui = gui;
        this.viewModel = viewModel;
        construir();
    }

    public void mostrar() {
        gui.addWindowAndWait(ventana);
    }

    private void construir() {
        ventana.setHints(Set.of(Window.Hint.CENTERED, Window.Hint.MODAL));

        Panel campos = new Panel(new GridLayout(2));
        campos.addComponent(new Label("Título*"));
        campos.addComponent(cajaDeTexto(viewModel.titulo(), new TerminalSize(50, 1)));
        campos.addComponent(new Label("Descripción"));
        campos.addComponent(cajaDeTexto(viewModel.descripcion(), new TerminalSize(50, 3)));

        campos.addComponent(new Label("Prioridad"));
        ComboBox<String> prioridad = new ComboBox<>(viewModel.opcionesPrioridad());
        prioridad.setSelectedItem(viewModel.prioridad().get());
        prioridad.addListener((indice, anterior, porUsuario) -> viewModel.prioridad().set(prioridad.getSelectedItem()));
        campos.addComponent(prioridad);

        campos.addComponent(new Label("Fecha límite"));
        campos.addComponent(cajaDeTexto(viewModel.fechaLimite(), new TerminalSize(12, 1)));
        campos.addComponent(new EmptySpace());
        campos.addComponent(new Label("(AAAA-MM-DD, opcional)"));

        Label error = new Label("");
        error.setForegroundColor(TextColor.ANSI.RED_BRIGHT);
        viewModel.error().observar(error::setText);

        Panel botones = new Panel(new LinearLayout(Direction.HORIZONTAL));
        botones.addComponent(new Button("Guardar", () -> {
            if (viewModel.guardar()) {
                ventana.close();
            }
        }));
        botones.addComponent(new Button("Cancelar", ventana::close));

        Panel principal = new Panel(new LinearLayout(Direction.VERTICAL));
        principal.addComponent(campos);
        principal.addComponent(error);
        principal.addComponent(botones);
        ventana.setComponent(principal);
    }

    /** Crea un TextBox cuyo contenido se copia a la propiedad del ViewModel en cada cambio. */
    private TextBox cajaDeTexto(Propiedad<String> propiedad, TerminalSize tamanio) {
        TextBox caja = new TextBox(tamanio, propiedad.get());
        caja.setTextChangeListener((texto, porUsuario) -> propiedad.set(texto));
        return caja;
    }
}
