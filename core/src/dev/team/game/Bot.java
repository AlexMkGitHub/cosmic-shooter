package dev.team.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import dev.team.screen.ScreenManager;

public class Bot extends Ship {
    private final float BASE_SIZE = 64;
    private final float BASE_RADIUS = BASE_SIZE / 2 - 3;
    private boolean active;
    public float botOldAngel;

    public Bot(GameController gc, int hpMax, float enginePower, int weapon) {
        super(gc, hpMax, enginePower, MathUtils.random(0, 4));
        this.texture = new Texture("red_ship.png");
        this.hitArea = new Circle(position, 29);
        this.hitArea.setRadius(BASE_RADIUS);
        this.active = false;


    }

    public void deactivate() {
        active = false;
    }

    @Override
    public void update(float dt) {
        fireTimer += dt;
        position.mulAdd(velocity, dt);
        checkSpaceBorders();
        hitArea.setPosition(position);
        radiusDetected.setPosition(position);
    }

    @Override
    protected void checkSpaceBorders() {
        if (position.x < -BASE_RADIUS) {
            position.x = ScreenManager.SCREEN_WIDTH + BASE_RADIUS;
        }

        if (position.y < -BASE_RADIUS) {
            position.y = ScreenManager.SCREEN_HEIGHT + BASE_RADIUS;
        }

        if (position.x > ScreenManager.SCREEN_WIDTH + BASE_RADIUS) {
            position.x = -BASE_RADIUS;
        }

        if (position.y > ScreenManager.SCREEN_HEIGHT + BASE_RADIUS) {
            position.y = -BASE_RADIUS;
        }
    }

    public void activate(float x, float y, float vx, float vy, int weapon) {
        position.set(x, y);
        velocity.set(vx, vy);
        weapon = MathUtils.random(0, 4);
        active = true;
        angle = getVelocity().angleDeg(getPosition())+45.0f;
        botOldAngel = angle;
    }

    @Override
    public boolean isActive() {
        return active;
    }
}