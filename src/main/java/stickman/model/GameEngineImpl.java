package stickman.model;
import javafx.util.Duration;
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

    private static final int fps = (int) ((int)1000/0.017)/1000;

    // speed of hero movement
    private static final double velocityX = 200.0/fps;
    private static final double gravity = 0.3;
    // highest point of jump
    private static final double jumpHeight = 40.0;
    // speed of jump
    private static final double velocityY = velocityX * 2;

    // keep track of imagepath for animation
    private int walkFrame = 0;
    private int standFrame = 0;

    // store state of hero movement
    private String state = "stop";

    // slow down tick to every 3/4 of a second
    private int tick = (int) (fps*0.75);

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

        startLevel();
    }

    @Override
    public Level getCurrentLevel() {
        return currentLevel;
    }

    @Override
    public void startLevel() {
        currentLevel = new LevelImpl((double) stickmanPos.get("x"), stickmanSize, cloudVelocity, floorHeight, fps, 400.0, 640.0);

        // create initial clouds
        int cloudNumber = 20;
        for (int i = 0; i < cloudNumber; i++) {
            currentLevel.addCloud(true);
        }
    }

    @Override
    public boolean jump() {
        double heroY = currentLevel.getHero().getYPos();
        double heroHeight = currentLevel.getHero().getHeight();

        if (!state.contains("fall") && (heroY == floorHeight - heroHeight || heroY > floorHeight - heroHeight - jumpHeight)) {

            double newY = heroY - (velocityY * gravity) ;
            currentLevel.getHero().updateY(newY);
            if (state.equals("left") || state.equals("leftjump")) {
                currentLevel.getHero().updateX(currentLevel.getHeroX() - (velocityX*gravity));
                state = "leftjump";
            } else if (state.equals("right") || state.equals("rightjump")) {
                currentLevel.getHero().updateX(currentLevel.getHeroX() + (velocityX*gravity));
                state = "rightjump";
            }

            if (heroY != floorHeight - heroHeight) {
                return false;
            }
            return true;
        }

            double newY = heroY + (velocityY * gravity);

        if (state.equals("leftjump") || state.equals("leftfall")) {
            currentLevel.getHero().updateX(currentLevel.getHeroX() - (velocityX*gravity));
            state = "leftfall";
        } else if (state.equals("rightjump") || state.equals("rightfall")) {
            currentLevel.getHero().updateX(currentLevel.getHeroX() + (velocityX*gravity));
            state = "rightfall";
        } else {
            state = "fall";
        }

        if (newY >= floorHeight - heroHeight) {
            newY = floorHeight - heroHeight;
            if (state.equals("leftfall")) {
                state = "left";
            } else if (state.equals("rightfall")) {
                state = "right";
            } else {
                state = "stop";
            }
        }
        currentLevel.getHero().updateY(newY);
        return false;
    }

    @Override
    public boolean moveLeft() {
        if (currentLevel.getHero().getYPos() != currentLevel.getFloorHeight() - currentLevel.getHero().getHeight()) {
            if (state.contains("fall")) {
                state = "leftfall";
            } else {
                state = "leftjump";
            }
            return false;
        }
        state = "left";

        if (tick > (fps*0.60)) {
            return true;
        }
        tick = (int) (fps*0.75);

        // loop around walk frames facing left
        walkFrame = walkFrame % 4 + 5;
        String path = "ch_walk" + walkFrame + ".png";
        currentLevel.getHero().updateImagePath(path);

        double newX = currentLevel.getHeroX() - velocityX;
        currentLevel.getHero().updateX(newX);

        return true;
    }

    @Override
    public boolean moveRight() {
        if (currentLevel.getHero().getYPos() != currentLevel.getFloorHeight() - currentLevel.getHero().getHeight()) {
            if (state.contains("fall")) {
                state = "rightfall";
            } else {
                state = "rightjump";
            }
            return false;
        }
        state = "right";

        if (tick > (fps*0.65)) {
            return true;
        }
        tick = (int) (fps*0.75);

        // loop around walk frames going right
        walkFrame = walkFrame % 4 + 1;
        String path = "ch_walk" + walkFrame + ".png";
        currentLevel.getHero().updateImagePath(path);

        double newX = currentLevel.getHeroX() + velocityX;
        currentLevel.getHero().updateX(newX);

        return true;
    }

    @Override
    public boolean stopMoving() {
        // make sure hero is on the floor
        if (currentLevel.getHero().getYPos() != currentLevel.getFloorHeight() - currentLevel.getHero().getHeight()) {
            if (state.contains("fall")) {
                state = "fall";
            } else {
                state = "jump";
            }
            return false;
        }

        state = "stop";

        if (tick > 0) {
            return true;
        }
        tick = (int) (fps*0.75);

        // update stand frame based on direction
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
        // call level's tick method to move clouds
        currentLevel.tick();

        // tick is decreased until a movement is allowed so that animations are slowed down
        if (state.equals("left")) {
            moveLeft();
        } else if (state.equals("right")) {
            moveRight();
        } else if (state.contains("jump") || state.contains("fall")) {
            jump();
        } else {
            stopMoving();
        }
    }
}
