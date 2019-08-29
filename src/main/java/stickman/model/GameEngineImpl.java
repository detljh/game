package stickman.model;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.nio.file.Files;
import java.nio.file.Paths;

public class GameEngineImpl implements GameEngine {
    private JSONObject configuration;
    private JSONObject stickmanPos;
    private double cloudVelocity;
    private String stickmanSize;
    private LevelImpl currentLevel;
    private static final double speed = 5.0;
    private int walkFrame;
    private int standFrame;
    private String state;

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
        walkFrame = 0;
        standFrame = 0;
        currentLevel = new LevelImpl((double) stickmanPos.get("x"), stickmanSize, floorHeight);
        state = "stop";
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
        double newY = currentLevel.getHero().getYPos() - 5.0;
        currentLevel.getHero().updateY(newY);
        state = "jump";
        return true;
    }

    @Override
    public boolean moveLeft() {
        walkFrame = walkFrame % 4 + 5;
        String path = "ch_walk" + walkFrame + ".png";
        currentLevel.getHero().updateImagePath(path);
        double newX = currentLevel.getHeroX() - speed;
        currentLevel.getHero().updateX(newX);
        state = "left";
        return true;
    }

    @Override
    public boolean moveRight() {
        walkFrame = walkFrame % 4 + 1;
        String path = "ch_walk" + walkFrame + ".png";
        currentLevel.getHero().updateImagePath(path);
        double newX = currentLevel.getHeroX() + speed;
        currentLevel.getHero().updateX(newX);
        state = "right";
        return true;
    }

    @Override
    public boolean stopMoving() {
        currentLevel.getHero().updateY(currentLevel.getFloorHeight() - currentLevel.getHero().getHeight());
        if (walkFrame <= 4) {
            standFrame = standFrame % 3 + 1;
            String path = "ch_stand" + standFrame + ".png";
            currentLevel.getHero().updateImagePath(path);
        } else {
            standFrame = standFrame % 3 + 4;
            String path = "ch_stand" + standFrame + ".png";
            currentLevel.getHero().updateImagePath(path);
        }
        state = "stop";
        return true;
    }

    @Override
    public void tick() {
        if (state == "left") {
            moveLeft();
        } else if (state == "right") {
            moveRight();
        } else if (state == "jump") {
            jump();
        } else {
            stopMoving();
        }



    }
}
