import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Area {
		private Area north;
		private Area south;
		private Area east;
		private Area west;
		
		private String streetEastWest;
		private String streetNorthSouth;
		
		private String message;
		
		private String lastStreetUsed;
		
	/*
	 * Specifies a new area as well as the streets connecting it.
	 */
	public Area(String message, String streetEastWest, String streetNorthSouth) {
		this.north = null;
		this.south = null;
		this.east = null;
		this.west = null;
		
		this.streetEastWest = streetEastWest;
		this.streetNorthSouth = streetNorthSouth;
		
		this.message = message;
	}
	
	/*
	 * Gets all possible directions (N,S,E,W) for current area.
	 */
	public List<String> getDirections() {
		List<String> directions = new ArrayList<String>();
		
		if(this.getNorth() != null) {
			directions.add("north");
		}
		
		if (this.getSouth() != null) {
			directions.add("south");
		} 
		
		if (this.getEast() != null) {
			directions.add("east");
		}
		
		if (this.getWest() != null) {
			directions.add("west");
		}
		
		return directions;
	}
	
	/*
	 * Randomly gets next area, return null if no areas are connected (ie. driver is outside the city)
	 */
	public Area getNextArea(Random random) {
		List<String> directions = getDirections();
		
		int index = -1;
		String direction = "";
		
		if(directions.size() == 0) {
			return null;
		} else {
			index = random.nextInt(directions.size());
			direction = directions.get(index);
		}
		
		Area nextArea = null;
		
		if (direction == "north") {
			nextArea = this.north;
			nextArea.lastStreetUsed = this.streetNorthSouth;
		} else if (direction == "south") {
			nextArea = this.south;
			nextArea.lastStreetUsed = this.streetNorthSouth;
		} else if (direction == "east") {
			nextArea = this.east;
			nextArea.lastStreetUsed = this.streetEastWest;
		} else if (direction == "west") {
			nextArea = this.west;
			nextArea.lastStreetUsed = this.streetEastWest;
		}
		
		return nextArea;
	}

	/*
	 * Get area to the north of current area.
	 */
	public Area getNorth() {
		return north;
	}
	
	/*
	 * Set area to the north of current area.
	 */
	public void setNorth(Area north) {
		this.north = north;
	}

	/*
	 * Get area to the south of current area.
	 */
	public Area getSouth() {
		return south;
	}

	/*
	 * Set area to the south of current area.
	 */
	public void setSouth(Area south) {
		this.south = south;
	}
	
	/*
	 * Get area to the east of current area.
	 */
	public Area getEast() {
		return east;
	}
	
	/*
	 * Set area to the east of current area.
	 */
	public void setEast(Area east) {
		this.east = east;
	}
	
	/*
	 * Get area to the west of current area.
	 */
	public Area getWest() {
		return west;
	}
	
	/*
	 * Set area to the west of current area.
	 */
	public void setWest(Area west) {
		this.west = west;
	}
	
	/*
	 * Get the street that traverses the area in the East / West direction.
	 */
	public String getStreetEastWest() {
		return streetEastWest;
	}
	
	/*
	 * Set the street that traverses the area in the East / West direction.
	 */
	public void setStreetEastWest(String streetEastWest) {
		this.streetEastWest = streetEastWest;
	}
	
	/*
	 * Get the street that traverses the area in the North / South direction.
	 */
	public String getStreetNorthSouth() {
		return streetNorthSouth;
	}
	
	/*
	 * Set the street that traverses the area in the North / South direction.
	 */
	public void setStreetNorthSouth(String streetNorthSouth) {
		this.streetNorthSouth = streetNorthSouth;
	}
	
	/*
	 * Get the message associated with the area (ie. its name).
	 */
	public String getMessage() {
		return message;
	}
	
	/*
	 * Set the message associated with the area (ie. its name).
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	/*
	 * Get the last street that was used to move to the area.
	 */
	public String getLastStreetUsed() {
		return lastStreetUsed;
	}

	/*
	 * Set the last street that was used to move to the area.
	 */
	public void setLastStreetUsed(String lastStreetUsed) {
		this.lastStreetUsed = lastStreetUsed;
	}
	
}