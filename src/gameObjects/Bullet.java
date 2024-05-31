package GameObjects;

import game.Game;

import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;

public class Bullet extends GameObject implements Destructible {

    private boolean drawHitbox = false;
    private int scale =2;
    private final int width = 10;
    private final int height = 20;

    private final int speed = 500;
    private int maxBounces;

    public Bullet(Point2D position, double direction){
        this.position =  new Point2D.Double(position.getX(), position.getY());;
        this.rotation = direction;
        this.hitbox = new Ellipse2D.Double(0,0,width,height); //todo height and width are switched for some reason??????????????????
        this.body = new Rectangle2D.Double(0,0,width,height);

        this.maxBounces = 1;
    }

    @Override
    public void update(double time, ArrayList<GameObject> gameObjects, Game game) {

        boolean isColliding = false;
        double xRotation = rotation;
        double yRotation = rotation;

        for (GameObject gameObject : gameObjects) {
            if(gameObject.getClass() == Wall.class){
                if(gameObject.getCollision(getCollider())){
                    isColliding = true;
                }
            }else if(gameObject.getClass() == Tank.class){
                if(gameObject.getCollision(getCollider())){
//                    isColliding = true;
//                    ((Tank) gameObject).destroy(gameObjects);
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


        double moveY = speed*time * Math.sin(Math.toRadians(xRotation));
        double moveX = speed*time * Math.cos(Math.toRadians(yRotation));


        Point2D newPosition = new Point2D.Double(this.position.getX()+moveX, this.position.getY()+moveY);




        if(!isColliding){
            this.position = newPosition;
        }
    }

    @Override
    public void draw(Graphics2D g2d) {

        AffineTransform tx = getTransform();

        g2d.setColor(Color.black);
        g2d.draw(tx.createTransformedShape(body));
        g2d.setColor(color);
        g2d.fill(tx.createTransformedShape(body));
        if (drawHitbox) {
            g2d.setColor(Color.RED);
            g2d.draw(tx.createTransformedShape(hitbox));
        }
    }

    @Override
    public AffineTransform getTransform() {
        AffineTransform tx = new AffineTransform();

        tx.translate(position.getX()-(width/2),position.getY()-(height/2));
        tx.rotate(Math.toRadians(rotation+90),(width/2),(height/2)); //+90 is necessary for proper rotation
        tx.translate(0,-50-(height*2)); //extra offset from tank

        return tx;
    }

    @Override
    public void destroy(ArrayList<GameObject> gameObjects) {
        System.out.println("destroy "+this.getClass());
        if(gameObjects.remove(this)){
            System.out.println("removed: "+this.getClass()+" "+this);
        };
    }
}
