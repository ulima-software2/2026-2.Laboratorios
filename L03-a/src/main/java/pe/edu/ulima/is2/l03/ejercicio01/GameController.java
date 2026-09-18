package pe.edu.ulima.is2.l03.ejercicio01;

/**
 * Contrato que el motor del juego espera de cualquier dispositivo de
 * entrada. Es el "Target" del patron Adapter.
 */
public interface GameController {

    Direction getDirection();

    boolean isAttackPressed();

    boolean isJumpPressed();

    boolean isReady();
}
