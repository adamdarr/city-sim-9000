import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class CitySim9000Test {
	
	/*
	 * Tests to make sure the map is specified as it is in the requirements.
	 * Compares all expected locations with actual locations in the map.
	 */
	@Test
	public void testCityConstructorMap() {
		City city = new City(1);
		Area locations[] = city.getSpawnLocations();
		
		Area outsideCityFourthEntrance = new Area("Outside City", "Fourth Ave.", null);
		Area outsideCityFourthExit = new Area("Outside City", "Fourth Ave.", null);
		Area outsideCityFifthEntrance = new Area("Outside City", "Fifth Ave.", null);
		Area outsideCityFifthExit = new Area("Outside City", "Fifth Ave.", null);
		
		Area mall = new Area("Mall", "Fourth Ave.", "Meow St.");
		Area bookstore = new Area("Bookstore", "Fourth Ave.", "Chirp St.");
		Area coffee = new Area("Coffee Shop", "Fifth Ave.", "Meow St.");
		Area university = new Area("University", "Fifth Ave.", "Chirp St.");
		
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
		
		Area expectedLocations[] = new Area[] {outsideCityFourthEntrance, outsideCityFifthEntrance, mall, bookstore, coffee, university};
		
		boolean equalMaps = false;
		for(int i = 0; i < locations.length; i++) {
			equalMaps = compareAreas(locations[i], expectedLocations[i]);
			
			if(!equalMaps) {
				break;
			}
		}
		
		assertTrue(equalMaps);
	}
	
	/*
	 * Test to ensure that the seed for the City constructor
	 * creates a PRNG object with the same seed that returns
	 * the same nextInt value.
	 */
	@Test
	public void testCityConstructorRandomSeed() {
		City city = new City(1);
		assertEquals(city.getRandom().nextInt(), new Random(1).nextInt());
	}
	
	/*
	 * Test to ensure that getting a random spawn location consists of 
	 * getting the next int from a PRNG seeded with the seed specified in 
	 * the City object.
	 */
	@Test
	public void testGetRandomSpawnLocation() {
		City city = new City(1);
		Area area = city.getRandomSpawnLocation();
		
		Area possibleSpawnLocations[] = city.getSpawnLocations();
		Random random = new Random(1);
		int index = random.nextInt(possibleSpawnLocations.length);
		
		assertEquals(area, possibleSpawnLocations[index]);
	}
	
	/*
	 * Tests to see that the system prints "Driver X has left the city!" 
	 * when there are no more areas to traverse
	 */
	@Test
	public void testTraverseCityNoNextArea() {
		System.setOut(null);
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outputStream));
		
		Area area = mock(Area.class);
		when(area.getNextArea(mock(Random.class))).thenReturn(null);
		
		City city = new City(1);
		city.traverseCity("Driver X", area);
		
		System.out.println(outputStream.toString());
		assertTrue(outputStream.toString().contains("Driver X has left the city!"));
	}
	
	/*
	 * Tests to see that the system prints the proper statements 
	 * when there a next area is found. Additionally check to see 
	 * that driver eventually leaves city.
	 */
	@Test
	public void testTraverseCityNextAreaFound() {
		System.setOut(null);
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outputStream));
		
		Area nextArea = mock(Area.class);
		when(nextArea.getNextArea(mock(Random.class))).thenReturn(null);
		when(nextArea.getMessage()).thenReturn("Mocked Next Area");
		when(nextArea.getLastStreetUsed()).thenReturn("Mocked Street");
		
		Area area = new Area("Area", "", "");
		area.setNorth(nextArea);
		
		City city = new City(1);
		city.traverseCity("Driver X", area);
		
		String firstExpectedOutput = "Driver X heading from Area to Mocked Next Area via Mocked Street";
		String secondExpectedOutput = "Driver X has left the city!";
		
		String actualOutput = outputStream.toString();
		
		assertTrue(actualOutput.contains(firstExpectedOutput) && actualOutput.contains(secondExpectedOutput));
	}
	
	/*
	 * Tests getSpawnLocations() method to ensure that it returns the proper
	 * length array.
	 */
	@Test
	public void testGetSpawnLocations() {
		City city = new City(Mockito.anyInt());
		Area spawnLocations[] = city.getSpawnLocations();
		assertEquals(spawnLocations.length, 6);
	}
	
	/*
	 * Tests getRandom() method for highest number in its range
	 */
	@Test
	public void testGetRandom() {
		City city = new City(9999999);
		assertEquals(city.getRandom().nextInt(), new Random(9999999).nextInt());
	}
	
	/*
	 * Ensure the area constructor sets N, S, E, W values to null and 
	 * sets the message, street east west, and street north south values
	 * to the proper units defined in the constructor.
	 */
	@Test
	public void testAreaConstructor() {
		Area area = new Area("Laboon St.", "Bill Ave.", "Testing Blvd.");
		
		boolean expected = area.getNorth() == null && 
						   area.getSouth() == null && 
						   area.getEast() == null && 
						   area.getWest() == null &&
						   area.getStreetEastWest().equals("Bill Ave.") && 
						   area.getStreetNorthSouth().equals("Testing Blvd.") &&
						   area.getMessage().equals("Laboon St.") ? true : false;
		
		assertTrue(expected);
	}
	
	/*
	 * Test to ensure that getDirections() return an empty list when there are no
	 * possible directions to move.
	 */
	@Test
	public void testGetDirectionsNull() {
		Area area = new Area("", "", "");
		List<String> directions = area.getDirections();
		assertEquals(directions.size(), 0);
	}
	
	/*
	 * Test to ensure that getDirections() returns a list containing only "north"
	 * when north is the only possible directions to move.
	 */
	@Test
	public void testGetDirectionsNorth() {
		Area area = new Area("", "", "");
		area.setNorth(mock(Area.class));
		List<String> directions = area.getDirections();
		
		List<String> expectedOutput = Arrays.asList("north");
		assertEquals(directions, expectedOutput);
	}
	
	/*
	 * Test to ensure that getDirections() returns a list containing only "south"
	 * when south is the only possible directions to move.
	 */
	@Test
	public void testGetDirectionsSouth() {
		Area area = new Area("", "", "");
		area.setSouth(mock(Area.class));
		List<String> directions = area.getDirections();
		
		List<String> expectedOutput = Arrays.asList("south");
		assertEquals(directions, expectedOutput);
	}
	
	/*
	 * Test to ensure that getDirections() returns a list containing only "east"
	 * when east is the only possible directions to move.
	 */
	@Test
	public void testGetDirectionsEast() {
		Area area = new Area("", "", "");
		area.setEast(mock(Area.class));
		List<String> directions = area.getDirections();
		
		List<String> expectedOutput = Arrays.asList("east");
		assertEquals(directions, expectedOutput);
	}
	
	/*
	 * Test to ensure that getDirections() returns a list containing only "west"
	 * when west is the only possible directions to move.
	 */
	@Test
	public void testGetDirectionsWest() {
		Area area = new Area("", "", "");
		area.setWest(mock(Area.class));
		List<String> directions = area.getDirections();
		
		List<String> expectedOutput = Arrays.asList("west");
		assertEquals(directions, expectedOutput);
	}
	
	/*
	 * Test to ensure that getDirections() returns a list containing all directions
	 * when it is possible to move in all directions.
	 */
	@Test
	public void testGetDirectionsAll() {
		Area area = new Area("", "", "");
		area.setNorth(mock(Area.class));
		area.setSouth(mock(Area.class));
		area.setEast(mock(Area.class));
		area.setWest(mock(Area.class));
		
		List<String> directions = area.getDirections();
		
		List<String> expectedOutput = Arrays.asList("north", "south", "east", "west");
		assertEquals(directions, expectedOutput);
	}
	
	/*
	 * Ensure that getNextArea() method returns null when there are no directions
	 * to move. (this means car has left the city)
	 */
	@Test
	public void testGetNextAreaEmptyDirections() {
		Area area = new Area("", "", "");
		Area nextArea = area.getNextArea(mock(Random.class));
		assertEquals(nextArea, null);
	}
	
	/*
	 * Ensure that getNextArea() returns the area to the north when it is 
	 * randomly selected in the directions array.
	 */
	@Test
	public void testGetNextAreaNorth() {
		Area area = new Area("", "", "");
		Area areaNorth = new Area("345 Forbes Ave", "", "");
		
		area.setNorth(areaNorth);
		area.setStreetNorthSouth("123 Main Street");
		
		Random randomMock = mock(Random.class);
		when(randomMock.nextInt(Mockito.anyInt())).thenReturn(0);
		
		Area nextArea = area.getNextArea(randomMock);
		assertEquals(nextArea, areaNorth);
	}
	
	/*
	 * Ensure that getNextArea() returns the area to the south when it is 
	 * randomly selected in the directions array.
	 */
	@Test
	public void testGetNextAreaSouth() {
		Area area = new Area("", "", "");
		Area areaSouth = new Area("345 Forbes Ave", "", "");
		
		area.setSouth(areaSouth);
		area.setStreetNorthSouth("123 Main Street");
		
		Random randomMock = mock(Random.class);
		when(randomMock.nextInt(Mockito.anyInt())).thenReturn(0);
		
		Area nextArea = area.getNextArea(randomMock);
		assertEquals(nextArea, areaSouth);
	}

	/*
	 * Ensure that getNextArea() returns the area to the east when it is 
	 * randomly selected in the directions array.
	 */
	@Test
	public void testGetNextAreaEast() {
		Area area = new Area("", "", "");
		Area areaEast = new Area("345 Forbes Ave", "", "");
		
		area.setEast(areaEast);
		area.setStreetNorthSouth("123 Main Street");
		
		Random randomMock = mock(Random.class);
		when(randomMock.nextInt(Mockito.anyInt())).thenReturn(0);
		
		Area nextArea = area.getNextArea(randomMock);
		assertEquals(nextArea, areaEast);
	}
	
	/*
	 * Ensure that getNextArea() returns the area to the west when it is 
	 * randomly selected in the directions array.
	 */
	@Test
	public void testGetNextAreaWest() {
		Area area = new Area("", "", "");
		Area areaWest = new Area("345 Forbes Ave", "", "");
		
		area.setWest(areaWest);
		area.setStreetNorthSouth("123 Main Street");
		
		Random randomMock = mock(Random.class);
		when(randomMock.nextInt(Mockito.anyInt())).thenReturn(0);
		
		Area nextArea = area.getNextArea(randomMock);
		assertEquals(nextArea, areaWest);
	}
	
	/*
	 * Ensure that getNextArea() returns the the area to the west when the PRNG
	 * selects the last element in the array when all directions are possible
	 * (North, South, East, West).
	 */
	@Test
	public void testGetNextAreaAllWest() {
		Area area = new Area("", "", "");
		Area areaWest = new Area("345 Forbes Ave", "", "");
		Area areaEast = new Area("678 Market Rd", "", "");
		Area areaNorth = new Area("910 Small Blvd", "", "");
		Area areaSouth = new Area("111 Large St", "", "");
		
		area.setWest(areaWest);
		area.setEast(areaEast);
		area.setSouth(areaSouth);
		area.setNorth(areaNorth);
		
		area.setStreetNorthSouth("123 Main Street");
		area.setStreetEastWest("960 Random Street");
		
		Random randomMock = mock(Random.class);
		when(randomMock.nextInt(Mockito.anyInt())).thenReturn(3);
		
		Area nextArea = area.getNextArea(randomMock);
		assertEquals(nextArea, areaWest);
	}
	
	/*
	 * Test that the getNorth() method returns the proper Area.
	 */
	@Test
	public void testGetNorth() {
		Area area = new Area("", "", "");
		Area northArea = new Area("1", "2", "3");
		
		area.setNorth(northArea);
		assertEquals(area.getNorth(), northArea);
	}
	
	/*
	 * Test that the getSouth() method returns the proper Area.
	 */
	@Test
	public void testGetSouth() {
		Area area = new Area("", "", "");
		Area southArea = new Area("1", "2", "3");
		
		area.setSouth(southArea);
		assertEquals(area.getSouth(), southArea);
	}
	
	/*
	 * Test that the getEast() method returns the proper Area.
	 */
	@Test
	public void testGetEast() {
		Area area = new Area("", "", "");
		Area eastArea = new Area("1", "2", "3");
		
		area.setEast(eastArea);
		assertEquals(area.getEast(), eastArea);
	}
	
	/*
	 * Test that the getWest() method returns the proper Area.
	 */
	@Test
	public void testGetWest() {
		Area area = new Area("", "", "");
		Area westArea = new Area("1", "2", "3");
		
		area.setWest(westArea);
		assertEquals(area.getWest(), westArea);
	}
	
	/*
	 * Test that the getStreetEastWest() method returns the proper street.
	 */
	@Test
	public void testGetStreetEastWest() {
		Area area = new Area("", "Street East West", "");
		assertEquals(area.getStreetEastWest(), "Street East West");
	}
	
	/*
	 * Test that the getStreetNorthSouth() method returns the proper street.
	 */
	@Test
	public void testGetStreetNorthSouth() {
		Area area = new Area("", "", "Street North South");
		assertEquals(area.getStreetNorthSouth(), "Street North South");
	}
	
	/*
	 * Test that getMessage() returns the specified message.
	 */
	@Test
	public void testGetMessage() {
		Area area = new Area("Street Message", "", "");
		assertEquals(area.getMessage(), "Street Message");
	}
	
	/*
	 * Test that getLastStreetUsed() returns the proper value.
	 */
	@Test
	public void testGetLastStreetUsed() {
		Area area = new Area("", "", "");
		area.setLastStreetUsed("Labooooooon St.");
		assertEquals(area.getLastStreetUsed(), "Labooooooon St.");
	}
	
	/*
	 * Helper method to compare two areas
	 */
	public boolean compareAreas(Area area1, Area area2) {
		return (area1.getNorth() == null && area2.getNorth() == null) || Objects.equals(area1.getNorth().getMessage(), area2.getNorth().getMessage()) &&
			   (area1.getSouth() == null && area2.getSouth() == null) || Objects.equals(area1.getSouth().getMessage(), area2.getSouth().getMessage()) &&
			   (area1.getEast() == null && area2.getEast() == null) || Objects.equals(area1.getEast().getMessage(), area2.getEast().getMessage()) &&
			   (area1.getWest() == null && area2.getWest() == null) || Objects.equals(area1.getWest().getMessage(), area2.getWest().getMessage()) &&
			   Objects.equals(area1.getStreetEastWest(), area2.getStreetEastWest()) &&
			   Objects.equals(area1.getStreetNorthSouth(), area2.getStreetNorthSouth()) &&
			   Objects.equals(area1.getMessage(), area2.getMessage()) &&
			   Objects.equals(area1.getLastStreetUsed(), area2.getLastStreetUsed()) ? true : false;
	}

}