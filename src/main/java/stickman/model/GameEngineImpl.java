package stickman.model;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.nio.file.Files;
import java.nio.file.Paths;

public class GameEngineImpl implements GameEngine {
    private JSONObject configuration;
    private JSONObject stickmanPos;
    private double cloudVelocity;
    private String stickmanSize;
    private Level currentLevel;

    public GameEngineImpl(String fileName) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(fileName)));
            JSONParser jp = new JSONParser();
            configuration = (JSONObject) jp.parse(content);
        } catch (Exception e) {
            e.printStackTrace();
        }

        stickmanPos = (JSONObject) configuration.get("stickmanPos");
        cloudVelocity = (double) configuration.get("cloudVelocity");
        stickmanSize = (String) configuration.get("stickmanSize");
        double floorHeight = (double) configuration.get("floorHeight");

        currentLevel = new LevelImpl((double) stickmanPos.get("x"), stickmanSize, floorHeight);
    }

    @Override
    public Level getCurrentLevel() {
        return currentLevel;
    }

    @Override
    public void startLevel() {

    }

    @Override
    public boolean jump() {
        return false;
    }

    @Override
    public boolean moveLeft() {
        System.out.println("left");
        return false;
    }

    @Override
    public boolean moveRight() {
        return false;
    }

    @Override
    public boolean stopMoving() {
        return false;
    }

    @Override
    public void tick() {

    }
}
