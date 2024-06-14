package gameObjects;

import game.Game;

import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Point2D;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class GameObject implements Serializable {
    Point2D position;
    double rotation;
    Shape body;
    Shape hitbox;
    int height;
    int width;
    Color color;

    public abstract void update(double time, CopyOnWriteArrayList<GameObject> gameObjects, Game game);

    public abstract void draw(Graphics2D g2d);
    public abstract AffineTransform getTransform();
    public boolean getCollision(Shape collider){
        Area colliderArea = new Area(collider);
        Area objectArea = new Area(getCollider());
        objectArea.intersect(colliderArea);
        if(!objectArea.isEmpty()){
            return true;
        }else{
            return false;
        }
    };
    public Shape getCollider(){
        return this.getTransform().createTransformedShape(this.hitbox);
    }

    @Override
    public String toString() {
        return "GameObject{" +
                "position=" + position +
                '}';
    }
}
