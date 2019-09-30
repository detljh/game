package stickman.model;

import org.junit.Before;
import org.junit.Test;
import stickman.ReadConfiguration;

import static org.junit.Assert.*;

public class LevelImplTest {
    private GameEngineImpl model;
    private LevelImpl level;

    @Before
    public void setUp() throws Exception {
        ReadConfiguration reader = new ReadConfiguration("test.json");
        model = new GameEngineImpl(reader.getConfig());
        level = (LevelImpl) model.getCurrentLevel();
    }

    @Test
    public void getFinish() {
        Finish finish = level.getFinish();
        assertEquals(300.0, finish.getXPos(), 0.001);
        assertEquals(level.getFloorHeight() - finish.getHeight(), finish.getYPos(), 0.001);
        assertEquals("finish.png", finish.getImagePath());
    }

    @Test
    public void getGravity() {
        assertEquals(9.8 / GameEngineImpl.FPS, level.getGravity(), 0.001);
    }

    @Test
    public void getHero() {
        Hero hero = level.getHero();
        assertEquals(20.0, hero.getXPos(), 0.001);
        assertEquals(level.getFloorHeight() - hero.getHeight(), hero.getYPos(), 0.001);
    }

    @Test
    public void getMovableEntities() {
        for (int i = 0; i < level.getMovableEntities().size(); i++) {
            assertNotNull(level.getMovableEntities().get(i).getController().getLevel());
        }
    }

    @Test
    public void getEntities() {
        assertEquals(15 + (int) (level.getWidth() / 40), level.getEntities().size());
    }

    @Test
    public void getHeight() {
        assertEquals(400.0, level.getHeight(), 0.001);
    }

    @Test
    public void getWidth() {
        assertEquals(640.0, level.getWidth(), 0.001);
    }

    @Test
    public void tick() {
        int initialSize = level.getEntities().size();
        int tick = (int) ((640 * 4 / 5) / (3.2 / GameEngineImpl.FPS));

        while (tick > 0) {
            assertEquals(initialSize, level.getEntities().size());
            tick--;
            level.tick();
        }
        assertTrue(level.getEntities().size() > initialSize);

        ReadConfiguration reader = new ReadConfiguration("testNegativeCloudVelocity.json");
        model = new GameEngineImpl(reader.getConfig());
        level = (LevelImpl) model.getCurrentLevel();
        initialSize = level.getEntities().size();
        tick = (int) ((640 * 4 / 5) / (3.2 / GameEngineImpl.FPS));

        while (tick > 0) {
            assertEquals(initialSize, level.getEntities().size());
            tick--;
            level.tick();
        }
        assertEquals(initialSize, level.getEntities().size());
    }

    @Test
    public void getFloorHeight() {
        assertEquals(350.0, level.getFloorHeight(), 0.001);
    }

    @Test
    public void getHeroX() {
        assertEquals(20.0, level.getHeroX(), 0.001);
    }

    @Test
    public void invalidConfig() {
        ReadConfiguration reader = new ReadConfiguration("testNoEnemyAndPlatform.json");
        model = new GameEngineImpl(reader.getConfig());

        reader = new ReadConfiguration("testDifferentSizedProperties.json");
        model = new GameEngineImpl(reader.getConfig());

        reader = new ReadConfiguration("testMissingProperty.json");
        model = new GameEngineImpl(reader.getConfig());
    }
}