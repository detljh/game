package stickman.model;

import javafx.geometry.Point2D;
import org.graalvm.compiler.lir.sparc.SPARCMove;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import stickman.controller.HeroController;

import java.io.File;
import java.io.FileReader;
import java.net.URI;
import java.text.DecimalFormat;

public class GameEngineImpl implements GameEngine {
    public static final int FPS = (int) (1000 / 0.017) / 1000;
    private JSONObject configuration;
    private LevelImpl currentLevel;
    private static int lives = 3;
    private String finish = "";
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
        .addEnemy((JSONArray) configuration.get("enemyType"))
        .spawnClouds((Double) configuration.get("cloudVelocity"))
        .addFinish((JSONObject) configuration.get("finish"))
        .build();
    }

    @Override
    public String finish() {
        return finish;
    }

    @Override
    public int getLives() {
        return lives;
    }

    @Override
    public boolean checkCollision(Entity a, Entity other) {
        if (a.equals(other)) {
            return false;
        }

        double width;
        if (a.equals(currentLevel.getHero())) {
            width = a.getWidth() / 2;
        } else {
            width = a.getWidth();
        }

        return (a.getDesiredX() < (other.getXPos() + other.getWidth())) &&
                ((a.getDesiredX() + width) > other.getXPos()) &&
                (a.getDesiredY() < (other.getYPos() + other.getHeight())) &&
                ((a.getDesiredY() + a.getHeight()) > other.getYPos());
    }

    @Override
    public void handleCollision(Entity a, Entity other) {
        if (other.equals(currentLevel.getFinish())) {
            finish = "won";
            return;
        }

        Point2D aPos = new Point2D(a.getDesiredX(), a.getDesiredY());
        Point2D otherPos = new Point2D(other.getXPos(), other.getYPos());

        Point2D collisionVector = otherPos.subtract(aPos);
        collisionVector = collisionVector.normalize();
        double width;
        if (a.equals(currentLevel.getHero())) {
            width = a.getWidth() / 2;
        } else {
            width = a.getWidth();
        }

        if (Math.abs(collisionVector.getX()) > Math.abs(collisionVector.getY())) {
            if (collisionVector.getX() < 0) {
                a.setDesiredX(other.getXPos() + other.getWidth());
                a.setXVel(0);
            } else {
                a.setDesiredX(other.getXPos() - width);
                a.setXVel(0);
            }
        } else {
            if (collisionVector.getY() > 0) {
                a.setDesiredY(other.getYPos() - a.getHeight());
                a.setYVel(0);
                if (a.equals(currentLevel.getHero())) {
                    HeroController aC = (HeroController) a.getController();
                    aC.setOnFloor(true);
                    aC.setJump(false);
                }

            } else {
                a.setDesiredY(other.getYPos() + other.getHeight());
                a.setYVel(0);
                if (a.equals(currentLevel.getHero())) {
                    HeroController aC = (HeroController) a.getController();
                    aC.setJump(false);
                }
            }
        }
    }

    @Override
    public void update() {
        double gravity = currentLevel.getGravity();
        Hero hero = currentLevel.getHero();
        hero.setYVel(hero.getYVel() + gravity);
        double yVel = hero.getYVel() / (FPS / 15);
        hero.setDesiredY(hero.getYPos() + yVel);
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

        // call hero controller tick to perform movements
        currentLevel.getHero().getController().tick();
        for (Entity a : currentLevel.getEntities()) {
            for (Entity other : currentLevel.getEntities()) {
                if (checkCollision(a, other)) {
                    if (currentLevel.isEnemy(other)) {
                        lives--;
                        if (lives == 0) {
                            finish = "lost";
                        }
                    }
                    handleCollision(a, other);
                    break;
                }
            }
        }

        hc.move();
    }
}
