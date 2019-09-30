package stickman.model;

import org.junit.Before;
import org.junit.Test;
import stickman.ReadConfiguration;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import static org.junit.Assert.*;

public class GameEngineImplTest {
    private GameEngineImpl model;
    private LevelImpl level;

    @Before
    public void setUp() throws Exception {
        ReadConfiguration reader = new ReadConfiguration("test.json");
        model = new GameEngineImpl(reader.getConfig());
        level = (LevelImpl) model.getCurrentLevel();
    }

    @Test
    public void getCurrentLevel() {
        level = (LevelImpl) model.getCurrentLevel();
        assertNotNull(level);
    }

    @Test
    public void getState() {
        assertEquals("", model.getState());
    }

    @Test
    public void getLives() {
        assertEquals(3, model.getLives());
    }

    @Test
    public void update() {
        double gravity = level.getGravity();
        List<MovableEntity> moveableEntities = level.getMovableEntities();
        List<Double> velocities = new ArrayList<>();
        List<Double> y = new ArrayList<>();
        for (int i = 0; i < moveableEntities.size(); i++) {
            velocities.add((moveableEntities.get(i).getYVel() + gravity) / (GameEngineImpl.FPS / 15));
            y.add(moveableEntities.get(i).getYPos());
        }

        model.update();
        for (int i = 0; i < moveableEntities.size(); i++) {
            assertEquals(velocities.get(i) + y.get(i), moveableEntities.get(i).getDesiredY(), 0.001);
        }
    }

    @Test
    public void getTime() {
        DecimalFormat time = new DecimalFormat("#.00");
        assertEquals(String.valueOf(time.format(0 * 0.017)), model.getTime());
        model.tick();
        assertEquals(String.valueOf(time.format(1 * 0.017)), model.getTime());
    }

    @Test
    public void tick() {
        Hero hero = level.getHero();

        hero.setDesiredX(level.getFinish().getXPos());
        model.tick();
        assertEquals("won", model.getState());

        List<Entity> entities = level.getEntities();
        int initialSize = entities.size();
        Enemy e;
        for (Entity entity : entities) {
            if (entity instanceof Enemy) {
                e = (Enemy) entity;
                hero.setDesiredX(e.getXPos());
                hero.setDesiredY(e.getYPos() - hero.getHeight() + 1);
                model.tick();
                assertEquals(initialSize - 1, level.getEntities().size());
                break;
            }
        }
    }
}