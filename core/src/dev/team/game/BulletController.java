package dev.team.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import dev.team.game.helpers.ObjectPool;
import dev.team.screen.utils.Assets;

public class BulletController extends ObjectPool<Bullet> {
    private TextureRegion bulletTexture;
    private GameController gc;

    @Override
    protected Bullet newObject() {
        return new Bullet(gc);
    }

    public BulletController(GameController gc) {
        this.gc = gc;
        this.bulletTexture = Assets.getInstance().getAtlas().findRegion("bullet");
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            Bullet b = activeList.get(i);
            batch.draw(bulletTexture, b.getPosition().x - 16, b.getPosition().y - 16, 16, 16, 32, 32, 1.0f,
                    1.0f, 0.0f);
        }
    }

    public void setup(Ship owner, float x, float y, float vx, float vy) {
        getActiveElement().activate(owner, x, y, vx, vy);
    }

    public void update(float dt) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }

    public void boomBullet(Vector2 v, SpriteBatch batch) {
        batch.draw(bulletTexture, v.x - 16, v.y - 16, 16, 16, 32, 32, 4f,
                4f, 0.0f);
    }

}
