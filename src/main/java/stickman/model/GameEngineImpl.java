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

    @Override
    public void update() {
        double gravity = currentLevel.getGravity();
        List<MovableEntity> moveableEntities = currentLevel.getMovableEntities();
        for (int i = 0; i < moveableEntities.size(); i++) {
            MovableEntity entity = moveableEntities.get(i);
            entity.setYVel(entity.getYVel() + gravity);
            double yVel = entity.getYVel() / (FPS / 15);
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
        // call level's tick method to move clouds
        currentLevel.tick();

        HeroController hc = (HeroController) currentLevel.getHero().getController();
        hc.setOnFloor(false);

        for (MovableEntity a : currentLevel.getMovableEntities()) {
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

        for (MovableEntity a : currentLevel.getMovableEntities()) {
            if (a.getController() != null) {
                a.getController().move();
            }
        }
    }
}
