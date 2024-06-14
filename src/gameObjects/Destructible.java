package gameObjects;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public interface Destructible {

    default void destroy(CopyOnWriteArrayList<GameObject> gameObjects){
        gameObjects.remove(this);
    };
}
