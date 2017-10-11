import java.awt.Color;
import java.util.Random;

public class Animal extends Cell {

	//ANIMAL PROPERTIES
	private int energyStart;
	private int energyPerTurn;
		protected final int ENERGY_PER_TURN_MIN = -1000000;
		protected final int ENERGY_PER_TURN_MAX = -10;
	private int energyNeededMove;
		protected final int ENERGY_NEEDED_MOVE_MIN = 0;
		protected final int ENERGY_NEEDED_MOVE_MAX = Integer.MAX_VALUE;
	private int energyUsedMove;
		protected final int ENERGY_USED_MOVE_MIN = -1000000;
		protected final int ENERGY_USED_MOVE_MAX = -10;
	private int energyNeededSplit;
		protected final int ENERGY_NEEDED_SPLIT_MIN = 0;
		protected final int ENERGY_NEEDED_SPLIT_MAX = Integer.MAX_VALUE;
	private int teeth;
		private final int TEETH_NEEDED_CARNIVORE = 100;
		private final int TEETH_MIN = -999;
		private final int TEETH_MAX = 999;
		
	
	public Animal (Board board, int x, int y, long turn) {
		super(board, x, y, turn);
		
		this.setEnergyStart(3000);
		this.setEnergy(this.getEnergyStart());
		this.setEnergyPerTurn(-100);
		this.setEnergyNeededMove(300);
		this.setEnergyUsedMove(-100);
		this.setEnergyNeededSplit(4000);
		this.setTeeth(0);
		this.setCanEat(new String[]{"nothing", "plant"});
		this.setType("animal");
		this.setColor(calculateColor());
	
		rand = new Random();
	}
	
	public void next(long turn){
		if(turn > lastTurn){ //check if the turn has advanced
			lastTurn = turn;
			increaseAge();
			this.isDead();
			
			//update energy based on turn
			this.changeEnergy(this.getEnergyPerTurn());
			
			//move
			if(this.getEnergy() > this.getEnergyNeededMove()){
				move();
				this.changeEnergy(this.getEnergyUsedMove());
			}
			
			//split
			if(getEnergy() >= getEnergyNeededSplit()){
				split();
			}
			
			this.setColor(calculateColor());
		} 
	}
	
	private void move(){
		int destX = getX() + (rand.nextInt(3)-1);
		int destY =  getY() + (rand.nextInt(3)-1);
		System.out.println("X: " + getX() + "  Y: " + getY());
		System.out.println("destX:  " + destX + "  destY: " + destY);
		
		if(canIEat(this.getCanEat(), board.getCell(destX, destY).getType())){
			changeEnergy(board.getCell(destX, destY).getEnergy());
			board.move(getX(), getY(), destX, destY);
			setX(destX);
			setY(destY);
		}
		
	}
	
	private void split(){
		boolean didSplit = false;
		int timesTried = 0;
		while(!didSplit && timesTried < 10){
			timesTried++;
			int destX = getX() + (rand.nextInt(3)-1);
			int destY = getY() + (rand.nextInt(3)-1);
			if(canIEat(this.getCanEat(), board.getCell(destX, destY).getType())){
				changeEnergy(board.getCell(destX, destY).getEnergy());
				
				Animal child = new Animal(board, destX, destY, lastTurn);
				
				child.setEnergy(this.getEnergyStart());
				child.setEnergyStart(this.getEnergyStart() + (rand.nextInt(50)-25));
				child.setEnergyPerTurn(this.getEnergyPerTurn() + (rand.nextInt(50)-25));
				child.setEnergyNeededMove(this.getEnergyNeededMove() + (rand.nextInt(50)-25));
				child.setEnergyUsedMove(this.getEnergyUsedMove() + (rand.nextInt(50)-25));
				child.setEnergyNeededSplit(this.getEnergyNeededSplit() + (rand.nextInt(50)-25));
				child.setTeeth(this.getTeeth() + (rand.nextInt(50)-25));
					
				this.changeEnergy(-this.getEnergyStart());
				board.setCell(child.getX(), child.getY(), child);
				
				didSplit = true;
			}
		}
	}
	
	private Color calculateColor(){
		float brightness = Math.min(getEnergy()/(getEnergyStart()*1.2f),1);
		brightness = Math.abs(brightness);
		brightness = Math.min(brightness, 1);
		Color newColor = new Color(brightness,brightness,0.1f);
		
		//red if carnivore
		if(this.getType().equals("carnivore")){
			newColor = new Color(brightness,0.1f,0.1f);
		}
		
		return newColor;
	}

	public int getEnergyStart() {
		return energyStart;
	}

	public void setEnergyStart(int energyStart) {
		if(energyStart < super.ENERGY_MIN){
			this.energyStart = super.ENERGY_MIN;
		} else if (energyStart > super.ENERGY_MAX){
			this.energyStart = super.ENERGY_MAX;
		} else {
			this.energyStart = energyStart;
		}
	}

	public int getEnergyPerTurn() {
		return energyPerTurn;
	}

	public void setEnergyPerTurn(int energyPerTurn) {
		if(energyPerTurn < ENERGY_PER_TURN_MIN){
			this.energyPerTurn = ENERGY_PER_TURN_MIN;
		} else if (energyPerTurn > ENERGY_PER_TURN_MAX){
			this.energyPerTurn = ENERGY_PER_TURN_MAX;
		} else {
			this.energyPerTurn = energyPerTurn;
		}
	}

	public int getEnergyNeededMove() {
		return energyNeededMove;
	}

	public void setEnergyNeededMove(int energyNeededMove) {
		if(energyNeededMove < ENERGY_NEEDED_MOVE_MIN){
			this.energyNeededMove = ENERGY_NEEDED_MOVE_MIN;
		} else if (energyNeededMove > ENERGY_NEEDED_MOVE_MAX){
			this.energyNeededMove = ENERGY_NEEDED_MOVE_MAX;
		} else {
			this.energyNeededMove = energyNeededMove;
		}
	}

	public int getEnergyUsedMove() {
		return energyUsedMove;
	}

	public void setEnergyUsedMove(int energyUsedMove) {
		if(energyUsedMove < ENERGY_USED_MOVE_MIN){
			this.energyUsedMove = ENERGY_USED_MOVE_MIN;
		} else if (energyUsedMove > ENERGY_USED_MOVE_MAX){
			this.energyUsedMove = ENERGY_USED_MOVE_MAX;
		} else {
			this.energyUsedMove = energyUsedMove;
		}
	}

	public int getEnergyNeededSplit() {
		return energyNeededSplit;
	}

	public void setEnergyNeededSplit(int energyNeededSplit) {
		if(energyNeededSplit < ENERGY_NEEDED_SPLIT_MIN){
			this.energyNeededSplit = ENERGY_NEEDED_SPLIT_MIN;
		} else if (energyNeededSplit > ENERGY_NEEDED_SPLIT_MAX){
			this.energyNeededSplit = ENERGY_NEEDED_SPLIT_MAX;
		} else {
			this.energyNeededSplit = energyNeededSplit;
		}
	}

	public int getTeeth() {
		return teeth;
	}

	public void setTeeth(int teeth) {
		if(teeth < TEETH_MIN){
			this.teeth = TEETH_MIN;
		} else if (teeth > TEETH_MAX){
			this.teeth = TEETH_MAX;
		} else {
			this.teeth = teeth;
		}
		
		//check if is carnivore
		if(this.teeth >= TEETH_NEEDED_CARNIVORE){
			this.setCanEat(new String[]{"nothing", "animal"});
			this.setType("carnivore");
		}
		if(this.teeth <= TEETH_NEEDED_CARNIVORE){
			this.setCanEat(new String[]{"nothing", "plant"});
			this.setType("animal");
		}
	}
	
	public String toString(){
		String out = "";
		
		out += "Energy: " + getEnergy();
		out += "\nAge: " + getAge();
		//out += "\nType: " + getType();
		out += "\n-------------------";
		out += "\nEnergy per turn:" + getEnergyPerTurn();
		out += "\nEnergy needed move:" + getEnergyNeededMove();
		out += "\nEnergy used move:" + getEnergyUsedMove();
		out += "\nEnergy split: " + getEnergyNeededSplit();
		out += "\nEnergy of child:" + getEnergyStart();
		out += "\nTeeth: " + getTeeth();
		
		return out;
	}
}
