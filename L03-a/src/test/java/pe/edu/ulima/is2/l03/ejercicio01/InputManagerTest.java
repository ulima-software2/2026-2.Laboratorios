package pe.edu.ulima.is2.l03.ejercicio01;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Estas pruebas describen el comportamiento observable que InputManager
 * debe conservar, sin importar si por debajo usa "instanceof" (version
 * actual) o un Adapter (version que vas a construir).
 *
 * Cuando termines tu refactor probablemente cambiaras registerDevice(Object)
 * por registerDevice(GameController). Si eso pasa, la linea que registra el
 * joystick legacy en cada prueba dejara de compilar: reemplazala por
 * registerDevice(new TuAdapter(joystick)). Las aserciones (el resultado
 * esperado) no deberian cambiar: eso es justamente lo que demuestra que el
 * Adapter preserva el comportamiento.
 */
class InputManagerTest {

    @Test
    void standardControllerMovesAndActsOnCharacter() {
        InputManager inputManager = new InputManager();
        StandardController controller = new StandardController();
        controller.setDirection(Direction.RIGHT);
        controller.setJumpPressed(true);
        inputManager.registerDevice(controller);

        PlayerCharacter character = new PlayerCharacter("Hero");
        inputManager.processFrame(character);

        assertEquals(List.of("MOVE_RIGHT", "JUMP"), character.getActionHistory());
    }

    @Test
    void legacyJoystickMovesAndAttacksOnCharacter() {
        InputManager inputManager = new InputManager();
        LegacyJoystickSDK joystick = new LegacyJoystickSDK();
        joystick.setAxisY(-1);
        joystick.pressButton("FIRE");
        inputManager.registerDevice(joystick);

        PlayerCharacter character = new PlayerCharacter("Hero");
        inputManager.processFrame(character);

        assertEquals(List.of("MOVE_UP", "ATTACK"), character.getActionHistory());
    }

    @Test
    void disconnectedLegacyJoystickIsIgnored() {
        InputManager inputManager = new InputManager();
        LegacyJoystickSDK joystick = new LegacyJoystickSDK();
        joystick.setStatus("OFFLINE");
        joystick.setAxisY(-1);
        inputManager.registerDevice(joystick);

        PlayerCharacter character = new PlayerCharacter("Hero");
        inputManager.processFrame(character);

        assertTrue(character.getActionHistory().isEmpty());
    }

    @Test
    void bothDeviceTypesCanBeProcessedTogether() {
        InputManager inputManager = new InputManager();
        StandardController pad = new StandardController();
        pad.setAttackPressed(true);
        LegacyJoystickSDK joystick = new LegacyJoystickSDK();
        joystick.setAxisX(1);

        inputManager.registerDevice(pad);
        inputManager.registerDevice(joystick);

        PlayerCharacter character = new PlayerCharacter("Hero");
        inputManager.processFrame(character);

        assertEquals(List.of("ATTACK", "MOVE_RIGHT"), character.getActionHistory());
    }
}
