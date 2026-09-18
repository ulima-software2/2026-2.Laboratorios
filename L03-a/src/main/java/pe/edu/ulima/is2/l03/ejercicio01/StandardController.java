package pe.edu.ulima.is2.l03.ejercicio01;

/**
 * Control moderno, disenado desde el inicio para el motor del juego.
 * Implementa GameController directamente: no necesita adaptacion.
 */
public class StandardController implements GameController {

    private Direction direction = Direction.NONE;
    private boolean attackPressed;
    private boolean jumpPressed;
    private boolean connected = true;

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setAttackPressed(boolean attackPressed) {
        this.attackPressed = attackPressed;
    }

    public void setJumpPressed(boolean jumpPressed) {
        this.jumpPressed = jumpPressed;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    @Override
    public boolean isAttackPressed() {
        return attackPressed;
    }

    @Override
    public boolean isJumpPressed() {
        return jumpPressed;
    }

    @Override
    public boolean isReady() {
        return connected;
    }
}
