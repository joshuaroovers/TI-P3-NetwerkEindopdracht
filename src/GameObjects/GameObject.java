package GameObjects;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import java.awt.*;

public abstract class GameObject {
    Point2D position;
    double rotation;
    Rectangle2D body;
    Rectangle2D hitbox;
    int height;
    int width;
    Color color;

    public abstract void update(double time);
    public abstract void draw(Graphics2D g2d);
}
