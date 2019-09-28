package stickman.collision;

import javafx.geometry.Point2D;
import stickman.controller.EnemyController;
import stickman.controller.HeroController;
import stickman.model.Enemy;
import stickman.model.Entity;
import stickman.model.Level;
import stickman.model.MovableEntity;
import stickman.model.Platform;

public class HeroCollisionStrategy extends CollisionStrategy {
    private HeroController hc;
    private Level currentLevel;

    public HeroCollisionStrategy(HeroController hc) {
        this.hc = hc;
        currentLevel = hc.getLevel();
    }

    @Override
    public boolean checkCollision(MovableEntity a, Entity other) {
        if (a.equals(other)) {
            return false;
        }

        // width of hero is half its height
        return (a.getDesiredX() < (other.getXPos() + other.getWidth())) &&
                ((a.getDesiredX() + (a.getWidth() / 2)) > other.getXPos()) &&
                (a.getDesiredY() < (other.getYPos() + other.getHeight())) &&
                ((a.getDesiredY() + a.getHeight()) > other.getYPos());
    }

    @Override
    public String handleCollision(MovableEntity a, Entity other) {
        if (other.equals(currentLevel.getFinish())) {
            return "won";
        } else if (other.getLayer().equals(Entity.Layer.BACKGROUND)) {
            return null;
        }

        Point2D aPos = new Point2D(a.getDesiredX(), a.getDesiredY());
        Point2D otherPos = new Point2D(other.getXPos(), other.getYPos());

        Point2D collisionVector = otherPos.subtract(aPos);
        collisionVector = collisionVector.normalize();
        double width = a.getWidth() / 2;

        if (Math.abs(collisionVector.getX()) > Math.abs(collisionVector.getY())) {
            if (collisionVector.getX() < 0) {
                a.setDesiredX(other.getXPos() + other.getWidth());
                a.setXVel(0);
            } else {
                a.setDesiredX(other.getXPos() - width);
                a.setXVel(0);
            }
            if (handleDeath(other)) {
                return "lost";
            }
        } else {
            if (collisionVector.getY() > 0) {
                a.setDesiredY(other.getYPos() - a.getHeight());
                a.setYVel(0);
                hc.setOnFloor(true);
                hc.setJump(false);
                handleKill(other);
                handlePlatformMovement(other);
            } else {
                a.setDesiredY(other.getYPos() + other.getHeight());
                a.setYVel(0);
                hc.setJump(false);
                if (handleDeath(other)) {
                    return "lost";
                }
            }
        }
        return null;
    }

    /*
    @returns true if Hero has no more lives
     */
    private boolean handleDeath(Entity other) {
        if (other instanceof Enemy) {
            hc.decrementLives();
            if (hc.getRemainingLives() == 0) {
                return true;
            } else {
                hc.restartHero();
            }
        }
        return false;
    }

    private void handleKill(Entity other) {
        if (other instanceof Enemy) {
            MovableEntity e = (MovableEntity) other;
            EnemyController ec = (EnemyController) e.getController();
            ec.kill();
        }
    }

    private void handlePlatformMovement(Entity other) {
        if (other instanceof Platform) {
            hc.platformMovement(((Platform) other).getType());
        }
    }
}
