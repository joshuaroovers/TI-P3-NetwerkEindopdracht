package game;

import javafx.scene.input.KeyCode;

import java.io.Serializable;
import java.util.UUID;

public class KeyInput implements Serializable {
    public UUID playerId;
    public KeyCode keyCode;
    public boolean isPress;

    public KeyInput(UUID playerId, KeyCode keyCode, boolean isPress) {
        this.playerId = playerId;
        this.keyCode = keyCode;
        this.isPress = isPress;
    }

    @Override
    public String toString() {
        return "game.KeyInput{" +
                "keyCode=" + keyCode +
                ", isPress=" + isPress +
                '}';
    }
}
