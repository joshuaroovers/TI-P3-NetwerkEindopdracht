package GameObjects;

import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;

public class Bullet extends GameObject {

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
    public void update(double time, ArrayList<GameObject> gameObjects) {



        double moveY = speed*time * Math.sin(Math.toRadians(rotation));
        double moveX = speed*time * Math.cos(Math.toRadians(rotation));


        Point2D newPosition = new Point2D.Double(this.position.getX()+moveX, this.position.getY()+moveY);

        boolean isColliding = false;
        for (GameObject gameObject : gameObjects) {
            if(gameObject.getClass() == Wall.class){
                if(gameObject.getCollision(getCollider())){
                    isColliding = true;
                }
            }
        }
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
    public boolean getCollision(Shape collider) {
        Area objectArea = new Area(collider);
        Area bulletArea = new Area(getCollider());
        bulletArea.intersect(objectArea);
        if(!bulletArea.isEmpty()){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public Shape getCollider() {
        return getTransform().createTransformedShape(hitbox);
    }
}
