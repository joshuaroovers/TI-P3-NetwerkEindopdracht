package game;

import gameObjects.GameObject;
import gameObjects.Tank;

import java.awt.*;
import java.awt.geom.Line2D;
import java.io.Serializable;
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
        if(debugActive){
            g2d.setColor(Color.BLUE);
            g2d.draw(new Line2D.Double(0,0,1000,0));
            g2d.setColor(Color.RED);
            g2d.draw(new Line2D.Double(0,0,0,1000));
        }

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
        return (Tank) gameObjects.stream().
                filter(p -> p.getClass().equals(Tank.class)).
                filter(p -> ((Tank) p).playerId.equals(playerId)).
                findFirst().get();
    }

    public void toggleDebug(){
        this.debugActive = !debugActive;
        for (GameObject gameObject : gameObjects) {
            gameObject.setDebugActive(debugActive);
        }
    }

}
