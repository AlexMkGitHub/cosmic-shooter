package dev.team.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import dev.team.helpers.Poolable;

public class Asteroid implements Poolable {
    private Vector2 position;
    private Vector2 velocity;
    private boolean active;
    private Texture asteroidTexture;
    private float angle;
    private float scale;
    private int scaleRnd;
    private int rotation;

    @Override
    public boolean isActive() {
        return active;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Texture getAsteroidTexture() {
        return asteroidTexture;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public float getAngle() {
        return angle;
    }

    public float getScale() {
        return scale;
    }

    public Asteroid() {
        this.asteroidTexture = new Texture("aster.png");
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.scaleRnd = MathUtils.random(1, 3);
        this.rotation = MathUtils.random(-2, 2);
        this.active = false;
    }

    public void deactivate() {
        active = false;
    }

    public void update(float dt) {

        /*----------------Произвольное вращение астероида-----------------------*/
        switch (rotation) {
            case -2:
                angle -= 1.5f;
                break;
            case -1:
                angle -= 0.7f;
                break;
            case 1:
                angle += 0.7f;
                break;
            case 2:
                angle += 1.5f;
                break;
        }
        /*---------------Произвольный размер астероида----------------*/
        switch (scaleRnd) {
            case 1:
                scale = 0.5f;
                break;
            case 2:
                scale = 0.75f;
                break;
            case 3:
                scale = 1.0f;
                break;
        }
    }

    public void activate(float x, float y, float vx, float vy) {
        position.set(x, y);
        velocity.set(vx, vy);
        active = true;
    }
}