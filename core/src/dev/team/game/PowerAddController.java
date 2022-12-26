package dev.team.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import dev.team.game.helpers.ObjectPool;

public class PowerAddController extends ObjectPool<PowerAdd> {
    private GameController gc;

    @Override
    protected PowerAdd newObject() {
        return new PowerAdd(gc);
    }

    public PowerAddController(GameController gc) {
        this.gc = gc;
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            PowerAdd pa = activeList.get(i);
            pa.render(batch);
        }
    }

    public void setup(float x, float y, float vx, float vy, float scale) {
        getActiveElement().activate(x, y, vx, vy, scale);
    }

    public void update(float dt) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }

    public void givePowerAdd(Vector2 position, float scale) {
        float rnd = MathUtils.random(0.0f, scale);
        System.out.println(rnd);
        if (rnd <= 0.1f) {
            setup(position.x, position.y, 0, -50, 0.7f);
        }
    }
}
