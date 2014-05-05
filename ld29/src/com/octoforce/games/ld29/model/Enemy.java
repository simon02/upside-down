package com.octoforce.games.ld29.model;

import com.badlogic.gdx.math.Vector2;
import com.octoforce.games.ld29.model.World.GameObjectType;
import com.octoforce.games.ld29.model.interfaces.Lethal;

public class Enemy extends GameObject implements Lethal {
	
	public Vector2 speed;
	public Vector2 accelleration;
	
	public Enemy(float x, float y, float width, float height) {
		super(x, y, width, height);
		this.speed = new Vector2();
		this.accelleration = new Vector2();
	}
	
	public Enemy update(float delta) {
		throw new RuntimeException("Implement this method");
	}

	@Override
	public GameObjectType getType() {
		return World.GameObjectType.ENEMY;
	}

}
