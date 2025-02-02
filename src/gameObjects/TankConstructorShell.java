package gameObjects;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.UUID;

public class TankConstructorShell implements Serializable {

    public UUID playerId;
    public Point2D position;
    public double tankRotation;
    public double turretRotation;
    public Tank.tankColor tankColor;

    public TankConstructorShell(UUID playerId, Tank.tankColor tankColor) {
        this(playerId, null, 0.0, 0.0, tankColor);
    }

    public TankConstructorShell(UUID playerId, Point2D position, double tankRotation, double turretRotation, Tank.tankColor tankColor) {
        this.playerId = playerId;
        this.position = position;
        this.tankRotation = tankRotation;
        this.turretRotation = turretRotation;
        this.tankColor = tankColor;
    }

    public double getTankRotation() {
        return tankRotation;
    }

    public double getTurretRotation() {
        return turretRotation;
    }

    public Point2D getPosition() {
        return position;
    }
}
