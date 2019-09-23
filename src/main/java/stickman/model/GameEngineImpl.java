package stickman.model;

import javafx.geometry.Point2D;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import stickman.controller.Controller;
import stickman.controller.HeroController;

import java.io.File;
import java.io.FileReader;
import java.net.URI;

public class GameEngineImpl implements GameEngine {
    public static final int FPS = (int) (1000 / 0.017) / 1000;
    private JSONObject configuration;
    private LevelImpl currentLevel;
    private static int lives = 3;

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
        .build();
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
        System.out.println(collisionVector);
        if (Math.abs(collisionVector.getX()) > Math.abs(collisionVector.getY())) {
            if (collisionVector.getX() < 0) {
                a.setDesiredX(other.getXPos() + other.getWidth());
                a.setXVel(0);
                System.out.println("left");
            } else {
                a.setDesiredX(other.getXPos() - width);
                a.setXVel(0);
                System.out.println("right");
            }
        } else {
            if (collisionVector.getY() > 0) {
                a.setDesiredY(other.getYPos() - a.getHeight());
                a.setYVel(0);
                if (a.equals(currentLevel.getHero())) {
                    HeroController aC = (HeroController) a.getController();
                    aC.setOnFloor(true);
                    aC.setJump(false);
                    System.out.println("down");
                }

            } else {
                a.setDesiredY(other.getYPos() + other.getHeight());
                a.setYVel(0);
                if (a.equals(currentLevel.getHero())) {
                    HeroController aC = (HeroController) a.getController();
                    aC.setJump(false);
                }
                System.out.println("up");
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
    public void tick() {
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
                    handleCollision(a, other);
                    break;
                }
            }
        }

        hc.move();
    }
}
