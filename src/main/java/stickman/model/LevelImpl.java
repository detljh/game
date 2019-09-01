package stickman.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LevelImpl implements Level {
    private List<Entity> entities;
    private double height;
    private double width;
    private double floorHeight;
    private Hero hero;
    private int tick;
    private double cloudVelocity;
    private int cloudNumber;
    private int fps;

    public LevelImpl(double heroX, String heroSize, double cloudVelocity, double floorHeight, int fps, double width, double height) {
        entities = new ArrayList<Entity>();
        this.floorHeight = floorHeight;
        hero = new Hero("ch_stand1.png", heroX, floorHeight, heroSize);
        entities.add(hero);
        cloudNumber = 0;
        this.cloudVelocity = Math.abs(cloudVelocity);
        this.fps = fps;
        this.width = width;
        this.height = height;
        // tick to spawn a new cloud once every time a cloud is a third across screen
        tick = (int) (((this.width/3)/(cloudVelocity/fps)));
    }

    public Hero getHero() {
        return hero;
    }

    public Cloud addCloud(boolean initial) {
        Random rand = new Random();
        int cloudType = rand.nextInt(2) + 1;
        double xPos;
        if (initial) {
            xPos = (width*4) - (rand.nextDouble() * (width*4));
        } else {
            xPos = -(width/4) - (rand.nextDouble() * (width/4));
        }
        double yPos = rand.nextDouble() * (height/4.5);
        String path = "cloud_" + cloudType + ".png";
        Cloud c = new Cloud(path, xPos, yPos, cloudVelocity);
        entities.add(c);
        cloudNumber++;
        return c;
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
        tick = (int) ((int)fps * (this.width/cloudVelocity));

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
            Cloud c = (Cloud)entities.get(i+1);
            c.updateX(c.getXPos() + (cloudVelocity/fps));
        }
        return true;
    }

    @Override
    public boolean stopMoving() {
        return true;
    }
}
