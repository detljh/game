package stickman.controller;

import stickman.model.Cloud;
import stickman.model.GameEngineImpl;
import stickman.model.Level;
import stickman.model.LevelImpl;

public class CloudController implements Controller {
    private Cloud cloud;
    private LevelImpl level;

    public CloudController(Cloud cloud) {
        this.cloud = cloud;
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
        cloud.setDesiredX(cloud.getXPos() + (cloud.getXVel() / GameEngineImpl.FPS));
        return true;
    }

    @Override
    public boolean stopMoving() {
        return false;
    }

    @Override
    public void tick() {
        moveRight();
        cloud.setDesiredY(cloud.getYPos());
    }

    @Override
    public void move() {
        cloud.setXPos(cloud.getDesiredX());
        cloud.setYPos(cloud.getDesiredY());
    }

    @Override
    public void setLevel(LevelImpl level) {
        this.level = level;
    }

    @Override
    public Level getLevel() {
        return level;
    }
}
