package stickman.view;

import org.junit.Before;
import org.junit.Test;
import stickman.ReadConfiguration;
import stickman.model.Entity;
import stickman.model.GameEngineImpl;
import stickman.model.Hero;
import stickman.model.LevelImpl;

import static org.junit.Assert.*;

public class EntityViewImplTest {
    private EntityView ev;
    private GameEngineImpl model;
    private GameWindow window;
    private LevelImpl level;
    private Hero hero;

    @Before
    public void setUp() throws Exception {
        ReadConfiguration reader = new ReadConfiguration("test.json");
        model = new GameEngineImpl(reader.getConfig());
        level = (LevelImpl) model.getCurrentLevel();

        ev = new EntityViewImpl(level.getHero());
        hero = level.getHero();
    }

    @Test
    public void update() {
        ev.update(10, 10);
        assertEquals(hero.getXPos() - 10, ev.getNode().getLayoutX(), 0.001);
        assertEquals(hero.getYPos() + 10, ev.getNode().getLayoutY(), 0.001);
        assertFalse(ev.isMarkedForDelete());
    }

    @Test
    public void matchesEntity() {
    }

    @Test
    public void markForDelete() {
    }

    @Test
    public void getNode() {
    }

    @Test
    public void isMarkedForDelete() {
    }
}