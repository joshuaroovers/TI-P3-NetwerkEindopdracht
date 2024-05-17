import GameObjects.Tank;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class TempMain extends Application {

    private ResizableCanvas canvas;
    private Tank tank1;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene;

        BorderPane mainPane = new BorderPane();
        scene = new Scene(mainPane);
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());

        tank1 = new Tank(new Point2D.Double(0,0),0,100,Color.BLUE);

        new AnimationTimer() {
            long last = -1;

            @Override
            public void handle(long now)
            {
                if (last == -1)
                    last = now;
                update((now - last) / 1000000000.0);
                last = now;
                draw(g2d);
            }
        }.start();

        scene.setOnKeyPressed(e -> keyPressedHandle(e));
        scene.setOnKeyReleased(e -> keyReleasedHandle(e));

        primaryStage.setScene(scene);
        primaryStage.setTitle("GameObjects.Tank Game");
        primaryStage.show();
        draw(g2d);
    }

    private void draw(FXGraphics2D g2d){
        g2d.setTransform(new AffineTransform());
        g2d.setBackground(Color.white);
        g2d.clearRect(0, 0, (int)canvas.getWidth(), (int)canvas.getHeight());
        g2d.translate((int)canvas.getWidth()/2, (int)canvas.getHeight()/2);

        tank1.draw(g2d);

        g2d.setColor(Color.RED);
        g2d.fill(new Rectangle2D.Double(-25,-25, 50,50));

    }

    private void update(double deltaTime){
        tank1.update(deltaTime);
    }

    private void keyPressedHandle(KeyEvent e) {
        System.out.println("key pressed: "+ e.getCharacter() + " :" + e.getCode());
        if(e.getCode() == KeyCode.W){
            tank1.setMovement(true,true);
        } else if(e.getCode() == KeyCode.S){
            tank1.setMovement(true,false);
        }
    }
    private void keyReleasedHandle(KeyEvent e) {
        System.out.println("key released: "+ e.getCharacter() + " :" + e.getCode());
        if(e.getCode() == KeyCode.W || e.getCode() == KeyCode.S){
            tank1.stopMovement();
        }
    }


}