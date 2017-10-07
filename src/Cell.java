import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

//CELL TYPES
//wall = 0
//empty = 1
//plant = 2
//animal = 3
//carnivore = 4
//nothing  = 9

public class Cell {
	protected Map<String, Integer> me;
	protected Color color;
	Random rand;
	
	public Cell(int x, int y){
		me = new HashMap<String, Integer>();
		
		me.put("energy", 0);
		me.put("x", x);
		me.put("y", y);
		me.put("age", 0);
		me.put("type", 1);
		me.put("can_eat", 9);
		
		color  = Color.WHITE;
	}
	
	public float getEnergy(){
		return me.get("energy");
	}
	
	public int getAge(){
		return me.get("age");
	}
	
	public Color getColor(){
		return color;
	}
	
	public String getType(){
		switch (me.get("type")) {
			case 0:
				return "wall";
			case 1:
				return "empty";
			case 2:
				return "plant";
			case 3:
				return "animal";
			case 4:
				return "carnivore";
		}
		
		return "n/a";
	}
	
	public int getTypeInt(){
		return me.get("type");
	}
	
	public Board next(Board currBoard){
		return currBoard;
	}
	
	public int getX(){
		return me.get("x");
	}
	
	public int getY(){
		return me.get("y");
	}
	
	public boolean isDead(){
		return (me.get("energy") < 0);
	}
	
	public void highlight(){
		color = Color.RED;
	}
	
	public void setLocation(int x, int y){
		me.replace("x", x);
		me.replace("y", y);
	}
	
	protected int limitStats(int original, int min, int max){
		if(original > max){
			original = max;
		}
		if(original < min){
			original = min;
		}
		
		return original;
	}
	
	public String toString(){
		return me.toString();
	}
}
