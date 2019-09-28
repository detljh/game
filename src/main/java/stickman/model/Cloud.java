package stickman.model;

import stickman.collision.CollisionStrategy;
import stickman.controller.CloudController;
import stickman.controller.Controller;

public class Cloud extends MovableEntity {
    private CloudController cc;

    Cloud(String imagePath, double xPos, double yPos, double velocity) {
        super(xPos, imagePath);
        setXVel(velocity);
        setYPos(yPos);
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
    public void setController(Controller c) {
        cc = (CloudController) c;
    }

    @Override
    public Controller getController() {
        return cc;
    }
}
