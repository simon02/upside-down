package com.octoforce.games.ld29.model;

import com.badlogic.gdx.math.Vector2;

public class SimpleEnemy extends Enemy {

	Vector2 startPosition;
	Vector2 endPosition;
	Vector2 direction;
	boolean movingToEndPosition;
	float speed;

	public SimpleEnemy(float x, float y, float width, float height, float x1, float y1, float x2, float y2, float speed) {
		super(x, y, width, height);
		this.startPosition = new Vector2(x1, y1);
		this.endPosition = new Vector2(x2, y2);
		this.movingToEndPosition = true;
		direction = endPosition.cpy().add(startPosition.tmp().mul(-1));
		direction.mul(1 / direction.len());
		this.speed = speed;
	}
	
	@Override
	public Enemy update(float delta) {
		float maxDistance = delta * speed, distance;
		if (movingToEndPosition)
			distance = Math.min(endPosition.dst(position), maxDistance);
		else
			distance = Math.min(startPosition.dst(position), maxDistance);
		
		this.increasePosition(direction.cpy().mul(movingToEndPosition ? distance : -distance));
		
		if (distance < maxDistance)
			movingToEndPosition = !movingToEndPosition;
		
		return this;
		
//		float totalMovingDistance = speed * delta;
//		Vector2 movement;
//		if (movingAway) {
//			float distance = endPosition.dst(position);
//			if (totalMovingDistance >= distance)
//				movingAway = false;
//			movement = direction.cpy().mul(totalMovingDistance);
//		}
//		else {
//			float distance = startingPosition.dst(position);
//			if (totalMovingDistance >= distance)
//				movingAway = true;
//			movement = direction.tmp().mul(-totalMovingDistance);
//		}
	}

}
