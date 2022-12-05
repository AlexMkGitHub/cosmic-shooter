package dev.team.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import dev.team.helpers.ObjectPool;
import dev.team.screen.ScreenManager;

public class BotController extends ObjectPool<Bot> {
    private GameController gc;
    private Vector2 position;
    private Vector2 velocity;
    private int hpMax;
    private float enginePower;
    private int weapon;
    private float angle;
    private float scale;

    public BotController(GameController gc) {
        this.gc = gc;
        this.enginePower = enginePower;
        this.weapon = weapon;

    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            Ship s = activeList.get(i);
            position = s.getPosition();
            velocity = s.getVelocity();
            angle = s.getAngle();
            scale = 1.00f;
            float dt = Gdx.graphics.getDeltaTime();
            if (position.x > ScreenManager.SCREEN_WIDTH + 128 || position.y > ScreenManager.SCREEN_HEIGHT + 128 ||
                    position.x < -128 || position.y < -128) {
                s.deactivate();
            } else {
                batch.draw(s.getTexture(), position.x, position.y, 64* scale, 64 * scale, 128 * scale, 128 * scale, 1,
                        1, angle, 0, 0, 128, 128, false, false);
                position.x += velocity.x * dt;
                position.y += velocity.y * dt;
            }
        }

    }

    public void setup(float x, float y, float vx, float vy, int weapon) {
        getActiveElement().activate(x, y, vx, vy, weapon);
    }

    public void update(float dt) {
        if (activeList.isEmpty()) {
            for (int i = 0; i < 4; i++) {
                position = new Vector2(MathUtils.random(-0, ScreenManager.SCREEN_WIDTH + 0), MathUtils.random(-0,
                        ScreenManager.SCREEN_HEIGHT + 0));
                velocity = new Vector2(MathUtils.random(-100, 100), MathUtils.random(-100, 100));
                int rndPosition = MathUtils.random(1, 4);
                switch (rndPosition) {
                    case 1:
                        position.x = -120;
                        break;
                    case 2:
                        position.x = ScreenManager.SCREEN_WIDTH + 120;
                        break;
                    case 3:
                        position.y = -120;
                        break;
                    case 4:
                        position.y = ScreenManager.SCREEN_HEIGHT + 120;
                        break;
                }
                setup(position.x, position.y, velocity.x, velocity.y, 1);
            }
        }
        checkPool();
    }

    @Override
    protected Bot newObject() {
        return new Bot(gc, hpMax, enginePower, weapon);
    }
}