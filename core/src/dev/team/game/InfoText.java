package dev.team.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import dev.team.game.helpers.Poolable;

public class InfoText implements Poolable {
    private Color color;
    private StringBuilder text;
    private boolean active;
    private Vector2 position;
    private Vector2 velocity;
    private float time;
    private float maxTime;

    @Override
    public boolean isActive() {
        return active;
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);
        time += dt;
        if (time >= maxTime) {
            active = false;
        }
    }
}
