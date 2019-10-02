package stickman.view;

import de.saxsys.javafx.test.JfxRunner;
import javafx.scene.Scene;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import stickman.ReadConfiguration;
import stickman.model.GameEngineImpl;
import stickman.model.LevelImpl;

import static org.junit.Assert.assertEquals;

@RunWith(JfxRunner.class)
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
        window.run();
    }

    @Test
    public void getScene() {
        Scene scene = window.getScene();
        assertEquals(640, scene.getWidth(), 0.001);
        assertEquals(400, scene.getHeight(), 0.001);
    }
}