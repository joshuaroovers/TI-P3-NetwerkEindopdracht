package GameObjects;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import java.awt.*;
import java.util.ArrayList;

public abstract class GameObject {
    Point2D position;
    double rotation;
    Shape body;
    Shape hitbox;
    int height;
    int width;
    Color color;

    public abstract void update(double time, ArrayList<GameObject> gameObjects);
    public abstract void draw(Graphics2D g2d);
    public abstract AffineTransform getTransform();
    public abstract boolean getCollision(Shape collider);
    public abstract Shape getCollider();
}
