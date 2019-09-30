package stickman.view;

import org.junit.Before;
import org.junit.Test;
import stickman.ReadConfiguration;
import stickman.controller.HeroController;
import stickman.model.GameEngineImpl;
import stickman.model.LevelImpl;

import static org.junit.Assert.*;

public class KeyboardInputHandlerTest {
    private KeyboardInputHandler k;
    private GameEngineImpl model;
    private LevelImpl level;

    @Before
    public void setUp() throws Exception {
        ReadConfiguration reader = new ReadConfiguration("test.json");
        model = new GameEngineImpl(reader.getConfig());
        level = (LevelImpl) model.getCurrentLevel();
        k = new KeyboardInputHandler((HeroController) model.getCurrentLevel().getHero().getController());
    }

    @Test
    public void handlePressed() {

    }

    @Test
    public void handleReleased() {
    }
}