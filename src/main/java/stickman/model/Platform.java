package stickman.model;

import stickman.controller.Controller;

public class Platform implements Entity {
    private double xPos;
    private double yPos;
    private String imagePath;

    Platform(double xPos, double yPos, String imagePath) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.imagePath = imagePath;
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
    public double getXVel() {
        return 0;
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

    @Override
    public void setXPos(double xPos) {

    }

    @Override
    public void setYPos(double yPos) {

    }

    @Override
    public double getHeight() {
        return 20.0;
    }

    @Override
    public double getWidth() {
        return 20.0;
    }

    @Override
    public Layer getLayer() {
        return Layer.FOREGROUND;
    }

    @Override
    public void setController(Controller c) {

    }

    @Override
    public Controller getController() {
        return null;
    }
}
