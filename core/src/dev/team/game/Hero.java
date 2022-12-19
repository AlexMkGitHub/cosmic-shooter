package dev.team.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
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
    private Circle magneticField;
    private int score;
    private int scoreView;
    private StringBuilder sb;
    private int money;
    private int heroLife;
    private Texture xpView;
    int xpWidth;

    public int getScore() {
        return score;
    }

    public int getHp() {
        return hp;
    }

    public void setTexture(TextureRegion texture) {
        this.texture = texture;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getHpMax() {
        return hpMax;
    }

    public Circle getMagneticField() {
        return magneticField;
    }

    public int getHeroLife() {
        return heroLife;
    }

    public Hero(GameController gc) {
        super(gc, 100, 500f);
        this.position = new Vector2(ScreenManager.SCREEN_WIDTH / 2, ScreenManager.SCREEN_HEIGHT / 2);
        this.velocity = new Vector2(0, 0);
        this.texture = Assets.getInstance().getAtlas().findRegion("ship10");
        this.xpView = new Texture("images/xp_hero.png");
        this.hitArea = new Circle(position, 29);
        this.money = 500;
        this.sb = new StringBuilder();
        this.magneticField = new Circle(position, 100);
        this.hitArea.setRadius(BASE_RADIUS);
        this.ownerType = OwnerType.PLAYER;
        this.heroLife = 5;
        weaponNum = 0;
    }

    public void addScore(int amount) {
        score += amount;
    }


    public boolean isMoneyEnough(int amount) {
        return money >= amount;
    }

    public void decreaseMoney(int amount) {
        money -= amount;
    }

    public void renderGUI(SpriteBatch batch, BitmapFont font) {
        sb.setLength(0);
        sb.append("LIFE: ").append(gc.getHeroLife()).append("\n");
        sb.append("SCORE: ").append(scoreView).append("\n");
        sb.append("HP: ").append(hp).append(" / ").append(hpMax).append("\n");
        sb.append("BULLETS: ").append(currentWeapon.getCurBullets()).append(" / ").append(currentWeapon.getMaxBullets()).append("\n");
        sb.append("MONEY: ").append(money).append("\n");
        sb.append("MAGNET: ").append((int) magneticField.radius).append("\n");
        font.draw(batch, sb, 20, 700);
        xpWidth = (hp * 60) / hpMax;
        batch.draw(xpView, position.x - 32, position.y + 32, xpWidth, 5);
    }

    public void setPause(boolean pause) {
        gc.setPause(pause);
    }

    public void update(float dt) {
        super.update(dt);
        updateScore(dt);
        if (isAlive()) {
            boardControl(dt);
        }
        magneticField.setPosition(position);
    }

    private void boardControl(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            tryToFire();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            angle += 180 * dt;
            float bx = position.x + MathUtils.cosDeg(angle + 90) * 20;
            float by = position.y + MathUtils.sinDeg(angle + 90) * 20;
            for (int i = 0; i < 2; i++) {
                gc.getParticleController().setup(bx + MathUtils.random(-4, 4), by + MathUtils.random(-4, 4),
                        velocity.x * 0.1f + MathUtils.random(-10, 10), velocity.y * 0.1f + MathUtils.random(-10, 10),
                        0.4f, 1.2f, 0.2f,
                        1.0f, 0.5f, 0, 1,
                        1, 1, 1, 0);
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            angle -= 180 * dt;
            float bx = position.x + MathUtils.cosDeg(angle - 90) * 20;
            float by = position.y + MathUtils.sinDeg(angle - 90) * 20;
            for (int i = 0; i < 2; i++) {
                gc.getParticleController().setup(bx + MathUtils.random(-4, 4), by + MathUtils.random(-4, 4),
                        velocity.x * 0.1f + MathUtils.random(-10, 10), velocity.y * 0.1f + MathUtils.random(-10, 10),
                        0.4f, 1.2f, 0.2f,
                        1.0f, 0.5f, 0, 1,
                        1, 1, 1, 0);
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            accelerate(dt);
            if (velocity.len() > 50.0f) {
                float bx = position.x + MathUtils.cosDeg(angle + 180) * 20;
                float by = position.y + MathUtils.sinDeg(angle + 180) * 20;
                for (int i = 0; i < 3; i++) {
                    gc.getParticleController().setup(bx + MathUtils.random(-4, 4), by + MathUtils.random(-4, 4),
                            velocity.x * -0.2f + MathUtils.random(-20, 20), velocity.y * -0.2f + MathUtils.random(-20, 20),
                            0.5f, 1.2f, 0.2f, 1.0f, 0.5f, 0, 1, 1, 1, 1, 0
                    );
                }
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            brake(dt);
            float bx = position.x + MathUtils.cosDeg(angle + 90) * 20;
            float by = position.y + MathUtils.sinDeg(angle + 90) * 20;
            for (int i = 0; i < 2; i++) {
                gc.getParticleController().setup(bx + MathUtils.random(-4, 4), by + MathUtils.random(-4, 4),
                        velocity.x * 0.1f + MathUtils.random(-20, 20), velocity.y * 0.1f + MathUtils.random(-20, 20),
                        0.4f, 1.2f, 0.2f,
                        1.0f, 0.5f, 0, 1,
                        1, 1, 1, 0);
            }
            bx = position.x + MathUtils.cosDeg(angle - 90) * 20;
            by = position.y + MathUtils.sinDeg(angle - 90) * 20;
            for (int i = 0; i < 2; i++) {
                gc.getParticleController().setup(bx + MathUtils.random(-4, 4), by + MathUtils.random(-4, 4),
                        velocity.x * 0.1f + MathUtils.random(-20, 20), velocity.y * 0.1f + MathUtils.random(-20, 20),
                        0.4f, 1.2f, 0.2f,
                        1.0f, 0.5f, 0, 1,
                        1, 1, 1, 0);
            }
        }
    }

    public void consume(PowerUp p) {
        sb.setLength(0);
        switch (p.getType()) {
            case MEDKIT:
                int oldHp = hp;
                hp += p.getPower();
                if (hp > hpMax) {
                    hp = hpMax;
                }
                sb.append("HP + ").append(hp - oldHp);
                gc.getInfoController().setup(p.getPosition().x, p.getPosition().y, sb.toString(), Color.GREEN);
                break;
            case MONEY:
                money += p.getPower();
                sb.append("Money + ").append(p.getPower());
                gc.getInfoController().setup(p.getPosition().x, p.getPosition().y, sb.toString(), Color.ORANGE);
                break;
            case AMMOS:
                currentWeapon.addAmmos(p.getPower());
                sb.append("Ammo + ").append(p.getPower());
                gc.getInfoController().setup(p.getPosition().x, p.getPosition().y, sb.toString(), Color.PURPLE);
                break;
        }
    }

    public void botDestroyPresent(int present) {
        sb.setLength(0);
        switch (present) {
            case 1:
                int oldHp = hp;
                hp += 50;
                if (hp > hpMax) {
                    hp = hpMax;
                }
                sb.append("HP + ").append(hp - oldHp);
                gc.getInfoController().setup(position.x - 32, position.y + 32, sb.toString(), Color.GREEN);
                break;
            case 2:
                money += 50;
                sb.append("Money + " + 50);
                gc.getInfoController().setup(position.x - 32, position.y + 32, sb.toString(), Color.ORANGE);
                break;
            case 3:
                currentWeapon.addAmmos(50);
                sb.append("Ammo + 50");
                gc.getInfoController().setup(position.x - 32, position.y + 32, sb.toString(), Color.PURPLE);
                break;
        }
    }

    private void updateScore(float dt) {
        if (scoreView < score) {
            scoreView += 2000 * dt;
            if (scoreView > score) {
                scoreView = score;
            }
        }
    }

}



