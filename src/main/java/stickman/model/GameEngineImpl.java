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
    private int tick;
    private static final double floorHeight = 350.0;

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
        walkFrame = 0;
        standFrame = 0;
        state = "stop";
        tick = 40;
        startLevel();

    }

    @Override
    public Level getCurrentLevel() {
        return currentLevel;
    }

    @Override
    public void startLevel() {
        currentLevel = new LevelImpl((double) stickmanPos.get("x"), stickmanSize, floorHeight);

        for (int i = 0; i < 5; i++) {
            currentLevel.addCloud(cloudVelocity);
        }
    }

    @Override
    public boolean jump() {
        state = "jump";

        double newY = currentLevel.getHero().getYPos() - 5.0;
        currentLevel.getHero().updateY(newY);

        return true;
    }

    @Override
    public boolean moveLeft() {
        state = "left";
        walkFrame = walkFrame % 4 + 5;
        String path = "ch_walk" + walkFrame + ".png";
        currentLevel.getHero().updateImagePath(path);
        double newX = currentLevel.getHeroX() - speed;
        currentLevel.getHero().updateX(newX);

        return true;
    }

    @Override
    public boolean moveRight() {
        state = "right";
        walkFrame = walkFrame % 4 + 1;
        String path = "ch_walk" + walkFrame + ".png";
        currentLevel.getHero().updateImagePath(path);
        double newX = currentLevel.getHeroX() + speed;
        currentLevel.getHero().updateX(newX);

        return true;
    }

    @Override
    public boolean stopMoving() {
        currentLevel.getHero().updateY(currentLevel.getFloorHeight() - currentLevel.getHero().getHeight());
        state = "stop";

        if (walkFrame <= 4) {
            standFrame = standFrame % 3 + 1;
            String path = "ch_stand" + standFrame + ".png";
            currentLevel.getHero().updateImagePath(path);
        } else {
            standFrame = standFrame % 3 + 4;
            String path = "ch_stand" + standFrame + ".png";
            currentLevel.getHero().updateImagePath(path);
        }

        return true;
    }

    @Override
    public void tick() {
        tick--;
        currentLevel.tick();
        if (state == "left") {
            if (tick > 33) {
                return;
            }

            tick = 40;
            moveLeft();
        } else if (state == "right") {
            if (tick > 33) {
                return;
            }

            tick = 40;
            moveRight();
        } else if (state == "jump") {
            if (tick > 35) {
                return;
            }

            tick = 40;
            jump();
        } else {
            if (tick > 0) {
                return;
            }

            tick = 40;
            stopMoving();
        }



    }
}
