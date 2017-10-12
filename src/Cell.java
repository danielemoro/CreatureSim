import java.awt.Color;
import java.util.Random;

//CELL TYPES
//wall = 0
//empty = 1
//plant = 2
//animal = 3
//carnivore = 4
//nothing  = 9

public class Cell {
	Board board;
	Random rand;
	
	//CELL PROPERTIES
	protected long lastTurn;
	private long id;
	private int energy; 
		protected final int ENERGY_MIN = 0;
		protected final int ENERGY_MAX = 1000000;		
	private int x;
	private int y;
	private int age;
	private String type;
	private String[] canEat;
	private Color color;
	
	
	public Cell(Board board, int x, int y){
		this.board = board;
		id = System.currentTimeMillis();
		setEnergy(0);
		setCanEat(new String[]{});
		setType("nothing");
		setAge(0);
		setX(x);
		setY(y);
		
		color = Color.WHITE;
	}
	
	public Cell(Board board, int x, int y, long turn){
		this.board = board;
		id = System.currentTimeMillis();
		setEnergy(0);
		setCanEat(new String[]{});
		setType("nothing");
		setAge(0);
		setX(x);
		setY(y);
		
		lastTurn = turn;
		
		color = Color.WHITE;
	}
	
	public void next(long turns){
		//do nothing;
	}
	
	public int getEnergy() {
		return energy;
	}

	public void setEnergy(int energy) {
		if(energy < ENERGY_MIN){
			board.killCell(this);
		} else if (energy > ENERGY_MAX){
			this.energy = ENERGY_MAX;
		} else {
			this.energy = energy;
		}
	}
	
	public void changeEnergy(int change_in_energy) {
		int newEnergy = this.getEnergy() + change_in_energy;
		
		this.setEnergy(newEnergy);
	}
	
	public void isDead() {
		if(this.energy < ENERGY_MIN){
			board.killCell(this);
		}
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		if(x >= board.getWidth()){
			this.x = board.getWidth()-1;
		} else  if (x < 0){
			this.x = 0;
		} else {
			this.x = x;
		}
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		if(y >= board.getWidth()){
			this.y = board.getWidth()-1;
		} else  if (y < 0){
			this.y = 0;
		} else {
			this.y = y;
		}
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
	
	public void increaseAge() {
		this.age++;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		if(type.equals("wall") || type.equals("plant") || type.equals("animal") || type.equals("carnivore") || type.equals("nothing") ) {
			this.type = type;
		} else {
			throw new IllegalArgumentException("Invalid Type");
		}
	}

	public String[] getCanEat() {
		return canEat;
	}

	public void setCanEat(String[] can_eat) {
		boolean everythingGood = true;
		for(String curr: can_eat){
			if(curr.equals("wall") || curr.equals("plant") || curr.equals("animal") || curr.equals("carnivore") || curr.equals("nothing") ) {
				//all good
			} else {
				everythingGood = false;
			}
		}
		
		if(everythingGood){
			this.canEat = can_eat;
		} else {
			throw new IllegalArgumentException("Invalid Type to Eat");
		}
		
	}
	
	public boolean canIEat(String[] source, String destination){
		for(String curr : source){
			if(curr.equals(destination)){
				return true;
			}
		}
		
		return false;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	public long getID(){
		return this.id;
	}

	public String toString(){
		return "NEED TO IMPLEMENT";
	}
	
	public int[] getDNA(){
		int dna[] = {getEnergy(), getAge()};
		return dna;
	}
	
	public static String[] getDNALabel(){
		return new String[]{"Energy", "Age"};
	}
	
	public static int[] getDNAEmpty(){
		return new int[]{0, 0};
	}
}
