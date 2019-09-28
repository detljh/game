package stickman.model;

import stickman.collision.CollisionStrategy;
import stickman.collision.EnemyCollisionStrategy;
import stickman.controller.Controller;
import stickman.controller.EnemyController;

public class Enemy extends MovableEntity {
    private EnemyController ec;
    private String type;

    Enemy(double xPos, double yPos, String imagePath, String type) {
        super(xPos, imagePath);
        this.type = type;
        setYPos(yPos - getHeight());
        setDesiredX(getXPos());
        setDesiredY(getYPos());
    }

    public String getType() {
        return type;
    }

    @Override
    public CollisionStrategy getCollisionStrategy() {
        return new EnemyCollisionStrategy(ec);
    }

    @Override
    public double getHeight() {
        if ("still".equals(type)) {
            return 40.0;
        }
        return 20.0;
    }

    @Override
    public double getWidth() {
        if ("still".equals(type)) {
            return 40.0;
        }
        return 20.0;
    }

    @Override
    public Layer getLayer() {
        return Layer.FOREGROUND;
    }

    @Override
    public void setController(Controller c) {
        ec = (EnemyController) c;
    }

    @Override
    public Controller getController() {
        return ec;
    }
}
