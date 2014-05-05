package com.octoforce.games.ld29.controller;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.octoforce.games.ld29.WorldRenderer;
import com.octoforce.games.ld29.model.GameObject;
import com.octoforce.games.ld29.model.Player;
import com.octoforce.games.ld29.model.Player.State;
import com.octoforce.games.ld29.model.World;
import com.octoforce.games.ld29.model.interfaces.Lethal;
import com.octoforce.games.ld29.model.interfaces.ObjectHolder;

public class PlayerController {
	
	Player player;
	World world;
	boolean falling;
	boolean alive;

	enum Key {
		LEFT, RIGHT, SWITCH
	}

	private static final float ACCELERATION 	= 15f;
	private static final float DAMP 			= 0.6f;
	private static final float MIN_VEL 			= 5;
	private static final float MAX_VEL 			= 15;
	
	static final float MOVING_SPEED = 12f;
	static final float FALLING_SPEED = 35;
	static Map<Key, Boolean> keys = new HashMap<Key, Boolean>();
	static {
		keys.put(Key.LEFT, false);
		keys.put(Key.RIGHT, false);
		keys.put(Key.SWITCH, false);
	};
	
	public PlayerController(Player player, World world) {
		this.player = player;
		this.world = world;
		falling = false;	
		alive = true;
	}
	
	public void update(float delta) {
		processInput();
		calculateVelocity(delta);
//		player.getVelocity().mul(delta);
		
		float movingSpeed = player.getVelocity().x;
		float distance = world.distanceToFirstBlockVertical(player.getBounds(), player.isUpwards());
		if (distance > 0) {
			falling = true;
			distance = Math.min(distance, FALLING_SPEED * delta);
			player.increasePosition(0, player.isUpwards() ? -distance : distance);
		}
		else
			falling = false;
		if (player.getVelocity().x < 0) {
			distance = world.distanceToFirstBlockHorizontal(player.getBounds(), true, player.isUpwards());
			player.increasePosition(- Math.min(distance, -movingSpeed * delta), 0);
		}
		else if (player.getVelocity().x > 0) {
			distance = world.distanceToFirstBlockHorizontal(player.getBounds(), false, player.isUpwards());
			System.out.println("" + distance);
			player.increasePosition(Math.min(distance, movingSpeed * delta), 0);
		}
		
		Vector2 flag = world.getFlagPosition();
		if (player.getBounds().overlaps(new Rectangle(flag.x, flag.y, 1, 2))) {
			if (world.hasNextLevel())
				world.nextLevel();
			else
				world.setFinished(true);
		}
		
		if (player.getPosition().x < 0 || player.getPosition().x + player.getBounds().width > WorldRenderer.CAMERA_WIDTH || player.getPosition().y < 0 || player.getPosition().y + player.getBounds().height > WorldRenderer.CAMERA_HEIGHT) {
			player.setPosition(new Vector2(WorldRenderer.CAMERA_WIDTH / 2, WorldRenderer.CAMERA_HEIGHT / 2));
			player.setAlive(false);
		}

//		player.getVelocity().mul(1/delta);

		GameObject object = world.getObjectFromTile((int)Math.floor(player.getPosition().x), (int)Math.floor(player.getPosition().y));
		if (object instanceof Lethal) {
			player.setAlive(false);
		}
//		else if (object instanceof ObjectHolder) {
//			((ObjectHolder) object).setGameObject(player);
//		}
	}
	
	private void processInput() {
		if (isKeyPressed(Key.LEFT) && !isKeyPressed(Key.RIGHT)) {
			player.setState(State.LEFT);
			player.setFacingLeft(true);
			player.getAcceleration().x = -ACCELERATION;
			if (player.getVelocity().x > -MIN_VEL)
				player.getVelocity().x = -MIN_VEL;
		}
		else if (!isKeyPressed(Key.LEFT) && isKeyPressed(Key.RIGHT)) {
			player.setState(State.RIGHT);
			player.setFacingLeft(false);
			player.getAcceleration().x = ACCELERATION;
			if (player.getVelocity().x < MIN_VEL)
				player.getVelocity().x = MIN_VEL;
		}
		else {
			player.setState(State.IDLE);
			player.getAcceleration().x = 0;
		}
		if (isKeyPressed(Key.SWITCH) && !falling) {
			player.setPosition(new Vector2(player.getPosition().x, world.findOppositeY(player.getBounds(), player.isUpwards())));
			switchReleased();
			player.switchWorld();
			if (!player.isUpwards()) {
				player.increasePosition(0, -player.getBounds().getHeight());
			}
		}
	}
	
	private void calculateVelocity(float delta) {

		// Convert acceleration to frame time
		player.getAcceleration().mul(delta);

		// apply acceleration to change velocity
		player.getVelocity().add(player.getAcceleration().x, player.getAcceleration().y);

		// apply damping to halt gummy nicely
		if (player.getAcceleration().x == 0)
			player.getVelocity().x *= DAMP;

		// ensure terminal velocity is not exceeded
		if (player.getVelocity().x > MAX_VEL)
			player.getVelocity().x = MAX_VEL;
		else if (player.getVelocity().x < -MAX_VEL)
			player.getVelocity().x = -MAX_VEL;
		
		if (Math.abs(player.getVelocity().x) < 0.1)
			player.getVelocity().x = 0;
	}
	
	private boolean isKeyPressed(Key key) {
		return keys.get(key);
	}
	
	// ** Key presses and touches **************** //
	
	public void leftPressed() {
		keys.get(keys.put(Key.LEFT, true));
	}
	
	public void rightPressed() {
		keys.get(keys.put(Key.RIGHT, true));
	}
	
	public void switchPressed() {
		keys.get(keys.put(Key.SWITCH, true));
	}
	
	public void leftReleased() {
		keys.get(keys.put(Key.LEFT, false));
	}
	
	public void rightReleased() {
		keys.get(keys.put(Key.RIGHT, false));
	}
	
	public void switchReleased() {
		keys.get(keys.put(Key.SWITCH, false));
	}
	
	public boolean isAlive() {
		return alive;
	}

}
