package pe.edu.ulima.is2.l03.ejercicio01;

import java.util.ArrayList;
import java.util.List;

/**
 * Motor de entrada del juego.
 *
 * CODIGO A MEJORAR (aplicar patron Adapter)
 * ------------------------------------------
 * processFrame() necesita soportar tanto GameController (dispositivos ya
 * compatibles) como LegacyJoystickSDK (dispositivo de un fabricante externo
 * con una interfaz incompatible). En lugar de adaptar LegacyJoystickSDK a
 * GameController, este metodo usa "instanceof" y traduce manualmente los
 * datos del joystick legacy cada vez que se procesa un frame.
 *
 * Problemas de este diseno:
 *  - Viola OCP: agregar un nuevo dispositivo incompatible (por ejemplo
 *    RetroGamepadDriver) obliga a modificar este metodo otra vez.
 *  - Viola DRY: la logica de traduccion de ejes/botones del joystick legacy
 *    vive aqui, mezclada con la logica del bucle de juego.
 *  - InputManager conoce detalles internos de LegacyJoystickSDK que no le
 *    incumben (alto acoplamiento, baja cohesion).
 *
 * Ver README.md para la tarea completa.
 */
public class InputManager {

    private final List<Object> devices = new ArrayList<>();

    public void registerDevice(Object device) {
        devices.add(device);
    }

    public void processFrame(PlayerCharacter character) {
        for (Object device : devices) {
            if (device instanceof GameController controller) {
                applyStandardController(controller, character);
            } else if (device instanceof LegacyJoystickSDK legacyJoystick) {
                applyLegacyJoystick(legacyJoystick, character);
            } else {
                throw new IllegalArgumentException(
                        "Dispositivo no soportado: " + device.getClass());
            }
        }
    }

    private void applyStandardController(GameController controller, PlayerCharacter character) {
        if (!controller.isReady()) {
            return;
        }
        moveAccordingTo(controller.getDirection(), character);
        if (controller.isAttackPressed()) {
            character.attack();
        }
        if (controller.isJumpPressed()) {
            character.jump();
        }
    }

    // Traduccion manual duplicada: esto es exactamente lo que deberia vivir
    // dentro de un Adapter, no aqui.
    private void applyLegacyJoystick(LegacyJoystickSDK legacyJoystick, PlayerCharacter character) {
        if (!"OK".equals(legacyJoystick.getStatus())) {
            return;
        }

        int axisX = legacyJoystick.getAxisX();
        int axisY = legacyJoystick.getAxisY();

        if (axisY < 0) {
            character.moveUp();
        } else if (axisY > 0) {
            character.moveDown();
        } else if (axisX < 0) {
            character.moveLeft();
        } else if (axisX > 0) {
            character.moveRight();
        }

        if (legacyJoystick.checkButton("FIRE")) {
            character.attack();
        }
        if (legacyJoystick.checkButton("JUMP")) {
            character.jump();
        }
    }

    private void moveAccordingTo(Direction direction, PlayerCharacter character) {
        switch (direction) {
            case UP -> character.moveUp();
            case DOWN -> character.moveDown();
            case LEFT -> character.moveLeft();
            case RIGHT -> character.moveRight();
            case NONE -> { /* sin movimiento */ }
        }
    }
}
