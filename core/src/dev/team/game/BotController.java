package dev.team.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dev.team.game.helpers.ObjectPool;

public class BotController extends ObjectPool<Bot> {
    private GameController gc;

    @Override
    protected Bot newObject() {
        return new Bot(gc);
    }

    public BotController(GameController gc) {
        this.gc = gc;
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            Bot b = activeList.get(i);
            b.render(batch);
        }
    }

    public void setup(float x, float y) {
        getActiveElement().activate(x, y);
        gc.getParticleController().getEffectBuilder().botAddEffect(x, y);
    }

    public void update(float dt) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }

}
