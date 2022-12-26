package dev.team.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import dev.team.game.helpers.Poolable;
import dev.team.screen.ScreenManager;
import dev.team.screen.utils.Assets;

public class Ship implements Poolable {

    protected TextureRegion texture;
    protected Vector2 position;
    protected Vector2 velocity;
    protected GameController gc;
    protected Circle hitArea;
    protected Circle radiusDetected;
    protected Weapon currentWeapon;
    protected Weapon[] weapons;
    protected float angle;
    protected float enginePower;
    protected float fireTimer;
    protected int hpMax;
    protected int hp;
    protected int weaponNum;
    protected OwnerType ownerType;
    private boolean active;
    private final float BASE_SIZE = 64;
    private final float BASE_RADIUS = BASE_SIZE / 2 - 3;

    public OwnerType getOwnerType() {
        return ownerType;
    }

    public Weapon getCurrentWeapon() {
        return currentWeapon;
    }

    public float getAngle() {
        return angle;
    }

    public Circle getHitArea() {
        return hitArea;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Vector2 getPosition() {
        return position;
    }


    public TextureRegion getTexture() {
        return texture;
    }

    public void setTexture(TextureRegion texture) {
        this.texture = texture;
    }

    public Ship(GameController gc, int hpMax, float enginePower) {
        this.gc = gc;
        this.hpMax = hpMax;
        this.hp = hpMax;
        this.angle = 0.0f;
        this.enginePower = enginePower;
        this.active = false;
        this.position = new Vector2(ScreenManager.SCREEN_WIDTH / 2, ScreenManager.SCREEN_HEIGHT / 2);
        this.velocity = new Vector2(0, 0);
        this.texture = Assets.getInstance().getAtlas().findRegion("bot");
        this.hitArea = new Circle(position, 29);
        this.hitArea.setRadius(BASE_RADIUS);
        this.radiusDetected = new Circle(position, 200);
        if (gc.getLevel() <= 3) {
            weaponNum = 0;
        } else if (gc.getLevel() > 3 && gc.getLevel() < 6) {
            weaponNum = MathUtils.random(0, 1);
        } else if (gc.getLevel() > 6 && gc.getLevel() < 10) {
            weaponNum = MathUtils.random(0, 2);
        } else if (gc.getLevel() > 10) {
            weaponNum = MathUtils.random(1, 3);
        } else if (gc.getLevel() > 15) {
            weaponNum = MathUtils.random(2, 4);
        }

        createWeapons();

        this.currentWeapon = weapons[weaponNum];
        this.active = false;
    }

    public void accelerate(float dt) {
        velocity.x += MathUtils.cosDeg(angle) * enginePower * dt;
        velocity.y += MathUtils.sinDeg(angle) * enginePower * dt;
    }

    public void brake(float dt) {
        velocity.x -= MathUtils.cosDeg(angle) * enginePower / 2 * dt;
        velocity.y -= MathUtils.sinDeg(angle) * enginePower / 2 * dt;
    }

    public Circle getRadiusDetected() {
        return radiusDetected;
    }

    public void update(float dt) {
        fireTimer += dt;
        position.mulAdd(velocity, dt);
        hitArea.setPosition(position);
        radiusDetected.setPosition(position);
        checkSpaceBorders();
        float stopKoef = 1.0f - 0.8f * dt;
        if (stopKoef < 0.0f) {
            stopKoef = 0.0f;
        }
        velocity.scl(stopKoef);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 32, position.y - 32, 32, 32, 64, 64, 1,
                1, angle);
    }

    public void takeDamage(float amount) {
        hp -= amount;
        if (hp <= 0) {
            hp = 0;
        }
    }

    protected void checkSpaceBorders() {
        if (position.x < 32) {
            position.x = 32;
            velocity.x *= -0.5;
        }
        if (position.y < 32) {
            position.y = 32;
            velocity.y *= -0.5;
        }
        if (position.x > ScreenManager.SCREEN_WIDTH - 32) {
            position.x = ScreenManager.SCREEN_WIDTH - 32;
            velocity.x *= -0.5;
        }
        if (position.y > ScreenManager.SCREEN_HEIGHT - 32) {
            position.y = ScreenManager.SCREEN_HEIGHT - 32;
            velocity.y *= -0.5;
        }
    }

    public void tryToFire() {
        float wx;
        float wy;
        if (fireTimer > currentWeapon.getFirePeriod()) {
            fireTimer = 0.0f;
            currentWeapon.fire();
        }
    }

    private void createWeapons() {
        weapons = new Weapon[]{
                new Weapon(gc, this, "Laser", 0.2f, 1, 300f, 300,
                        new Vector3[]{
                                new Vector3(28, 90, 0),
                                new Vector3(28, -90, 0),
                        }),
                new Weapon(gc, this, "Laser", 0.2f, 1, 600f, 500,
                        new Vector3[]{
                                new Vector3(28, 0, 0),
                                new Vector3(28, 90, 20),
                                new Vector3(28, -90, -20),
                        }),
                new Weapon(gc, this, "Laser", 0.1f, 1, 600f, 1000,
                        new Vector3[]{
                                new Vector3(28, 0, 0),
                                new Vector3(28, 90, 20),
                                new Vector3(28, -90, -20),
                        }),
                new Weapon(gc, this, "Laser", 0.1f, 2, 600f, 1000,
                        new Vector3[]{
                                new Vector3(28, 90, 0),
                                new Vector3(28, -90, 0),
                                new Vector3(28, 90, 15),
                                new Vector3(28, -90, -15),
                        }),
                new Weapon(gc, this, "Laser", 0.1f, 3, 600f, 1500,
                        new Vector3[]{
                                new Vector3(28, 0, 0),
                                new Vector3(28, 90, 10),
                                new Vector3(28, 90, 20),
                                new Vector3(28, -90, -10),
                                new Vector3(28, -90, -20),
                        }),
        };
    }

    public void deactivate() {
        active = false;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public void activate(float x, float y, float vx, float vy) {
        position.set(x, y);
        velocity.set(vx, vy);
        active = true;
    }

}
