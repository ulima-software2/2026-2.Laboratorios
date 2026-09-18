package pe.edu.ulima.is2.l03.ejercicio02;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Describe el orden exacto en que deben invocarse los subsistemas al
 * (re)iniciar un nivel. Este orden debe conservarse cuando la orquestacion
 * se mueva detras de un Facade: solo cambia quien la ejecuta, no la
 * secuencia observable.
 */
class LevelRetryHandlerTest {

    @Test
    void retryLevelRunsSubsystemsInOrder() {
        GameEventLog log = new GameEventLog();
        LevelRetryHandler retryHandler = new LevelRetryHandler(log);

        retryHandler.retryLevel("level-2");

        assertEquals(List.of(
                "GFX_CLEAR",
                "PHYSICS_RESET",
                "ENEMIES_CLEAR",
                "AUDIO_STOP",
                "ASSETS_LEVEL:level-2",
                "ASSETS_SPRITES:level-2",
                "GFX_LOAD:level-2",
                "PHYSICS_COLLIDERS:level-2",
                "AUDIO_LOAD:level-2-theme.ogg",
                "AUDIO_PLAY:level-2-theme.ogg",
                "ENEMIES_SPAWN:level-2:1"
        ), log.getEvents());
    }
}
