package stickman.collision;

import javafx.geometry.Point2D;
import stickman.controller.EnemyController;
import stickman.model.Entity;
import stickman.model.Level;
import stickman.model.MoveableEntity;

public class EnemyCollisionStrategy extends CollisionStrategy {
    private EnemyController ec;
    private Level currentLevel;

    public EnemyCollisionStrategy(EnemyController ec) {
        this.ec = ec;
        currentLevel = ec.getLevel();
    }

    @Override
    public String handleCollision(MoveableEntity a, Entity other) {
        if (other.equals(currentLevel.getFinish()) || currentLevel.isEnemy(other) || other.equals(currentLevel.getHero())) {
            return null;
        }

        Point2D aPos = new Point2D(a.getDesiredX(), a.getDesiredY());
        Point2D otherPos = new Point2D(other.getXPos(), other.getYPos());

        Point2D collisionVector = otherPos.subtract(aPos);
        collisionVector = collisionVector.normalize();

        double width;
        if (other.equals(currentLevel.getHero())) {
            width = other.getWidth() / 2;
        } else {
            width = other.getWidth();
        }

        if (Math.abs(collisionVector.getX()) > Math.abs(collisionVector.getY())) {
            if (collisionVector.getX() < 0) {
                a.setDesiredX(other.getXPos() + width);
                a.setXVel(0);
            } else {
                a.setDesiredX(other.getXPos() - a.getWidth());
                a.setXVel(0);
            }
        } else {
            if (collisionVector.getY() > 0) {
                a.setDesiredY(other.getYPos() - a.getHeight());
                a.setYVel(0);
            } else {
                a.setDesiredY(other.getYPos() + other.getHeight());
                a.setYVel(0);
            }
        }
        return null;
    }
}
