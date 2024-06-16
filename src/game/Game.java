package game;

import gameObjects.Arena;
import gameObjects.GameObject;
import gameObjects.Tank;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class Game implements Serializable {
    private CopyOnWriteArrayList<GameObject> gameObjects;
    private boolean debugActive;


    public Game(Arena arena) {
        this.debugActive = false;

        gameObjects = new CopyOnWriteArrayList<>();
        gameObjects.addAll(arena.getWalls());

        System.out.println("new game with: ");

        for (GameObject gameObject : gameObjects) {
            System.out.println(gameObject.toString());
        }
    }

    public void update(double time) {
//        System.out.println("updating gameObjects");
        for (GameObject gameObject : gameObjects) {
//            System.out.println("gameobject: " + gameObject);
            gameObject.update(time, gameObjects);
        }
    }

    public void draw(Graphics2D g2d) {
        for (GameObject gameObject : gameObjects) {
            gameObject.draw(g2d);
        }
    }

    public CopyOnWriteArrayList<GameObject> getGameObjects() {
        return gameObjects;
    }

    public void addGameObject(GameObject gameObject) {
        gameObjects.add(gameObject);
    }

    public Tank getTank(UUID playerId){
        Tank tank = null;
        for (GameObject gameObject : gameObjects) {
            if(gameObject.getClass() == Tank.class){
                System.out.println("looking for tank: " + playerId + " found: " + ((Tank) gameObject).playerId);
                System.out.println(((Tank) gameObject).playerId.equals(playerId));
                if(((Tank) gameObject).playerId.equals(playerId)){
                    System.out.println("found tank for " + playerId + ": "+ ((Tank) gameObject).playerId);
                    tank = (Tank) gameObject;
                }
            }
        }
        return tank;
    }

    public void toggleDebug(){
        this.debugActive = !debugActive;
        for (GameObject gameObject : gameObjects) {
            gameObject.setDebugActive(debugActive);
        }
    }

}
