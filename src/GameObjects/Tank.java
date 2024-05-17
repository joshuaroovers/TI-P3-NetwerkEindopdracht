package GameObjects;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import java.awt.*;

public class Tank extends GameObject {

    private Color color;
    private final int speed = 100;
    private boolean isMoving;
    private boolean isMovingUp;

    public Tank(Point2D position, double rotation, int size, Color color) {
        this.position = new Point2D.Double(position.getX()-(size/2), position.getY()-(size/2));
        this.rotation = rotation;
        this.body = new Rectangle2D.Double(0, 0, size, size);
        this.hitbox = new Rectangle2D.Double(0, 0, size, size);

        this.color = color;
        this.isMoving = false;
        this.isMovingUp = false;
    }

    @Override
    public void update(double time) {
        if(isMoving){
            double moveY = 0;
            if(isMovingUp){
                moveY = speed*time;
            }else{
                moveY = -speed*time;
            }
            this.position = new Point2D.Double(this.position.getX(), this.position.getY()+moveY);
        }
    }

    @Override
    public void draw(Graphics2D g2d) {
        AffineTransform tx = new AffineTransform();
        tx.translate(position.getX(),position.getY());


        g2d.setColor(Color.black);
        g2d.draw(tx.createTransformedShape(body));
        g2d.setColor(color);
        g2d.fill(tx.createTransformedShape(body));
    }

    public void setMovement(boolean move, boolean moveUp) {
        this.isMoving = move;
        this.isMovingUp = moveUp;
    }
    public void stopMovement(){
        this.isMoving = false;
    }
}
