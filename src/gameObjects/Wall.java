package gameObjects;

import game.Game;

import java.awt.*;
import java.awt.geom.*;

import java.util.ArrayList;



public class Wall extends GameObject{

    private Rectangle2D hitBoxLineTop;
    private Rectangle2D hitBoxLineRight;
    private Rectangle2D hitBoxLineBottom;
    private Rectangle2D hitBoxLineLeft;

    public Wall(Point2D posistion, int width,int height){
        this.position = posistion;
        this.body = new Rectangle2D.Double(0,0,width,height);

        this.hitbox = new Rectangle2D.Double(0,0,width,height);

        //top and bottom are switched for some reason (see coordinates)
        this.hitBoxLineTop = new Rectangle2D.Double(0,0, width, 1 );
        this.hitBoxLineRight = new Rectangle2D.Double(width,0, 1, height );
        this.hitBoxLineBottom = new Rectangle2D.Double(0,height, width, 1 );
        this.hitBoxLineLeft = new Rectangle2D.Double(0,0, 1, height );
    }

    @Override
    public void update(double time, ArrayList<GameObject> gameObjects, Game game) {

    }

    @Override
    public void draw(Graphics2D g2d) {
        AffineTransform tx = getTransform();

        g2d.setColor(Color.black);
        g2d.draw(tx.createTransformedShape(body));
        g2d.fill(tx.createTransformedShape(body));

//        g2d.setColor(Color.RED);
//        g2d.draw(tx.createTransformedShape(hitbox));

        g2d.setColor(Color.RED);
        g2d.draw(tx.createTransformedShape(hitBoxLineTop));
        g2d.setColor(Color.BLUE);
        g2d.draw(tx.createTransformedShape(hitBoxLineRight));
        g2d.setColor(Color.YELLOW);
        g2d.draw(tx.createTransformedShape(hitBoxLineBottom));
        g2d.setColor(Color.GREEN);
        g2d.draw(tx.createTransformedShape(hitBoxLineLeft));
    }


    public boolean isVerticalCollision(Shape collider){
        Area colliderArea1 = new Area(collider);
        Area colliderArea2 = new Area(collider);
        Area colliderArea3 = new Area(collider);
        Area colliderArea4 = new Area(collider);
        Area topLineArea = new Area(getTransform().createTransformedShape(hitBoxLineTop));
        Area rightLineArea = new Area(getTransform().createTransformedShape(hitBoxLineRight));
        Area bottomLineArea = new Area(getTransform().createTransformedShape(hitBoxLineBottom));
        Area leftLineArea = new Area(getTransform().createTransformedShape(hitBoxLineLeft));

        colliderArea1.intersect(topLineArea);
        colliderArea2.intersect(bottomLineArea);

        colliderArea3.intersect(rightLineArea);
        colliderArea4.intersect(leftLineArea);
        if(!colliderArea3.isEmpty() || !colliderArea4.isEmpty()){
            System.out.println("!!!!either left or right wall");
            return true;
        }else if(!colliderArea1.isEmpty() || !colliderArea2.isEmpty()){
            System.out.println("!!!!either top or bottom wall");
            return false;
        }else{
            System.out.println("no border collision");
            return false;
        }
    }


    @Override
    public AffineTransform getTransform() {
        AffineTransform tx = new AffineTransform();
        tx.translate(position.getX()-(width/2),position.getY()-(height/2));

        return tx;
    }

}
