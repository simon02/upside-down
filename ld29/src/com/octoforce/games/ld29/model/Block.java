package com.octoforce.games.ld29.model;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.octoforce.games.ld29.model.World.GameObjectType;

public class Block extends GameObject {
	
	public Block(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	public Vector2 getPosition() {
		return position;
	}

	public Rectangle getBounds() {
		return bounds;
	}
	
	public boolean containsIncludingBorder(float x, float y) {
		return position.x <= x && position.x + bounds.width >= x && position.y <= y && position.y + bounds.height >= y;
	}

	@Override
	public GameObjectType getType() {
		return World.GameObjectType.BLOCK;
	}

}
