package stickman.view;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import stickman.controller.HeroController;

import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class KeyboardInputHandler {
    private final HeroController hc;
    private boolean left = false;
    private boolean right = false;
    private Set<KeyCode> pressedKeys = new HashSet<>();
    private Map<String, MediaPlayer> sounds = new HashMap<>();

    KeyboardInputHandler(HeroController hc) {
        this.hc = hc;

        URL mediaUrl = getClass().getResource("/jump.wav");
        String jumpURL = mediaUrl.toExternalForm();

        Media sound = new Media(jumpURL);
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        sounds.put("jump", mediaPlayer);
    }

    void handlePressed(KeyEvent keyEvent) {
        // if up has already been pressed
        if (pressedKeys.contains(keyEvent.getCode())) {
            // and is still being pressed, attempt to jump again
            if (keyEvent.getCode().equals(KeyCode.UP)) {
                if (hc.jump()) {
                    MediaPlayer jumpPlayer = sounds.get("jump");
                    jumpPlayer.stop();
                    jumpPlayer.play();
                }
            }
            return;
        }
        pressedKeys.add(keyEvent.getCode());

        if (keyEvent.getCode().equals(KeyCode.UP)) {
            if (hc.jump()) {
                MediaPlayer jumpPlayer = sounds.get("jump");
                jumpPlayer.stop();
                jumpPlayer.play();
            }
        }

        if (keyEvent.getCode().equals(KeyCode.LEFT)) {
            left = true;
        }
        else if (keyEvent.getCode().equals(KeyCode.RIGHT)) {
            right = true;
        } else {
            return;
        }

        if (left) {
            if (right) {
                hc.stopMoving();
            } else {
                hc.moveLeft();
            }
        } else {
            hc.moveRight();
        }
    }

    void handleReleased(KeyEvent keyEvent) {
        pressedKeys.remove(keyEvent.getCode());

        if (keyEvent.getCode().equals(KeyCode.LEFT)) {
            left = false;
        }
        else if (keyEvent.getCode().equals(KeyCode.RIGHT)) {
            right = false;
        } else {
            return;
        }

        if (!(right || left)) {
            hc.stopMoving();
        } else if (right) {
            hc.moveRight();
        } else {
            hc.moveLeft();
        }
    }
}
