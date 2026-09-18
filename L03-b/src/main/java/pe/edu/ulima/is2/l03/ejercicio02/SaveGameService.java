package pe.edu.ulima.is2.l03.ejercicio02;

import java.util.HashMap;
import java.util.Map;

/** Subsistema de guardado. Traduce un id de checkpoint al nivel que le corresponde. */
public class SaveGameService {

    private final GameEventLog log;
    private final Map<String, String> checkpoints = new HashMap<>();

    public SaveGameService(GameEventLog log) {
        this.log = log;
        checkpoints.put("checkpoint-42", "level-3");
    }

    public String loadCheckpoint(String checkpointId) {
        log.record("SAVE_LOAD:" + checkpointId);
        String levelId = checkpoints.get(checkpointId);
        System.out.println("[Save] Checkpoint " + checkpointId + " -> " + levelId);
        return levelId;
    }

    public void saveCheckpoint(String checkpointId, String levelId) {
        log.record("SAVE_STORE:" + checkpointId + ":" + levelId);
        checkpoints.put(checkpointId, levelId);
        System.out.println("[Save] Guardando " + checkpointId + " en " + levelId);
    }
}
