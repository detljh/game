package stickman.model;

import stickman.controller.CloudController;
import stickman.controller.Controller;

public class Cloud implements Entity {
    private double velocity;
    private String imagePath;
    private double xPos;
    private double yPos;
    private CloudController cc;

    Cloud(String imagePath, double xPos, double yPos, double velocity) {
        this.imagePath = imagePath;
        this.xPos = xPos;
        this.yPos = yPos;
        this.velocity = velocity;
    }

    @Override
    public double getXVel() {
        return velocity;
    }

    @Override
    public double getYVel() {
        return 0;
    }

    @Override
    public void setDesiredX(double xPos) {

    }

    @Override
    public void setDesiredY(double yPos) {

    }

    @Override
    public void setXVel(double xVel) {
    }

    @Override
    public void setYVel(double yVel) {
    }

    public void setXPos(double xPos) {
        this.xPos = xPos;
    }

    @Override
    public void setYPos(double yPos) {
        this.yPos = yPos;
    }

    @Override
    public String getImagePath() {
        return imagePath;
    }

    @Override
    public double getDesiredX() {
        return 0;
    }

    @Override
    public double getDesiredY() {
        return 0;
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
        return 0;
    }

    @Override
    public double getWidth() {
        return 0;
    }

    @Override
    public Layer getLayer() {
        return Layer.BACKGROUND;
    }

    @Override
    public void setController(Controller c) {
        cc = (CloudController) c;
    }

    @Override
    public Controller getController() {
        return cc;
    }
}
