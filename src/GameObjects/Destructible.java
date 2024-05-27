package GameObjects;

import java.util.ArrayList;

public interface Destructible {

    default void destroy(ArrayList<GameObject> gameObjects){
        gameObjects.remove(this);
    };
}
