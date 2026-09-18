package pe.edu.ulima.is2.l03.ejercicio02;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Estas pruebas describen el comportamiento observable de los power-ups
 * que debe conservarse, sin importar si por debajo se usan banderas
 * booleanas (version actual) o el patron Decorator (version que vas a
 * construir).
 */
class PlayerPowerUpTest {

    @Test
    void baseStatsWithoutPowerUps() {
        Player player = new Player("Hero", 10, 5, 2);

        assertEquals(10, player.getAttackPower());
        assertEquals(5, player.getSpeed());
        assertEquals(2, player.getDefense());
    }

    @Test
    void doubleDamageDoublesAttackPower() {
        Player player = new Player("Hero", 10, 5, 2);
        player.activateDoubleDamage();

        assertEquals(20, player.getAttackPower());
    }

    @Test
    void doubleDamageAndFireTrailStackOnAttackPower() {
        Player player = new Player("Hero", 10, 5, 2);
        player.activateDoubleDamage();
        player.activateFireTrail();

        assertEquals(25, player.getAttackPower());
    }

    @Test
    void speedBoostIncreasesSpeed() {
        Player player = new Player("Hero", 10, 5, 2);
        player.activateSpeedBoost();

        assertEquals(9, player.getSpeed());
    }

    @Test
    void shieldIncreasesDefense() {
        Player player = new Player("Golem", 6, 2, 4);
        player.activateShield();

        assertEquals(14, player.getDefense());
    }

    @Test
    void describeMentionsActivePowerUps() {
        Player player = new Player("Hero", 10, 5, 2);
        player.activateFireTrail();

        String description = player.describe();

        assertTrue(description.contains("Hero"));
        assertTrue(description.contains("+FireTrail"));
    }
}
