package pe.edu.ulima.is2.l03.ejercicio01;

import java.util.HashSet;
import java.util.Set;

/**
 * SDK de un joystick retro provisto por un fabricante externo.
 *
 * IMPORTANTE: este es codigo de terceros (piensa en el como una libreria
 * .jar que no puedes modificar). Su interfaz es incompatible con
 * GameController: usa ejes numericos en vez de Direction, codigos de boton
 * en String, y el estado de conexion como String en vez de boolean.
 */
public class LegacyJoystickSDK {

    private int axisX; // -1 = izquierda, 0 = centro, 1 = derecha
    private int axisY; // -1 = arriba,    0 = centro, 1 = abajo
    private final Set<String> pressedButtons = new HashSet<>();
    private String status = "OK"; // "OK" o "OFFLINE"

    public void setAxisX(int axisX) {
        this.axisX = axisX;
    }

    public void setAxisY(int axisY) {
        this.axisY = axisY;
    }

    public void pressButton(String buttonCode) {
        pressedButtons.add(buttonCode);
    }

    public void releaseButton(String buttonCode) {
        pressedButtons.remove(buttonCode);
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAxisX() {
        return axisX;
    }

    public int getAxisY() {
        return axisY;
    }

    public boolean checkButton(String buttonCode) {
        return pressedButtons.contains(buttonCode);
    }

    public String getStatus() {
        return status;
    }
}
