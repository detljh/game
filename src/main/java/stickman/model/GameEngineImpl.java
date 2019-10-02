package stickman.model;

import org.json.simple.JSONObject;
import stickman.collision.CollisionStrategy;
import stickman.controller.HeroController;

import java.text.DecimalFormat;
import java.util.List;

public class GameEngineImpl implements GameEngine {
    public static final int FPS = (int) (1000 / 0.017) / 1000;
    private JSONObject configuration;
    private LevelImpl currentLevel;
    // store state of game [won, lost], initially empty
    private String state = "";
    private int tick = 0;

    public GameEngineImpl(JSONObject file) {
        configuration = file;
        startLevel();
    }

    @Override
    public Level getCurrentLevel() {
        return currentLevel;
    }

    @Override
    public void startLevel() {
        // call builder constructor here and build each entity/platform through config file
        currentLevel = new LevelImpl.LevelBuilder()
        .setHeight((double) configuration.get("height"))
        .setWidth((double) configuration.get("width"))
        .setFloorHeight((double) configuration.get("floorHeight"))
        .addPlatform((JSONObject) configuration.get("platform"))
        .addHero((JSONObject) configuration.get("stickman"))
        .addEnemy((JSONObject) configuration.get("enemy"))
        .spawnClouds((Double) configuration.get("cloudVelocity"))
        .addFinish((JSONObject) configuration.get("finish"))
        .build();
    }

    @Override
    public String getState() {
        return state;
    }

    @Override
    public int getLives() {
        return Math.max(currentLevel.getHero().getRemainingLives(), 0);
    }

    /** Updates all movable entities with the force of gravity (towards floor) */
    @Override
    public void update() {
        double gravity = currentLevel.getGravity();
        List<MovableEntity> moveableEntities = currentLevel.getMovableEntities();
        for (MovableEntity entity : moveableEntities) {
            entity.setYVel(entity.getYVel() + gravity);
            double yVel = entity.getYVel() / FPS;
            entity.setDesiredY(entity.getYPos() + yVel);
        }
    }

    @Override
    public String getTime() {
        DecimalFormat time = new DecimalFormat("#.00");
        return time.format(tick * 0.017);
    }

    @Override
    public void tick() {
        tick++;
        update();
        currentLevel.tick();

        HeroController hc = (HeroController) currentLevel.getHero().getController();
        // set hero to not be on floor by default every tick to be updated by hero's movements
        hc.setOnFloor(false);
        for (MovableEntity a : currentLevel.getMovableEntities()) {
            // call each entity's controller for their movements
            a.getController().tick();
            for (Entity other : currentLevel.getEntities()) {
                CollisionStrategy strat = a.getCollisionStrategy();
                if (strat.checkCollision(a, other)) {
                     String out = strat.handleCollision(a, other);
                     if (out != null) {
                         state = out;
                     }
                }
            }
        }

        // remove entities that were added to the remove list
        currentLevel.clearRemoveEntities();

        // move all movable entities
        for (MovableEntity a : currentLevel.getMovableEntities()) {
            a.getController().move();
        }
    }
}
