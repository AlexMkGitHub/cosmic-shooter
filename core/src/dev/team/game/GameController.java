package dev.team.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import dev.team.screen.ScreenManager;
import dev.team.screen.utils.Assets;

public class GameController {
    private int heroLife;
    private Background background;
    private AsteroidController asteroidController;
    private BulletController bulletController;
    private ParticleController particleController;
    private PowerUpsController powerUpsController;
    private InfoController infoController;
    private BotController botController;
    private Hero hero;
    private Vector2 tempVec;
    private Stage stage;
    private int level;
    private boolean pause;
    private float timer;
    private float timerAsteroidsAdds;
    private float timerBots;
    private float addBotsTimer;
    private boolean addBots;
    private float rndTime;
    private StringBuilder sb;
    private BotHpView botHpView;
    private WorldRenderer wr;
    private float delta;
    private float dtTime;
    private float gameOverTimer;
    private float sizeAsteroid;
    private boolean heroVisible;
    private SpriteBatch batch;
    private boolean crashHero;

    public float getTimer() {
        return timer;
    }

    public float getTimerAsteroidsAdds() {
        return timerAsteroidsAdds;
    }

    public int getLevel() {
        return level;
    }

    public Hero getHero() {
        return hero;
    }

    public boolean isHeroVisible() {
        return heroVisible;
    }

    public InfoController getInfoController() {
        return infoController;
    }

    public ParticleController getParticleController() {
        return particleController;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public PowerUpsController getPowerUpsController() {
        return powerUpsController;
    }

    public AsteroidController getAsteroidController() {
        return asteroidController;
    }


    public Background getBackground() {
        return background;
    }

    public BulletController getBulletController() {
        return bulletController;
    }

    public BotHpView getHpView() {
        return botHpView;
    }

    public BotController getBotController() {
        return botController;
    }

    public Stage getStage() {
        return stage;
    }

    public int getHeroLife() {
        return heroLife;
    }

    public GameController(SpriteBatch batch) {
        this.batch = batch;
        this.crashHero = false;
        this.powerUpsController = new PowerUpsController(this);
        this.background = new Background(this);
        this.hero = new Hero(this);
        this.asteroidController = new AsteroidController(this);
        this.bulletController = new BulletController(this);
        this.particleController = new ParticleController();
        this.botController = new BotController(this);
        this.tempVec = new Vector2();
        this.stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        this.infoController = new InfoController();
        this.botHpView = new BotHpView(this);
        this.sb = new StringBuilder();
        this.level = 0;
        this.heroLife = 5;
        this.delta = 1.0f;
        this.timerBots = MathUtils.random(5.0f, 12.0f);
        this.addBotsTimer = 0.0f;
        this.addBots = false;
        this.heroVisible = true;
        this.gameOverTimer = 0.0f;
        this.sizeAsteroid = 0.0f;
        this.dtTime = Gdx.graphics.getDeltaTime();
        this.wr = new WorldRenderer(this, batch);
        this.timerAsteroidsAdds = 0.0f;
        Gdx.input.setInputProcessor(stage);
    }

    public void update(float dt) {
        if (pause) {
            return;
        }
        delta = dt;
        timer += dt;
        background.update(dt);
        asteroidController.update(dt);
        hero.update(dt);
        bulletController.update(dt);
        particleController.update(dt);
        infoController.update(dt);
        botController.update(dt);
        powerUpsController.update(dt);
        checkCollisions();
        if (heroVisible) {
            if (asteroidController.getActiveList().isEmpty() && botController.getActiveList().isEmpty()) {
                botController.getActiveList().clear();
                asteroidController.getActiveList().clear();
                addBots = false;
                timer += dt;
                if (timer > 3.0f) {
                    timerAsteroidsAdds += dt;
                    if (asteroidController.getActiveList().isEmpty() && botController.getActiveList().isEmpty() &&
                            timerAsteroidsAdds > 2.5f && timerAsteroidsAdds < 3.0f) {
                        hero.hp = hero.hpMax;
                    }
                    if (asteroidController.getActiveList().isEmpty() && botController.getActiveList().isEmpty() &&
                            timerAsteroidsAdds > 5.0f && timerAsteroidsAdds <= 5.5f) {
                        addBots = true;
                        timerAsteroidsAdds = 0.0f;
                        addBotsTimer = 0.0f;
                        level += 1;
                        addAsteroids(level);
                        timer = 0.0f;
                    }

                }
            }
            hero.weaponNum=4;
        }

        if (botController.getActiveList().isEmpty() && !asteroidController.getActiveList().isEmpty() && timerBots == 0 && addBots) {
            botController.getActiveList().clear();
            timerBots = MathUtils.random(3.0f, 10.0f);
        }
        if (botController.getActiveList().isEmpty() && timerBots > 0.0f) {
            botController.getActiveList().clear();
            addBotsTimer += dt;
            if (addBotsTimer > timerBots) {
                botController.setup(MathUtils.random(-100, ScreenManager.SCREEN_WIDTH + 100), MathUtils.random(-100, ScreenManager.SCREEN_HEIGHT + 100));
                addBotsTimer = 0.0f;
            }
        }

        if (!hero.isAlive()) {
            heroVisible = false;
            gameOverTimer += dt;
            hero.setTexture(Assets.getInstance().getAtlas().findRegion("mini"));
            hero.getTexture().setRegion(hero.getPosition().x, hero.getPosition().x, 2, 2);
            tempVec.set(-1256.0f, -1256.0f);
            hero.getPosition().mulAdd(tempVec, 0);
            if (gameOverTimer < 0.5f) {
                getParticleController().getEffectBuilder().destroyEffect(hero.getPosition().x, hero.getPosition().y);
            }
            if (gameOverTimer > 2.5f) {
            }
            if (gameOverTimer > 3.0f) {
                for (int i = 0; i < asteroidController.getActiveList().size(); i++) {
                    Asteroid a = asteroidController.getActiveList().get(i);
                    a.deactivate();
                }
                for (int i = 0; i < botController.getActiveList().size(); i++) {
                    Bot b = botController.getActiveList().get(i);
                    b.deactivate();
                }
            }
            if (gameOverTimer > 4) {
                hero.getPosition().set(ScreenManager.SCREEN_WIDTH / 2, ScreenManager.SCREEN_HEIGHT / 2);
                hero.setTexture(Assets.getInstance().getAtlas().findRegion("ship10"));
            }
            if (gameOverTimer > 6.0f) {
                hero.hp = hero.hpMax;
                gameOverTimer = 0;
                heroLife -= 1;
                level -= 1;
                heroVisible = true;
            }
            if (heroLife <= 0 && gameOverTimer > 2.5f) {
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAMEOVER, hero);
            }
        }

        stage.act(dt);
    }

    private void checkCollisions() {
        if (heroVisible) {
            for (int j = 0; j < asteroidController.getActiveList().size(); j++) {
                Asteroid a = asteroidController.getActiveList().get(j);
                if (a.getHitArea().overlaps(hero.getHitArea())) {
                    float dst = a.getPosition().dst(hero.getPosition());

                    float halfOverLen = (a.getHitArea().radius + hero.getHitArea().radius - dst) / 2;
                    tempVec.set(hero.getPosition()).sub(a.getPosition()).nor();
                    hero.getPosition().mulAdd(tempVec, halfOverLen);
                    a.getPosition().mulAdd(tempVec, -halfOverLen);

                    float sumScl = hero.getHitArea().radius + a.getHitArea().radius;
                    hero.getVelocity().mulAdd(tempVec, a.getHitArea().radius / sumScl * 100);
                    a.getVelocity().mulAdd(tempVec, -hero.getHitArea().radius / sumScl * 100);

                    if (a.takeDamage(2)) {
                        hero.addScore(a.getHpMax() * 50);
                    }
                    hero.takeDamage(level * 2);
                    sb.setLength(0);
                    sb.append("HP -  ").append(level * 2);
                    infoController.setup(hero.getPosition().x, hero.getPosition().y, sb.toString(), Color.RED);
                }
            }

            for (int j = 0; j < asteroidController.getActiveList().size(); j++) {
                Asteroid a = asteroidController.getActiveList().get(j);
                for (int i = 0; i < botController.getActiveList().size(); i++) {
                    Bot b = botController.getActiveList().get(i);
                    if (a.getHitArea().overlaps(b.getHitArea())) {
                        float dst = a.getPosition().dst(b.getPosition());
                        float halfOverLen = (a.getHitArea().radius + b.getHitArea().radius - dst) / 2;
                        tempVec.set(b.getPosition()).sub(a.getPosition()).nor();
                        b.getPosition().mulAdd(tempVec, halfOverLen);
                        a.getPosition().mulAdd(tempVec, -halfOverLen);
                        float sumScl = b.getHitArea().radius + a.getHitArea().radius;
                        b.getVelocity().mulAdd(tempVec, a.getHitArea().radius / sumScl * 100);
                        a.getVelocity().mulAdd(tempVec, -b.getHitArea().radius / sumScl * 100);
                    }
                }
            }

            for (int i = 0; i < bulletController.getActiveList().size(); i++) {
                Bullet b = bulletController.getActiveList().get(i);
                for (int j = 0; j < asteroidController.getActiveList().size(); j++) {
                    Asteroid a = asteroidController.getActiveList().get(j);
                    if (a.getHitArea().contains(b.getPosition())) {
                        particleController.getEffectBuilder().bulletCollideWithAsteroid(b);
                        b.deactivate();
                        if (a.getHp() <= 0) {
                            for (int d = 0; d < MathUtils.random(1, 100); d++) {
                                if (d <= 1) {
                                    if (botController.getActiveList().size() <= level && botController.getActiveList().size() < 2) {
                                        botController.setup(a.getPosition().x, a.getPosition().y);
                                    }
                                }
                            }
                        }

                        if (a.takeDamage(b.getOwner().getCurrentWeapon().getDamage())) {
                            if (b.getOwner().getOwnerType() == OwnerType.PLAYER) {
                                hero.addScore(a.getHpMax() * 100);
                                for (int k = 0; k < 3; k++) {
                                    powerUpsController.setup(a.getPosition().x, a.getPosition().y, a.getScale() * 0.25f);
                                }
                            }
                        }
                        break;
                    }
                }
            }

            for (int i = 0; i < bulletController.getActiveList().size(); i++) {
                Bullet b = bulletController.getActiveList().get(i);

                if (b.getOwner().getOwnerType() == OwnerType.BOT) {
                    if (hero.getHitArea().contains(b.getPosition())) {
                        particleController.getEffectBuilder().bulletCollideWithHero(b);
                        hero.takeDamage(b.getOwner().getCurrentWeapon().getDamage());
                        b.deactivate();
                    }
                }

                if (b.getOwner().getOwnerType() == OwnerType.PLAYER) {
                    for (int j = 0; j < botController.getActiveList().size(); j++) {
                        Bot bot = botController.getActiveList().get(j);
                        if (bot.getHitArea().contains(b.getPosition())) {
                            particleController.getEffectBuilder().bulletCollideWithAsteroid(b);
                            bot.takeDamage(b.getOwner().getCurrentWeapon().getDamage());
                            b.deactivate();
                        }
                    }
                }
            }

            for (int i = 0; i < powerUpsController.getActiveList().size(); i++) {
                PowerUp p = powerUpsController.getActiveList().get(i);

                if (hero.getMagneticField().contains(p.getPosition())) {
                    tempVec.set(hero.getPosition()).sub(p.getPosition()).nor();
                    p.getVelocity().mulAdd(tempVec, 100);
                }

                if (hero.getHitArea().contains(p.getPosition())) {
                    hero.consume(p);
                    particleController.getEffectBuilder().takePowerUpEffect(p.getPosition().x, p.getPosition().y, p.getType());
                    p.deactivate();
                }
            }
        }
    }

    private void addAsteroids(int astCount) {
        if (level >= 0 && level < 5) {
            sizeAsteroid = 0.5f;
        } else if (level >= 5 && level < 10) {
            sizeAsteroid = 0.5f;
        } else if (level >= 10) {
            sizeAsteroid = 0.75f;
        }
        sizeAsteroid += 0.25f;
        if (sizeAsteroid > 2.0f) {
            sizeAsteroid = 2.0f;
        }
        if (astCount > 3) {
            astCount = 3;
        }
        for (int i = 0; i < astCount; i++) {
            asteroidController.setup(MathUtils.random(0, ScreenManager.SCREEN_WIDTH),
                    MathUtils.random(0, ScreenManager.SCREEN_HEIGHT),
                    MathUtils.random(-200, 200),
                    MathUtils.random(-200, 200), MathUtils.random(0.5f, sizeAsteroid));
        }
    }

    public void dispose() {
        background.dispose();
    }
}