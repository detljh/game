package stickman.collision;

import javafx.geometry.Point2D;
import stickman.controller.EnemyController;
import stickman.model.Enemy;
import stickman.model.Entity;
import stickman.model.Level;
import stickman.model.MovableEntity;

public class EnemyCollisionStrategy extends CollisionStrategy {
    private Level currentLevel;

    public EnemyCollisionStrategy(EnemyController ec) {
        currentLevel = ec.getLevel();
    }

    /**
     * Moves the enemy according to which side the @param other entity came from.
     * Returns the status of the game if it has changed, otherwise null.
     *
     * @param a An entity that can move
     * @param other An entity that may collide with @param a
     */
    @Override
    public String handleCollision(MovableEntity a, Entity other) {
        if (other instanceof Enemy || other.equals(currentLevel.getHero())) {
            return null;
        }

        Point2D aPos = new Point2D(a.getDesiredX(), a.getDesiredY());
        Point2D otherPos = new Point2D(other.getXPos(), other.getYPos());

        Point2D collisionVector = otherPos.subtract(aPos);
        collisionVector = collisionVector.normalize();

        // if collision is on the x axis
        if (Math.abs(collisionVector.getX()) > Math.abs(collisionVector.getY())) {
            // collision to the left
            if (collisionVector.getX() < 0) {
                a.setDesiredX(other.getXPos() + other.getWidth());
                a.setXVel(0);
            } else {
                // collision to the right
                a.setDesiredX(other.getXPos() - a.getWidth());
                a.setXVel(0);
            }
        } else {
            // collision on bottom
            if (collisionVector.getY() > 0) {
                a.setDesiredY(other.getYPos() - a.getHeight());
                a.setYVel(0);
            } else {
                // collision on top
                a.setDesiredY(other.getYPos() + other.getHeight());
                a.setYVel(0);
            }
        }
        return null;
    }
}
