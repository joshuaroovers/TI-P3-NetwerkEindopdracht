package gameObjects;

import javax.imageio.ImageIO;

import game.Game;
import javafx.scene.input.KeyCode;

import java.awt.geom.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class Tank extends GameObject implements Destructible, Serializable {

    public final UUID playerId;
    private final int speed = 200;

    private boolean isMoving;
    private boolean isMovingUp;
    private boolean isRotatingRight;
    private boolean isRotatingLeft;

    private double turretRotation;
    private boolean isRotatingTurretRight;
    private boolean isRotatingTurretLeft;

    private Timer timer = new Timer(1000);
    private final int turretWidth;
    private final int turretHeight;

    private int pointerLength;
    Rectangle2D pointer;

    private tankColor tankColor;
    private BufferedImage bodyImage;
    private BufferedImage turretImage;

    public enum tankColor {blue, dark, green, red, sand};


    public Tank(UUID playerId, Point2D position, Tank.tankColor tankColor) {
        this(playerId, position, 90.0, 90.0, tankColor);
    }

    public Tank(UUID playerId, Point2D position, double tankRotation, double turretRotation, Tank.tankColor tankColor) {

        this.playerId = playerId;

        setTankColor(tankColor);

        this.position = new Point2D.Double(position.getX(), position.getY());
        this.rotation = tankRotation;
        this.body = new Rectangle2D.Double(0, 0, bodyImage.getWidth(), bodyImage.getHeight());
        this.hitbox = new Rectangle2D.Double(0, 0, bodyImage.getWidth(), bodyImage.getHeight());


        this.isMoving = false;
        this.isMovingUp = false;

        this.width = bodyImage.getWidth();
        this.height = bodyImage.getHeight();

        this.turretWidth = turretImage.getWidth();
        this.turretHeight = turretImage.getHeight();

        this.isRotatingRight = false;
        this.isRotatingLeft = false;

        this.turretRotation = turretRotation;
        this.isRotatingTurretRight = false;
        this.isRotatingTurretLeft = false;

        this.pointerLength = 100;
    }

    private void setTankColor(Tank.tankColor tankColor) {
        this.tankColor = tankColor;
        String bodyImagePath = "resources/tankBody_"+ tankColor.toString() +".png";
        String turretImagePath = "resources/tankBarrel_"+ tankColor.toString() +".png";


        try {
            this.bodyImage = ImageIO.read(new FileInputStream(bodyImagePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            this.turretImage = ImageIO.read(new FileInputStream(turretImagePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        switch (tankColor){
            case blue:
                this.color = Color.BLUE.darker();
                break;
            case green:
                this.color = Color.GREEN.darker();
                break;
            case red:
                this.color = Color.red.darker();
                break;
            case dark:
                this.color = Color.BLACK.brighter();
                break;
            case sand:
                this.color = Color.ORANGE.darker();
                break;
        }
    }

    @Override
    public void update(double time, CopyOnWriteArrayList<GameObject> gameObjects) {
//        System.out.println("updating Tank");
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
                    if (gameObject.getCollision(getDirectTransform().createTransformedShape(pointer))){
                        pointerLength -=10;
//                        System.out.println("yes");
                    }
                    else {
                        pointerLength++;
//                        System.out.println("no");
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
                turretRotation -= speed*time;
            }
            if(isRotatingTurretLeft){
                turretRotation += speed*time;
            }
        }
    }

    @Override
    public void draw(Graphics2D g2d) {

//        g2d.setColor(color);
//        g2d.fill(getTransform().createTransformedShape(body));

        g2d.drawImage(bodyImage, getTransform(),null);

//        g2d.setColor(color.darker());
//        g2d.fill(getTurretTransform().createTransformedShape(new Rectangle2D.Double(0,0,turretWidth,turretHeight)));
        pointer = new Rectangle2D.Double(0,0, pointerLength,1);
        g2d.draw(getDirectTransform().createTransformedShape(pointer));

        g2d.drawImage(turretImage, getTurretTransform(),null);



        if(debugActive){
            g2d.setColor(Color.RED);
            g2d.draw(getTransform().createTransformedShape(hitbox.getBounds2D()));
            g2d.setColor(Color.WHITE);
            g2d.fill(new Ellipse2D.Double(position.getX()-1, position.getY()-1,2,2));
        }
    }

    @Override
    public AffineTransform getTransform() {
        AffineTransform tx = new AffineTransform();
        tx.translate(position.getX()-(width/2),position.getY()-(height/2));
        tx.rotate(Math.toRadians(rotation), (width/2),(height/2));

        return tx;
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

    public TankConstructorShell getConstructorShell(){
        return new TankConstructorShell(playerId, position, rotation, turretRotation, tankColor);
    }

//#region setters for movement booleans
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
//#endregion

    public void fireBullet(CopyOnWriteArrayList<GameObject> gameObjects) {
        if (timer.timeout()){
            gameObjects.add(new Bullet(position, turretRotation, debugActive));
            timer.setInterval(500);
        }
    }

    public void handleKeyInput(KeyInput keyInput, Game game){
        if(keyInput.isPress){
            if(keyInput.keyCode == KeyCode.W){
                setMovement(true,true);
            } else if(keyInput.keyCode == KeyCode.S){
                setMovement(true,false);
            }else if(keyInput.keyCode == KeyCode.A){
                setRotateLeft();
            }else if(keyInput.keyCode == KeyCode.D){
                setRotateRight();
            }else if(keyInput.keyCode == KeyCode.LEFT){
                setRotateTurretLeft();
            }else if(keyInput.keyCode == KeyCode.RIGHT){
                setRotateTurretRight();
            }else if(keyInput.keyCode == KeyCode.UP){
                fireBullet(game.getGameObjects());
            }
        }
        else{
            if(keyInput.keyCode == KeyCode.W || keyInput.keyCode == KeyCode.S){
                stopMovement();
            }else if(keyInput.keyCode == KeyCode.A || keyInput.keyCode == KeyCode.D){
                stopRotate();
            }else if(keyInput.keyCode == KeyCode.LEFT || keyInput.keyCode == KeyCode.RIGHT){
                stopRotateTurret();
            }
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
