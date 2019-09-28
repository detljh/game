package stickman.collision;

import stickman.model.Entity;
import stickman.model.MovableEntity;

public class CollisionStrategy {
    public boolean checkCollision(MovableEntity a, Entity other) {
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

    public String handleCollision(MovableEntity a, Entity other) {
        return null;
    }
}
