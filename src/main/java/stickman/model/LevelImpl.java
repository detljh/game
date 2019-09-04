package stickman.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LevelImpl implements Level {
    private double gravity = 9.8 / GameEngineImpl.FPS;
    private List<Entity> entities;
    private double height;
    private double width;
    private double floorHeight;
    private Hero hero;
    private int tick;
    private double cloudVelocity;
    private int cloudNumber;


    LevelImpl(double heroX, String heroSize, double cloudVelocity, double width, double height) {
        entities = new ArrayList<>();
        floorHeight = height - height / 7;
        hero = new Hero("ch_stand1.png", heroX,200.0 / GameEngineImpl.FPS,
                200.0 / GameEngineImpl.FPS, heroSize, 80.0, floorHeight);
        entities.add(hero);

        // ignore negative cloud velocity
        this.cloudVelocity = Math.abs(cloudVelocity);
        this.width = width;
        this.height = height;

        // tick to spawn a new cloud once every time a cloud is a third across screen
        tick = (int) (((this.width / 3) / (cloudVelocity / GameEngineImpl.FPS)));

        // spawn initial number of clouds
        for (int i = 0; i < (int) width / 30; i++) {
            addCloud(true);
        }
    }

    double getGravity() {
        return gravity;
    }

    Hero getHero() {
        return hero;
    }

    void addCloud(boolean initial) {
        Random rand = new Random();
        // randomize cloud image used
        int cloudType = rand.nextInt(2) + 1;

        double xPos;
        /* if clouds are initial clouds, spawn them on screen, else spawn them off screen so they move in screen from
         * left of screen */
        if (initial) {
            xPos = (width * 4) - (rand.nextDouble() * (width * 4));
        } else {
            xPos = -(width / 4) - (rand.nextDouble() * (width / 4));
        }

        // randomise y position of cloud
        double yPos = rand.nextDouble() * (height / 4.5);
        String path = "cloud_" + cloudType + ".png";
        Cloud c = new Cloud(path, xPos, yPos, cloudVelocity);
        entities.add(c);
        cloudNumber++;
    }

    @Override
    public List<Entity> getEntities() {
        return entities;
    }

    @Override
    public double getHeight() {
        return height;
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public void tick() {
        moveRight();
        tick--;
        if (tick > 0) {
            return;
        }
        tick = (int) (GameEngineImpl.FPS * (this.width / cloudVelocity));

        // spawn 1-5 clouds if tick is 0
        int spawn = new Random().nextInt(5) + 1;
        for (int i = 0; i < spawn; i++) {
            addCloud(false);
        }
    }

    @Override
    public double getFloorHeight() {
        return floorHeight;
    }

    @Override
    public double getHeroX() {
        return hero.getXPos();
    }

    @Override
    public boolean jump() {
        return false;
    }

    @Override
    public boolean moveLeft() {
        return false;
    }

    @Override
    public boolean moveRight() {
        for (int i = 0; i < cloudNumber; i++) {
            Cloud c = (Cloud) entities.get(i + 1);
            c.updateX(c.getXPos() + (c.getVelocity() / GameEngineImpl.FPS));
        }
        return true;
    }

    @Override
    public boolean stopMoving() {
        return true;
    }
}
