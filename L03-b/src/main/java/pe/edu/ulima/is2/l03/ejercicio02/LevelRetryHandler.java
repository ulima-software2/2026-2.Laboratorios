package pe.edu.ulima.is2.l03.ejercicio02;

/**
 * Maneja el boton "Reintentar nivel" del menu de pausa. Necesita repetir,
 * a mano y en el orden correcto, la misma secuencia de subsistemas que
 * {@link Main} ya usa para iniciar y para reanudar un nivel.
 */
public class LevelRetryHandler {

    private final GraphicsRenderer renderer;
    private final PhysicsWorld physics;
    private final AudioEngine audio;
    private final AssetLoader assets;
    private final EnemySpawner spawner;

    public LevelRetryHandler(GameEventLog log) {
        this.renderer = new GraphicsRenderer(log);
        this.physics = new PhysicsWorld(log);
        this.audio = new AudioEngine(log);
        this.assets = new AssetLoader(log);
        this.spawner = new EnemySpawner(log);
    }

    public void retryLevel(String levelId) {
        renderer.clearScene();
        physics.reset();
        spawner.clearEnemies();
        audio.stopMusic();

        assets.loadLevelDefinition(levelId);
        assets.loadSpritesFor(levelId);
        renderer.loadTextureAtlas(levelId);
        physics.loadColliders(levelId);
        audio.loadTrack(levelId + "-theme.ogg");
        audio.playMusic(levelId + "-theme.ogg");
        spawner.spawnWave(levelId, 1);

        System.out.println(levelId + " reiniciado.\n");
    }
}
