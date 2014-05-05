package com.octoforce.games.ld29.model;

import com.badlogic.gdx.math.Vector2;
import com.octoforce.games.ld29.model.World.GameObjectType;
import com.octoforce.games.ld29.model.interfaces.ObjectHolder;

public class MovingBlock extends Block implements ObjectHolder {

	Vector2 startingPosition;
	Vector2 endPosition;
	Vector2 direction;
	float speed;
	boolean movingAway;
	Player player;
	
	public MovingBlock(int x, int y, int width, int height, Vector2 start, Vector2 end, float speed) {
		super(x, y, width, height);
		this.startingPosition = start;
		this.endPosition = end;
		this.speed = speed;
		player = null;
		movingAway = true;
		direction = endPosition.cpy().add(startingPosition.tmp().mul(-1));
		direction.mul(1 / direction.len());
	}
	
	public void update(float delta) {
		float totalMovingDistance = speed * delta;
		Vector2 movement;
		if (movingAway) {
			float distance = endPosition.dst(position);
			if (totalMovingDistance >= distance)
				movingAway = false;
			movement = direction.cpy().mul(totalMovingDistance);
		}
		else {
			float distance = startingPosition.dst(position);
			if (totalMovingDistance >= distance)
				movingAway = true;
			movement = direction.tmp().mul(-totalMovingDistance);
		}
		setPosition(position.add(movement));
		if (player != null) {
			player.increasePosition(movement.x, movement.y);
			player = null;
		}
	}
	
	public void setPlayer(Player p) {
		player = p;
	}

	@Override
	public GameObjectType getType() {
		return World.GameObjectType.MOVING_BLOCK;
	}

	@Override
	public void setGameObject(GameObject object) {
		if (object instanceof Player)
			player = (Player) object;
	}

}
