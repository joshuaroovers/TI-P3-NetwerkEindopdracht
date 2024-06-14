package gameObjects;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;

public class Arena implements Serializable {
    private final int wallWidth = 50;

    private Point2D topLeft;
    private Point2D topRight;
    private Point2D bottomLeft;
    private Point2D bottomRight;

    private ArrayList<Wall> walls;

    public Arena(Point2D position, int width, int height){
        topRight = new Point2D.Double(position.getX()+(width/2),position.getY()+(height/2));
        topLeft = new Point2D.Double(position.getX()-(width/2), position.getY()+(height/2));
        bottomLeft = new Point2D.Double(position.getX()-(width/2), position.getY()-(height/2));
        bottomRight = new Point2D.Double(position.getX()+(width/2), position.getY()-(height/2));

        walls = new ArrayList<>();

        walls.add(new Wall(new Point2D.Double(topLeft.getX(), topLeft.getY()), width, wallWidth));
        walls.add(new Wall(new Point2D.Double(bottomLeft.getX()-wallWidth,bottomLeft.getY()),wallWidth,height));
        walls.add(new Wall(new Point2D.Double(bottomRight.getX(),bottomRight.getY()),wallWidth,height));
        walls.add(new Wall(new Point2D.Double(bottomLeft.getX(),bottomLeft.getY()-wallWidth),width,wallWidth));
    }

    public ArrayList<Wall> getWalls(){
        return walls;
    }

}
