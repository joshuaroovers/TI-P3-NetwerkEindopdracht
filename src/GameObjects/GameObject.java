package GameObjects;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import java.awt.*;

public abstract class GameObject {
    Point2D position;
    double rotation;
    Rectangle2D body;
    Rectangle2D hitbox;

    public abstract void update(double deltaTime);
    public abstract void draw(Graphics2D g2d);
}
