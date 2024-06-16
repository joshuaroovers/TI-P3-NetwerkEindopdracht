package gameObjects;

import java.awt.*;
import java.awt.geom.*;
import java.io.Serializable;
import java.util.concurrent.CopyOnWriteArrayList;

public class  Bullet extends GameObject implements Destructible, Serializable {

    private int scale =2;
    private final int width = 10;
    private final int height = 20;

    private final int speed = 500;
    private int speedX;
    private int speedY;
    private double displayRotation;
    private final int maxBounces  = 2;
    private int bounces;

    private GameObject lastCollision;

    public Bullet(Point2D position, double direction, boolean debugActive){

        this.debugActive = debugActive;
        //2d rotation matrix math
        double cos = Math.cos(Math.toRadians(direction));
        double sin = Math.sin(Math.toRadians(direction));
        double x = position.getX()+85 - position.getX();  //85 is the offset should prob be relative to tank and bullet size rather than hard coded
        double y = position.getY()+0 - position.getY();
        double newX = x*cos - y*sin +position.getX();
        double newY = y*cos + x*sin +position.getY();

        this.position =  new Point2D.Double(newX, newY);;
        this.rotation = direction;
        this.hitbox = new Ellipse2D.Double(0,0,width,height);
        this.body = new Rectangle2D.Double(0,0,width,height);

        this.color = Color.BLACK;

        this.bounces = 0;

        this.speedX = speed;
        this.speedY = speed;
        this.displayRotation = direction;
    }

    @Override
    public void update(double time, CopyOnWriteArrayList<GameObject> gameObjects) {
        boolean isColliding = false;

        for (GameObject gameObject : gameObjects) {
            if(gameObject.getClass() == Wall.class){
                if(gameObject.getCollision(getCollider()) && gameObject != lastCollision){
                    if (bounces != maxBounces){
                        bounces++;
                        lastCollision = gameObject;
                        if(((Wall) gameObject).isVerticalCollision(getCollider())){
                            System.out.println("bounce vertical wall");
                            this.speedX = -speedX;
                        }else{
                            System.out.println("bounce horizontal wall");
                            this.speedY = -speedY;
                        }
                        displayRotation = 180-displayRotation;
                    }
                    else {this.destroy(gameObjects);}
                }
            }else if(gameObject.getClass() == Tank.class){
                if(gameObject.getCollision(getCollider())){
                    ((Tank) gameObject).destroy(gameObjects);
                    this.destroy(gameObjects);
                }
            }else if(gameObject.getClass() == Bullet.class && gameObject != this){
                if(gameObject.getCollision(getCollider())){
                    isColliding = true;
                    ((Bullet) gameObject).destroy(gameObjects);
                    this.destroy(gameObjects);
                }
            }

        }


        double moveY = speedY*time * Math.sin(Math.toRadians(rotation));
        double moveX = speedX*time * Math.cos(Math.toRadians(rotation));

//        System.out.println("moveY: " +moveY);
//        System.out.println("moveX: " +moveX);

        Point2D newPosition = new Point2D.Double(this.position.getX()+moveX, this.position.getY()+moveY);




        if(!isColliding){
            this.position = newPosition;
        }
    }

    @Override
    public void draw(Graphics2D g2d) {

        AffineTransform tx = getTransform();

        g2d.setColor(color);
        g2d.fill(tx.createTransformedShape(body));

        if (debugActive) {
            g2d.setColor(Color.RED);
            g2d.draw(tx.createTransformedShape(hitbox));
            g2d.setColor(Color.GREEN);
            g2d.fill(new Ellipse2D.Double(position.getX()-1, position.getY()-1,2,2));
        }
    }

    @Override
    public AffineTransform getTransform() {
        AffineTransform tx = new AffineTransform();

        tx.translate(position.getX()-(width/2),position.getY()-(height/2));
        tx.rotate(Math.toRadians(displayRotation+90),(width/2),(height/2)); //+90 is necessary for proper rotation

        return tx;
    }

    @Override
    public void destroy(CopyOnWriteArrayList<GameObject> gameObjects) {
        System.out.println("destroy "+this.getClass());
        if(gameObjects.remove(this)){
            System.out.println("removed: "+this.getClass()+" "+this);
        };
    }
}
