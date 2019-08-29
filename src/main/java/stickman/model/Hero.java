package stickman.model;

public class Hero implements Entity {
    private String imagePath;
    public double xPos;
    public double yPos;
    private String heroSize;

    public Hero(String imagePath, double xPos, double floorHeight, String heroSize) {
        this.imagePath = imagePath;
        this.xPos = xPos;
        this.heroSize = heroSize;
        this.yPos = floorHeight - getHeight();
    }

    public void updateX(double newX) {
        xPos = newX;
    }

    public void updateY(double newY) {
        yPos = newY;
    }

    public void updateImagePath(String path) {
        imagePath = path;
    }

    @Override
    public String getImagePath() {
        return imagePath;
    }

    @Override
    public double getXPos() {
        return xPos;
    }

    @Override
    public double getYPos() {
        return yPos;
    }

    @Override
    public double getHeight() {
        switch (heroSize) {
            case "tiny": return 20.0;
            case "normal": return 30.0;
            case "large": return 50.0;
            case "giant": return 100.0;
            default: return 0.0;
        }
    }

    @Override
    public double getWidth() {
        switch (heroSize) {
            case "tiny": return 20.0;
            case "normal": return 30.0;
            case "large": return 50.0;
            case "giant": return 100.0;
            default: return 0.0;
        }
    }

    @Override
    public Layer getLayer() {
        return Layer.FOREGROUND;
    }
}
