import gameObjects.*;
import game.Game;
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
import java.awt.geom.*;
import java.util.ArrayList;

public class TempMain extends Application {

    private ResizableCanvas canvas;
    private Tank tank1;
    private Game game;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene;

        BorderPane mainPane = new BorderPane();
        scene = new Scene(mainPane);
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());


        ArrayList<GameObject> gameObjects = new ArrayList<>();

        tank1 = new Tank(new Point2D.Double(0,0),0,100,Color.BLUE);

        gameObjects.add(tank1);
        gameObjects.add(new Tank(new Point2D.Double(200,200), 0, 200, Color.GREEN));
        Arena arena = new Arena(new Point2D.Double(0,0),400,400);
        game = new Game(arena,gameObjects);
        for (Wall wall : arena.getWalls()) {
            gameObjects.add(wall);
        }

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
        primaryStage.setTitle("GameObjects.Tank game.Game");
        primaryStage.show();
        draw(g2d);
    }

    private void draw(FXGraphics2D g2d){
        g2d.setTransform(new AffineTransform());
        g2d.setBackground(Color.white);
        g2d.clearRect(0, 0, (int)canvas.getWidth(), (int)canvas.getHeight());
        g2d.translate((int)canvas.getWidth()/2, (int)canvas.getHeight()/2);
        g2d.scale(1,-1);

        g2d.setColor(Color.BLUE);
        g2d.draw(new Line2D.Double(0,0,1000,0));
        g2d.setColor(Color.RED);
        g2d.draw(new Line2D.Double(0,0,0,1000));


        game.draw(g2d);

        g2d.setColor(Color.RED);
        g2d.fill(new Rectangle2D.Double(-1,-1, 2,2));

    }

    private void update(double deltaTime){
        game.update(deltaTime);
    }

    private void keyPressedHandle(KeyEvent e) {
//        System.out.println("key pressed: "+ e.getCharacter() + " :" + e.getCode());
        if(e.getCode() == KeyCode.W){
            tank1.setMovement(true,true);
        } else if(e.getCode() == KeyCode.S){
            tank1.setMovement(true,false);
        }else if(e.getCode() == KeyCode.A){
            tank1.setRotateLeft();
        }else if(e.getCode() == KeyCode.D){
            tank1.setRotateRight();
        }else if(e.getCode() == KeyCode.LEFT){
            tank1.setRotateTurretLeft();
        }else if(e.getCode() == KeyCode.RIGHT){
            tank1.setRotateTurretRight();
        }else if(e.getCode() == KeyCode.UP){
            tank1.fireBullet(game.getGameObjects());
        }
    }
    private void keyReleasedHandle(KeyEvent e) {
//        System.out.println("key released: "+ e.getCharacter() + " :" + e.getCode());
        if(e.getCode() == KeyCode.W || e.getCode() == KeyCode.S){
            tank1.stopMovement();
        }else if(e.getCode() == KeyCode.A || e.getCode() == KeyCode.D){
            tank1.stopRotate();
        }else if(e.getCode() == KeyCode.LEFT || e.getCode() == KeyCode.RIGHT){
            tank1.stopRotateTurret();
        }
    }


}