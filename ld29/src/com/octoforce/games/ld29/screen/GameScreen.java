package com.octoforce.games.ld29.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.octoforce.games.ld29.WorldRenderer;
import com.octoforce.games.ld29.controller.PlayerController;
import com.octoforce.games.ld29.model.World;

public class GameScreen implements Screen, InputProcessor {
	
	private World world;
	private PlayerController playerController;
	private WorldRenderer worldRenderer;

	@Override
	public void render(float delta) {
		if (world.getPlayer().isAlive() && !world.firstStar && !world.isFinished()) {
			world.update(delta);
			playerController.update(delta);
		}
		worldRenderer.render();
	}

	@Override
	public void resize(int width, int height) {
		worldRenderer.setSize(width, height);
//		this.width = width;
//		this.height = height;
	}

	@Override
	public void show() {
		this.world = new World();
		this.playerController = new PlayerController(world.getPlayer(), world);
		this.worldRenderer = new WorldRenderer(world);
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean keyDown(int keycode) {
		if (world.firstStar) {
			world.firstStar = false;
			return false;
		}
		else if (!world.getPlayer().isAlive()) {
			world.reset();
			world.getPlayer().setAlive(true);
			return false;
		}
		else if (world.isFinished()) {
			show();
			return false;
		}
		switch (keycode) {
		case Keys.LEFT:
			playerController.leftPressed();
			break;
		case Keys.RIGHT:
			playerController.rightPressed();
			break;
		case Keys.UP:
			playerController.switchPressed();
			break;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		switch (keycode) {
		case Keys.LEFT:
			playerController.leftReleased();
			break;
		case Keys.RIGHT:
			playerController.rightReleased();
			break;
		case Keys.UP:
			playerController.switchReleased();
			break;
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchMoved(int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
