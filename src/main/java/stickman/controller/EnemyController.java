package stickman.controller;

import stickman.model.Enemy;
import stickman.model.GameEngineImpl;
import stickman.model.Level;
import stickman.model.LevelImpl;

import java.util.Random;

public class EnemyController implements Controller {
    private String standFrame = "b";
    // slow down tick to every 3/4 of a second
    private int tick = (int) (GameEngineImpl.FPS * 0.75);
    private int time = GameEngineImpl.FPS;
    private LevelImpl level;
    private Enemy e;
    private int timeTillChange;
    private int move;

    public EnemyController(Enemy e) {
        this.e = e;
    }

    @Override
    public boolean jump() {
        return false;
    }

    @Override
    public boolean moveLeft() {
        Random rand = new Random();
        double xVel = 20 - (rand.nextDouble() * 70);

        e.setXVel(xVel);
        e.setDesiredX(e.getXPos() - (e.getXVel() / time));
        return true;
    }

    @Override
    public boolean moveRight() {
        Random rand = new Random();
        double xVel = 20 - (rand.nextDouble() * 70);

        e.setXVel(xVel);
        e.setDesiredX(e.getXPos() + (e.getXVel() / time));
        return true;
    }

    @Override
    public boolean stopMoving() {
        e.setXVel(0);
        return true;
    }

    private void chooseRandom() {
        Random rand = new Random();
        timeTillChange = rand.nextInt(time * 2) + 40;
    }

    @Override
    public void tick() {
        tick--;
        timeTillChange--;
        if (e.getType().equals("still")) {
            stopMoving();
        } else {
            if (timeTillChange < 0) {
                Random rand = new Random();
                move = rand.nextInt(3);
                chooseRandom();
            }

            if (move == 0) {
                moveLeft();
            } else if (move == 1) {
                moveRight();
            } else {
                stopMoving();
            }
        }

        if (level.getFloorHeight() - (e.getDesiredY() + e.getHeight()) < 0.001) {
            e.setYVel(0);
            e.setDesiredY(level.getFloorHeight() - e.getHeight());
        }

        // slow down animation
        if (tick > 0) {
            return;
        }
        tick = (int) (time * 0.75);

        if (standFrame.equals("a")) {
            standFrame = "b";
        } else {
            standFrame = "a";
        }
        String path = e.getImagePath().substring(0, 6) + standFrame + ".png";
        e.updateImagePath(path);
    }

    @Override
    public void move() {
        e.setXPos(e.getDesiredX());
        e.setYPos(e.getDesiredY());
    }

    @Override
    public void setLevel(LevelImpl level) {
        this.level = level;
    }

    @Override
    public Level getLevel() {
        return level;
    }

    public void kill() {
        level.getEntities().remove(e);
    }
}
