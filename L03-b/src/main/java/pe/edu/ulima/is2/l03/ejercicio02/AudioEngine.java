package pe.edu.ulima.is2.l03.ejercicio02;

/** Subsistema de audio del motor. Uno de varios que hay que coordinar a mano hoy. */
public class AudioEngine {

    private final GameEventLog log;

    public AudioEngine(GameEventLog log) {
        this.log = log;
    }

    public void initialize() {
        log.record("AUDIO_INIT");
        System.out.println("[Audio] Inicializando motor de audio...");
    }

    public void loadTrack(String trackFile) {
        log.record("AUDIO_LOAD:" + trackFile);
        System.out.println("[Audio] Cargando pista: " + trackFile);
    }

    public void playMusic(String trackFile) {
        log.record("AUDIO_PLAY:" + trackFile);
        System.out.println("[Audio] Reproduciendo: " + trackFile);
    }

    public void stopMusic() {
        log.record("AUDIO_STOP");
        System.out.println("[Audio] Deteniendo musica");
    }
}
