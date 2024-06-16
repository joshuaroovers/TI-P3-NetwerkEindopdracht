package game;

import gameObjects.Arena;
import gameObjects.GameObject;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class Game implements Serializable {
    private CopyOnWriteArrayList<GameObject> gameObjects;
    private BufferedImage image;

    public Game(Arena arena, ArrayList<GameObject> gameObjects){
        this.gameObjects = new CopyOnWriteArrayList<>();
         for (GameObject gameObject : arena.getWalls()) {
            gameObjects.add(gameObject);
        }
        this.gameObjects.addAll(gameObjects);

    }

    public Game(Arena arena) {
        gameObjects = new CopyOnWriteArrayList<>();
        gameObjects.addAll(arena.getWalls());

        System.out.println("new game with: ");

        for (GameObject gameObject : gameObjects) {
            System.out.println(gameObject.toString());
        }
    }

    public synchronized void update(double time) {
//        System.out.println("updating gameObjects");
        for (GameObject gameObject : gameObjects) {
//            System.out.println("gameobject: " + gameObject);
            gameObject.update(time, gameObjects, this);
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

    public synchronized void addGameObject(GameObject gameObject) {
        gameObjects.add(gameObject);
    }

}
