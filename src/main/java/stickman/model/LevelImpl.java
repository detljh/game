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

    public LevelImpl(double heroX, String heroSize, double floorHeight) {
        entities = new ArrayList<Entity>();
        this.floorHeight = floorHeight;
        hero = new Hero("ch_stand1.png", heroX, floorHeight, heroSize);
        entities.add(hero);
        cloudNumber = 0;
    }

    public Hero getHero() {
        return hero;
    }

    public void addCloud(double velocity) {
        cloudVelocity = velocity;
        tick = (int)velocity * 40;
        Random rand = new Random();
        int cloudType = rand.nextInt(2) + 1;
        double xPos = 640 - (rand.nextDouble() * 640);
        double yPos = rand.nextDouble() * 150;
        String path = "cloud_" + cloudType + ".png";
        Cloud c = new Cloud(path, xPos, yPos, velocity);
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
        tick--;
        moveRight();
        if (tick > 0) {
            return;
        }
        tick = (int)cloudVelocity * 40;

        addCloud(cloudVelocity);
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
            c.update(c.getXPos() + 0.32, c.getYPos());
        }
        return true;
    }

    @Override
    public boolean stopMoving() {
        return true;
    }
}
