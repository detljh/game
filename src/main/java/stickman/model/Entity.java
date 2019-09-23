package stickman.model;

import javafx.geometry.Point2D;
import stickman.controller.Controller;

public interface Entity {
    String getImagePath();
    double getXPos();
    double getYPos();
    double getHeight();
    double getWidth();
    Layer getLayer();
    void setController(Controller c);
    Controller getController();
    double getDesiredX();
    double getDesiredY();
    double getXVel();
    double getYVel();
    void setDesiredX(double xPos);
    void setDesiredY(double yPos);
    void setXVel(double xVel);
    void setYVel(double yVel);
    void setXPos(double xPos);
    void setYPos(double yPos);

    enum Layer {
        BACKGROUND, FOREGROUND, EFFECT
    }
}
