package com.octoforce.games.ld29.model;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.octoforce.games.ld29.WorldRenderer;

public class World {
	
	public enum GameObjectType {
		EMPTY,
		BLOCK,
		MOVING_BLOCK,
		ENEMY,
		BREAKABLE_BLOCK,
		SPIKE,
		STAR
	}
	
	public int width;
	public int height;
	
	// Save all game objects based on what tile they are on.
	// The first 6 bits indicate the type of object (based on the ordinal of enum game object type above),
	// the other 10 bits indicate of the object in the relative array (bitwise operators, funzies right)
	short [][] tiles;
	
	// Arrays containing the game objects, this is were the indices above come in handy
	public Array<Block> blocks;
	public Array<MovingBlock> movingBlocks;
	public Array<Star> stars;
	public Array<Enemy> enemies;
	public Array<BreakableBlock> breakableBlocks;
	
	Player player;
	boolean [] dirt;
	boolean [][] kill;
	Level level;
	float stateTime;
	int starsCollected;
	int starsTotal;
	public boolean firstStar = false;
	static String baseLevelsDir = "data/levels/";
	static String [] levels = { "level1.csv", "level2.csv", "level3.csv", "level4.csv", "level5.csv" };
	static String testLevel = "test_level.csv";
	int currentLevel;
	Vector2 flag;
	boolean finished = false;
	
	public World() {
		this.blocks = new Array<Block>();
		
		this.width = (int) WorldRenderer.CAMERA_WIDTH;
		this.height = (int) WorldRenderer.CAMERA_HEIGHT;

		this.dirt = new boolean [width * height];
		this.kill = new boolean [width][height];
		this.tiles = new short [width][height];
		
		this.stateTime = 0;
		stars = new Array<Star>();
		starsCollected = 0;
		
		currentLevel = 1;
		this.player = new Player(0, 0);
		
		reset();
	}
	
	public boolean hasNextLevel() {
		return currentLevel < levels.length - 1;
	}
	
	public void nextLevel() {
		stars = new Array<Star>();
		currentLevel++;
		reset();
	}
	
	public void reset() {
		
		if (currentLevel == -1)
			level = new Level(Gdx.files.internal(baseLevelsDir + testLevel), this);
		else
			level = new Level(Gdx.files.internal(baseLevelsDir + levels[currentLevel]), this);
		this.movingBlocks = new Array<MovingBlock>();
		
		char [][] data = getLevelData();
		enemies = new Array<Enemy>();
		breakableBlocks = new Array<BreakableBlock>();
		resetBlocks();
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[0].length; j++) {
				if (data[i][j] == 'x')
					dirt[j + (height - i - 1) * width] = true;
				if (data[i][j] == 'v')
					kill[j][height - i - 1] = true;
				if (data[i][j] == 's')
					stars.add(new Star(j, height - i - 1));
				if (data[i][j] == 'e')
					flag = new Vector2(j, height - i - 1);
			}
		}
		switch (currentLevel) {
		case -1:
			initTestLevel();
			break;
		case 0:
			initLevel1();
			break;
		case 1:
			initLevel2();
			break;
		case 2:
			initLevel3();
			break;
		case 3:
			initLevel4();
			break;
		case 4:
			initLevel5();
			break;
		}
	}
	
	private void initTestLevel() {
		player.setPosition(new Vector2(22, 20));
		player.upwards = true;
	}
	
	private void initLevel1() {
		player.setPosition(new Vector2(3, 14));
		player.upwards = true;
//		movingBlocks.add(new MovingBlock(5, 5, 2, 1, new Vector2(5, 5), new Vector2(8, 5), 2f));
		movingBlocks.add(new MovingBlock(18, 5, 4, 1, new Vector2(18 , 5), new Vector2(27, 5), 5f));
		enemies.add(new SimpleEnemy(6, 13, 2, 2, 6, 19, 6, 9, 5));
		enemies.add(new SimpleEnemy(11, 10, 2, 2, 11, 19, 11, 9, 8));
		
	}
	
	private void initLevel2() {
		player.setPosition(new Vector2(0, 12));
		player.upwards = true;
		movingBlocks.add(new MovingBlock(3, 7, 4, 1, new Vector2(3, 7), new Vector2(10, 7), 4f));
		movingBlocks.add(new MovingBlock(20, 25, 4, 1, new Vector2(20, 25), new Vector2(25, 25), 5f));
		movingBlocks.add(new MovingBlock(17, 10, 4, 1, new Vector2(17, 10), new Vector2(21, 10), 3f));
		movingBlocks.add(new MovingBlock(30, 24, 4, 1, new Vector2(30, 24), new Vector2(37, 24), 6f));
		movingBlocks.add(new MovingBlock(26, 11, 4, 1, new Vector2(26, 11), new Vector2(34, 11), 3f));
	}
	
	private void initLevel3() {
		player.setPosition(new Vector2(0, 12));
		player.upwards = true;
	}
	
	private void initLevel4() {
		player.setPosition(new Vector2(3, 18));
		player.upwards = true;
//		enemies.add(new SimpleEnemy(20, 5, 2, 2, -2, 16, -2, 16, 3));
		enemies.add(new AroundTheWorldEnemy(10, 11, // start position
				48, 11, // reset position
				-2, 11, // end position
				5)); // speed
		enemies.add(new AroundTheWorldEnemy(15, 17, // start position
				48, 17, // reset position
				-2, 17, // end position
				5)); // speed
		enemies.add(new AroundTheWorldEnemy(20, 11, // start position
				48, 11, // reset position
				-2, 11, // end position
				5)); // speed
		enemies.add(new AroundTheWorldEnemy(25, 17, // start position
				48, 17, // reset position
				-2, 17, // end position
				5)); // speed
		enemies.add(new AroundTheWorldEnemy(35, 11, // start position
				48, 11, // reset position
				-2, 11, // end position
				5)); // speed
		enemies.add(new AroundTheWorldEnemy(35, 17, // start position
				48, 17, // reset position
				-2, 17, // end position
				5)); // speed
		enemies.add(new AroundTheWorldEnemy(45, 17, // start position
				48, 17, // reset position
				-2, 17, // end position
				5)); // speed
		enemies.add(new AroundTheWorldEnemy(50, 11, // start position
				48, 11, // reset position
				-2, 11, // end position
				5)); // speed
		enemies.add(new AroundTheWorldEnemy(55, 17, // start position
				48, 17, // reset position
				-2, 17, // end position
				5)); // speed
	}
	
	private void initLevel5() {
		player.setPosition(new Vector2(0, 14));
		player.upwards = true;
		breakableBlocks.add(new BreakableBlock(20, 30, 2, 1, 1));
	}
	
	public void update(float delta) {
		stateTime += delta;
		
		checkIfPlayerOnMovingBlock();
		resetMovingBlockTiles();
		for (MovingBlock b : movingBlocks)
			b.update(delta);
		setMovingBlockTiles();
		for (Enemy enemy : enemies) {
			enemy.update(delta);
			if (enemy.bounds.overlaps(player.bounds))
				player.alive = false;
		}
		Star temp;
		for (Iterator<Star> it = stars.iterator(); it.hasNext();) {
			temp = it.next();
			if (temp.bounds.overlaps(player.bounds)) {
				if (starsCollected == 0) {
					firstStar = true;
				}
				starsCollected ++;
				it.remove();
			}
		}
	}
	
	private void resetMovingBlockTiles() {
		int startX, endX, startY, endY;
		MovingBlock block;
		for (int i = 0; i < movingBlocks.size; i++) {
			block = movingBlocks.get(i);
			startX = (int) Math.floor(block.bounds.x);
			endX = (int) Math.ceil(block.bounds.x + block.bounds.width);
			startY = (int) Math.floor(block.bounds.y);
			endY = (int) Math.ceil(block.bounds.y + block.bounds.height);
			for (int x = startX; x < endX; x++)
				for (int y = startY; y < endY; y++) 
					tiles[x][y] = calculateTileValue(GameObjectType.EMPTY, 0);
		}
	}
	
	private void setMovingBlockTiles() {
		int startX, endX, startY, endY;
		MovingBlock block;
		for (int i = 0; i < movingBlocks.size; i++) {
			block = movingBlocks.get(i);
			startX = (int) Math.floor(block.bounds.x);
			endX = (int) Math.ceil(block.bounds.x + block.bounds.width);
			startY = (int) Math.floor(block.bounds.y);
			endY = (int) Math.ceil(block.bounds.y + block.bounds.height);
			for (int x = startX; x < endX; x++)
				for (int y = startY; y < endY; y++) 
					tiles[x][y] = calculateTileValue(block.getType(), i);
		}
	}
	
	public int findOppositeY(Rectangle rect, boolean top) {
		boolean foundEmptyRow = true;
		GameObject temp;
		int y = (int) (top ? Math.floor(rect.y) - 1 : Math.ceil(rect.y + rect.height));
		
		int startX = (int) Math.floor(rect.x);
		int endX = (int) Math.ceil(rect.x + rect.width);
		do {
			foundEmptyRow = true;
			for (int i = startX; i < endX; i++) {
				temp = getObjectFromTile(i, y);
				if (temp instanceof Block) {
					if (temp.getType() == GameObjectType.MOVING_BLOCK) {
						if (rect.x >= temp.bounds.x && rect.x + rect.width <= temp.bounds.x + temp.bounds.width) 
							foundEmptyRow = false;
						// else foundEmptyRow does not change
					}
					else
						foundEmptyRow = false;
				}
			}
			if (!foundEmptyRow)
				y += top ? -1 : 1;
		} while (!foundEmptyRow);
		
		return y + (top ? 1 : 0);
	}
	
	public float distanceToFirstBlockHorizontal(Rectangle bounds, boolean left, boolean upwards) {
		int startY = (int) Math.floor(bounds.y);
		int endY = (int) Math.ceil(bounds.y + bounds.height);
		int x = (int) Math.floor(bounds.x);
		GameObject object = null;
		
		while (true) {
			if (x >= width || x < 0)
				return Integer.MAX_VALUE;
			for (int j = startY; j < endY; j++) {
				object = getObjectFromTile(x, j);
				if (object instanceof Block && object.bounds.overlaps(bounds)) {
					return Math.abs(bounds.x - x) - (left ? object.bounds.width : bounds.width);
				}
			}
			x += left ? -1 : 1;
		}
	}
	
	public float distanceToFirstBlockVertical(Rectangle rect, boolean top) {
		GameObject temp;
		int y = (int) (top ? Math.floor(rect.y) - 1 : Math.ceil(rect.y + rect.height));
		
		int startX = (int) Math.floor(rect.x);
		int endX = (int) Math.ceil(rect.x + rect.width);
		while (true) {
			if (y >= height || y < 0)
				return Integer.MAX_VALUE;
			for (int i = startX; i < endX; i++) {
				temp = getObjectFromTile(i, y);
				if (temp instanceof Block && temp.bounds.overlaps(new Rectangle(rect.x, y, rect.width, rect.height)))
					return Math.abs(rect.y - temp.bounds.y) - (top ? temp.bounds.height : rect.height);
			}
			y += top ? -1 : 1;
		}
	}
	
	public Array<Block> getBlocks() {
		return blocks;
	}
	
	public boolean isMortSubite(Rectangle bounds) {
		int startX = (int) Math.floor(bounds.x);
		int endX = (int) Math.ceil(bounds.x + bounds.width);
		int startY = (int) Math.floor(bounds.y);
		int endY = (int) Math.ceil(bounds.y + bounds.height);
		for (int i = startX; i < endX; i++)
			for (int j = startY; j < endY; j++)
				if (kill[i][j])
					return true;
		return false;
	}
	
	
	public void addBlock(int x, int y, int w, int h) {
		if (x + w >= WorldRenderer.CAMERA_WIDTH || y + h >= WorldRenderer.CAMERA_HEIGHT)
			return;
		this.blocks.add(new Block(x, y, w, h));
		for (int i = 0; i < w; i++)
			for (int j = 0; j < h; j++)
				dirt[x + i + this.width * (y + j)] = true;
	}
	
	private void resetBlocks() {
		for (int i = 0; i < dirt.length; i++)
			dirt[i] = false;
		for (int i = 0; i < kill.length; i++)
			for (int j = 0; j < kill[0].length; j++)
			kill[i][j] = false;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public char [][] getLevelData() {
		return level.getLevelData();
	}
	
	public Array<MovingBlock> getMovingBlocks() {
		return this.movingBlocks;
	}
	
	public float getStateTime() {
		return stateTime;
	}

	public Array<Star> getStars() {
		return stars;
	}
	
	public int getStarsCollected() {
		return starsCollected;
	}
	
	public int getTotalStars() {
		return starsTotal;
	}
	
	public Vector2 getFlagPosition() {
		return flag;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}
	
	public void addObjectToTile(GameObject object, int x, int y) {
		int index = 0;
		switch (object.getType()) {
		case BREAKABLE_BLOCK:
			index = breakableBlocks.size;
			breakableBlocks.add((BreakableBlock) object);
			break;
		case MOVING_BLOCK:
			index = movingBlocks.size;
			movingBlocks.add((MovingBlock) object);
			break;
		case BLOCK:
			index = blocks.size;
			blocks.add((Block) object);
			break;
		case STAR:
			index = stars.size;
			stars.add((Star) object);
			break;
		case ENEMY:
			index = enemies.size;
			enemies.add((Enemy) object);
			break;
		case EMPTY:
			break;
		case SPIKE:
			break;
		default:
			index = -1;
			break;
		}
		
		if (index != -1)
			tiles[x][y] = calculateTileValue(object.getType(), index);
	}
	
	public short calculateTileValue(GameObjectType type, int index) {
		return (short) ((type.ordinal() << 10) + index);
	}
	
	public Array<GameObject> getObjectsInRectangle(Rectangle rect) {
		Array<GameObject> array = new Array<GameObject>();
		int startX = (int) Math.floor(rect.x);
		int endX = (int) Math.ceil(rect.x + rect.width);
		int startY = (int) Math.floor(rect.y);
		int endY = (int) Math.ceil(rect.y + rect.height);
		
		for (int x = startX; x < endX; x++)
			for (int y = startY; y < endY; y++)
				array.add(getObjectFromTile(x, y));
		
		return array;
	}
	
	public void checkIfPlayerOnMovingBlock() {
		Rectangle temp = new Rectangle(player.bounds.x, player.bounds.width, 0, 0);
		if (player.isUpwards())
			temp.y = player.bounds.y;
		else
			temp.y = player.bounds.y + player.bounds.height;
		
		for (MovingBlock block : movingBlocks)
			if (temp.overlaps(block.bounds))
				block.setGameObject(player);
	}
	
	public GameObject getObjectFromTile(int x, int y) {
 		short tile = tiles[x][y];
		int type = tile >> 10;
		int index = tile & 0x03ff; // select last 10 bits
		
		Array<? extends GameObject> array = null;
		
		if (type >= GameObjectType.values().length)
			return null;
		
		switch (GameObjectType.values()[type]) {
		case BREAKABLE_BLOCK:
			array = breakableBlocks;
			break;
		case MOVING_BLOCK:
			array = movingBlocks;
			break;
		case BLOCK:
			array = blocks;
			break;
		case STAR:
			array = stars;
			break;
		case ENEMY:
			array = enemies;
			break;
		case EMPTY:
			return null;
		case SPIKE:
			// TODO fix this to proper spike
			return new Spike(0, 0, Spike.Direction.UP);
		default:
			break;
		}
		try {
		if (array == null || !(array.get(index) instanceof GameObject))
			throw new RuntimeException("Illegal value (" + tile + ") present in tile " + x + "," + y);
		} catch (IndexOutOfBoundsException e) {
			throw new RuntimeException("Out of bounds value (" + tile + ") present in tile " + x + "," + y);
		}
		
		return (GameObject) array.get(index);
	}
	
	

}
