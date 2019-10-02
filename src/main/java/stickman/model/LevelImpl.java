package stickman.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LevelImpl implements Level {
    private double gravity;
    private List<Entity> entities;
    private List<MovableEntity> movableEntities;
    // list of entities to remove each tick
    private List<Entity> removeEntities;
    private double height;
    private double width;
    private double floorHeight;
    private Hero hero;
    private int tick;
    private double cloudVelocity;
    private Finish finish;

    private LevelImpl(LevelBuilder builder) {
        this.gravity = builder.gravity;
        this.entities = builder.entities;
        this.height = builder.height;
        this.width = builder.width;
        this.floorHeight = builder.floorHeight;
        this.hero = builder.hero;
        this.cloudVelocity = builder.cloudVelocity;
        this.finish = builder.finish;
        this.movableEntities = builder.movableEntities;
        tick = (int) ((width * 4 / 5) / (cloudVelocity / GameEngineImpl.FPS));
        removeEntities = new ArrayList<>();

        // set the level context for all controllers
        for (MovableEntity movableEntity : movableEntities) {
            movableEntity.getController().setLevel(this);
        }
    }

    public void addEntityToRemove(Entity e) {
        removeEntities.add(e);
    }

    void clearRemoveEntities() {
        for (Entity e : removeEntities) {
            entities.remove(e);
        }
        removeEntities = new ArrayList<>();
    }

    private void addCloud(double velocity) {
        Random rand = new Random();
        // randomize cloud image used
        int cloudType = rand.nextInt(2) + 1;
        // spawn them off screen so they move in screen from left of screen
        double xPos = -width + (rand.nextDouble() * (-width / 2));
        // randomise y position of cloud
        double yPos = rand.nextDouble() * (getHeight() / 4.5);
        String path = "cloud_" + cloudType + ".png";
        Cloud c = new Cloud(path, xPos, yPos, velocity);
        entities.add(c);
        movableEntities.add(c);
    }

    double getGravity() {
        return gravity;
    }

    @Override
    public Hero getHero() {
        return hero;
    }

    @Override
    public Finish getFinish() {
        return finish;
    }

    List<MovableEntity> getMovableEntities() {
        return movableEntities;
    }

    @Override
    public List<Entity> getEntities() {
        return entities;
    }

    @Override
    public double getHeight() {
        return height;
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public void tick() {
        // spawn 1-3 clouds if tick is 0 and velocity is positive
        if (cloudVelocity > 0) {
            tick--;
            if (tick > 0) {
                return;
            }
            tick = (int) ((width * 4 / 5) / (cloudVelocity / GameEngineImpl.FPS));

            int spawn = new Random().nextInt(3) + 1;
            for (int i = 0; i < spawn; i++) {
                addCloud(cloudVelocity);
            }
        }
    }

    @Override
    public double getFloorHeight() {
        return floorHeight;
    }

    @Override
    public double getHeroX() {
        return hero.getXPos();
    }

    /** Builder class for level */
    static class LevelBuilder {
        private double gravity;
        private List<Entity> entities;
        private List<MovableEntity> movableEntities;
        private double height;
        private double width;
        private double floorHeight;
        private Hero hero;
        private double cloudVelocity;
        private Finish finish;

        LevelBuilder() {
            entities = new ArrayList<>();
            movableEntities = new ArrayList<>();
            gravity = 9.8;
        }

        LevelBuilder setWidth(double width) {
            this.width = width;
            return this;
        }

        LevelBuilder setHeight(double height) {
            this.height = height;
            return this;
        }

        LevelBuilder setFloorHeight(double floorHeight) {
            this.floorHeight = height - floorHeight;
            return this;
        }

        LevelBuilder addHero(JSONObject config) {
            String size = (String) config.get("size");
            double xPos = (double) config.get("x");
            double jumpHeight = (double) config.get("jumpHeight");
            Hero hero = new Hero("ch_stand1.png", xPos, size, jumpHeight, floorHeight);
            entities.add(hero);
            movableEntities.add(hero);
            this.hero = hero;
            addSafeZone();
            return this;
        }

        private void addSafeZone() {
            double size = hero.getHeight() * 1.2;
            SafeZone s = new SafeZone(hero.getInitialX() - size / 4,
                    hero.getInitialY() + hero.getHeight(), size,
                    size, "safe_zone.png");
            entities.add(s);
        }

        LevelBuilder addEnemy(JSONObject enemies) {
            JSONArray type = (JSONArray) enemies.get("enemyType");
            JSONArray x = (JSONArray) enemies.get("x");
            JSONArray y = (JSONArray) enemies.get("y");

            if (x == null || y == null || type == null) {
                return this;
            }

            if (x.size() != y.size() || y.size() != type.size()) {
                return this;
            }

            // randomise initial stance of enemy
            Random rand = new Random();
            String[] enemyImage = {"B", "G", "P", "R", "Y"};

            for (int i = 0; i < type.size(); i++) {
                String enemyStance;
                int randomStance = rand.nextInt(2);
                if (randomStance == 0) {
                    enemyStance = "a";
                } else {
                    enemyStance = "b";
                }

                int enemyType;
                if (type.get(i).equals("still")) {
                    // still enemies are yellow
                    enemyType = 4;
                } else {
                    enemyType = rand.nextInt(4);
                }

                String path = "slime" + enemyImage[enemyType] + enemyStance + ".png";
                Enemy e = new Enemy((double) x.get(i), floorHeight - (double) y.get(i), path, (String) type.get(i));
                entities.add(e);
                movableEntities.add(e);
            }
            return this;
        }

        LevelBuilder spawnClouds(double velocity) {
            cloudVelocity = velocity;
            // spawn initial number of clouds
            for (int i = 0; i < (int) width / 40; i++) {
                addCloud(velocity);
            }
            return this;
        }

       private void addCloud(double velocity) {
            Random rand = new Random();
            // randomize cloud image used
            int cloudType = rand.nextInt(2) + 1;
            // spawn them on screen
            double xPos =  rand.nextDouble() * (width * 4);

            // randomise y position of cloud
            double yPos = rand.nextDouble() * (height / 4.5);
            String path = "cloud_" + cloudType + ".png";
            Cloud c = new Cloud(path, xPos, yPos, velocity);
            entities.add(c);
            movableEntities.add(c);
        }

        LevelBuilder addPlatform(JSONObject coord) {
            JSONArray x = (JSONArray)coord.get("x");
            JSONArray y = (JSONArray)coord.get("y");
            JSONArray platformType = (JSONArray)coord.get("platformType");

            if (x == null || y == null || platformType == null) {
                return this;
            }

            if (x.size() != y.size() || y.size() != platformType.size()) {
                return this;
            }

            for (int i = 0; i < x.size(); i++) {
                String type = (String) platformType.get(i);
                String path = "foot_tile.png";
                if (type.equals("icy")) {
                    path = "foot_tile_icy.png";
                }
                Platform p = new Platform((double) x.get(i), floorHeight - (double) y.get(i), type, path);
                entities.add(p);
            }
            return this;
        }

        LevelBuilder addFinish(JSONObject coord) {
            double x = (double) coord.get("x");
            double y = (double) coord.get("y");
            String path = "finish.png";
            Finish f = new Finish(x, floorHeight - y, path);
            entities.add(f);
            finish = f;
            return this;
        }

        LevelImpl build() {
            LevelImpl level = new LevelImpl(this);
            return level;
        }
    }
}
