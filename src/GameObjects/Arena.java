package GameObjects;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Arena extends GameObject{
    private final int wallWidth = 50;
    private Point2D topLeft;
    private Point2D topRight;
    private Point2D bottomLeft;
    private Point2D bottomRight;
    private ArrayList<Wall> walls;
    public Arena(int height, int width,Point2D position){
        this.width = width;
        this.height = height;
        this.position =  position;
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

    @Override
    public void update(double deltaTime) {

    }

    @Override
    public void draw(Graphics2D g2d) {
        AffineTransform tx = new AffineTransform();
        tx.translate(position.getX()-(width/2),position.getY()-(height/2));

        g2d.setColor(Color.black);

        g2d.draw(tx.createTransformedShape(new Rectangle2D.Double(0,0,width,height)));
        for (Wall wall : walls) {
            wall.draw(g2d);
        }

    }
}
