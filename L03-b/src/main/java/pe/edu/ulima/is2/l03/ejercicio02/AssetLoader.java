package pe.edu.ulima.is2.l03.ejercicio02;

/** Subsistema de carga de datos y sprites del nivel. */
public class AssetLoader {

    private final GameEventLog log;

    public AssetLoader(GameEventLog log) {
        this.log = log;
    }

    public void loadLevelDefinition(String levelId) {
        log.record("ASSETS_LEVEL:" + levelId);
        System.out.println("[Assets] Cargando definicion de " + levelId);
    }

    public void loadSpritesFor(String levelId) {
        log.record("ASSETS_SPRITES:" + levelId);
        System.out.println("[Assets] Cargando sprites de " + levelId);
    }
}
