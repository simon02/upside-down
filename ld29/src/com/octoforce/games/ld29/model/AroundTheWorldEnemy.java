package com.octoforce.games.ld29.model;

import com.badlogic.gdx.math.Vector2;

public class AroundTheWorldEnemy extends Enemy {

	Vector2 startPosition;
	Vector2 endPosition;
	Vector2 direction;
	float speed;

	public AroundTheWorldEnemy(float x, float y, float x1, float y1, float x2, float y2, float speed) {
		super(x, y, 2, 2);
		this.startPosition = new Vector2(x1, y1);
		this.endPosition = new Vector2(x2, y2);
		direction = endPosition.cpy().add(startPosition.tmp().mul(-1));
		direction.mul(1 / direction.len());
		this.speed = speed;
	}
	
	@Override
	public Enemy update(float delta) {
		float distance = endPosition.dst(position);
		this.increasePosition(direction.cpy().mul(delta * speed));
		
		// if we are further from the end position after update, then we passed it
		if (distance < endPosition.dst(position)) {
			setPosition(startPosition.cpy());
		}
		
		return this;
	}

}
