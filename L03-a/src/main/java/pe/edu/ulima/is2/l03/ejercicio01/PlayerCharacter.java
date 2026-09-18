package pe.edu.ulima.is2.l03.ejercicio01;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Personaje controlado por el jugador. Reacciona a las ordenes que recibe
 * desde el/los dispositivo(s) de entrada.
 */
public class PlayerCharacter {

    private final String name;
    private int x;
    private int y;
    private final List<String> actionHistory = new ArrayList<>();

    public PlayerCharacter(String name) {
        this.name = name;
    }

    public void moveUp() {
        y--;
        log("MOVE_UP");
    }

    public void moveDown() {
        y++;
        log("MOVE_DOWN");
    }

    public void moveLeft() {
        x--;
        log("MOVE_LEFT");
    }

    public void moveRight() {
        x++;
        log("MOVE_RIGHT");
    }

    public void attack() {
        log("ATTACK");
    }

    public void jump() {
        log("JUMP");
    }

    private void log(String action) {
        actionHistory.add(action);
        System.out.printf("[%s] %s (x=%d, y=%d)%n", name, action, x, y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public List<String> getActionHistory() {
        return Collections.unmodifiableList(actionHistory);
    }
}
