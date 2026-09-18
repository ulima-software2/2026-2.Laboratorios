package pe.edu.ulima.is2.l03.ejercicio02;

/**
 * Personaje jugable. Los power-ups recogidos en el nivel se representan
 * como banderas booleanas que esta clase debe conocer y combinar dentro
 * de cada metodo de estadisticas.
 */
public class Player {

    private final String name;
    private final int baseAttackPower;
    private final int baseSpeed;
    private final int baseDefense;

    private boolean hasSpeedBoost;
    private boolean hasShield;
    private boolean hasDoubleDamage;
    private boolean hasFireTrail;

    public Player(String name, int baseAttackPower, int baseSpeed, int baseDefense) {
        this.name = name;
        this.baseAttackPower = baseAttackPower;
        this.baseSpeed = baseSpeed;
        this.baseDefense = baseDefense;
    }

    public void activateSpeedBoost() {
        this.hasSpeedBoost = true;
    }

    public void activateShield() {
        this.hasShield = true;
    }

    public void activateDoubleDamage() {
        this.hasDoubleDamage = true;
    }

    public void activateFireTrail() {
        this.hasFireTrail = true;
    }

    public int getAttackPower() {
        int power = baseAttackPower;
        if (hasDoubleDamage) {
            power = power * 2;
        }
        if (hasFireTrail) {
            power = power + 5;
        }
        return power;
    }

    public int getSpeed() {
        int speed = baseSpeed;
        if (hasSpeedBoost) {
            speed = speed + 4;
        }
        return speed;
    }

    public int getDefense() {
        int defense = baseDefense;
        if (hasShield) {
            defense = defense + 10;
        }
        return defense;
    }

    public String getName() {
        return name;
    }

    public String describe() {
        StringBuilder sb = new StringBuilder(name)
                .append(" [ATK=").append(getAttackPower())
                .append(", SPD=").append(getSpeed())
                .append(", DEF=").append(getDefense())
                .append("]");
        if (hasSpeedBoost) {
            sb.append(" +SpeedBoost");
        }
        if (hasShield) {
            sb.append(" +Shield");
        }
        if (hasDoubleDamage) {
            sb.append(" +DoubleDamage");
        }
        if (hasFireTrail) {
            sb.append(" +FireTrail");
        }
        return sb.toString();
    }
}
