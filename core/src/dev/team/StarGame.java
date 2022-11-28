package dev.team;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class StarGame extends ApplicationAdapter {
    private SpriteBatch batch;
    private Background background;


    @Override
    public void create() {
        this.batch = new SpriteBatch();
        this.background = new Background(this);
    }

    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();
        update(dt);
        ScreenUtils.clear(0.0f, 0.1f, 0.5f, 1);
        batch.begin();
        background.render(batch);
        batch.end();
    }

    public void update(float dt) {
        background.update(dt);
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
