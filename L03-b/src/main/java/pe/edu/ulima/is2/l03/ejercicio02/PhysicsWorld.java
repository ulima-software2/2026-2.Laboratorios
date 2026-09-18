package pe.edu.ulima.is2.l03.ejercicio02;

/** Subsistema de fisica del motor. */
public class PhysicsWorld {

    private final GameEventLog log;

    public PhysicsWorld(GameEventLog log) {
        this.log = log;
    }

    public void initialize(double gravity) {
        log.record("PHYSICS_INIT:" + gravity);
        System.out.println("[Physics] Inicializando mundo fisico (g=" + gravity + ")");
    }

    public void loadColliders(String levelId) {
        log.record("PHYSICS_COLLIDERS:" + levelId);
        System.out.println("[Physics] Cargando colisionadores de " + levelId);
    }

    public void reset() {
        log.record("PHYSICS_RESET");
        System.out.println("[Physics] Reiniciando mundo fisico");
    }
}
