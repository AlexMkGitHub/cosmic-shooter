package dev.team.game;

import com.badlogic.gdx.math.MathUtils;

public class GameController {

    private Background background;
    private Hero hero;
    private AsteroidController asteroidController;
    private BotController botController;

    public AsteroidController getAsteroidController() {
        return asteroidController;
    }

    public Background getBackground() {
        return background;
    }

    public Hero getHero() {
        return hero;
    }

    public BotController getBotController() {
        return botController;
    }

    public GameController() {
        this.background = new Background(this);
        this.hero = new Hero(this);
        this.asteroidController = new AsteroidController();
        this.botController = new BotController(this);
        botController.setup(MathUtils.random(-50, 50), MathUtils.random(-100, 300), MathUtils.random(-50, 50), MathUtils.random(-100, 300),MathUtils.random(0, 4));

    }

    public void update(float dt) {
        background.update(dt);
        hero.update(dt);
        asteroidController.update(dt);
        botController.update(dt);
    }

}