package gameObjects;

import javax.imageio.ImageIO;

import game.Game;

import java.awt.geom.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.CopyOnWriteArrayList;

public class Tank extends GameObject implements Destructible, Serializable {

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
    private String bodyImagePath;
    private String turretImagePath;
    private final int turretWidth;
    private final int turretHeight;

    Rectangle2D direct;


    public Tank(Point2D position, Color color,String colour) {

        this.bodyImagePath = "resources/tankBody_"+ colour+".png";
        this.turretImagePath = "resources/tankBarrel_"+colour+".png";

        BufferedImage bodyImage;
        BufferedImage turretImage;

        try {
            bodyImage = ImageIO.read(new FileInputStream(bodyImagePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            turretImage = ImageIO.read(new FileInputStream(turretImagePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        this.position = new Point2D.Double(position.getX(), position.getY());
        this.rotation = 90;
        this.body = new Rectangle2D.Double(0, 0, bodyImage.getWidth(), bodyImage.getHeight());
        this.hitbox = new Rectangle2D.Double(0, 0, bodyImage.getWidth(), bodyImage.getHeight());

        this.color = color;
        this.isMoving = false;
        this.isMovingUp = false;

        this.width = bodyImage.getWidth();
        this.height = bodyImage.getHeight();

        this.turretWidth = turretImage.getWidth();
        this.turretHeight = turretImage.getHeight();

        this.isRotatingRight = false;
        this.isRotatingLeft = false;

        this.turretRotation = rotation+90;
        this.isRotatingTurretRight = false;
        this.isRotatingTurretLeft = false;

        this.length = 100;
    }

    @Override
    public synchronized void update(double time, CopyOnWriteArrayList<GameObject> gameObjects, Game game) {
        System.out.println("updating Tank");
        boolean isColliding = false;
        double newRotation = rotation;

        if(isRotatingRight || isRotatingLeft){

            if(rotation > 360){
                newRotation = 0;
            }else if(rotation < 0){
                newRotation = 360;
            }
            if(isRotatingRight){
                newRotation -= speed*time;
            }
            if(isRotatingLeft){
                newRotation += speed*time;
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
            if(gameObject != this && gameObject.getClass() != Bullet.class && gameObject.getCollision(tempTx.createTransformedShape(hitbox))){
                isColliding = true;
            }else if(gameObject.getClass() == Bullet.class && gameObject.getCollision(tempTx.createTransformedShape(hitbox))){
                this.destroy(gameObjects);
                ((Bullet) gameObject).destroy(gameObjects);
            }else if(gameObject.getClass() == Wall.class){
//                    if (gameObject.getCollision(getDirectTransform().createTransformedShape(direct))){
//                        length -=5;
////                        System.out.println("yes");
//                    }
//                    else {
//                        length++;
////                        System.out.println("no");
//                    }
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
                turretRotation -= speed*time;
            }
            if(isRotatingTurretLeft){
                turretRotation += speed*time;
            }
        }
    }

    @Override
    public void draw(Graphics2D g2d) {

//        BufferedImage bodyImage = null;
//        BufferedImage turretImage = null;
//
//        try {
//           bodyImage = ImageIO.read(new FileInputStream(bodyImagePath));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        try {
//            turretImage = ImageIO.read(new FileInputStream(turretImagePath));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        AffineTransform tankTx = getTransform();
        g2d.setColor(color);
        g2d.fill(tankTx.createTransformedShape(body));

//        g2d.drawImage(bodyImage,tankTx,null);

        AffineTransform turretTx = getTurretTransform();
        g2d.setColor(color.darker());
        g2d.fill(turretTx.createTransformedShape(new Rectangle2D.Double(0,0,turretWidth,turretHeight)));

//        g2d.drawImage(turretImage,turretTx,null);

        direct = new Rectangle2D.Double(0,0,length,1);
        g2d.draw(getDirectTransform().createTransformedShape(direct));


        g2d.setColor(Color.RED);
        g2d.draw(tankTx.createTransformedShape(hitbox.getBounds2D()));
//        g2d.setColor(Color.GREEN);
//        g2d.fill(new Ellipse2D.Double(position.getX()-1, position.getY()-1,2,2));
    }

    private AffineTransform getTurretTransform() {
        AffineTransform turretTx = new AffineTransform();
        turretTx.translate(position.getX()-(turretWidth/2),position.getY()-(turretHeight/2));
        turretTx.rotate(Math.toRadians(turretRotation), (turretWidth/2), (turretHeight/2));
        return turretTx;
    }

    private AffineTransform getDirectTransform() {
        AffineTransform directTx = getTurretTransform();
        directTx.translate((turretWidth/2),(turretHeight/2));
        return directTx;
    }


    @Override
    public synchronized AffineTransform getTransform() {
        AffineTransform tx = new AffineTransform();
        tx.translate(position.getX()-(width/2),position.getY()-(height/2));
        tx.rotate(Math.toRadians(rotation), (width/2),(height/2));

        return tx;
    }


    public synchronized void setMovement(boolean move, boolean moveUp) {
        this.isMoving = move;
        this.isMovingUp = moveUp;
    }
    public synchronized void stopMovement(){
        this.isMoving = false;
    }

    public synchronized void setRotateRight(){
        this.isRotatingRight = true;
    }

    public synchronized void setRotateLeft(){
        this.isRotatingLeft = true;
    }

    public synchronized void stopRotate(){
        this.isRotatingRight = false;
        this.isRotatingLeft = false;
    }

    public synchronized void setRotateTurretRight() {
        this.isRotatingTurretRight = true;
    }
    public synchronized void setRotateTurretLeft() {
        this.isRotatingTurretLeft = true;
    }
    public synchronized void stopRotateTurret(){
        this.isRotatingTurretRight = false;
        this.isRotatingTurretLeft = false;
    }

    public synchronized void fireBullet(CopyOnWriteArrayList<GameObject> gameObjects) {
        if (timer.timeout()){
            gameObjects.add(new Bullet(position, turretRotation));
            timer.setInterval(500);
        }
    }

    @Override
    public synchronized void destroy(CopyOnWriteArrayList<GameObject> gameObjects) {
        System.out.println("destroy "+this.getClass());
        if(gameObjects.remove(this)){
            System.out.println("removed: "+this.getClass()+" "+this);
        }
    }
}
