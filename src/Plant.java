import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Plant extends Cell{
	

	/**
	 * @param me - Map<String, Integer>
	 */
	public Plant(Map<String, Integer> me){//int x, int y, int energy_start, int energy_per_turn, int energy_needed_grow){
		super(me.get("x"), me.get("y"));
		
		//check that map is correct
		Set<String> correctSet = new HashSet<String>();
		correctSet.add("x");
		correctSet.add("y");
		correctSet.add("energy_start");
		correctSet.add("energy_per_turn");
		correctSet.add("energy_needed_grow");
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
	public Plant(int x, int y){
		super(x, y);
		
		me.put("energy_start", 100);
		me.put("energy_per_turn", 10);
		me.put("energy_needed_grow", 120);
		me.put("can_eat", 1);
		me.put("type", 2);
		me.replace("energy", me.get("energy_start"));
		
		color = calculateColor();
		rand = new Random();
	}
	
	private Color calculateColor(){
		float brightness = Math.min(me.get("energy")/(me.get("energy_start")*5f),0.6f);
		brightness = Math.abs(brightness);
		brightness = Math.min(brightness, 1f);
		
		Color newColor = new Color(0.1f,1f-brightness,0.1f);
		return newColor;
	}
	
	public Board next(Board nextBoard){
		
		color = calculateColor();
		
		if(isDead()){
			nextBoard.setCell(me.get("x"), me.get("y"), new Cell(me.get("x"), me.get("y")));
//			return nextBoard;
		} else {
			me.replace("age", me.get("age")+1);
		}
		
		//update energy based on turn
		me.replace("energy", me.get("energy") + me.get("energy_per_turn"));
		
		//grow
		if(me.get("energy") > me.get("energy_needed_grow")){			
			if(grow(nextBoard)){
				me.replace("energy", me.get("energy") - me.get("energy_start"));
			}
		}
			
		return nextBoard;
	}
	
	private boolean grow(Board nextBoard){
		boolean didGrow = false;
		
		for(int xDelta = -1; xDelta < 2; xDelta++){
			for(int yDelta = -1; yDelta < 2; yDelta++){
				if(nextBoard.getCell(me.get("x") + xDelta, me.get("y") + yDelta).getTypeInt() == me.get("can_eat")){
					
					//make new plant with different parameters
					Map<String, Integer> childStats = new HashMap<String, Integer>();
					
					childStats.putAll(me);
					
					childStats.replace("x", me.get("x") + xDelta);
					childStats.replace("y", me.get("y") + yDelta);
					childStats.replace("energy", me.get("energy_start"));
					childStats.replace("age", 0);
					childStats.replace("energy_start", me.get("energy_start") + (rand.nextInt(51)-25));
					childStats.replace("energy_per_turn", me.get("energy_per_turn") + (rand.nextInt(51)-25));
					childStats.replace("energy_needed_grow", me.get("energy_needed_grow") + (rand.nextInt(51)-25));
					
					//check if stats are reasonable
					childStats.replace("energy_start", limitStats(childStats.get("energy_start"), 10, 99999));
					childStats.replace("energy_per_turn", limitStats(childStats.get("energy_per_turn"), 0, 100));
					
					Plant child = new Plant(childStats); 
					
					nextBoard.setCell(me.get("x") + xDelta, me.get("y") + yDelta, child); 
					didGrow = true;
				}
			}
		}
		
		return didGrow;
	}

	public int get_energy_start() {
		return me.get("energy_start");
	}
	
	public int get_energy_per_turn() {
		return me.get("energy_per_turn");
	}
	
	public int get_energy_needed_grow() {
		return me.get("energy_needed_grow");
	}
	
	public String toString(){
		return me.toString();
	}
}
