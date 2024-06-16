import game.Game;
import gameObjects.Arena;
import gameObjects.GameObject;
import gameObjects.Tank;
import gameObjects.Wall;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

public class Client extends Application {
    private ResizableCanvas canvas;
    private Game game;

    private KeyCode lastKeyPress;

    private FXGraphics2D g2d;
    private Socket serverSocket;
    private static ObjectOutputStream output;


    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene;

        BorderPane mainPane = new BorderPane();
        scene = new Scene(mainPane);
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        g2d = new FXGraphics2D(canvas.getGraphicsContext2D());


        scene.setOnKeyPressed(e -> keyPressedHandle(e));
        scene.setOnKeyReleased(e -> keyReleasedHandle(e));


        new Thread(this::handleConnection).start();


        primaryStage.setScene(scene);
        primaryStage.setTitle("Tank Game");
        primaryStage.show();
        draw(g2d);

        primaryStage.setOnCloseRequest(t -> {
            try {
                serverSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Platform.exit();
            System.exit(0);
        });
    }


    private void handleConnection()
    {
        try
        {
            serverSocket = new Socket("localhost", 8000);

            output = new ObjectOutputStream(serverSocket.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(serverSocket.getInputStream());

            while(serverSocket.isConnected())
            {
                game = getGameUpdate(input);
                draw(g2d);
            }


        } catch (IOException | ClassNotFoundException e)
        {
            throw new RuntimeException(e);
        }
    }

    private synchronized Game getGameUpdate(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        return (Game) ois.readObject();
    }

    private synchronized void draw(FXGraphics2D g2d){
        g2d.setTransform(new AffineTransform());
        g2d.setBackground(Color.white);
        g2d.clearRect(0, 0, (int)canvas.getWidth(), (int)canvas.getHeight());
        g2d.translate((int)canvas.getWidth()/2, (int)canvas.getHeight()/2);
        g2d.scale(1,-1);

        g2d.setColor(Color.BLUE);
        g2d.draw(new Line2D.Double(0,0,1000,0));
        g2d.setColor(Color.RED);
        g2d.draw(new Line2D.Double(0,0,0,1000));


        if(game != null){
            game.draw(g2d);
        }


//        g2d.setColor(Color.RED);
//        g2d.fill(new Rectangle2D.Double(-1,-1, 2,2));
    }


    private void keyPressedHandle(KeyEvent e) {

        if(e.getCode() != lastKeyPress){
            if(e.getCode() == KeyCode.W || e.getCode() == KeyCode.S ||
                    e.getCode() == KeyCode.A ||
                    e.getCode() == KeyCode.D ||
                    e.getCode() == KeyCode.UP ||
                    e.getCode() == KeyCode.LEFT ||
                    e.getCode() == KeyCode.RIGHT){
                System.out.println("key pressed: "+ e.getCharacter() + " :" + e.getCode());
                lastKeyPress = e.getCode();
                sendKeyInput(e.getCode(), true);
            }
        }
    }
    private void keyReleasedHandle(KeyEvent e) {
        System.out.println("key released: "+ e.getCharacter() + " :" + e.getCode());
        if(e.getCode() == KeyCode.W ||
            e.getCode() == KeyCode.S ||
            e.getCode() == KeyCode.A ||
            e.getCode() == KeyCode.D ||
            e.getCode() == KeyCode.UP ||
            e.getCode() == KeyCode.LEFT ||
            e.getCode() == KeyCode.RIGHT) {
            if(lastKeyPress == e.getCode()){
                lastKeyPress = null;
            }
            sendKeyInput(e.getCode(), false);
        }
    }

    private void sendKeyInput(KeyCode keyCode, boolean isPress){
        try {
            KeyInput input = new KeyInput(keyCode, isPress);
            output.reset();
            output.writeObject(input);
            output.flush();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
