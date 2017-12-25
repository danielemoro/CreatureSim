package com.daniele.creature.client;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class CreatureSimWeb implements EntryPoint {
	static Canvas canvas;
	static long turn;
	Timer timer;

	protected static Board board;
	public static int deltaTime = 100;
	static Random rand;
	static Label debug;
	static int canvasHeight = 1000;
	static int canvasWidth = 1000;
	static final int boardDimensions = 100;

	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network " + "connection and try again.";
	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

	public void onModuleLoad() {

		debug = new Label("");

		turn = 0;

		canvas = Canvas.createIfSupported();
		canvas.setStyleName("mainCanvas");
		canvas.setWidth(canvasWidth + "px");
		canvas.setCoordinateSpaceWidth(canvasWidth);
		canvas.setHeight(canvasHeight + "px");
		canvas.setCoordinateSpaceHeight(canvasHeight);

		board = new Board(boardDimensions, boardDimensions);
		board.setCell(1, 1, new Plant(board, 1, 1, 0));
		drawWalls(boardDimensions);
		drawGrid(turn);

		RootPanel.get("debugContainer").add(debug);
		RootPanel.get("canvasContainer").add(canvas);

		timer = new Timer() {
			@Override
			public void run() {
				turn++;
				drawGrid(turn);

				// UPDATE BOARD
				for (int i = 0; i < board.getNumCells(); i++) {
					board.getCell(i).next(turn);
				}

				// Make animal on turn 30
				if (turn == 5) {
					board.setCell(3, 3, new Animal(board, 3, 3, turn));
				}

				// if (turns == 100) killAllPlants();

				timer.schedule(deltaTime);
			}
		};
		timer.schedule(deltaTime);
	}

	public static void drawGrid(long turn) {
		Context2d ctx = canvas.getContext2d();

		int row = board.getHeigth();
		int col = board.getWidth();
		int widthx = canvasWidth / board.getWidth();
		int widthy = canvasHeight / board.getHeigth();

		ctx.setFillStyle("#000000");
		ctx.fillRect(0, 0, 1000, 1000); // Draw a rectangle with new settings

		for (int r = 0; r < row; r++) {
			for (int c = 0; c < col; c++) {

				
				//ctx.setFillStyle("#" + board.getCell(c, r).getColor());
				Color currColor = board.getCell(c, r).getColor();
				ctx.setFillStyle(CssColor.make(currColor.getR(), currColor.getG(), currColor.getB()));
				
				ctx.fillRect(r * widthx, c * widthy, widthx, widthy);
			}
		}
	}

	public static void drawWalls(int dimensions) {
		for (int i = 0; i < dimensions / 1.5; i++) {
			int ranX = Random.nextInt(board.getWidth());
			int ranY = Random.nextInt(board.getHeigth());

			for (int x = 0; x < dimensions / 8; x++) {
				for (int y = 0; y < dimensions / 8; y++) {
					board.setCell(ranX + x, ranY + y, new Wall(board, 0, 0));
				}
			}
		}
	}

	public static void killAllPlants() {
		System.out.println("=============================================\nKilling all plants");
		int counter = 0;

		for (int i = 0; i < board.getNumCells(); i++) {
			Cell cell = board.getCell(i);
			if (cell.getType().equals("plant")) {
				board.setCell(i, new Cell(board, 0, 0));
				counter++;
			}
		}

		System.out.println("Killed " + counter);
	}

	public static void printToConsole() {
		int numPlants = 0;
		int[] avgPlantDNA = Plant.getDNAEmpty();

		int numAnimals = 0;
		int[] avgAnimalDNA = Animal.getDNAEmpty();

		int numCarnivores = 0;
		int[] avgCarnivoreDNA = Animal.getDNAEmpty();

		for (int i = 0; i < board.getNumCells(); i++) {
			Cell cell = board.getCell(i);
			if (cell.getType().equals("plant")) { // if is plant
				numPlants++;
				avgPlantDNA = addDNA(avgPlantDNA, cell.getDNA());
			}
			if (cell.getType().equals("animal")) { // if is animal
				numAnimals++;
				avgAnimalDNA = addDNA(avgAnimalDNA, cell.getDNA());
			}
			if (cell.getType().equals("carnivore")) { // if is carnivore
				numCarnivores++;
				avgCarnivoreDNA = addDNA(avgCarnivoreDNA, cell.getDNA());
			}

		}

		// find averages
		avgPlantDNA = divideDNA(avgPlantDNA, numPlants);
		avgAnimalDNA = divideDNA(avgAnimalDNA, numAnimals);
		avgCarnivoreDNA = divideDNA(avgCarnivoreDNA, numCarnivores);

		System.out.println("TURN " + turn + "--------------------");
		System.out.println(" PLNT: total:" + numPlants + " " + printDNA(avgPlantDNA, Plant.getDNALabel()));
		System.out.println(" ANML: total:" + numAnimals + " " + printDNA(avgAnimalDNA, Animal.getDNALabel()));
		System.out.println(" CRNV: total:" + numCarnivores + " " + printDNA(avgCarnivoreDNA, Animal.getDNALabel()));
	}

	private static int[] addDNA(int a[], int b[]) {
		for (int i = 0; i < a.length; i++) {
			a[i] += b[i];
		}

		return a;
	}

	private static int[] divideDNA(int a[], int b) {
		if (b == 0)
			b = 1;

		for (int i = 0; i < a.length; i++) {
			a[i] /= b;
		}

		return a;
	}

	private static String printDNA(int dna[], String label[]) {
		String out = "";
		for (int i = 0; i < dna.length; i++) {
			out += label[i] + ": " + dna[i] + " ";
		}

		return out;
	}

}
