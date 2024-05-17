package GameObjects;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import java.awt.*;

public class Tank extends GameObject {

    private Color color;

    public Tank(Point2D position, double rotation, int size, Color color) {
        this.position = new Point2D.Double(position.getX()-(size/2), position.getY()-(size/2));
        this.rotation = rotation;
        this.body = new Rectangle2D.Double(position.getX(), position.getY(), size, size);
        this.hitbox = new Rectangle2D.Double(position.getX(), position.getY(), size, size);
        this.color = color;
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Graphics2D g2d) {

    }
}
