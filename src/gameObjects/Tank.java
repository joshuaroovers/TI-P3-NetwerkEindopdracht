package gameObjects;

import javax.imageio.ImageIO;
import javax.sound.midi.Soundbank;

import game.Game;

import java.awt.geom.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Tank extends GameObject implements Destructible{

    private final int speed = 200;
    private int length;
    private boolean isMoving;
    private boolean isMovingUp;
    private boolean isRotatingRight;
    private boolean isRotatingLeft;
    private double turretRotation;
    private boolean isRotatingTurretRight;
    private boolean isRotatingTurretLeft;
    private Timer timer = new Timer(1000);
    private String bodyColor;
    private String turretColor;
    private BufferedImage bodyImage;
    private BufferedImage turretImage;
    private GameObject lastCollision;
    Line2D direction;
    Rectangle2D direct;

    public Tank(Point2D position, double rotation, int size, Color color,String colour) {
        this.bodyColor = "resources/tankBody_"+ colour+".png";
        this.turretColor = "resources/tankBarrel_"+colour+".png";
        try {
            bodyImage = ImageIO.read(new FileInputStream(bodyColor));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            turretImage = ImageIO.read(new FileInputStream(turretColor));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.position = new Point2D.Double(position.getX(), position.getY());
        this.rotation = rotation+90;
        this.body = new Rectangle2D.Double(0, 0, bodyImage.getWidth(), bodyImage.getHeight());
        this.hitbox = new Rectangle2D.Double(0, 0, bodyImage.getWidth(), bodyImage.getHeight());

        this.color = color;
        this.isMoving = false;
        this.isMovingUp = false;

        this.width = bodyImage.getWidth();
        this.height = bodyImage.getHeight();

        this.isRotatingRight = false;
        this.isRotatingLeft = false;

        this.turretRotation = rotation+90;
        this.isRotatingTurretRight = false;
        this.isRotatingTurretLeft = false;

        this.length = 100;
    }

    @Override
    public void update(double time, ArrayList<GameObject> gameObjects, Game game) {
        boolean isColliding = false;
        double newRotation = rotation;

        if(isRotatingRight || isRotatingLeft){

            if(rotation > 360){
                newRotation = 0;
            }else if(rotation < 0){
                newRotation = 360;
            }
            if(isRotatingRight){
                newRotation--;
            }
            if(isRotatingLeft){
                newRotation++;
            }
        }

        double moveY = 0;
        double moveX = 0;

        if(isMoving){

            if(isMovingUp){
//                System.out.println(newRotation);
                moveY = speed*time * Math.sin(Math.toRadians(newRotation));
                moveX = speed*time * Math.cos(Math.toRadians(newRotation));
            }else{
                moveY = -speed*time * Math.sin(Math.toRadians(newRotation));
                moveX = -speed*time * Math.cos(Math.toRadians(newRotation));
            }
        }

        Point2D newPosition = new Point2D.Double(this.position.getX()+moveX, this.position.getY()+moveY);

        AffineTransform tempTx = new AffineTransform();
        tempTx.translate(newPosition.getX()-(width/2),newPosition.getY()-(height/2));
        tempTx.rotate(Math.toRadians(newRotation), (width/2),(height/2));

        for (GameObject gameObject : gameObjects) {
            if(gameObject != this &&gameObject.getClass() != Bullet.class && gameObject.getCollision(tempTx.createTransformedShape(hitbox))){
                isColliding = true;
            }else if(gameObject.getClass() == Bullet.class && gameObject.getCollision(tempTx.createTransformedShape(hitbox))){
                this.destroy(gameObjects);
                ((Bullet) gameObject).destroy(gameObjects);
            }
        }
        for (GameObject gameObject : gameObjects) {
            if(gameObject.getClass() == Wall.class){
                    if (((Wall)gameObject).getCollision(getDirectTransform().createTransformedShape(direct))){
                        length -=5;
                        System.out.println("yes");
                    }
                    else {
                        length++;
                        System.out.println("no");
                    }
            }
        }

        if(!isColliding){
            this.rotation = newRotation;
            this.position = newPosition;
        }

        if(isRotatingTurretRight || isRotatingTurretLeft){
//            System.out.println(turretRotation);
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
        AffineTransform tankTx = getTransform();

        g2d.drawImage(bodyImage,tankTx,null);

        AffineTransform turretTx = getTurretTransform();

        g2d.drawImage(turretImage,turretTx,null);

        g2d.setColor(Color.RED);
        g2d.draw(tankTx.createTransformedShape(hitbox.getBounds2D()));

        g2d.setColor(Color.GREEN);
        g2d.fill(new Ellipse2D.Double(position.getX()-1, position.getY()-1,2,2));

//        direction = new Line2D.Double(0,0,length,0);
        direct = new  Rectangle2D.Double(0,0,length,1);

//        g2d.draw(turretTx.createTransformedShape(direction));
        g2d.draw(getDirectTransform().createTransformedShape(direct));
    }

    private AffineTransform getTurretTransform() {
        AffineTransform turretTx = new AffineTransform();
        turretTx.translate(position.getX()-(turretImage.getWidth()/2),position.getY()-(turretImage.getHeight()/2));
        turretTx.rotate(Math.toRadians(turretRotation), (turretImage.getWidth()/2), (turretImage.getHeight()/2));
        return turretTx;
    }

    private AffineTransform getDirectTransform() {
        AffineTransform directTx = getTurretTransform();
        directTx.translate((turretImage.getWidth()/2),(turretImage.getHeight()/2));
        return directTx;
    }


    @Override
    public AffineTransform getTransform() {
        AffineTransform tx = new AffineTransform();
        tx.translate(position.getX()-(width/2),position.getY()-(height/2));
        tx.rotate(Math.toRadians(rotation), (width/2),(height/2));

        return tx;
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

    @Override
    public void destroy(ArrayList<GameObject> gameObjects) {
        System.out.println("destroy "+this.getClass());
        if(gameObjects.remove(this)){
            System.out.println("removed: "+this.getClass()+" "+this);
        };
    }
}
