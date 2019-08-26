package stickman.model;

interface MovableEntity extends Entity {
    void accelerate(double velXMod, double velYMod);
    void setXVelocity(double velX);
    void setYVelocity(double velY);
    void moveTick();
}
