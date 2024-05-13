import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class Main extends Application {

    private ResizableCanvas canvas;

    @Override
    public void start(Stage primaryStage) throws Exception {

        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());

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

        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setTitle("Tank Game");
        primaryStage.show();
        draw(g2d);
    }

    private void draw(FXGraphics2D g2d){
        g2d.setTransform(new AffineTransform());
        g2d.setBackground(Color.white);
        g2d.clearRect(0, 0, (int)canvas.getWidth(), (int)canvas.getHeight());
        g2d.translate((int)canvas.getWidth()/2, (int)canvas.getHeight()/2);

        g2d.setColor(Color.RED);
        g2d.fill(new Rectangle2D.Double(-50,-50, 100,100));

    }

    private void update(double deltaTime){

    }
}