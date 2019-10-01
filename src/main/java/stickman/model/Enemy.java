package stickman.model;

import stickman.collision.CollisionStrategy;
import stickman.collision.EnemyCollisionStrategy;
import stickman.controller.Controller;
import stickman.controller.EnemyController;

public class Enemy extends MovableEntity {
    private EnemyController ec;
    private String type;

    public Enemy(double xPos, double yPos, String imagePath, String type) {
        super(xPos, imagePath);
        this.type = type;
        setYPos(yPos - getHeight());
        setDesiredX(getXPos());
        setDesiredY(getYPos());
        ec = new EnemyController(this);
    }

    public String getType() {
        return type;
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
    public Controller getController() {
        return ec;
    }

    @Override
    public CollisionStrategy getCollisionStrategy() {
        return new EnemyCollisionStrategy(ec);
    }
}
