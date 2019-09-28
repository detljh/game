package stickman.controller;

import stickman.model.Entity;
import stickman.model.Level;
import stickman.model.LevelImpl;

public interface Controller {
    boolean jump();
    boolean moveLeft();
    boolean moveRight();
    boolean stopMoving();
    void tick();
    void move();

    void setLevel(LevelImpl level);
    Level getLevel();
}
