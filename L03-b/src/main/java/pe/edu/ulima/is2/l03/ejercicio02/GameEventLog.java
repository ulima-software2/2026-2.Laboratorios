package pe.edu.ulima.is2.l03.ejercicio02;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Bitacora compartida por los subsistemas del motor. Permite verificar en
 * las pruebas, sin parsear la salida de consola, en que orden exacto se
 * invocaron los subsistemas al iniciar o reanudar un nivel.
 */
public class GameEventLog {

    private final List<String> events = new ArrayList<>();

    public void record(String event) {
        events.add(event);
    }

    public List<String> getEvents() {
        return Collections.unmodifiableList(events);
    }
}
