package stickman.model;

import stickman.collision.CollisionStrategy;
import stickman.collision.HeroCollisionStrategy;
import stickman.controller.Controller;
import stickman.controller.HeroController;

public class Hero extends MovableEntity {
    private String heroSize;
    private double jumpHeight;
    private HeroController hc;
    private double initialX;
    private double initialY;
    private int lives = 3;

    public Hero(String imagePath, double xPos, String heroSize, double jumpHeight, double floorHeight) {
        super(xPos, imagePath);
        setXPos(xPos);
        this.heroSize = heroSize;
        setYPos(floorHeight - getHeight());
        this.jumpHeight = jumpHeight;
        setXVel(0.0);
        setYVel(0.0);
        setDesiredX(getXPos());
        setDesiredY(getYPos());
        initialX = xPos;
        initialY = floorHeight - getHeight();
        hc = new HeroController(this);
    }

    public double getInitialX() {
        return initialX;
    }

    public double getInitialY() {
        return  initialY;
    }

    public double getJumpForce() {
        return jumpHeight / 2;
    }

    /** Get x movement speed of hero */
    public double getHorizontalMovement() {
        return 150.0;
    }

    public double getJumpHeight() {
        return jumpHeight;
    }

    @Override
    public double getHeight() {
        switch (heroSize) {
            case "tiny": return 20.0;
            case "normal": return 30.0;
            case "large": return 50.0;
            case "giant": return 100.0;
            default: return 0.0;
        }
    }

    @Override
    public double getWidth() {
        switch (heroSize) {
            case "tiny": return 20.0;
            case "normal": return 30.0;
            case "large": return 50.0;
            case "giant": return 100.0;
            default: return 0.0;
        }
    }

    @Override
    public Layer getLayer() {
        return Layer.FOREGROUND;
    }

    @Override
    public Controller getController() {
        return hc;
    }

    @Override
    public CollisionStrategy getCollisionStrategy() {
        return new HeroCollisionStrategy(hc);
    }

    public void decrementLives() {
        lives--;
    }

    public int getRemainingLives() {
        return lives;
    }
}
