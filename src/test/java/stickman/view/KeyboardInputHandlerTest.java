package stickman.view;

import de.saxsys.javafx.test.JfxRunner;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import stickman.ReadConfiguration;
import stickman.controller.HeroController;
import stickman.model.GameEngineImpl;
import stickman.model.Hero;
import stickman.model.LevelImpl;

import static org.junit.Assert.*;

@RunWith(JfxRunner.class)
public class KeyboardInputHandlerTest {
    private GameEngineImpl model;
    private LevelImpl level;
    private KeyboardInputHandler k;
    private Hero hero;
    private HeroController hc;
    private GameWindow window;

    @Before
    public void setUp() throws Exception {
        ReadConfiguration reader = new ReadConfiguration("test.json");
        model = new GameEngineImpl(reader.getConfig());
        level = (LevelImpl) model.getCurrentLevel();
        hero = level.getHero();
        hc = (HeroController) hero.getController();
        k = new KeyboardInputHandler(hc);
        window = new GameWindow(model, 640, 400);
    }

    @Test
    public void keyboardEvents() {
        hc.setOnFloor(true);
        double initialY = hero.getYPos();
        // press up
        KeyEvent up = new KeyEvent(window.getScene(), null, null, "", "", KeyCode.UP, false, false ,false, false);
        // initial jump
        k.handlePressed(up);
        hero.setYPos(hero.getDesiredY());
        assertTrue(hero.getYPos() < initialY);
        // holding up
        initialY = hero.getYPos();
        k.handlePressed(up);
        hero.setYPos(hero.getDesiredY());
        assertTrue(hero.getYPos() < initialY);
        // release up button
        initialY = hero.getYPos();
        k.handleReleased(up);
        hero.setYPos(hero.getDesiredY());
        assertEquals(hero.getDesiredY(), initialY, 0.001);
        // press left
        KeyEvent left = new KeyEvent(window.getScene(), null, null, "", "", KeyCode.LEFT, false, false ,false, false);
        double initialX = hero.getXPos();
        k.handlePressed(left);
        hero.setXPos(hero.getDesiredX());
        assertTrue(hero.getXPos() < initialX);
        // press right
        KeyEvent right = new KeyEvent(window.getScene(), null, null, "", "", KeyCode.RIGHT, false, false ,false, false);
        initialX = hero.getXPos();
        k.handlePressed(right);
        hero.setXPos(hero.getDesiredX());
        assertEquals(hero.getXPos(), initialX, 0.001);
        // release left
        initialX = hero.getXPos();
        k.handleReleased(left);
        hero.setXPos(hero.getDesiredX());
        assertTrue(hero.getXPos() > initialX);
        // release right
        initialX = hero.getXPos();
        k.handleReleased(right);
        hero.setXPos(hero.getDesiredX());
        assertEquals(hero.getXPos(), initialX, 0.001);
        // press right
        initialX = hero.getXPos();
        k.handlePressed(right);
        hero.setXPos(hero.getDesiredX());
        assertTrue(hero.getXPos() > initialX);
        // hold right
        initialX = hero.getXPos();
        k.handlePressed(right);
        hero.setXPos(hero.getDesiredX());
        assertEquals(hero.getXPos(), initialX, 0.001);
        // press left
        initialX = hero.getXPos();
        k.handlePressed(left);
        hero.setXPos(hero.getDesiredX());
        assertEquals(hero.getXPos(), initialX, 0.001);
        // release right
        initialX = hero.getXPos();
        k.handleReleased(right);
        hero.setXPos(hero.getDesiredX());
        assertTrue(hero.getXPos() < initialX);
        // press up but hero is mid jump
        hc.setOnFloor(false);
        hc.setJump(false);
        initialY = hero.getYPos();
        k.handlePressed(up);
        hero.setYPos(hero.getDesiredY());
        assertEquals(hero.getDesiredY(), initialY, 0.001);
    }
}