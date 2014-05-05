package com.octoforce.games.ld29;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.octoforce.games.ld29.model.MovingBlock;
import com.octoforce.games.ld29.model.Player.State;
import com.octoforce.games.ld29.model.Enemy;
import com.octoforce.games.ld29.model.Star;
import com.octoforce.games.ld29.model.World;

public class WorldRenderer {
	
	public static final float CAMERA_WIDTH = 45;
	public static final float CAMERA_HEIGHT = 30;
	
	World world;
	OrthographicCamera cam;
	BitmapFont font;
	
	/** for debug rendering **/
	ShapeRenderer debugRenderer = new ShapeRenderer();
	SpriteBatch batch;
	
	private int width;
	private int height;
	private float ppuX;	// pixels per unit on the X axis
	private float ppuY;	// pixels per unit on the Y axis
	
	public WorldRenderer(World world) {
		this.world = world;
		this.cam = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT);
		this.cam.position.set(CAMERA_WIDTH / 2, CAMERA_HEIGHT / 2, 0);
		this.cam.update();
		batch = new SpriteBatch();
		font = new BitmapFont();
		font.scale(0.5f);
		
		createAnimations();
	}
	
	public void render() {
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		debugRenderer.setProjectionMatrix(cam.combined);
		
		char [][] data = world.getLevelData();
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[0].length; j++) {
				if (data[i][j] == 'x')
					drawBlock(j, (int)CAMERA_HEIGHT - i - 1);
				if (data[i][j] == 'v')
					drawDownSpike(j, (int)CAMERA_HEIGHT - i - 1);
				if (data[i][j] == '^')
					drawUpSpike(j, (int)CAMERA_HEIGHT - i - 1);
			}
		}

		debugRenderer.begin(ShapeType.FilledRectangle);
		debugRenderer.setColor(Color.RED);
		for (MovingBlock b : world.getMovingBlocks()) {
			debugRenderer.filledRect(b.getBounds().x, b.getBounds().y, b.getBounds().width, b.getBounds().height);
		}
		debugRenderer.end();

		batch.begin();
		renderPlayer();
		renderStars();
		renderEnemies();
		drawUIStuff();
		batch.draw(flagPole, world.getFlagPosition().x * ppuX, world.getFlagPosition().y * ppuY + 1);

		batch.end();

		if (world.firstStar || !world.getPlayer().isAlive() || world.isFinished()) {
			debugRenderer.end();
			debugRenderer.begin(ShapeType.FilledRectangle);
			debugRenderer.setColor(Color.WHITE);
			debugRenderer.filledRect(4.5f, 4.5f, CAMERA_WIDTH - 9, CAMERA_HEIGHT - 9);
			debugRenderer.setColor(0, 0, 0, 0.5f);
			debugRenderer.filledRect(5, 5, CAMERA_WIDTH - 10, CAMERA_HEIGHT - 10);
			debugRenderer.end();
			batch.begin();
			if (world.firstStar) {
				font.draw(batch, "Congratulations!", width / 2 - 100, height / 2 + 70);
				font.draw(batch, "You collected your first star.", width / 2 - 150, height / 2 + 25);
				font.draw(batch, "These don't do anything, except", width / 2 - 175, height / 2 - 5);
				font.draw(batch, "keep you busy. There are more...", width / 2 - 178, height / 2 - 35);
				font.draw(batch, "PRESS ANY KEY TO CONTINUE", width / 2 - 180, height / 2 - 100);
			}
			else if (!world.getPlayer().isAlive()) {
				font.draw(batch, "You DIED", width / 2 - 75, height / 2 + 70);
				font.draw(batch, "You could not wrap your head around this world", width / 2 - 210, height / 2 + 15);
				font.draw(batch, "and your brain eventually just gave up on it all.", width / 2 - 200, height / 2 - 15);
				font.draw(batch, "PRESS ANY KEY TO RESTART", width / 2 - 150, height / 2 - 100);
			}
			else if (world.isFinished()) {
				font.draw(batch, "You WON!", width / 2 - 75, height / 2 + 70);
				String time = "TIME" + (int)(Math.floor(world.getStateTime() / 60)) + ":" + (int)(Math.floor(world.getStateTime()) % 60) + "." + (int)((world.getStateTime() - Math.floor(world.getStateTime())) * 100);
				font.draw(batch, time, width / 2 - 70, height / 2 + 15);
				String stars = "STARS COLLECTED: " + world.getStarsCollected() / 2;
				font.draw(batch, stars, width / 2 - 120, height / 2 - 30);
				font.draw(batch, "PRESS ANY KEY TO RESTART", width / 2 - 150, height / 2 - 100);
			}
			batch.end();
		}

//		debugRenderer.begin(ShapeType.Rectangle);
//		debugRenderer.setColor(Color.GREEN);
//		Rectangle rect = world.getPlayer().getBounds();
//		debugRenderer.rect(rect.x, rect.y, rect.width, rect.height);
//		debugRenderer.end();
	}
	
	private void drawUIStuff() {
		String time = (int)(Math.floor(world.getStateTime())) + "." + (int)((world.getStateTime() - Math.floor(world.getStateTime())) * 100) + "s";
//		font.drawWrapped(batch, time, width - 100, height - 15, 85, BitmapFont.HAlignment.RIGHT);
	}
	
	private void renderPlayer() {
		Animation anim = null;
		boolean loop = true;
		Rectangle rect = world.getPlayer().getBounds();
		if (world.getPlayer().getState() == State.IDLE) {
			boolean upsideDown = !world.getPlayer().isUpwards();
			boolean facingLeft = world.getPlayer().isFacingLeft();
			batch.draw(upsideDown ?
					(facingLeft ? playerLeftIdleSwitched : playerRightIdleSwitched) :
					(facingLeft ? playerLeftIdle : playerRightIdle), rect.x * ppuX, rect.y * ppuY);
			return;
		}
		else if (world.getPlayer().getState() == State.LEFT) {
			anim = world.getPlayer().isUpwards() ? playerLeft : playerLeftSwitched;
		}
		else if (world.getPlayer().getState() == State.RIGHT) {
			anim = world.getPlayer().isUpwards() ? playerRight : playerRightSwitched;
		}
		else
			return;
		batch.draw(anim.getKeyFrame(world.getStateTime(), loop), rect.x * ppuX, rect.y * ppuY);
	}
	
	private void renderStars() {
		for (Star star : world.getStars()) {
			batch.draw(starAnimation.getKeyFrame(world.getStateTime(), true), star.position.x * ppuX, star.position.y * ppuY);
		}
	}
	
	private void renderEnemies() {
		for (Enemy enemy : world.enemies) {
			batch.draw(simpleEnemy, enemy.position.x * ppuX, enemy.position.y * ppuY);
		}
	}
	
	private void drawBlock(int x, int y) {
		debugRenderer.begin(ShapeType.Rectangle);
		debugRenderer.setColor(Color.RED);
		debugRenderer.rect(x, y, 1, 1);
		debugRenderer.end();
	}
	
	private void drawDownSpike(int x, int y) {
		debugRenderer.begin(ShapeType.FilledTriangle);
		debugRenderer.setColor(Color.RED);
		debugRenderer.filledTriangle(x, y + 1, x + 1, y + 1, x + 0.5f, y + 0.2f);
		debugRenderer.end();
	}
	
	private void drawUpSpike(int x, int y) {
		debugRenderer.begin(ShapeType.FilledTriangle);
		debugRenderer.setColor(Color.RED);
		debugRenderer.filledTriangle(x, y, x + 1, y, x + 0.5f, y + 0.8f);
		debugRenderer.end();
	}

	public void setSize (int w, int h) {
		this.width = w;
		this.height = h;
		ppuX = (float)width / CAMERA_WIDTH;
		ppuY = (float)height / CAMERA_HEIGHT;
	}

	Animation playerLeft;
	Animation playerRight;
	Animation playerLeftSwitched;
	Animation playerRightSwitched;
	TextureRegion playerLeftIdle;
	TextureRegion playerRightIdle;
	TextureRegion playerLeftIdleSwitched;
	TextureRegion playerRightIdleSwitched;
	Animation starAnimation;
	TextureRegion flagPole;
	Animation flag;
	TextureRegion simpleEnemy;
	
	private void createAnimations() {
		Texture playerTexture = new Texture(Gdx.files.internal("data/tileset.png"));
		TextureRegion[] normal = new TextureRegion(playerTexture).split(20, 40)[0];
		TextureRegion[] normalMirror = new TextureRegion(playerTexture).split(20, 40)[0];
		TextureRegion[] upsideDown = new TextureRegion(playerTexture).split(20, 40)[0];
		TextureRegion[] upsideDownMirror = new TextureRegion(playerTexture).split(20, 40)[0];
		for (TextureRegion region : normalMirror)
			region.flip(true, false);
		for (TextureRegion region : upsideDown)
			region.flip(false, true);
		for (TextureRegion region : upsideDownMirror)
			region.flip(true, true);
		playerRight = new Animation(0.15f, normal[0], normal[1], normal[0], normal[2]);
		playerLeft = new Animation(0.15f, normalMirror[0], normalMirror[1], normalMirror[0], normalMirror[2]);
		playerRightSwitched = new Animation(0.15f, upsideDown[0], upsideDown[1], upsideDown[0], upsideDown[2]);
		playerLeftSwitched = new Animation(0.15f, upsideDownMirror[0], upsideDownMirror[1], upsideDownMirror[0], upsideDownMirror[2]);
		playerLeftIdle = normalMirror[0];
		playerRightIdle = normal[0];
		playerLeftIdleSwitched = upsideDownMirror[0];
		playerRightIdleSwitched = upsideDown[0];
		simpleEnemy = new TextureRegion(playerTexture).split(40, 40)[2][0];
		TextureRegion[] star = new TextureRegion(playerTexture).split(20, 20)[2];
		starAnimation = new Animation(0.1f, star[0], star[1]);
		flagPole = normal[7];
		flag = new Animation(0.1f, normal[4], normal[5], normal[6]);
	}

}
