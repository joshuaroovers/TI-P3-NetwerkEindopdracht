package GameObjects;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Arena extends GameObject{
    private int wallWidth = 50;
    private Point2D topLeft;
    private Point2D topRight;
    private Point2D bottomLeft;
    private Point2D bottomRight;
    public Arena(int height, int width,Point2D position){
        this.width = width;
        this.height = height;
        this.position =  position;
        topRight = new Point2D.Double(position.getX()+(width/2),position.getY()+(height/2));
        topLeft = new Point2D.Double(position.getX()-(width/2), position.getY()+(height/2));
        bottomLeft = new Point2D.Double(position.getX()-(width/2), position.getY()-(height/2));
        bottomRight = new Point2D.Double(position.getX()+(width/2), position.getY()-(height/2));
    }

    @Override
    public void update(double deltaTime) {

    }

    @Override
    public void draw(Graphics2D g2d) {
        AffineTransform tx = new AffineTransform();
        tx.translate(position.getX()-(width/2),position.getY()-(height/2));
        g2d.setColor(Color.black);
        Rectangle2D topSide = new Rectangle2D.Double(topLeft.getX(),topLeft.getY(),height,wallWidth);
        Rectangle2D leftSide = new Rectangle2D.Double(bottomLeft.getX()-wallWidth,bottomLeft.getY(),wallWidth,height);
        Rectangle2D rightSide = new Rectangle2D.Double(bottomRight.getX(),bottomRight.getY(),wallWidth,height);
        Rectangle2D bottomSide = new Rectangle2D.Double(bottomLeft.getX(),bottomLeft.getY()-wallWidth,height,wallWidth);

        g2d.setColor(Color.black);
        g2d.draw(topSide);
        g2d.draw(leftSide);
        g2d.draw(rightSide);
        g2d.draw(bottomSide);
        g2d.draw(tx.createTransformedShape(new Rectangle2D.Double(0,0,width,height)));
        g2d.fill(topSide);
        g2d.fill(leftSide);
        g2d.fill(rightSide);
        g2d.fill(bottomSide);
    }
}
