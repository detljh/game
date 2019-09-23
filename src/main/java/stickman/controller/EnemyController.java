package stickman.controller;

import stickman.model.LevelImpl;

public class EnemyController implements Controller {
    private LevelImpl level;

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

    @Override
    public void tick() {

    }

    @Override
    public void setLevel(LevelImpl level) {
        this.level = level;
    }
}
