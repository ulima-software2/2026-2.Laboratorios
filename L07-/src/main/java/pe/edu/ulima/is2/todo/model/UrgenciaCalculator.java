package pe.edu.ulima.is2.todo.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Calcula el nivel de urgencia de un TODO combinando su estado,
 * su prioridad y los días que faltan para su fecha límite.
 *
 * <p>Reglas:
 * <ul>
 *   <li>Un TODO completado no tiene urgencia.</li>
 *   <li>Sin fecha límite: prioridad ALTA es urgencia MEDIA; el resto, BAJA.</li>
 *   <li>Fecha límite vencida: CRITICA.</li>
 *   <li>Vence en 0 a 2 días: prioridad ALTA es CRITICA; el resto, ALTA.</li>
 *   <li>Vence en 3 a 7 días: prioridad ALTA o MEDIA es MEDIA; BAJA es BAJA.</li>
 *   <li>Vence en más de 7 días: BAJA.</li>
 * </ul>
 */
public class UrgenciaCalculator {

    public static final int DIAS_INMINENTE = 2;
    public static final int DIAS_PROXIMO = 7;

    public NivelUrgencia calcular(Todo todo, LocalDate hoy) {
        if (todo.isCompletado()) {
            return NivelUrgencia.NINGUNA;
        }
        Prioridad prioridad = todo.getPrioridad();
        LocalDate fechaLimite = todo.getFechaLimite();
        if (fechaLimite == null) {
            if (prioridad == Prioridad.ALTA) {
                return NivelUrgencia.MEDIA;
            }
            return NivelUrgencia.BAJA;
        }
        long diasRestantes = ChronoUnit.DAYS.between(hoy, fechaLimite);
        if (diasRestantes < 0) {
            return NivelUrgencia.CRITICA;
        }
        if (diasRestantes <= DIAS_INMINENTE) {
            if (prioridad == Prioridad.ALTA) {
                return NivelUrgencia.CRITICA;
            }
            return NivelUrgencia.ALTA;
        }
        if (diasRestantes <= DIAS_PROXIMO && prioridad != Prioridad.BAJA) {
            return NivelUrgencia.MEDIA;
        }
        return NivelUrgencia.BAJA;
    }
}
