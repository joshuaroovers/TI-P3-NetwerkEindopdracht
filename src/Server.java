import game.Game;
import gameObjects.Arena;
import gameObjects.Tank;
import javafx.scene.input.KeyCode;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {

    private static ServerSocket serverSocket;
    private static Game game;
//    private static Game updatedGame;
//    private static Game clientGame;
//    private static boolean readyUpdateGame;
    private static CopyOnWriteArrayList<Socket> clients;
    private static ConcurrentHashMap<Socket, ObjectOutputStream> oos;

    public static void main(String[] args) {
        clients = new CopyOnWriteArrayList<>();
        oos = new ConcurrentHashMap<>();

        try {
            game = new Game(new Arena(new Point2D.Double(0,0), 500, 500));
//            updatedGame = game;
            serverSocket = new ServerSocket(8000);

            new Thread(Server::updateGame).start();

            while (true) {
                Socket client = serverSocket.accept();

                new Thread(() -> getClientUpdate(client)).start();

                oos.put(client, new ObjectOutputStream(client.getOutputStream()));
                clients.add(client);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void updateGame(){
        while(true) {
            game.update(0.001);

            for (Socket client : clients) {
                try {

                    if(client.isConnected()) {

                        ObjectOutputStream clientOos = oos.get(client);

//                        System.out.println("Server updating:\t" +client);
                        clientOos.reset();
                        clientOos.writeObject(game);
                        clientOos.flush();
                    }else{
                        client.close();
                        clients.remove(client);
                        oos.remove(client);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void getClientUpdate(Socket client) {
        try {
            ObjectInputStream ois = new ObjectInputStream(client.getInputStream());

            Tank tank = new Tank(/*game.getAvailableSpawnPoint()*/new Point2D.Double(0,0), Color.BLUE, "blue");

            game.addGameObject(tank);
//            readyUpdateGame = true;

            while (client.isConnected()) {
//                System.out.println("Server receiving data from:\t" +client);
                KeyInput keyInput = (KeyInput) ois.readObject();

//                System.out.println(keyInput);
                if(keyInput.isPress){
                    if(keyInput.keyCode == KeyCode.W){
                        tank.setMovement(true,true);
                    } else if(keyInput.keyCode == KeyCode.S){
                        tank.setMovement(true,false);
                    }else if(keyInput.keyCode == KeyCode.A){
                        tank.setRotateLeft();
                    }else if(keyInput.keyCode == KeyCode.D){
                        tank.setRotateRight();
                    }else if(keyInput.keyCode == KeyCode.LEFT){
                        tank.setRotateTurretLeft();
                    }else if(keyInput.keyCode == KeyCode.RIGHT){
                        tank.setRotateTurretRight();
                    }else if(keyInput.keyCode == KeyCode.UP){
                        tank.fireBullet(game.getGameObjects());
                    }
                }
                else{
                    if(keyInput.keyCode == KeyCode.W || keyInput.keyCode == KeyCode.S){
                        tank.stopMovement();
                    }else if(keyInput.keyCode == KeyCode.A || keyInput.keyCode == KeyCode.D){
                        tank.stopRotate();
                    }else if(keyInput.keyCode == KeyCode.LEFT || keyInput.keyCode == KeyCode.RIGHT){
                        tank.stopRotateTurret();
                    }
                }

            }

            client.close();
            clients.remove(client);
            oos.remove(client);
//            tank.destroy(game.getGameObjects());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void updateClient(Socket client) {
        try {
            System.out.println("Got connnection from " + client.getInetAddress().getHostAddress());
            ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());

            while (client.isConnected()) {
//                System.out.println("Server updating:\t" +client);
                oos.reset();
                oos.writeObject(getGame());
                oos.flush();
            }

            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Game getGame() {
        return game;
    }
}
