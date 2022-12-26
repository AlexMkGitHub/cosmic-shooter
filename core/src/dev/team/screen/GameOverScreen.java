package dev.team.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import dev.team.game.BackgroundOver;
import dev.team.game.Hero;
import dev.team.screen.utils.Assets;


public class GameOverScreen extends AbstractScreen {
    private BackgroundOver backgroundOver;
    private BitmapFont font72;
    private BitmapFont font48;
    private BitmapFont font24;
    private StringBuilder sb;
    private Hero defeatedHero;
    private Music gameOverMusic;

    public void setDefeatedHero(Hero defeatedHero) {
        this.defeatedHero = defeatedHero;
    }

    public GameOverScreen(SpriteBatch batch) {
        super(batch);
        this.sb = new StringBuilder();
    }

    @Override
    public void show() {
        this.backgroundOver = new BackgroundOver(null);
        this.font72 = Assets.getInstance().getAssetManager().get("fonts/font72.ttf");
        this.font48 = Assets.getInstance().getAssetManager().get("fonts/font48.ttf");
        this.font24 = Assets.getInstance().getAssetManager().get("fonts/font24.ttf");
        this.gameOverMusic = Assets.getInstance().getAssetManager().get("audio/game_over.mp3");
        this.gameOverMusic.setLooping(false);
        this.gameOverMusic.setVolume(0.50f);
        gameOverMusic.play();

    }

    public void update(float dt) {
        backgroundOver.update(dt);
        if (Gdx.input.justTouched()) {
            ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);
        }
    }

    @Override
    public void render(float delta) {
        update(delta);
        ScreenUtils.clear(0.0f, 0.0f, 0.0f, 1);
        batch.begin();
        backgroundOver.render(batch);
        font72.draw(batch, "Game over!", 0, 600, 1280, Align.center, false);
        sb.setLength(0);
        sb.append("SCORE: ").append(defeatedHero.getScore()).append("\n");
        sb.append("MONEY: ").append(defeatedHero.getMoney()).append("\n");
        font48.draw(batch, sb, 0, 400, 1280, Align.center, false);
        font24.draw(batch, "Tap screen for return to main menu...", 0, 60, 1280, Align.center, false);
        batch.end();
    }

    @Override
    public void dispose() {
        backgroundOver.dispose();
    }
}
