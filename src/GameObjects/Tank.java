package GameObjects;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import java.awt.*;
import java.util.ArrayList;

public class Tank extends GameObject {

    private final int speed = 100;
    private boolean isMoving;
    private boolean isMovingUp;
    private boolean isRotatingRight;
    private boolean isRotatingLeft;
    private double turretRotation;
    private boolean isRotatingTurretRight;
    private boolean isRotatingTurretLeft;
    private Timer timer = new Timer(1000);

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

        this.turretRotation = rotation+90;
        this.isRotatingTurretRight = false;
        this.isRotatingTurretLeft = false;
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

        if(isRotatingTurretRight || isRotatingTurretLeft){
            System.out.println(turretRotation);
            if(turretRotation > 360){
                turretRotation = 0;
            }else if(turretRotation < 0){
                turretRotation = 360;
            }
            if(isRotatingTurretRight){
                turretRotation--;
            }
            if(isRotatingTurretLeft){
                turretRotation++;
            }
        }
    }

    @Override
    public void draw(Graphics2D g2d) {
        AffineTransform tankTx = new AffineTransform();
        tankTx.translate(position.getX()-(width/2),position.getY()-(height/2));
        tankTx.rotate(Math.toRadians(rotation), (width/2),(height/2));

        g2d.setColor(Color.black);
        g2d.draw(tankTx.createTransformedShape(body));
        g2d.setColor(color);
        g2d.fill(tankTx.createTransformedShape(body));

        AffineTransform turretTx = new AffineTransform();
        turretTx.translate(position.getX()-(width/4),position.getY()-(height/4));
        turretTx.rotate(Math.toRadians(turretRotation), (width/4), (height/4));

        g2d.setColor(Color.MAGENTA);
        g2d.fill(turretTx.createTransformedShape(new Rectangle2D.Double(0,0, (width/2), (height/2))));
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

    public void setRotateTurretRight() {
        this.isRotatingTurretRight = true;
    }
    public void setRotateTurretLeft() {
        this.isRotatingTurretLeft = true;
    }
    public void stopRotateTurret(){
        this.isRotatingTurretRight = false;
        this.isRotatingTurretLeft = false;
    }

    public void fireBullet(ArrayList<GameObject> gameObjects) {
        if (timer.timeout()){
        gameObjects.add(new Bullet(position, turretRotation));
        timer.setInterval(500);
        }
    }
}
