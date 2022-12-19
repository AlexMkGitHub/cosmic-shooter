package dev.team.game;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import dev.team.screen.utils.Assets;

public class BotHpView {
    private GameController gc;
    private BitmapFont font32;
    private BitmapFont font24;
    private TextureRegion texture;
    int xpWidth;

    public BotHpView(GameController gc) {

        this.gc = gc;
        this.font32 = Assets.getInstance().getAssetManager().get("fonts/font32.ttf");
        this.font24 = Assets.getInstance().getAssetManager().get("fonts/font24.ttf");
        this.texture = Assets.getInstance().getAtlas().findRegion("xp");

    }

    public void render(SpriteBatch batch) {
        if (!gc.getBotController().getActiveList().isEmpty()) {
            for (int i = 0; i < gc.getBotController().getActiveList().size(); i++) {
                Bot b = gc.getBotController().getActiveList().get(i);
                xpWidth = (b.hp * 60) / b.hpMax;
                batch.draw(texture, b.getPosition().x - 32, b.getPosition().y + 32, xpWidth, 5);
            }
        }


    }
}
