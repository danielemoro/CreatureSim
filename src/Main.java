import java.awt.Dimension;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.swing.JFrame;

public class Main {

	public static Board board;
	public static long systemTime;
	public static double deltaTime = 300;
	public static long turns;
	static Random rand;

	public static void main(String[] args) {
		int dimensions = 40;
		rand = new Random();
		board = new Board(dimensions, dimensions);
		board.setCell(4, 4, new Plant(4, 4));

		for (int i = 0; i < dimensions/1.5; i++) {
			int ranX = rand.nextInt(board.getWidth());
			int ranY = rand.nextInt(board.getHeigth());
//			if (i <= dimensions/4) {
				for (int x = 0; x < dimensions/8; x++){
					for(int y = 0; y < dimensions/8; y++){
						board.setCell(ranX + x, ranY + y, new Wall(0, 0));
					}
			} //else {
//				for (int j = 0; j < dimensions/4; j++)
//					board.setCell(ranX + j, ranY + j, new Wall(0, 0));
//			}
		}

		// draw
		JFrame frame = new JFrame("CreatureSim");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(2000, 2000));
		GUIboard guiboard = new GUIboard(board);
		frame.getContentPane().add(guiboard);
		frame.pack();
		frame.setVisible(true);

		long oldTime = getTime();
		systemTime = getTime();
		turns = 0;
		Board nextBoard = board.clone();
		// runs every delta time
		while (turns < 999999) {
			oldTime = getTime();
			while (systemTime - oldTime < deltaTime) {
				systemTime = getTime();
			}
			turns++;

			board = nextBoard.clone();

			// System.out.println(Runtime.getRuntime().freeMemory());

			// draw
			if(turns % 1 == 0){
				guiboard.createGridPanel(board);
			}

			// UPDATE BOARD
			for (int i = 0; i < board.getNumCells(); i++) {
				nextBoard = board.getCell(i).next(nextBoard);
			}

			// Make animal on turn 30
			if (turns == 5) {
				nextBoard.setCell(3, 3, new Animal(3, 3));
			}

			if (turns == -1) {
				System.out.println("=============================================\nKilling all plants");
				int counter = 0;
				for (int i = 0; i < nextBoard.getNumCells(); i++) {
					Cell cell = nextBoard.getCell(i);
					if (cell.getTypeInt() == 2) {
						nextBoard.setCell(i, new Cell(0, 0));
						counter++;
					}
				}
				System.out.println("Killed " + counter);
			}

			boolean isReadable = true;
			if (isReadable) {
				Map<String, Integer> sumPlant = new HashMap<String, Integer>();
				Map<String, Integer> sumAnimal = new HashMap<String, Integer>();
				Map<String, Integer> sumCarnivore = new HashMap<String, Integer>();

				int numPlants = 0;
				int numAnimals = 0;
				int numCarnivores = 0;

				for (int i = 0; i < board.getNumCells(); i++) {
					Cell cell = board.getCell(i);
					if (cell.getTypeInt() == 2) { // if is plant
						sumPlant = sum(sumPlant, cell.me);
						numPlants++;
					}
					if (cell.getTypeInt() == 3) { // if is animal
						sumAnimal = sum(sumAnimal, cell.me);
						numAnimals++;
					}
					if (cell.getTypeInt() == 4) { // if is carnivore
						sumCarnivore = sum(sumCarnivore, cell.me);
						numCarnivores++;
					}

				}

				System.out.println("------ TURN " + turns + " ----------------------------------");
				System.out.println("PLNT: total:" + numPlants + " " + divide(sumPlant, numPlants));
				System.out.println("ANML: total:" + numAnimals + " " + divide(sumAnimal, numAnimals));
				System.out.println("CRNV: total:" + numCarnivores + " " + divide(sumCarnivore, numCarnivores));
			} else {

			 //CSV
//			 String output = turns + ", " + sumEnergy + ", ";
//			 String animalOut = "";
//			 String plantOut = "";
//			 if(numAnimals > 0){
//			 animalOut = numAnimals + ", " + sumAnimalAge/numAnimals + ", " +
//			 SUM_ANIMAL_ENERGY/numAnimals + ", " +
//			 SUM_ENERGY_AT_START/numAnimals + ", " +
//			 SUM_ENERGY_LOST_PER_TURN/numAnimals + ", " +
//			 SUM_ENERGY_NEEDED_TO_MOVE/numAnimals + ", " +
//			 SUM_ENERGY_USED_TO_MOVE/numAnimals + ", " +
//			 SUM_ENERGY_NEEDED_TO_SPLIT/numAnimals + ", ";
//			 } else {
//			 animalOut = " , , , , , , ,";
//			 }
//			 if(numPlants > 0){
//			 plantOut = numPlants + ", " + sumPlantAge/numPlants + ", " +
//			 SUM_PLANT_ENERGY/numPlants + ", "+
//			 SUM_PLANT_ENERGY_AT_START/numPlants + ", " +
//			 SUM_ENERGY_GAINED_PER_TURN/numPlants + ", " +
//			 SUM_ENERGY_NEEDED_TO_GROW/numPlants + ", " + ", ";
//			 } else {
//			 plantOut = " , , , , , ,";
//			 }
//			
//			 System.out.println(output + plantOut + animalOut);
			}

		}

		System.out.println("ERROR! EXITED LOOP!");
	}

	public static long getTime() {
		return System.currentTimeMillis();
	}

	private static Map<String, Integer> sum(Map<String, Integer> map1, Map<String, Integer> map2) {
		Map<String, Integer> result = new HashMap<String, Integer>();
		result.putAll(map1);
		for (String key : map2.keySet()) {
			Integer value = result.get(key);
			if (value != null) {
				Integer newValue = value + map2.get(key);
				result.put(key, newValue);
			} else {
				result.put(key, map2.get(key));
			}
		}
		return result;
	}

	private static Map<String, Integer> divide(Map<String, Integer> map, float divisor) {
		for (String key : map.keySet()) {
			map.put(key, (int) (map.get(key) / divisor));
		}
		return map;
	}

}
