package pe.edu.ulima.is2.todo.viewmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Valor observable: la vista se suscribe y se actualiza cuando el ViewModel lo cambia.
 * Es el mecanismo de "data binding" del patrón MVVM en esta aplicación.
 */
public class Propiedad<T> {

    private T valor;
    private final List<Consumer<T>> observadores = new ArrayList<>();

    public Propiedad(T valorInicial) {
        this.valor = valorInicial;
    }

    public T get() {
        return valor;
    }

    public void set(T nuevoValor) {
        if (Objects.equals(valor, nuevoValor)) {
            return;
        }
        valor = nuevoValor;
        observadores.forEach(o -> o.accept(nuevoValor));
    }

    /** Registra un observador y lo invoca de inmediato con el valor actual. */
    public void observar(Consumer<T> observador) {
        observadores.add(observador);
        observador.accept(valor);
    }
}
