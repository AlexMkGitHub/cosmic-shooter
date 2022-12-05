package dev.team.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import dev.team.helpers.ObjectPool;
import dev.team.screen.ScreenManager;

public class AsteroidController extends ObjectPool<Asteroid> {
    private Vector2 position;
    private Vector2 velocity;
    private Texture asteroidTexture;
    private float angle;
    private float scale;

    @Override
    protected Asteroid newObject() {
        return new Asteroid();
    }

    public AsteroidController() {
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getScale() {
        return scale;
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            Asteroid a = activeList.get(i);
            asteroidTexture = a.getAsteroidTexture();
            position = a.getPosition();
            velocity = a.getVelocity();
            angle = a.getAngle();
            scale = a.getScale();
            float dt = Gdx.graphics.getDeltaTime();
            if (position.x > ScreenManager.SCREEN_WIDTH + 258 || position.y > ScreenManager.SCREEN_HEIGHT + 258 ||
                    position.x < -258 || position.y < -258) {
                a.deactivate();
            } else {
                batch.draw(asteroidTexture, position.x, position.y, 128* scale, 128 * scale, 256 * scale, 256 * scale, 1,
                        1, angle, 0, 0, 256, 256, false, false);
                position.x += velocity.x * dt;
                position.y += velocity.y * dt;
            }
        }
    }

    public void setup(float x, float y, float vx, float vy) {
        getActiveElement().activate(x, y, vx, vy);
    }

    public void update(float dt) {

        if (activeList.isEmpty()) {
            for (int i = 0; i < 4; i++) {
                position = new Vector2(MathUtils.random(-256, ScreenManager.SCREEN_WIDTH + 256), MathUtils.random(-256,
                        ScreenManager.SCREEN_HEIGHT + 256));
                velocity = new Vector2(MathUtils.random(-100, 100), MathUtils.random(-100, 100));
                int rndPosition = MathUtils.random(1, 4);
                switch (rndPosition) {
                    case 1:
                        position.x = -250;
                        break;
                    case 2:
                        position.x = ScreenManager.SCREEN_WIDTH + 250;
                        break;
                    case 3:
                        position.y = -250;
                        break;
                    case 4:
                        position.y = ScreenManager.SCREEN_HEIGHT + 250;
                        break;
                }
                setup(position.x, position.y, velocity.x, velocity.y);
            }
        }

        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }

}