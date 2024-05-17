import java.awt.*;
import java.awt.geom.Point2D;

public class Tank {
    private Point2D pos;
    private double direction;
    private String playerName;
    private Color color;
    public Tank(Point2D pos, double direction,String playerName,Color color){
        this.pos = pos;
        this.direction = direction;
        this.playerName = playerName;
        this.color = color;
    }
    public void update(){
    }
}
