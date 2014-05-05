package com.octoforce.games.ld29.model;

import com.octoforce.games.ld29.model.World.GameObjectType;

public class Star extends GameObject {

	public Star(int x, int y) {
		super(x, y, 1, 1);
	}

	@Override
	public GameObjectType getType() {
		return World.GameObjectType.STAR;
	}

}
