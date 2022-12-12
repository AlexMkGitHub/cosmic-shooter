package dev.team.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import dev.team.screen.ScreenManager;
import dev.team.screen.utils.Assets;

public class Hero extends Ship {
    public enum Skill {
        HP_MAX(20, 10), HP(20, 10), WEAPON(100, 1), MAGNET(50, 10);


        int cost;
        int power;

        Skill(int cost, int power) {
            this.cost = cost;
            this.power = power;
        }
    }

    private final float BASE_SIZE = 64;
    private final float BASE_RADIUS = BASE_SIZE / 2 - 3;

    private StringBuilder sb;
    int xpWidth;

    public void setTexture(TextureRegion texture) {
        this.texture = texture;
    }

    public Hero(GameController gc) {
        super(gc, 100, 500f);
        this.position = new Vector2(ScreenManager.SCREEN_WIDTH / 2, ScreenManager.SCREEN_HEIGHT / 2);
        this.velocity = new Vector2(0, 0);
        this.texture = Assets.getInstance().getAtlas().findRegion("ship10");
        this.hitArea = new Circle(position, 29);
        this.sb = new StringBuilder();
        this.hitArea.setRadius(BASE_RADIUS);
        this.ownerType = OwnerType.PLAYER;
        weaponNum = 0;
    }
    public void renderGUI(SpriteBatch batch, BitmapFont font) {
        sb.setLength(0);
        xpWidth = (hp * 60) / hpMax;
    }


    public void update(float dt) {
        super.update(dt);
        if (isAlive()) {
            boardControl(dt);
        }
    }

    private void boardControl(float dt) {

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            angle += 180 * dt;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            angle -= 180 * dt;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            accelerate(dt);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            brake(dt);
        }

    }

}



