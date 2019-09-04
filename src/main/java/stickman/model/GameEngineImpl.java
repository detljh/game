package stickman.model;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.net.URI;

public class GameEngineImpl implements GameEngine {
    static final int FPS = (int) (1000 / 0.017) / 1000;
    private JSONObject configuration;
    // initial x position of stickman
    private JSONObject stickmanPos;
    private String stickmanSize;
    private double cloudVelocity;
    private LevelImpl currentLevel;
    // keep track of imagepath frame for animation
    private int walkFrame = 0;
    private int standFrame = 0;
    // store state of hero movement, initially not moving
    private String state = "stop";
    // slow down tick to every 3/4 of a second
    private int tick = (int) (FPS * 0.75);

    public GameEngineImpl(String fileName) {
        try {
            URI uri = new URI(this.getClass().getResource("/" + fileName).toString());
            JSONParser jp = new JSONParser();
            configuration = (JSONObject) jp.parse(new FileReader(new File(uri.getPath())));
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
        // level dimensions
        double width = 640.0;
        double height = 400.0;
        currentLevel = new LevelImpl((double) stickmanPos.get("x"), stickmanSize, cloudVelocity, width, height);
    }

    @Override
    public boolean jump() {
        Hero hero = currentLevel.getHero();
        // get current hero y position
        double heroY = hero.getYPos();
        double heroHeight = hero.getHeight();
        double floorHeight = currentLevel.getFloorHeight() - heroHeight;
        double jumpHeight = hero.getJumpHeight();
        double gravity = currentLevel.getGravity();

        // if hero is on the floor
        if (heroY >= floorHeight) {
            // if hero is landing on floor
            if (state.contains("fall")) {
                // if hero was moving left, continue moving left
                if (state.equals("leftfall")) {
                    state = "left";
                // if hero was moving right, continue right
                } else if (state.equals("rightfall")) {
                    state = "right";
                } else {
                    state = "stop";
                }

                // end of jump, reset vertical and horizontal velocity
                hero.resetVelocityY();
                hero.resetVelocityX();
                // set hero's y position to be on the floor
                hero.updateY(floorHeight);
                return false;
            }

            // change x position according to previous move
            if (state.equals("left") || state.equals("leftjump")) {
                hero.updateX(currentLevel.getHeroX() - hero.getVelocityX());
                state = "leftjump";
            } else if (state.equals("right") || state.equals("rightjump")) {
                hero.updateX(currentLevel.getHeroX() + hero.getVelocityX());
                state = "rightjump";
            } else {
                state = "jump";
            }

            // initial jump, slow down x velocity
            hero.updateVelocityX(hero.getVelocityX() / 3);
            // make velocity negative so that hero jumps, add gravity to a negative velocity to decrease the amount of
            // y displacement
            hero.updateVelocityY((hero.getVelocityY() * -1) + gravity);
            heroY += hero.getVelocityY();
            hero.updateY(heroY);
            return true;
        } else {
            // if hero is jumping and hits the max jump height, make velocity positive again so hero falls
            if ((hero.getVelocityY() > 0) && (heroY < (floorHeight - jumpHeight))) {
                hero.updateVelocityY((hero.getVelocityY() * -1));
            }

            if (state.equals("leftjump") || state.equals("leftfall")) {
                currentLevel.getHero().updateX(currentLevel.getHeroX() - hero.getVelocityX());
                state = "leftfall";
            } else if (state.equals("rightjump") || state.equals("rightfall")) {
                currentLevel.getHero().updateX(currentLevel.getHeroX() + hero.getVelocityX());
                state = "rightfall";
            } else {
                state = "fall";
            }

            // increase positive velocity by gravity so hero falls quicker
            hero.updateVelocityY(hero.getVelocityY() + gravity);
            heroY += hero.getVelocityY();
            hero.updateY(heroY);
            return false;
        }
    }

    @Override
    public boolean moveLeft() {
        // if hero is not on the floor, hero is moving left in the air
        if (currentLevel.getHero().getYPos() < currentLevel.getFloorHeight() - currentLevel.getHero().getHeight()) {
            if (state.contains("fall")) {
                state = "leftfall";
            } else {
                state = "leftjump";
            }
            return false;
        }
        state = "left";

        // slow down animation
        if (tick > (FPS * 0.60)) {
            return true;
        }
        tick = (int) (FPS * 0.75);

        // loop around walk frames facing left
        walkFrame = walkFrame % 4 + 5;
        String path = "ch_walk" + walkFrame + ".png";
        currentLevel.getHero().updateImagePath(path);

        double newX = currentLevel.getHeroX() - currentLevel.getHero().getVelocityX();
        currentLevel.getHero().updateX(newX);
        return true;
    }

    @Override
    public boolean moveRight() {
        // if hero is not on floor, hero is moving right in the air
        if (currentLevel.getHero().getYPos() < currentLevel.getFloorHeight() - currentLevel.getHero().getHeight()) {
            if (state.contains("fall")) {
                state = "rightfall";
            } else {
                state = "rightjump";
            }
            return false;
        }
        state = "right";

        // slow down animation
        if (tick > (FPS * 0.65)) {
            return true;
        }
        tick = (int) (FPS * 0.75);

        // loop around walk frames going right
        walkFrame = walkFrame % 4 + 1;
        String path = "ch_walk" + walkFrame + ".png";
        currentLevel.getHero().updateImagePath(path);

        double newX = currentLevel.getHeroX() + currentLevel.getHero().getVelocityX();
        currentLevel.getHero().updateX(newX);
        return true;
    }

    @Override
    public boolean stopMoving() {
        // if hero is not on floor, then either falling or jumping in place
        if (currentLevel.getHero().getYPos() < currentLevel.getFloorHeight() - currentLevel.getHero().getHeight()) {
            if (state.contains("fall")) {
                state = "fall";
            } else {
                state = "jump";
            }
            return false;
        }

        state = "stop";

        // slow down animation
        if (tick > 0) {
            return true;
        }
        tick = (int) (FPS * 0.75);

        // update stand frame based on direction hero was previously walking in
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
        // tick is decreased to slow down animations
        tick--;
        // call level's tick method to move clouds
        currentLevel.tick();
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
