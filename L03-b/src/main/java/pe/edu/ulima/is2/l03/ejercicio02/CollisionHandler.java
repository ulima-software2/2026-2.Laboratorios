package pe.edu.ulima.is2.l03.ejercicio02;

/**
 * Resuelve los combates cuerpo a cuerpo del nivel. Depende directamente de
 * la clase concreta {@link Player}, por lo que solo sabe combatir contra
 * jugadores, nunca contra otro tipo de personaje.
 */
public class CollisionHandler {

    public int resolveIncomingDamage(Player defender, int rawDamage) {
        int damage = rawDamage - defender.getDefense();
        return Math.max(damage, 0);
    }

    public String resolveMeleeHit(Player attacker, Player defender, int rawDamage) {
        int finalDamage = resolveIncomingDamage(defender, rawDamage);
        return attacker.getName() + " golpea a " + defender.getName()
                + " por " + finalDamage + " de dano (ATK=" + attacker.getAttackPower() + ")";
    }
}
