package stickman.model;

public interface GameEngine {
    Level getCurrentLevel();

    void startLevel();

    void tick() throws InterruptedException;

    boolean checkCollision(Entity a, Entity other);
    void handleCollision(Entity a, Entity other);
    void update();
    String finish();
    String getTime();
    int getLives();
}
