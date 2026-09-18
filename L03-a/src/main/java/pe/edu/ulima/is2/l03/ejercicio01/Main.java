package pe.edu.ulima.is2.l03.ejercicio01;

/**
 * Demo del sistema de entrada. Simula un par de frames de juego usando un
 * control moderno y un joystick legacy al mismo tiempo.
 */
public class Main {

    public static void main(String[] args) {
        PlayerCharacter hero = new PlayerCharacter("Hero");

        StandardController pad = new StandardController();
        pad.setDirection(Direction.RIGHT);
        pad.setJumpPressed(true);

        LegacyJoystickSDK retroJoystick = new LegacyJoystickSDK();
        retroJoystick.setAxisY(-1); // arriba
        retroJoystick.pressButton("FIRE");

        InputManager inputManager = new InputManager();
        inputManager.registerDevice(pad);
        inputManager.registerDevice(retroJoystick);

        System.out.println("=== Frame 1 ===");
        inputManager.processFrame(hero);

        pad.setDirection(Direction.NONE);
        pad.setJumpPressed(false);
        retroJoystick.setAxisY(0);
        retroJoystick.releaseButton("FIRE");
        retroJoystick.pressButton("JUMP");

        System.out.println("=== Frame 2 ===");
        inputManager.processFrame(hero);

        System.out.println("Historial completo: " + hero.getActionHistory());
    }
}
