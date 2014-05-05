package com.octoforce.games.ld29.model;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.octoforce.games.ld29.model.World.GameObjectType;

public class Player extends GameObject {

	public static final int HEIGHT = 2;
	public static final int WIDTH = 1;
	
	public enum State {
		LEFT, RIGHT, IDLE, FALLING
	}
	
	Vector2 velocity;
	Vector2 acceleration;
	boolean upwards;
	boolean facingLeft;
	boolean alive;
	
	State state;
	
	public Player(int x, int y) {
		super(x, y, WIDTH, HEIGHT);
		velocity = new Vector2(0,0);
		acceleration = new Vector2(0,0);
		upwards = true;
		state = State.IDLE;
		facingLeft = true;
		alive = true;
	}

	public Vector2 getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2 velocity) {
		this.velocity = velocity;
	}

	public Vector2 getAcceleration() {
		return acceleration;
	}

	public void setAcceleration(Vector2 acceleration) {
		this.acceleration = acceleration;
	}
	
	public void switchWorld() {
		this.upwards = !upwards;
	}
	
	public boolean isUpwards() {
		return upwards;
	}
	
	public void setState(State state) {
		this.state = state;
	}
	
	public State getState() {
		return state;
	}
	
	public void setFacingLeft(boolean facingLeft) {
		this.facingLeft = facingLeft;
	}
	
	public boolean isFacingLeft() {
		return facingLeft;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	@Override
	public GameObjectType getType() {
		return null;
	}

	public void increasePosition(float g, float f) {
		this.increasePosition(new Vector2(g, f));
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public Vector2 getPosition() {
		return position;
	}
	
	

}
