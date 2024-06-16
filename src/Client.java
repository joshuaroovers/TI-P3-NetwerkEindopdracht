import game.Arena;
import game.Game;
import game.KeyInput;
import game.MessageType;
import gameObjects.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.UUID;

public class Client extends Application {
    private ResizableCanvas canvas;
    private Game game;

    private KeyCode lastKeyPress;

    private FXGraphics2D g2d;
    private Socket serverSocket;
    private static ObjectOutputStream output;

    private UUID playerId;
    private ArrayList<UUID> players;
    private int tankColorStepIndex; //current selected color from Tank.tankColor.values()


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

        tankColorStepIndex = 0;

        players = new ArrayList<>();

        playerId = UUID.randomUUID();
        players.add(playerId);
        System.out.println(playerId);

        game = new Game(new Arena(new Point2D.Double(0,0), 1500, 800));

        Tank tank = new Tank(playerId, new Point2D.Double(0,0), Tank.tankColor.blue);
        game.addGameObject(tank);

        new Thread(this::handleConnection).start();

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


        primaryStage.setScene(scene);
        primaryStage.setTitle("Tank Game " + playerId.toString().substring(0,5));
        primaryStage.show();
        draw(g2d);

        primaryStage.setOnCloseRequest(t -> {
            if(serverSocket != null){
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            Platform.exit();
            System.exit(0);
        });
    }

    private void update(double v) {
        game.update(v);
    }


    private void handleConnection()
    {
        try
        {
            serverSocket = new Socket("localhost", 8000);

            output = new ObjectOutputStream(serverSocket.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(serverSocket.getInputStream());

            sendMessage(MessageType.NEW_TANK, game.getTank(playerId).getConstructorShell());

            while(serverSocket.isConnected())
            {

                MessageType messageType = (MessageType) input.readObject();
                System.out.println(messageType);
                switch (messageType){
                    case TANK_INPUT:
                        KeyInput keyInput = (KeyInput) input.readObject();
                        System.out.println("recieved input from: " + keyInput.playerId);
                        game.getTank(keyInput.playerId).handleKeyInput(keyInput, game);
                        break;
                    case NEW_TANK:
                        TankConstructorShell tankShell = (TankConstructorShell) input.readObject();
                        if(isNewPlayer(tankShell.playerId)){
                            Tank tank = new Tank(tankShell.playerId, tankShell.position, tankShell.tankRotation, tankShell.turretRotation, tankShell.tankColor);
                            game.addGameObject(tank);
                        }
                        break;
                    case FETCH_TANK:
                        sendMessage(MessageType.NEW_TANK, game.getTank(playerId).getConstructorShell());
                        break;
                    case UPDATE_COLOR:
                        TankConstructorShell tankShell2 = (TankConstructorShell) input.readObject();
                        game.getTank(tankShell2.playerId).setTankColor(tankShell2.tankColor);
                        break;
                }

                draw(g2d);
            }


        } catch (IOException | ClassNotFoundException e)
        {
            throw new RuntimeException(e);
        }
    }

    private synchronized void draw(FXGraphics2D g2d){
        g2d.setTransform(new AffineTransform());
        g2d.setBackground(Color.white);
        g2d.clearRect(0, 0, (int)canvas.getWidth(), (int)canvas.getHeight());
        g2d.translate((int)canvas.getWidth()/2, (int)canvas.getHeight()/2);
        g2d.scale(1,-1);


        game.draw(g2d);

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
                handleKeyInput(e.getCode(), true);
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
            handleKeyInput(e.getCode(), false);
        }else if(e.getCode() == KeyCode.SLASH){
            game.toggleDebug();
        }else if(e.getCode() == KeyCode.PERIOD){
            tankColorStepIndex++;
            if(tankColorStepIndex >= Tank.tankColor.values().length){
                tankColorStepIndex = 0;
            }
            game.getTank(playerId).setTankColor(Tank.tankColor.values()[tankColorStepIndex]);
            sendMessage(MessageType.UPDATE_COLOR, new TankConstructorShell(playerId, Tank.tankColor.values()[tankColorStepIndex]));
        }
    }

    private boolean isNewPlayer(UUID playerId){
        boolean isNewPlayer = true;
        for (UUID player : players) {
            if(player == playerId){
                isNewPlayer = false;
            }
        }
        return isNewPlayer;
    }

    private void handleKeyInput(KeyCode keyCode, boolean isPress){
        KeyInput keyInput = new KeyInput(playerId, keyCode, isPress);

        game.getTank(keyInput.playerId).handleKeyInput(keyInput, game);
        sendMessage(MessageType.TANK_INPUT, keyInput);
    }

    private void sendMessage(MessageType messageType, Object data){
            try {
                output.reset();
                output.writeObject(messageType);
                output.writeObject(data);
                output.flush();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
    }

}
