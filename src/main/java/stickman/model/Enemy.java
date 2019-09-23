package stickman.model;

import stickman.controller.Controller;

public class Enemy implements Entity {
    @Override
    public String getImagePath() {
        return null;
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
        return 0;
    }

    @Override
    public double getYPos() {
        return 0;
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
        return 0;
    }

    @Override
    public double getWidth() {
        return 0;
    }

    @Override
    public Layer getLayer() {
        return null;
    }

    @Override
    public void setController(Controller c) {

    }

    @Override
    public Controller getController() {
        return null;
    }

}
