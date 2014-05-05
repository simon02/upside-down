package com.octoforce.games.ld29.client;

import com.octoforce.games.ld29.LD29Game;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

public class GwtLauncher extends GwtApplication {
	@Override
	public GwtApplicationConfiguration getConfig () {
		GwtApplicationConfiguration cfg = new GwtApplicationConfiguration(900, 600);
		return cfg;
	}

	@Override
	public ApplicationListener getApplicationListener () {
		return new LD29Game();
	}
}