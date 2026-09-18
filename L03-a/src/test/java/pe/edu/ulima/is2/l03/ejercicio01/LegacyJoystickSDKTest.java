package pe.edu.ulima.is2.l03.ejercicio01;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LegacyJoystickSDKTest {

    @Test
    void tracksAxesAndButtonsIndependently() {
        LegacyJoystickSDK joystick = new LegacyJoystickSDK();
        joystick.setAxisX(1);
        joystick.setAxisY(-1);
        joystick.pressButton("FIRE");

        assertEquals(1, joystick.getAxisX());
        assertEquals(-1, joystick.getAxisY());
        assertTrue(joystick.checkButton("FIRE"));
        assertFalse(joystick.checkButton("JUMP"));
    }

    @Test
    void defaultsToOnlineStatus() {
        LegacyJoystickSDK joystick = new LegacyJoystickSDK();
        assertEquals("OK", joystick.getStatus());

        joystick.setStatus("OFFLINE");
        assertEquals("OFFLINE", joystick.getStatus());
    }
}
