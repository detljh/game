package stickman.model;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import stickman.collision.CollisionStrategy;
import stickman.controller.HeroController;

import java.io.File;
import java.io.FileReader;
import java.net.URI;
import java.text.DecimalFormat;
import java.util.List;

public class GameEngineImpl implements GameEngine {
    public static final int FPS = (int) (1000 / 0.017) / 1000;
    private JSONObject configuration;
    private LevelImpl currentLevel;
    private String state = "";
    private int tick = 0;

    public GameEngineImpl(String fileName) {
        try {
            URI uri = new URI(this.getClass().getResource("/" + fileName).toString());
            JSONParser jp = new JSONParser();
            configuration = (JSONObject) jp.parse(new FileReader(new File(uri.getPath())));
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        List<MoveableEntity> moveableEntities = currentLevel.getMoveableEntities();
        for (int i = 0; i < moveableEntities.size(); i++) {
            MoveableEntity entity = moveableEntities.get(i);
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
        Hero hero = currentLevel.getHero();
        hc.setOnFloor(false);

//        // call hero controller tick to perform movements
//        currentLevel.getHero().getController().tick();
        for (MoveableEntity a : currentLevel.getMoveableEntities()) {
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

        for (MoveableEntity a : currentLevel.getMoveableEntities()) {
            if (a.getController() != null) {
                a.getController().move();
            }
        }

    }
}
