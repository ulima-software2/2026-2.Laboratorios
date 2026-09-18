package pe.edu.ulima.is2.l03.ejercicio02;

/** Subsistema grafico del motor. */
public class GraphicsRenderer {

    private final GameEventLog log;

    public GraphicsRenderer(GameEventLog log) {
        this.log = log;
    }

    public void createContext(int width, int height) {
        log.record("GFX_CONTEXT:" + width + "x" + height);
        System.out.println("[Graphics] Creando contexto " + width + "x" + height);
    }

    public void loadTextureAtlas(String levelId) {
        log.record("GFX_LOAD:" + levelId);
        System.out.println("[Graphics] Cargando atlas de texturas de " + levelId);
    }

    public void clearScene() {
        log.record("GFX_CLEAR");
        System.out.println("[Graphics] Limpiando escena");
    }
}
