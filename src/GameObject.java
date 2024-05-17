import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class GameObject {
    private Point2D pos;
    private double direction;
    private Rectangle2D hitbox;
    public GameObject(Point2D pos, double direction,Rectangle2D hitbox){
        this.pos = pos;
        this.direction = direction;
        this.hitbox = hitbox;
    }
}
