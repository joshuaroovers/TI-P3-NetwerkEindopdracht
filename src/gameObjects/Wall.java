package GameObjects;

import game.Game;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Wall extends GameObject{
    public Wall(Point2D posistion, int width,int height){
        this.position = posistion;
        this.body = new Rectangle2D.Double(0,0,width,height);
        this.hitbox = new Rectangle2D.Double(0,0,width,height);
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

        g2d.setColor(Color.RED);
        g2d.draw(tx.createTransformedShape(hitbox));
    }

    @Override
    public AffineTransform getTransform() {
        AffineTransform tx = new AffineTransform();
        tx.translate(position.getX()-(width/2),position.getY()-(height/2));

        return tx;
    }

}
