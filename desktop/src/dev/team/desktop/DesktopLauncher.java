package dev.team.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import dev.team.StarGame;
import dev.team.screen.ScreenManager;

public class DesktopLauncher {
	static public float x;
	static public float y;
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = ScreenManager.SCREEN_WIDTH;
		x = config.width;
		config.height = ScreenManager.SCREEN_HEIGHT;
		y = config.height;
		new LwjglApplication(new StarGame(), config);

	}
}
