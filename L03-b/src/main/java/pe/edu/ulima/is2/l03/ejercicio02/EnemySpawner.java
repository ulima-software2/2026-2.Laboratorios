package pe.edu.ulima.is2.l03.ejercicio02;

/** Subsistema que controla la aparicion de enemigos en el nivel. */
public class EnemySpawner {

    private final GameEventLog log;

    public EnemySpawner(GameEventLog log) {
        this.log = log;
    }

    public void spawnWave(String levelId, int waveNumber) {
        log.record("ENEMIES_SPAWN:" + levelId + ":" + waveNumber);
        System.out.println("[Enemies] Generando oleada " + waveNumber + " de " + levelId);
    }

    public void clearEnemies() {
        log.record("ENEMIES_CLEAR");
        System.out.println("[Enemies] Eliminando enemigos restantes");
    }
}
