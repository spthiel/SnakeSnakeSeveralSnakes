package me.elspeth.engine.game.map;

import me.elspeth.engine.utils.OpenSimplexNoise;

public class Map {

	private int width;
	private int height;
	private boolean[][] walls;
	
	public Map(int width, int height) {
		generate();
	}
	
	private void generate() {
		this.walls = new boolean[width][height];
		var noise = new OpenSimplexNoise(System.currentTimeMillis());
		
		for (var y = 0; y < this.walls.length; y++) {
			for(var x = 0; x < this.walls[y].length; x++) {
				this.walls[y][x] = noise.eval(x, y) < 0;
			}
		}
	}
	
	public boolean[][] getWalls() {
		
		return walls;
	}
}
