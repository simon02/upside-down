package com.octoforce.games.ld29.model;

import java.io.BufferedInputStream;
import java.io.IOException;

import com.badlogic.gdx.files.FileHandle;
import com.octoforce.games.ld29.WorldRenderer;
import com.octoforce.games.ld29.model.Spike.Direction;
import com.octoforce.games.ld29.model.World.GameObjectType;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;

public class Level {
	
	public static GameObject EMPTY_OBJECT = new GameObject() {
		@Override
		public GameObjectType getType() {
			return World.GameObjectType.EMPTY;
		}
	};
	
	char [][] data;
	int width;
	int height;
	World world;
	
	public Level(FileHandle file, World world) {
		this.width = (int) WorldRenderer.CAMERA_WIDTH;
		this.height = (int) WorldRenderer.CAMERA_HEIGHT;
		
		this.world = world;
		
		this.data = new char [height][width];
		
		String levelData = file.readString();
		
		String rows [] = levelData.split("\n");
		String temp [];
		int y = 0;
		for (int i = 0; i < rows.length; i++) {
			temp = rows[i].split(",");
			for (int j = 0; j < temp.length; j++) {
				y = height - i - 1;
				try {
					if (temp[j].isEmpty())
						world.addObjectToTile(EMPTY_OBJECT, j, y);
					else
						world.addObjectToTile(getObjectForChar(temp[j].charAt(0), j, y), j, y);
				} catch (ArrayIndexOutOfBoundsException e) {
					System.out.println(String.format("No room for %s at %d,%d", temp[j], j, y));
				}
				data[i][j] = temp[j].isEmpty() ? 'n' : temp[j].charAt(0);
			}
		}
	}
	
	public GameObject getObjectForChar(char c, int x, int y) {
		switch(c) {
		case 'x':
			return new Block(x, y, 1, 1);
//		case 'd':
//			return GameObjectType.BREAKABLE_BLOCK;
		case 'v':
			return new Spike(x, y, Direction.DOWN);
		case '^':
			return new Spike(x, y, Direction.UP);
		case '>':
			return new Spike(x, y, Direction.RIGHT);
		case '<':
			return new Spike(x, y, Direction.LEFT);
		case 'e':
			return new Star(x, y);
		default:
			return EMPTY_OBJECT;
		}
	}
	
	public char [][] getLevelData() {
		return data;
	}

}
