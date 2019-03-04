package model;

import java.util.ArrayList;
import java.util.List;

import model.wall.BrickWall;
import model.wall.Wall;
import util.MapIO;

/**
 * Map class, singleton mode
 */
public class Map {
	public static List<Wall> walls = new ArrayList<>();

	/**
	 * Private construction method
	 */
	private Map() {

	}

	/**
	 * Get map object
	 * 
	 * @param level
	 *       
	 * @return ָSpecify the map object of the level
	 */
	public static Map getMap(String level) {
		walls.clear();// Wall block collection empty
		walls.addAll(MapIO.readMap(level));// Read the wall block collection of the specified level
		// Base brick wall
		for (int a = 340; a <= 400; a += 20) {
			for (int b = 500; b <= 560; b += 20) {
				if (a >= 360 && a <= 380 && b >= 520) {//If the wall block coincides with the base
					continue;
				} else {
					walls.add(new BrickWall(a, b));
				}
			}
		}
		return new Map();
	}

	/**
	 * Get map object
	 * 
	 * @param level
	 *            
	 */
	public static Map getMap(int level) {
		return getMap(String.valueOf(level));
	}

	/**
	 * Get all the wall tiles in the map object
	 * 
	 * @return Wall block list
	 */
	public static List<Wall> getWalls() {
		return walls;
	}

}
