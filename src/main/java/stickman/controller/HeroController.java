package stickman.controller;

import stickman.model.GameEngineImpl;
import stickman.model.Hero;
import stickman.model.Level;
import stickman.model.LevelImpl;

public class HeroController implements Controller {
    private Hero hero;
    // keep track of imagepath frame for animation
    private int walkFrame = 0;
    private int standFrame = 0;
    private int tick = GameEngineImpl.FPS;
    private LevelImpl level;
    private double beforeJumpY;
    private boolean onFloor;
    private boolean right;
    private boolean left;
    private boolean jump;
    private String prevMove;
    private int time = GameEngineImpl.FPS;

    public HeroController(Hero hero) {
        this.hero = hero;
    }

    @Override
    public boolean jump() {
        double jumpForce = hero.getJumpForce() / time;
        double yVel = hero.getYVel() / (time / 15);
        if (onFloor) {
            // get initial y pos before the jump
            beforeJumpY = hero.getYPos();
            jump = true;
            hero.setYVel(yVel + jumpForce);
            hero.setDesiredY(hero.getDesiredY() - hero.getYVel());
            return true;
        } else if (jump) {
            hero.setYVel(yVel + jumpForce);
            hero.setDesiredY(hero.getDesiredY() - hero.getYVel());
            // continue rising until the max jump height is reached
            if (hero.getDesiredY() < beforeJumpY - hero.getJumpHeight()) {
                jump = false;
            }
        }
        return false;
    }

    @Override
    public boolean moveLeft() {
        prevMove = "left";
        left = true;

        double xVel = hero.getXVel() / time;
        // make x velocity negative to move left
        if (xVel > 0) {
            xVel *= -1;
        }

        double horizontalMovement = hero.getHorizontalMovement() / time;

        hero.setXVel(xVel - horizontalMovement);
        hero.setDesiredX(hero.getXPos() + hero.getXVel());

        // slow down animation
        if (tick > (time * 0.9)) {
            return true;
        }
        tick = GameEngineImpl.FPS;

        // loop around walk frames facing left
        walkFrame = walkFrame % 4 + 5;
        String path = "ch_walk" + walkFrame + ".png";
        hero.updateImagePath(path);
        return true;
    }

    @Override
    public boolean moveRight() {
        prevMove = "right";
        right = true;

        double xVel = hero.getXVel() / time;
        // make x velocity positive to move right
        if (xVel < 0) {
            xVel *= -1;
        }
        double horizontalMovement = hero.getHorizontalMovement() / time;

        hero.setXVel(xVel + horizontalMovement);
        hero.setDesiredX(hero.getXPos() + hero.getXVel());

        // slow down animation
        if (tick > (time * 0.9)) {
            return true;
        }
        tick = GameEngineImpl.FPS;

        // loop around walk frames going right
        walkFrame = walkFrame % 4 + 1;
        String path = "ch_walk" + walkFrame + ".png";
        hero.updateImagePath(path);
        return true;
    }

    @Override
    public boolean stopMoving() {
        right = false;
        left = false;
        hero.setXVel(0);

        // slow down animation
        if (tick > (GameEngineImpl.FPS * 0.8)) {
            return true;
        }
        tick = GameEngineImpl.FPS;

        // update stand frame based on direction hero was previously walking in
        if (walkFrame <= 4) {
            standFrame = standFrame % 3 + 1;
            String path = "ch_stand" + standFrame + ".png";
            hero.updateImagePath(path);
        } else {
            standFrame = standFrame % 3 + 4;
            String path = "ch_stand" + standFrame + ".png";
            hero.updateImagePath(path);
        }
        return true;
    }

    @Override
    public void move() {
        hero.setXPos(hero.getDesiredX());
        hero.setYPos(hero.getDesiredY());
    }

    public void restartHero() {
        // set hero's position back to original starting position
        hero.setDesiredX(hero.getInitialX());
        hero.setDesiredY(hero.getInitialY());
    }

    @Override
    public void tick() {
        tick--;

        if (jump) {
           jump();
        }

        if (right) {
            moveRight();
        } else if (left) {
            moveLeft();
        } else {
            stopMoving();
        }

        // make sure hero does not go beneath floor
        if (level.getFloorHeight() - (hero.getDesiredY() + hero.getHeight()) < 0.001) {
            hero.setYVel(0);
            hero.setDesiredY(level.getFloorHeight() - hero.getHeight());
            onFloor = true;
            jump = false;
        }
    }

    @Override
    public void setLevel(LevelImpl level) {
        this.level = level;
    }

    @Override
    public Level getLevel() {
        return level;
    }

    public void setOnFloor(boolean value) {
        onFloor = value;
    }

    public void setJump(boolean value) {
        jump = value;
    }

    public void decrementLives() {
        hero.decrementLives();
    }

    public int getRemainingLives() {
        return hero.getRemainingLives();
    }

    public void platformMovement(String type) {
        if (type.equals("icy")) {
            if (prevMove.equals("right")) {
                // if hero was previously moving right, continue sliding right
                hero.setDesiredX(hero.getXPos() + hero.getXVel() + 0.2);
            } else {
                // if hero was previously moving left, continue sliding left
                hero.setDesiredX(hero.getXPos() + hero.getXVel() - 0.2);
            }
        }
    }
}
