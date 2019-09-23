package stickman.model;

import javafx.geometry.Point2D;
import stickman.controller.Controller;

public interface Entity {
    String getImagePath();
    double getDesiredX();
    double getDesiredY();
    double getXPos();
    double getYPos();
    double getXVel();
    double getYVel();
    void setDesiredX(double xPos);
    void setDesiredY(double yPos);
    void setXVel(double xVel);
    void setYVel(double yVel);
    void setXPos(double xPos);
    void setYPos(double yPos);
    double getHeight();
    double getWidth();
    Layer getLayer();
    void setController(Controller c);
    Controller getController();


    enum Layer {
        BACKGROUND, FOREGROUND, EFFECT
    }
}
