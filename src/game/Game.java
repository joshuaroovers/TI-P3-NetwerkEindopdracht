package game;

import gameObjects.Arena;
import gameObjects.GameObject;
import gameObjects.GameObject;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Game {
    private Arena arena;
    private ArrayList<GameObject> gameObjects;
    private BufferedImage image;

    public Game(Arena arena, ArrayList<GameObject> gameObjects){
        this.arena = arena;
         for (GameObject gameObject : arena.getWalls()) {
            gameObjects.add(gameObject);
        }
        this.gameObjects = gameObjects;

//        try {
//            BufferedImage tempImage = ImageIO.read(new FileInputStream("resources/tankBody_blue.png"));
//            this.image = tempImage;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public void update(double time) {
        for (GameObject gameObject : gameObjects) {
            gameObject.update(time, gameObjects, this);
        }
    }

    public void draw(Graphics2D g2d) {
        for (GameObject gameObject : gameObjects) {
            gameObject.draw(g2d);
        }


//        AffineTransform tx = new AffineTransform();
//        tx.translate(-image.getWidth()/2,-image.getHeight()/2);
//        g2d.drawImage(image, tx,null);
    }

    public ArrayList<GameObject> getGameObjects() {
        return gameObjects;
    }
}