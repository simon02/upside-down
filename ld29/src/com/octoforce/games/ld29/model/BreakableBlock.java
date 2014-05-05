package com.octoforce.games.ld29.model;

import com.octoforce.games.ld29.model.World.GameObjectType;

public class BreakableBlock extends Block {
	
	float time;
	float startTime;
	public boolean gone;

	public BreakableBlock(int x, int y, int width, int height, float time) {
		super(x, y, width, height);
		this.time = time;
		this.startTime = -1;
	}
	
	public Block update(float delta) {
		if (startTime != -1) {
			startTime += delta;
			if (startTime >= time)
				gone = true;
		}
		return this;
	}
	
	public Block start() {
		startTime = 0;
		return this;
	}

	@Override
	public GameObjectType getType() {
		return World.GameObjectType.BREAKABLE_BLOCK;
	}

}
