import GameObjects.Arena;
import GameObjects.GameObject;

import java.util.ArrayList;

public class Game {
    private Arena arena;
    private ArrayList<GameObject> gameObjects;

    public Game(Arena arena,ArrayList<GameObject> gameObjects){
        this.arena = arena;
         for (GameObject gameObject : arena.getWalls()) {
            gameObjects.add(gameObject);
        }
        this.gameObjects = gameObjects;
    }
}
