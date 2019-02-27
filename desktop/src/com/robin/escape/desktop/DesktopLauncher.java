package com.robin.escape.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.robin.escape.escape;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = escape.WIDTH;
		config.height = escape.HEIGHT;
		config.title = escape.TITLE;
		new LwjglApplication(new escape(), config);
	}
}
