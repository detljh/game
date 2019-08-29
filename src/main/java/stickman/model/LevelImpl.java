package stickman.model;

import java.util.ArrayList;
import java.util.List;

public class LevelImpl implements Level {
    private List<Entity> entities;
    private double height;
    private double width;
    private double floorHeight;
    private Hero hero;

    public LevelImpl(double heroX, String heroSize, double floorHeight) {
        entities = new ArrayList<Entity>();
        this.floorHeight = floorHeight;
        hero = new Hero("ch_stand1.png", heroX, floorHeight, heroSize);
        entities.add(hero);
    }

    public Hero getHero() {
        return hero;
    }

    public void addEntity(Entity e) {
        entities.add(e);
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
        return false;
    }

    @Override
    public boolean stopMoving() {
        return false;
    }
}
