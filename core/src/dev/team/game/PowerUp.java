package dev.team.game;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import dev.team.game.helpers.Poolable;

public class PowerUp implements Poolable {
    public enum Type {
        MEDKIT(0), MONEY(1), AMMOS(2);

        int index;

        Type(int index) {
            this.index = index;
        }
    }

    private GameController gc;
    private Vector2 position;
    private Vector2 velocity;
    private float time;
    private boolean active;
    private Type type;
    private int power;
    private Circle hitArea;
    private final float BASE_SIZE = 64;
    private final float BASE_RADIUS = BASE_SIZE / 2;
    private float scl = 50.0f;

    public Circle getHitArea() {
        return hitArea;
    }

    public Type getType() {
        return type;
    }

    public float getTime() {
        return time;
    }

    public int getPower() {
        return power;
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getScl() {
        return scl;
    }

    public void setScl(float scl) {
        this.scl = scl;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void deactivate() {
        active = false;
    }

    public PowerUp(GameController gc) {
        this.gc = gc;
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.active = false;
        this.hitArea = new Circle(position, BASE_RADIUS);
    }

    public void activate(Type type, float x, float y, int power) {
        this.type = type;
        this.position.set(x, y);
        this.velocity.set(MathUtils.random(-1.0f, 1.0f), MathUtils.random(-1.0f, 1.0f));
        this.velocity.nor().scl(scl);
        this.active = true;
        this.power = power;
        this.time = 0.0f;
        hitArea.setPosition(position);
        hitArea.setRadius(BASE_RADIUS * 0.9f);
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);
        hitArea.setPosition(position);
        time += dt;
        if (time >= 7.0f) {
            deactivate();
        }
    }
}
