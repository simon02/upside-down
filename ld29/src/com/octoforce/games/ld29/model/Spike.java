package com.octoforce.games.ld29.model;

import com.octoforce.games.ld29.model.World.GameObjectType;
import com.octoforce.games.ld29.model.interfaces.Lethal;

public class Spike extends GameObject implements Lethal {
	
	public enum Direction {
		UP, DOWN, LEFT, RIGHT
	}
	
	private Direction direction;

	public Spike(float x, float y, Direction direction) {
		super(x, y, 1, 1);
		this.direction = direction;
	}
	
	public Direction getDirection() {
		return this.direction;
	}

	@Override
	public GameObjectType getType() {
		return World.GameObjectType.SPIKE;
	}

}
