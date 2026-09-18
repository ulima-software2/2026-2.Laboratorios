package pe.edu.ulima.is2.l03.ejercicio01;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StandardControllerTest {

    @Test
    void reportsConfiguredState() {
        StandardController controller = new StandardController();
        controller.setDirection(Direction.LEFT);
        controller.setAttackPressed(true);
        controller.setJumpPressed(false);
        controller.setConnected(true);

        assertEquals(Direction.LEFT, controller.getDirection());
        assertTrue(controller.isAttackPressed());
        assertFalse(controller.isJumpPressed());
        assertTrue(controller.isReady());
    }

    @Test
    void defaultsToNoInputAndReady() {
        StandardController controller = new StandardController();

        assertEquals(Direction.NONE, controller.getDirection());
        assertFalse(controller.isAttackPressed());
        assertFalse(controller.isJumpPressed());
        assertTrue(controller.isReady());
    }
}
