package stickman.collision;

import stickman.model.Entity;
import stickman.model.MoveableEntity;

public class CollisionStrategy {
    public boolean checkCollision(MoveableEntity a, Entity other) {
        if (a.equals(other)) {
            return false;
        }

        if (other.getLayer().equals(Entity.Layer.BACKGROUND)) {
            return false;
        }

        return (a.getDesiredX() < (other.getXPos() + other.getWidth())) &&
                ((a.getDesiredX() + a.getWidth()) > other.getXPos()) &&
                (a.getDesiredY() < (other.getYPos() + other.getHeight())) &&
                ((a.getDesiredY() + a.getHeight()) > other.getYPos());
    }

    public String handleCollision(MoveableEntity a, Entity other) {
        return null;
    }

    private boolean handleDeath(Entity other) {
        return false;
    }
}
