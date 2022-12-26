package dev.team.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import dev.team.game.helpers.Poolable;
import dev.team.screen.ScreenManager;
import dev.team.screen.utils.Assets;


public class PowerAdd implements Poolable {
    private GameController gc;
    private TextureRegion texture;
    private Vector2 position;
    private Vector2 velocity;
    private boolean active;
    private int count;
    private Circle hitArea;
    private float scale;
    private int rndPower;
    private String title;

    private final float BASE_SIZE = 64;
    private final float BASE_RADIUS = BASE_SIZE / 2;

    @Override
    public boolean isActive() {
        return active;
    }

    public String getTitle() {
        return title;
    }

    public int getRndPower() {
        return rndPower;
    }

    public int getCount() {
        return count;
    }

    public Circle getHitArea() {
        return hitArea;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public PowerAdd(GameController gc) {
        this.gc = gc;
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.hitArea = new Circle(0, 0, 0);
        this.active = false;
        this.rndPower = MathUtils.random(1, 3);
        System.out.println(rndPower);
        if (rndPower > 0 && rndPower < 4) {
            switch (rndPower) {
                case 1:
                    this.title = "Ammo";
                    this.texture = Assets.getInstance().getAtlas().findRegion("ammo");
                    this.count = 50;
                    break;
                case 2:
                    this.title = "First Aid Kit";
                    this.texture = Assets.getInstance().getAtlas().findRegion("aidkit");
                    this.count = 20;
                    break;
                case 3:
                    this.title = "Money";
                    this.texture = Assets.getInstance().getAtlas().findRegion("money");
                    this.count = 5;
                    break;
            }
        }

    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 32, position.y - 32, 32, 32,
                64, 64, scale, scale,
                0);
    }

    public void deactivate() {
        active = false;
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);
        if (position.x < -(BASE_RADIUS * scale) ||
                position.x > ScreenManager.SCREEN_WIDTH + (BASE_RADIUS * scale) ||
                position.y < -(BASE_RADIUS * scale) ||
                position.y > ScreenManager.SCREEN_HEIGHT + (BASE_RADIUS * scale)) {
            deactivate();

        }
        hitArea.setPosition(position);
    }

    public void activate(float x, float y, float vx, float vy, float scale) {
        position.set(x, y);
        velocity.set(vx, vy);
        active = true;
        this.scale = scale;
        hitArea.setPosition(position);
        hitArea.setRadius(BASE_RADIUS * scale * 0.9f);
    }

}
