package com.octoforce.games.ld29.model;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class GameObject {
	
	public Vector2 position;
	public Rectangle bounds;
	
	public GameObject() {}
	
	public GameObject(float x, float y, float width, float height) {
		this.position = new Vector2(x, y);
		this.bounds = new Rectangle(x, y, width, height);
	}
	
	public void setPosition(Vector2 v) {
		this.position = v;
		this.bounds.x = v.x;
		this.bounds.y = v.y;
	}
	
	public void increasePosition(Vector2 v) {
		this.setPosition(position.add(v));
	}
	
	public abstract World.GameObjectType getType();

}
