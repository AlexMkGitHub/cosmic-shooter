package dev.team;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.Timer;
import java.util.TimerTask;

public class Asteroid {
    private final Texture texture;
    private Vector2 position;
    private Vector2 velocity;
    private final float angle;
    private int asteroidRun;

    public Asteroid() {
        this.texture = new Texture("aster.png");
        this.position = new Vector2(MathUtils.random(-200, ScreenManager.SCREEN_WIDTH + 200), MathUtils.random(-200, ScreenManager.SCREEN_HEIGHT + 200));
        this.velocity = new Vector2(MathUtils.random(-100, 100), MathUtils.random(-100, 100));
        this.angle = 0.0f;
        this.asteroidRun = 1;
    }

    public void update(float dt) {
        if (position.x > ScreenManager.SCREEN_WIDTH + 260 || position.y > ScreenManager.SCREEN_HEIGHT + 260 || position.x < -260 || position.y < -260) {
            position = new Vector2(MathUtils.random(-256, ScreenManager.SCREEN_WIDTH + 256), MathUtils.random(-256, ScreenManager.SCREEN_HEIGHT + 256));
            velocity = new Vector2(MathUtils.random(-50, 10), MathUtils.random(-20, 10));
            int rnd = MathUtils.random(1, 4);
            switch (rnd) {
                case 1:
                    position.x = -256;
                    break;
                case 2:
                    position.x = ScreenManager.SCREEN_WIDTH + 256;
                    break;
                case 3:
                    position.y = -256;
                    break;
                case 4:
                    position.y = ScreenManager.SCREEN_HEIGHT + 256;
                    break;
            }
        }

        if (asteroidRun == 0) {
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    asteroidRun = 1;
                }
            };
            Timer timer = new Timer();
            long delay = MathUtils.random(2000, 5000);
            timer.schedule(task, delay);
        } else {
            position.x += velocity.x * dt;
            position.y += velocity.y * dt;
        }
    }


    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y, 128, 128, 256, 256, 1,
                1, angle, 0, 0, 256, 256, false, false);
    }
}
