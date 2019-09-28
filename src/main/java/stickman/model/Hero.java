package stickman.model;

import stickman.collision.CollisionStrategy;
import stickman.collision.HeroCollisionStrategy;
import stickman.controller.Controller;
import stickman.controller.HeroController;

public class Hero extends MoveableEntity {
    private String heroSize;
    private double jumpHeight;
    private HeroController hc;
    private double initialX;
    private double initialY;
    private static int lives = 3;

    Hero(String imagePath, double xPos, String heroSize, double jumpHeight, double floorHeight) {
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
    }

    public double getInitialX() {
        return initialX;
    }

    public double getInitialY() {
        return  initialY;
    }

    public double getJumpForce() {
        return 150.0;
    }

    public double getHorizontalMovement() {
        return 80.0;
    }

    public double getJumpHeight() {
        return jumpHeight;
    }

    @Override
    public CollisionStrategy getCollisionStrategy() {
        return new HeroCollisionStrategy(hc);
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
    public void setController(Controller c) {
        hc = (HeroController) c;
    }

    @Override
    public Controller getController() {
        return hc;
    }

    public void decrementLives() {
        lives--;
    }

    public int getRemainingLives() {
        return lives;
    }
}
