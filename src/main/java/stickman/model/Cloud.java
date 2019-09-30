package stickman.model;

import stickman.collision.CollisionStrategy;
import stickman.controller.CloudController;
import stickman.controller.Controller;

public class Cloud extends MovableEntity {
    private CloudController cc;

    public Cloud(String imagePath, double xPos, double yPos, double velocity) {
        super(xPos, imagePath);
        setXVel(velocity);
        setYPos(yPos);
        cc = new CloudController(this);
    }

    /** Override super class to enable negative x positions for spawning off screen */
    @Override
    public void setXPos(double xPos) {
        super.xPos = xPos;
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
    public CollisionStrategy getCollisionStrategy() {
        return new CollisionStrategy();
    }

    @Override
    public Controller getController() {
        return cc;
    }


}
