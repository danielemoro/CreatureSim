//package com.daniele.creature.client;
//import java.awt.Dimension;
//import java.util.Random;
//
//import javax.swing.JFrame;
//
//public class Main {
//
//	protected static Board board;
//	public static long systemTime;
//	public static double deltaTime = 300;
//	public static long turns;
//	static Random rand;
//
//	public static void main(String[] args) {
//		int dimensions = 40;
//		rand = new Random();
//		board = new Board(dimensions, dimensions);
//		board.setCell(1, 1, new Plant(board, 1, 1, 0));
//
//		drawWalls(dimensions);
//
//		// draw
//		JFrame frame = new JFrame("CreatureSim");
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setPreferredSize(new Dimension(2000, 2000));
//		GUIboard guiboard = new GUIboard(board);
//		frame.getContentPane().add(guiboard);
//		frame.pack();
//		frame.setVisible(true);
//
//		long oldTime = getTime();
//		systemTime = getTime();
//		turns = 0;
//		// runs every delta time
//		while (turns < 999999) {
//			//TIME
//			oldTime = getTime();
//			while (systemTime - oldTime < deltaTime) {
//				systemTime = getTime();
//			}
//			turns++;
//
//			//DRAW
//			if (turns % 1 == 0) {
//				guiboard.createGridPanel(board);
//			}
//
//			// UPDATE BOARD
//			for (int i = 0; i < board.getNumCells(); i++) {
//				board.getCell(i).next(turns);
//			}
//
//			// Make animal on turn 30
//			if (turns == 5) {
//				board.setCell(3, 3, new Animal(board, 3, 3, turns));
//			}
//
//			//if (turns == 100) killAllPlants();
//
//			printToConsole();
//
//			// CSV
//			// String output = turns + ", " + sumEnergy + ", ";
//			// String animalOut = "";
//			// String plantOut = "";
//			// if(numAnimals > 0){
//			// animalOut = numAnimals + ", " + sumAnimalAge/numAnimals + ", " +
//			// SUM_ANIMAL_ENERGY/numAnimals + ", " +
//			// SUM_ENERGY_AT_START/numAnimals + ", " +
//			// SUM_ENERGY_LOST_PER_TURN/numAnimals + ", " +
//			// SUM_ENERGY_NEEDED_TO_MOVE/numAnimals + ", " +
//			// SUM_ENERGY_USED_TO_MOVE/numAnimals + ", " +
//			// SUM_ENERGY_NEEDED_TO_SPLIT/numAnimals + ", ";
//			// } else {
//			// animalOut = " , , , , , , ,";
//			// }
//			// if(numPlants > 0){
//			// plantOut = numPlants + ", " + sumPlantAge/numPlants + ", " +
//			// SUM_PLANT_ENERGY/numPlants + ", "+
//			// SUM_PLANT_ENERGY_AT_START/numPlants + ", " +
//			// SUM_ENERGY_GAINED_PER_TURN/numPlants + ", " +
//			// SUM_ENERGY_NEEDED_TO_GROW/numPlants + ", " + ", ";
//			// } else {
//			// plantOut = " , , , , , ,";
//			// }
//			//
//			// System.out.println(output + plantOut + animalOut);
//
//		}
//
//		System.out.println("ERROR! EXITED LOOP!");
//
//	}
//
//	public static long getTime() {
//		return System.currentTimeMillis();
//	}
//
//	public static void drawWalls(int dimensions) {
//		for (int i = 0; i < dimensions / 1.5; i++) {
//			int ranX = rand.nextInt(board.getWidth());
//			int ranY = rand.nextInt(board.getHeigth());
//			// if (i <= dimensions/4) {
//			for (int x = 0; x < dimensions / 8; x++) {
//				for (int y = 0; y < dimensions / 8; y++) {
//					board.setCell(ranX + x, ranY + y, new Wall(board, 0, 0));
//				}
//			} // else {
//				// for (int j = 0; j < dimensions/4; j++)
//				// board.setCell(ranX + j, ranY + j, new Wall(0, 0));
//				// }
//		}
//	}
//
//	public static void killAllPlants() {
//		System.out.println("=============================================\nKilling all plants");
//		int counter = 0;
//		
//		for (int i = 0; i < board.getNumCells(); i++) {
//			Cell cell = board.getCell(i);
//			if (cell.getType().equals("plant")) {
//				board.setCell(i, new Cell(board, 0, 0));
//				counter++;
//			}
//		}
//		
//		System.out.println("Killed " + counter);
//	}
//
//	public static void printToConsole(){
//		int numPlants = 0;
//			int[] avgPlantDNA = Plant.getDNAEmpty();
//		
//		int numAnimals = 0;
//			int[] avgAnimalDNA = Animal.getDNAEmpty();
//		
//		int numCarnivores = 0;
//			int[] avgCarnivoreDNA = Animal.getDNAEmpty();
//
//		for (int i = 0; i < board.getNumCells(); i++) {
//			Cell cell = board.getCell(i);
//			if (cell.getType().equals("plant")) { // if is plant
//				numPlants++;
//				avgPlantDNA = addDNA(avgPlantDNA, cell.getDNA());
//			}
//			if (cell.getType().equals("animal")) { // if is animal
//				numAnimals++;
//				avgAnimalDNA = addDNA(avgAnimalDNA, cell.getDNA());
//			}
//			if (cell.getType().equals("carnivore")) { // if is carnivore
//				numCarnivores++;
//				avgCarnivoreDNA = addDNA(avgCarnivoreDNA, cell.getDNA());
//			}
//
//		}	
//		
//		//find averages
//		avgPlantDNA = divideDNA(avgPlantDNA, numPlants);
//		avgAnimalDNA = divideDNA(avgAnimalDNA, numAnimals);
//		avgCarnivoreDNA = divideDNA(avgCarnivoreDNA, numCarnivores);
//		
//		
//		System.out.println("TURN " + turns + "--------------------");
//		System.out.println("      PLNT: total:" + numPlants + "   " + printDNA(avgPlantDNA, Plant.getDNALabel()));
//		System.out.println("      ANML: total:" + numAnimals + "   " + printDNA(avgAnimalDNA, Animal.getDNALabel()));
//		System.out.println("      CRNV: total:" + numCarnivores + "   " + printDNA(avgCarnivoreDNA, Animal.getDNALabel()));
//	}
//	
//	private static int[] addDNA(int a[], int b[]){
//		for(int i = 0; i < a.length; i++){
//			a[i] += b[i];
//		}
//		
//		return a;
//	}
//	
//	private static int[] divideDNA(int a[], int b){
//		if(b == 0) b = 1;
//		
//		for(int i = 0; i < a.length; i++){
//			a[i] /= b;
//		}
//		
//		return a;
//	}
//	
//	private static String printDNA(int dna[], String label[]){
//		String out = "";
//		for(int i =0; i < dna.length; i ++){
//			out += label[i] + ": " + dna[i] + "  ";
//		}
//		
//		return out;
//	}
//}
