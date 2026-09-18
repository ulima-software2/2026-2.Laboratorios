package pe.edu.ulima.is2.l03.ejercicio01;

/**
 * Driver de un segundo fabricante, usado en el reto de extensibilidad.
 *
 * IMPORTANTE: tambien es codigo de terceros, no se puede modificar.
 * Representa la direccion en coordenadas polares (angulo + magnitud) en
 * lugar de un enum Direction, y el estado de conexion como un entero
 * (1 = conectado, 0 = desconectado).
 */
public class RetroGamepadDriver {

    private double angleDegrees;
    private double magnitude;
    private final boolean[] buttonsHeld = new boolean[2]; // 0 = fuego, 1 = salto
    private int connectionState = 1;

    public void setStickPosition(double angleDegrees, double magnitude) {
        this.angleDegrees = angleDegrees;
        this.magnitude = magnitude;
    }

    public void holdButton(int buttonIndex, boolean held) {
        buttonsHeld[buttonIndex] = held;
    }

    public void setConnectionState(int connectionState) {
        this.connectionState = connectionState;
    }

    public double getAngleDegrees() {
        return angleDegrees;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public boolean isButtonHeld(int buttonIndex) {
        return buttonsHeld[buttonIndex];
    }

    public int getConnectionState() {
        return connectionState;
    }
}
