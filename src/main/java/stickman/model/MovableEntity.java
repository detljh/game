package stickman.model;

import stickman.collision.CollisionStrategy;
import stickman.controller.Controller;

public abstract class MovableEntity implements Entity {
    private String imagePath;
    double xPos;
    private double yPos;
    private double xVel;
    private double yVel;
    // stores entity's desired position before it being set to prevent movements being backtracked by collisions
    private double desiredX;
    private double desiredY;

    MovableEntity(double xPos, String imagePath) {
        this.xPos = xPos;
        this.imagePath = imagePath;
    }

    public void updateImagePath(String path) {
        imagePath = path;
    }

    @Override
    public String getImagePath() {
        return imagePath;
    }

    @Override
    public double getXPos() {
        return xPos;
    }

    @Override
    public double getYPos() {
        return yPos;
    }

    @Override
    public abstract Layer getLayer();

    public double getDesiredX() {
        return desiredX;
    }

    public double getDesiredY() {
        return desiredY;
    }

    public double getXVel() {
        return xVel;
    }

    public double getYVel() {
        return yVel;
    }

    public void setDesiredX(double xPos) {
        desiredX = xPos;
    }

    public void setDesiredY(double yPos) {
        desiredY = yPos;
    }

    public void setXVel(double xVel) {
        this.xVel = xVel;
    }

    public void setYVel(double yVel) {
        this.yVel = yVel;
    }

    public void setXPos(double xPos) {
        // prevent entities from moving off left side of screen
        if (xPos < 0) {
            xPos = 0;
        }
        this.xPos = xPos;
    }

    public void setYPos(double yPos) {
        this.yPos = yPos;
    }

    public abstract Controller getController();
    public abstract CollisionStrategy getCollisionStrategy();
}
