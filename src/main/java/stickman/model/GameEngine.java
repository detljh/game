package stickman.model;

public interface GameEngine {
    Level getCurrentLevel();
    void startLevel();
    void tick() throws InterruptedException;
    void update();
    String getState();
    String getTime();
    int getLives();
}
