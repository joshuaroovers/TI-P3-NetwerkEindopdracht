package GameObjects;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import java.awt.*;

public class Tank extends GameObject {

    private final int speed = 100;
    private boolean isMoving;
    private boolean isMovingUp;
    private boolean isRotatingRight;
    private boolean isRotatingLeft;

    public Tank(Point2D position, double rotation, int size, Color color) {
        this.position = new Point2D.Double(position.getX(), position.getY());
        this.rotation = rotation+90;
        this.body = new Rectangle2D.Double(0, 0, size, size);
        this.hitbox = new Rectangle2D.Double(0, 0, size, size);

        this.color = color;
        this.isMoving = false;
        this.isMovingUp = false;

        this.width = size;
        this.height = size;

        this.isRotatingRight = false;
        this.isRotatingLeft = false;
    }

    @Override
    public void update(double time) {
        if(isRotatingRight || isRotatingLeft){
            if(rotation > 360){
                rotation = 0;
            }else if(rotation < 0){
                rotation = 360;
            }
            if(isRotatingRight){
                rotation--;
            }
            if(isRotatingLeft){
                rotation++;
            }
        }

        if(isMoving){
            double moveY = 0;
            double moveX = 0;
            if(isMovingUp){
                System.out.println(rotation);
                moveY = speed*time * Math.sin(Math.toRadians(rotation));
                moveX = speed*time * Math.cos(Math.toRadians(rotation));
            }else{
                moveY = -speed*time * Math.sin(Math.toRadians(rotation));
                moveX = -speed*time * Math.cos(Math.toRadians(rotation));
            }
            this.position = new Point2D.Double(this.position.getX()+moveX, this.position.getY()+moveY);
        }

//            Point2D newPosition = new Point2D.Double(
//                    this.position.getX() + speed * Math.cos(rotation),
//                    this.position.getY() + speed
//            );
    }

    @Override
    public void draw(Graphics2D g2d) {
        AffineTransform tx = new AffineTransform();
        tx.translate(position.getX()-(width/2),position.getY()-(height/2));
        tx.rotate(Math.toRadians(rotation), (width/2),(height/2));
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

    public void setRotateRight(){
        this.isRotatingRight = true;
    }

    public void setRotateLeft(){
        this.isRotatingLeft = true;
    }

    public void stopRotate(){
        this.isRotatingRight = false;
        this.isRotatingLeft = false;
    }
}
