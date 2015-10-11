public class CitySim9000 {
	
	public static void main(String[] args) {
		if(args.length != 1) {
			System.out.println("Error: Please enter a single integer argument.");
			System.exit(1);
		}
		
		int seed = -1;
		try {
			seed = Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
			System.out.println("Error: Please enter a integer as the first argument.");
			System.exit(1);
		}
		
		City city = new City(seed);
		
		for(int i = 0; i < 5; i++) {
			String driver = "Driver " + i;
			city.traverseCity(driver, city.getRandomSpawnLocation());
			System.out.println("-----");
		}
	}
	
}