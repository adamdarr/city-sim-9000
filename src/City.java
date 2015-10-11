import java.util.Random;

public class City {
	
	private Area outsideCityFourthEntrance;
	private Area outsideCityFourthExit;
	private Area outsideCityFifthEntrance;
	private Area outsideCityFifthExit;
	
	private Area mall;
	private Area bookstore;
	private Area coffee;

	private Area university;
	
	private Area[] spawnLocations;
	
	private Random random;
	
	/* 
	 * Sets up city map as specified below and sets spawn locations. Also seeds PRNG.
	 * 
	 * City Map
     *  ---> [Mall] ----> [Bookstore] ----> Fourth Ave.
     *         A  |         A   |
     * Meow St.|  |         |   | Chirp St.
     *         |  V         |   V
     *  <--- [Coffee] <-- [University] <--- Fifth Ave.
	 */
	public City(int seed) {
		random = new Random(seed);
		
		outsideCityFourthEntrance = new Area("Outside City", "Fourth Ave.", null);
		outsideCityFourthExit = new Area("Outside City", "Fourth Ave.", null);
		outsideCityFifthEntrance = new Area("Outside City", "Fifth Ave.", null);
		outsideCityFifthExit = new Area("Outside City", "Fifth Ave.", null);
		
		mall = new Area("Mall", "Fourth Ave.", "Meow St.");
		bookstore = new Area("Bookstore", "Fourth Ave.", "Chirp St.");
		coffee = new Area("Coffee Shop", "Fifth Ave.", "Meow St.");
		university = new Area("University", "Fifth Ave.", "Chirp St.");
		
		outsideCityFourthEntrance.setEast(mall);
		
		mall.setEast(bookstore);
		mall.setSouth(coffee);
		
		bookstore.setEast(outsideCityFourthExit);
		bookstore.setSouth(university);
		
		outsideCityFifthEntrance.setWest(university);
		
		university.setWest(coffee);
		university.setNorth(bookstore);
		
		coffee.setWest(outsideCityFifthExit);
		coffee.setNorth(mall);
		
		spawnLocations = new Area[] {outsideCityFourthEntrance, outsideCityFifthEntrance, mall, bookstore, coffee, university};
	}
	
	/*
	 * Returns random spawn location from list of spawn locations.
	 */
	public Area getRandomSpawnLocation() {
		int index = random.nextInt(spawnLocations.length);
		return spawnLocations[index];
	}
	
	/*
	 * Traverses the city from a specified area until the driver leaves the city.
	 */
	public void traverseCity(String driver, Area area) {
		Area nextArea = area.getNextArea(random);
		
		while(nextArea != null) {
			System.out.println(driver + " heading from " + area.getMessage() + " to " + nextArea.getMessage() + " via " + nextArea.getLastStreetUsed());
			
			area = nextArea;
			nextArea = area.getNextArea(random);
		}
		
		System.out.println(driver + " has left the city!");
	}
	
	/*
	 * Get a list of spawn locations (set in the city constructor)
	 */
	public Area[] getSpawnLocations() {
		return spawnLocations;
	}
	
	/*
	 * Get the PRNG (set in city constructor)
	 */
	public Random getRandom() {
		return random;
	}
}