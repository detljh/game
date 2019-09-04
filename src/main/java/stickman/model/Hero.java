package stickman.model;

public class Hero implements Entity {
    private String imagePath;
    private double xPos;
    private double yPos;
    private String heroSize;
    private double xVelocity;
    private double yVelocity;
    private double jumpHeight;

    Hero(String imagePath, double xPos, double xVelocity, double yVelocity, String heroSize, double jumpHeight,
         double floorHeight) {
        this.imagePath = imagePath;
        this.xPos = xPos;
        this.heroSize = heroSize;
        this.yPos = floorHeight - getHeight();
        this.xVelocity = xVelocity;
        this.yVelocity = yVelocity;
        this.jumpHeight = jumpHeight;
    }

    double getJumpHeight() {
        return jumpHeight;
    }

    void resetVelocityX() {
        this.xVelocity = 200.0 / GameEngineImpl.FPS;
    }

    void resetVelocityY() {
        this.yVelocity = 200.0 / GameEngineImpl.FPS;
    }

    void updateVelocityX(double x) {
        this.xVelocity = x;
    }

    void updateVelocityY(double y) {
        this.yVelocity = y;
    }

    double getVelocityX() {
        return xVelocity;
    }

    double getVelocityY() {
        return yVelocity;
    }

    void updateX(double x) {
        // prevent hero from moving off left of screen
        if (x < 0) {
            x = 0;
        }
        xPos = x;
    }

    void updateY(double y) {
        yPos = y;
    }

    void updateImagePath(String path) {
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
