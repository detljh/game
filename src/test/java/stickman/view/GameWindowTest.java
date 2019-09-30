package stickman.view;

import org.junit.Before;
import org.junit.Test;
import stickman.ReadConfiguration;
import stickman.model.GameEngineImpl;
import stickman.model.LevelImpl;

import static org.junit.Assert.*;

public class GameWindowTest {
    private GameEngineImpl model;
    private LevelImpl level;
    private GameWindow window;

    @Before
    public void setUp() throws Exception {
        ReadConfiguration reader = new ReadConfiguration("test.json");
        model = new GameEngineImpl(reader.getConfig());
        level = (LevelImpl) model.getCurrentLevel();
        window = new GameWindow(model, 640, 400);
    }

    @Test
    public void getScene() {
        assertEquals(640, window.getScene().getWidth(), 0.001);
        assertEquals(400, window.getScene().getHeight(), 0.001);
    }

    @Test
    public void run() {
    }
}