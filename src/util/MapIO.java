package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

import enumtype.WallType;
import model.wall.BrickWall;
import model.wall.GrassWall;
import model.wall.IronWall;
import model.wall.RiverWall;
import model.wall.Wall;

/**
 * Map data IO class
 *
 */

public class MapIO {
	// Map data file path
	public final static String DATA_PATH = "map/data/";
	// Map preview path
	public final static String IMAGE_PATH = "map/image/";
	// Map data file suffix
	public final static String DATA_SUFFIX = ".map";
	// Map preview suffix
	public final static String IMAGE_SUFFIX = ".png";

	/**
	 * Get all wall block collections for the specified name map
	 *
	 * @param mapName
	 * @return
	 */
	public static List<Wall> readMap(String mapName) {
		// Create a map file with the corresponding name
		File file = new File(DATA_PATH + mapName + DATA_SUFFIX);
		return readMap(file);// Call overloaded method
	}

	/**
	 *
	 * Get all the wall block collections of the map file
	 *
	 * @param file
	 *           Map file
	 * @return All wall block collections in this map
	 */
	public static List<Wall> readMap(File file) {
		Properties pro = new Properties();
		List<Wall> walls = new ArrayList<>();
		try {
			pro.load(new FileInputStream(file));// Property set object reads the map file
			String brickStr = (String) pro.get(WallType.BRICK.name());// Read string data of the brick wall name attribute in the map file
			String grassStr = (String) pro.get(WallType.GRASS.name());
			String riverStr = (String) pro.get(WallType.RIVER.name());
			String ironStr = (String) pro.get(WallType.IRON.name());
			if (brickStr != null) {// If the brick wall data read is not null
				walls.addAll(readWall(brickStr, WallType.BRICK));// Parse the data and add the parsed wall block set in the data to the total wall block collection
			}
			if (grassStr != null) {
				walls.addAll(readWall(grassStr, WallType.GRASS));
			}
			if (riverStr != null) {
				walls.addAll(readWall(riverStr, WallType.RIVER));
			}
			if (ironStr != null) {
				walls.addAll(readWall(ironStr, WallType.IRON));
			}
			return walls;// Return to the total wall block collection
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Parse wall data
	 * 
	 * @param data
	 *    
	 * @param type
	 * 
	 */
	private static List<Wall> readWall(String data, WallType type) {
		String walls[] = data.split(";");// Use ";" to split a string
		Wall wall;
		List<Wall> w = new ArrayList<>();
		switch (type) {// Judge wall type
		case BRICK:
			for (String wStr : walls) {
				String axes[] = wStr.split(",");
				// Create a wall block object
				wall = new BrickWall(Integer.parseInt(axes[0]), Integer.parseInt(axes[1]));// Create a brick wall object on this coordinate
				w.add(wall);
			}
			break;
		case RIVER:
			for (String wStr : walls) {
				String axes[] = wStr.split(",");
				wall = new RiverWall(Integer.parseInt(axes[0]), Integer.parseInt(axes[1]));
				w.add(wall);
			}
			break;
		case GRASS:
			for (String wStr : walls) {
				String axes[] = wStr.split(",");
				wall = new GrassWall(Integer.parseInt(axes[0]), Integer.parseInt(axes[1]));
				w.add(wall);
			}
			break;
		case IRON:
			for (String wStr : walls) {
				String axes[] = wStr.split(",");
				wall = new IronWall(Integer.parseInt(axes[0]), Integer.parseInt(axes[1]));
				w.add(wall);
			}
			break;
		default:
			break;
		}
		return w;
	}

	/**
	 * Remove the repeated elements in the list collection through the hashset
	 * 
	 * @param list
	 *           
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void removeDuplicate(List list) {
		HashSet h = new HashSet(list);
		list.clear();
		list.addAll(h);
	}
}
