package stickman.model;

import javafx.geometry.Point2D;
import stickman.controller.Controller;
import stickman.controller.HeroController;

public class Hero implements Entity {
    private String imagePath;
    private double xPos;
    private double yPos;
    private String heroSize;
    private double jumpHeight;
    private HeroController hc;
    private double xVel;
    private double yVel;
    private double desiredX;
    private double desiredY;

    Hero(String imagePath, double xPos, String heroSize, double jumpHeight, double floorHeight) {
        this.imagePath = imagePath;
        setXPos(xPos);
        this.heroSize = heroSize;
        setYPos(floorHeight - getHeight());
        this.jumpHeight = jumpHeight;
        resetVelocityX();
        resetVelocityY();
        setDesiredX(getXPos());
        setDesiredY(getYPos());

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

    public void resetVelocityX() {
        this.xVel = 0.0;
    }

    public void resetVelocityY() {
        this.yVel = 0.0;
    }

    @Override
    public void setXVel(double xVel) {
        this.xVel = xVel;
    }

    @Override
    public void setYVel(double yVel) {
        this.yVel = yVel;
    }

    @Override
    public void setXPos(double xPos) {
        // prevent hero from moving off left of screen
        if (xPos < 0) {
            xPos = 0;
        }
        this.xPos = xPos;
    }

    @Override
    public void setYPos(double yPos) {
        this.yPos = yPos;
    }

    public double getXVel() {
        return xVel;
    }

    public double getYVel() {
        return yVel;
    }

    @Override
    public void setDesiredX(double xPos) {
        desiredX = xPos;
    }

    @Override
    public void setDesiredY(double yPos) {
        desiredY = yPos;
    }

    public void updateImagePath(String path) {
        imagePath = path;
    }

    @Override
    public String getImagePath() {
        return imagePath;
    }

    @Override
    public double getDesiredX() {
        return desiredX;
    }

    @Override
    public double getDesiredY() {
        return desiredY;
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
}
