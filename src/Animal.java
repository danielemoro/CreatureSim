import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Animal extends Cell {

	private int TEETH_NEEDED_CARNIVORE = 100;
	
	Random rand;
	
	
	
	/**
	 * @param me - Map<String, Integer>
	 */
	public Animal (Map<String, Integer> me) {
		super(me.get("x"), me.get("y"));
		
		//check that map is correct
		Set<String> correctSet = new HashSet<String>();
		correctSet.add("x");
		correctSet.add("y");
		correctSet.add("energy_start");
		correctSet.add("energy_per_turn");
		correctSet.add("energy_needed_move");
		correctSet.add("energy_used_move");
		correctSet.add("energy_needed_split");
		correctSet.add("teeth");
		correctSet.add("age");
		correctSet.add("type");
		correctSet.add("can_eat");
		correctSet.add("energy");
		
		if(!me.keySet().equals(correctSet)){
			throw new IllegalArgumentException();
		}
		
		//initialize stats
		this.me.putAll(me);
		
		color = calculateColor();
		rand = new Random();
	}
	
	/**
	 * Simple constructor
	 * @param x
	 * @param y
	 */
	public Animal (int x, int y) {
		super(x, y);
		
		me.put("energy_start", 3000);
		me.put("energy_per_turn", -250);
		me.put("energy_needed_move", 250);
		me.put("energy_used_move", -200);
		me.put("energy_needed_split", 900);
		me.put("teeth", 0);
		me.put("can_eat", 12);
		me.put("type", 3);
		
		me.replace("energy", me.get("energy_start"));
		
		color = calculateColor();
		rand = new Random();
	}
	
	public Board next(Board currBoard){
		color = calculateColor();
		Board nextBoard = currBoard;
		
		//check if dead
		if(isDead()){
			nextBoard.setCell(me.get("x"), me.get("y"), new Cell(me.get("x"), me.get("y")));
			return nextBoard;
		} else {
			me.replace("age", me.get("age")+1);
		}
		
		if(me.get("energy") < 0){
			System.out.println("================================/nERROR-NEGATIVE-ENERGY-ERROR\n========================================");
			while(true){}
		}
		
		//update energy based on turn
		me.replace("energy", me.get("energy") + me.get("energy_per_turn"));
		
		//move
		if(me.get("energy") >= me.get("energy_needed_move")){
			nextBoard = move(currBoard);
			me.replace("energy", me.get("energy") + me.get("energy_used_move"));;
		}
		
		//split
		if(me.get("energy") >= me.get("energy_needed_split")){
			nextBoard = split(nextBoard);
		}
			
		return nextBoard;
	}
	
	private Board move(Board board){
		int destX = me.get("x") + (rand.nextInt(3)-1);
		int destY =  me.get("y") + (rand.nextInt(3)-1);
		if(canEat(board, destX, destY)){
			me.replace("energy", (int) (me.get("energy") + board.getCell(destX, destY).getEnergy()));
			board.move(me.get("x"), me.get("y"), destX, destY);
			me.put("x", destX);
			me.put("y", destY);
		}
		
		return board;
	}
	
	private Board split(Board board){
		boolean didSplit = false;
		int timesTried = 0;
		while(!didSplit && timesTried < 10){
			timesTried++;
			int destX = me.get("x") + (rand.nextInt(3)-1);
			int destY = me.get("y") + (rand.nextInt(3)-1);
			if(canEat(board, destX, destY)){
				me.replace("energy", (int) (me.get("energy") + board.getCell(destX, destY).getEnergy()));
				
				//make new plant with different parameters
				Map<String, Integer> childStats = new HashMap<String, Integer>();
				
				childStats.putAll(me);
				
				childStats.replace("x", destX);
				childStats.replace("y", destY);
				childStats.replace("energy_start", me.get("energy_start") + (rand.nextInt(51)-25));
				childStats.replace("energy_per_turn", me.get("energy_per_turn") + (rand.nextInt(51)-25));
				childStats.replace("energy_needed_move", me.get("energy_needed_move") + (rand.nextInt(51)-25));
				childStats.replace("energy_used_move", me.get("energy_used_move") + (rand.nextInt(51)-25));
				childStats.replace("energy_needed_split", me.get("energy_needed_split") + (rand.nextInt(51)-25));
				childStats.replace("teeth", me.get("teeth") + (rand.nextInt(51)-25));
				
				//check if is carnivore
				if(childStats.get("teeth") >= TEETH_NEEDED_CARNIVORE){
					childStats.put("can_eat", 13);
					childStats.put("type", 4);
					//childStats.replace("energy_per_turn", limitStats(childStats.get("energy_per_turn"), -99999, -300));
				}
				
				//check if stats are reasonable
				childStats.replace("energy_start", limitStats(childStats.get("energy_start"), 1000, 99999));
				childStats.replace("energy_per_turn", limitStats(childStats.get("energy_per_turn"), -99999, -200));
				childStats.replace("energy_used_move", limitStats(childStats.get("energy_used_move"), -99999, -200));
				
				me.put("energy", me.get("energy") - childStats.get("energy_start"));
				
				childStats.replace("energy", me.get("energy_start"));
				Animal child = new Animal(childStats); 
				board.setCell(destX, destY, child); 
				
				didSplit = true;
			}
		}
		
		return board;
	}
	
	private Color calculateColor(){
		float brightness = Math.min(me.get("energy")/(me.get("energy_start")*1.2f),1);
		brightness = Math.abs(brightness);
		brightness = Math.min(brightness, 1);
		Color newColor = new Color(brightness,brightness,0.1f);
		
		//red if carnivore
		if(me.get("teeth") >= TEETH_NEEDED_CARNIVORE){
			newColor = new Color(brightness,0.1f,0.1f);
		}
		
		return newColor;
	}
	
	private boolean canEat(Board board, int destX, int destY){
		return Integer.toString(me.get("can_eat")).contains(Integer.toString(board.getCell(destX, destY).getTypeInt()));
	}
}
