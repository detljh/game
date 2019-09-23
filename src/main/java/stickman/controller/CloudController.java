package stickman.controller;

import stickman.model.Cloud;
import stickman.model.GameEngineImpl;
import stickman.model.LevelImpl;

import java.util.Random;

public class CloudController implements Controller {
    private int tick;
    Cloud cloud;
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
        cloud.setXPos(cloud.getXPos() + (cloud.getXVel() / GameEngineImpl.FPS));
        return true;
    }

    @Override
    public boolean stopMoving() {
        return false;
    }

    @Override
    public void tick() {
        moveRight();
    }

    @Override
    public void setLevel(LevelImpl level) {
        this.level = level;
    }
}
