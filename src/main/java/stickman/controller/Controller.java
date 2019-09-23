package stickman.controller;

import stickman.model.LevelImpl;

public interface Controller {
    boolean jump();
    boolean moveLeft();
    boolean moveRight();
    boolean stopMoving();
    void tick();

    void setLevel(LevelImpl level);
}
