package stickman.model;

import java.util.List;

public interface Level {
    List<Entity> getEntities();
    double getHeight();
    double getWidth();
    void tick();
    double getFloorHeight();
    double getHeroX();
    Hero getHero();
    Finish getFinish();
}
