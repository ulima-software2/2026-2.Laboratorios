package pe.edu.ulima.is2.l03.ejercicio02;

/**
 * Demo por consola. Simula: iniciar el nivel 1 desde cero, reanudar desde
 * un checkpoint guardado, reintentar el nivel actual, y un combate simple
 * entre dos jugadores usando power-ups.
 */
public class Main {

    public static void main(String[] args) {
        runLevelStartupDemo();
        runPowerUpDemo();
    }

    private static void runLevelStartupDemo() {
        GameEventLog log = new GameEventLog();

        AudioEngine audio = new AudioEngine(log);
        GraphicsRenderer renderer = new GraphicsRenderer(log);
        PhysicsWorld physics = new PhysicsWorld(log);
        AssetLoader assets = new AssetLoader(log);
        EnemySpawner spawner = new EnemySpawner(log);
        SaveGameService saveService = new SaveGameService(log);

        // --- Iniciar el nivel 1 desde cero ---
        renderer.createContext(1280, 720);
        audio.initialize();
        physics.initialize(9.8);

        renderer.clearScene();
        physics.reset();
        spawner.clearEnemies();
        audio.stopMusic();

        assets.loadLevelDefinition("level-1");
        assets.loadSpritesFor("level-1");
        renderer.loadTextureAtlas("level-1");
        physics.loadColliders("level-1");
        audio.loadTrack("level-1-theme.ogg");
        audio.playMusic("level-1-theme.ogg");
        spawner.spawnWave("level-1", 1);

        System.out.println("Nivel 1 iniciado.\n");

        // --- Reanudar desde un checkpoint guardado (misma secuencia, a mano otra vez) ---
        String checkpointLevel = saveService.loadCheckpoint("checkpoint-42");

        renderer.clearScene();
        physics.reset();
        spawner.clearEnemies();
        audio.stopMusic();

        assets.loadLevelDefinition(checkpointLevel);
        assets.loadSpritesFor(checkpointLevel);
        renderer.loadTextureAtlas(checkpointLevel);
        physics.loadColliders(checkpointLevel);
        audio.loadTrack(checkpointLevel + "-theme.ogg");
        audio.playMusic(checkpointLevel + "-theme.ogg");
        spawner.spawnWave(checkpointLevel, 1);

        System.out.println("Nivel reanudado desde checkpoint.\n");

        // --- El jugador muere y pulsa "Reintentar nivel" (tercera copia de la secuencia) ---
        LevelRetryHandler retryHandler = new LevelRetryHandler(log);
        retryHandler.retryLevel(checkpointLevel);
    }

    private static void runPowerUpDemo() {
        Player hero = new Player("Hero", 10, 5, 2);
        Player golem = new Player("Golem", 6, 2, 4);

        hero.activateDoubleDamage();
        hero.activateFireTrail();
        golem.activateShield();

        System.out.println(hero.describe());
        System.out.println(golem.describe());

        CollisionHandler collisionHandler = new CollisionHandler();
        System.out.println(collisionHandler.resolveMeleeHit(hero, golem, hero.getAttackPower()));
    }
}
