package stickman.collision;

import stickman.model.Entity;
import stickman.model.MovableEntity;

public class CollisionStrategy {
    /**
     * Default method for collision strategies. Checks if two entities collide with each other.
     *
     * @param a An entity that can move
     * @param other An entity that may collide with @param a
     * @return True if there is a colllision between different entities that are not in the background layer, false
     * otherwise
     */
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

    /**
     * Default method for collision strategies. Does nothing if there is a collision.
     * Returns the status of the game if it has changed, otherwise null.
     *
     * @param a An entity that can move
     * @param other An entity that may collide with @param a
     */
    public String handleCollision(MovableEntity a, Entity other) {
        return null;
    }
}
