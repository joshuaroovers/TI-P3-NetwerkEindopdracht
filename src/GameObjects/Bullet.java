package GameObjects;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Bullet extends GameObject {

    private final int width = 10;
    private final int height = 20;

    private final int speed = 10;
    private int bounceCount;

    public Bullet(Point2D pos, double direction, int bounceCount){
        this.position =  new Point2D.Double(position.getX()-(width/2), position.getY()-(height/2));;
        this.rotation = direction;
        this.hitbox = new Rectangle2D.Double();

        this.bounceCount = 0;
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Graphics2D g2d) {

    }
}
