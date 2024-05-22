package GameObjects;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Bullet extends GameObject {

    private boolean drawHitbox = false;
    private int scale =2;
    private final int width = 10;
    private final int height = 20;

    private final int speed = 10;
    private int bounceCount;

    public Bullet(Point2D pos, double direction, int bounceCount){
        this.position =  new Point2D.Double(position.getX()-(width/2), position.getY()-(height/2));;
        this.rotation = direction;
        this.hitbox = new Rectangle2D.Double(0,0,width,height);
        this.body = new Rectangle2D.Double(0,0,width,height);

        this.bounceCount = 0;
    }

    @Override
    public void update(double deltaTime) {

    }

    @Override
    public void draw(Graphics2D g2d) {
        AffineTransform tx = new AffineTransform();
        tx.translate(position.getX()-(width/2),position.getY()-(height/2));
        g2d.setColor(Color.black);
        g2d.draw(body);
        g2d.setColor(color);
        g2d.fill(body);
        if (drawHitbox) {
            g2d.setColor(Color.RED);
            g2d.draw(hitbox);
        }
    }
}
