package stickman.view;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import stickman.controller.HeroController;
import stickman.model.Entity;
import stickman.model.GameEngine;
import java.util.ArrayList;
import java.util.List;

public class GameWindow {
    private final int width;
    private final int height;
    private Scene scene;
    private Pane pane;
    private GameEngine model;
    private List<EntityView> entityViews;
    private BackgroundDrawer backgroundDrawer;
    private Text time;
    private Text lives;
    private Timeline timeline;
    private double xViewportOffset = 0.0;
    private static double VIEWPORT_MARGIN_X;
    private double yViewportOffset = 0.0;
    private static double VIEWPORT_MARGIN_Y;

    public GameWindow(GameEngine model, int width, int height) {
        this.model = model;
        this.pane = new Pane();
        this.width = width;
        this.height = height;
        this.scene = new Scene(pane, width, height);
        VIEWPORT_MARGIN_X = width * 2/5;
        VIEWPORT_MARGIN_Y = height * 3/5;

        time = new Text();
        time.setFill(Color.BLACK);
        time.setX(0.0);
        time.setY(10.0);
        time.setViewOrder(0.0);
        pane.getChildren().add(time);

        lives = new Text();
        lives.setFill(Color.BLACK);
        lives.setX(width - 50.0);
        lives.setY(10.0);
        lives.setViewOrder(0.0);
        pane.getChildren().add(lives);

        this.entityViews = new ArrayList<>();

        KeyboardInputHandler keyboardInputHandler = new KeyboardInputHandler((HeroController) model.getCurrentLevel().getHero().getController());

        scene.setOnKeyPressed(keyboardInputHandler::handlePressed);
        scene.setOnKeyReleased(keyboardInputHandler::handleReleased);

        this.backgroundDrawer = new ParallaxBackground();
        backgroundDrawer.draw(model, pane);
    }

    public Scene getScene() {
        return this.scene;
    }

    public void run() {
        timeline = new Timeline(new KeyFrame(Duration.millis(17),
                t -> this.draw()));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void setTimer() {
        time.setText("Elapsed time: " + model.getTime());
    }

    private void setLives() {
        lives.setText("Lives: " + model.getLives());
    }

    private void setGameOverText(String s) {
        for (Node n : pane.getChildren()) {
            n.setOpacity(0.5);
        }

        Text gameOverText = new Text(s);
        gameOverText.setFill(Color.BLACK);
        gameOverText.setFont(Font.font(20));
        gameOverText.setViewOrder(0.0);
        gameOverText.setX(width / 3);
        gameOverText.setY(scene.getHeight() / 2);
        pane.getChildren().add(gameOverText);
        timeline.stop();
    }

    private void draw() {
        try {
            model.tick();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }

        if (timeline.getStatus().equals(Animation.Status.STOPPED)) {
            return;
        }

        setTimer();
        setLives();
        if (model.getState().equals("won")) {
            setGameOverText("Congratulations!\n You have won!");
        } else if (model.getState().equals("lost")) {
            setGameOverText("Game Over!");
        }

        List<Entity> entities = model.getCurrentLevel().getEntities();

        for (EntityView entityView: entityViews) {
            entityView.markForDelete();
        }

        double heroXPos = model.getCurrentLevel().getHeroX();
        heroXPos -= xViewportOffset;

        double heroYPos = model.getCurrentLevel().getHero().getDesiredY();
        heroYPos += yViewportOffset;

        if (heroXPos < VIEWPORT_MARGIN_X) {
            if (xViewportOffset >= 0) { // Don't go further left than the start of the level
                xViewportOffset -= VIEWPORT_MARGIN_X - heroXPos;
                if (xViewportOffset < 0) {
                    xViewportOffset = 0;
                }
            }
        } else if (heroXPos > width - VIEWPORT_MARGIN_X) {
            xViewportOffset += heroXPos - (width - VIEWPORT_MARGIN_X);
        }

        if (heroYPos > VIEWPORT_MARGIN_Y) {
            if (yViewportOffset >= 0) { // Don't go further than bottom of screen
                yViewportOffset -= heroYPos - VIEWPORT_MARGIN_Y;
                if (yViewportOffset < 0) {
                    yViewportOffset = 0;
                }
            }
        } else if (heroYPos < height - VIEWPORT_MARGIN_Y) {
            yViewportOffset += (height - VIEWPORT_MARGIN_Y) - heroYPos;
        }

        backgroundDrawer.update(xViewportOffset, yViewportOffset);

        for (Entity entity : entities) {
            boolean notFound = true;
            for (EntityView view: entityViews) {
                if (view.matchesEntity(entity)) {
                    notFound = false;
                    view.update(xViewportOffset, yViewportOffset);
                    break;
                }
            }
            if (notFound) {
                EntityView entityView = new EntityViewImpl(entity);
                entityViews.add(entityView);
                pane.getChildren().add(entityView.getNode());
            }
        }

        for (EntityView entityView: entityViews) {
            if (entityView.isMarkedForDelete()) {
                pane.getChildren().remove(entityView.getNode());
            }
        }
        entityViews.removeIf(EntityView::isMarkedForDelete);
    }
}
