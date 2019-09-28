package stickman.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import stickman.controller.CloudController;
import stickman.controller.EnemyController;
import stickman.controller.HeroController;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LevelImpl implements Level {
    private double gravity;
    private List<Entity> entities;
    private List<Cloud> clouds;
    private List<Enemy> enemies;
    private List<MovableEntity> movableEntities;
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
        hero.getController().setLevel(this);
        this.cloudVelocity = builder.cloudVelocity;
        this.finish = builder.finish;
        this.clouds = builder.clouds;
        this.enemies = builder.enemies;
        this.movableEntities = builder.movableEntities;
        tick = (int) ((width * 4 / 5) / (cloudVelocity / GameEngineImpl.FPS));

        for (MovableEntity movableEntity : movableEntities) {
            movableEntity.getController().setLevel(this);
        }
    }

    private void addCloud(double velocity) {
        Random rand = new Random();
        // randomize cloud image used
        int cloudType = rand.nextInt(2) + 1;

        /* spawn them on screen, else spawn them off screen so they move in screen from
         * left of screen */
        double xPos = -(width/4) - (rand.nextDouble() * (width));
        // randomise y position of cloud
        double yPos = rand.nextDouble() * (getHeight() / 4.5);
        String path = "cloud_" + cloudType + ".png";
        Cloud c = new Cloud(path, xPos, yPos, velocity);
        CloudController cc = new CloudController(c);
        c.setController(cc);
        cc.setLevel(this);
        entities.add(c);
        clouds.add(c);
        movableEntities.add(c);
    }

    public Finish getFinish() {
        return finish;
    }

    double getGravity() {
        return gravity;
    }

    @Override
    public Hero getHero() {
        return hero;
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

    static class LevelBuilder {
        private double gravity;
        private List<Entity> entities;
        private List<Cloud> clouds;
        private List<Enemy> enemies;
        private List<MovableEntity> movableEntities;
        private double height;
        private double width;
        private double floorHeight;
        private Hero hero;
        private double cloudVelocity;
        private Finish finish;

        LevelBuilder() {
            entities = new ArrayList<>();
            clouds = new ArrayList<>();
            enemies = new ArrayList<>();
            movableEntities = new ArrayList<>();
            gravity = 9.8 / GameEngineImpl.FPS;
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
            HeroController hc = new HeroController(hero);
            hero.setController(hc);
            entities.add(hero);
            movableEntities.add(hero);
            this.hero = hero;
            return this;
        }

        LevelBuilder addEnemy(JSONObject enemies) {
            JSONArray type = (JSONArray) enemies.get("enemyType");
            JSONArray x = (JSONArray) enemies.get("x");
            JSONArray y = (JSONArray) enemies.get("y");
            Random rand = new Random();

            String[] enemyImage = {"B", "G", "P", "R", "Y"};
            String enemyStance;
            if (rand.nextInt(2) == 0) {
                enemyStance = "a";
            } else {
                enemyStance = "b";
            }

            for (int i = 0; i < type.size(); i++) {
                int enemyType;
                if (type.get(i).equals("still")) {
                    enemyType = 4;
                } else {
                    enemyType = rand.nextInt(4);
                }

                String path = "slime" + enemyImage[enemyType] + enemyStance + ".png";
                Enemy e = new Enemy((double) x.get(i), floorHeight - (double) y.get(i), path, (String) type.get(i));
                EnemyController ec = new EnemyController(e);
                e.setController(ec);
                this.enemies.add(e);
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
            double xPos = (width * 4) - (rand.nextDouble() * (width * 4));

            // randomise y position of cloud
            double yPos = rand.nextDouble() * (height / 4.5);
            String path = "cloud_" + cloudType + ".png";
            Cloud c = new Cloud(path, xPos, yPos, velocity);
            CloudController cc = new CloudController(c);
            c.setController(cc);
            entities.add(c);
            clouds.add(c);
            movableEntities.add(c);
        }

        LevelBuilder addPlatform(JSONObject coord) {
            JSONArray x = (JSONArray)coord.get("x");
            JSONArray y = (JSONArray)coord.get("y");
            JSONArray platformType = (JSONArray)coord.get("platformType");

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
