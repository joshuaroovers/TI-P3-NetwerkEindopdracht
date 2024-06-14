import javafx.scene.input.KeyCode;

import java.io.Serializable;

public class KeyInput implements Serializable {
    public KeyCode keyCode;
    public boolean isPress;

    public KeyInput(KeyCode keyCode, boolean isPress) {
        this.keyCode = keyCode;
        this.isPress = isPress;
    }

    @Override
    public String toString() {
        return "KeyInput{" +
                "keyCode=" + keyCode +
                ", isPress=" + isPress +
                '}';
    }
}
