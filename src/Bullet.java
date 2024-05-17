import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Bullet {
    private Point2D pos;
    private double direction;
    private int speed;
    private Rectangle2D hitbox;
    private int bounceCount;
    public Bullet(Point2D pos,double direction,int speed,Rectangle2D hitbox,int bounceCount){
        this.pos = pos;
        this.direction = direction;
        this.speed = speed;
        this.hitbox = hitbox;
        this.bounceCount = bounceCount;
    }
}
