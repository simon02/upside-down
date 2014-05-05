package com.octoforce.games.ld29;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "ld29";
		cfg.useGL20 = false;
		cfg.width = 900;
		cfg.height = 600;
		
		new LwjglApplication(new LD29Game(), cfg);
	}
}
