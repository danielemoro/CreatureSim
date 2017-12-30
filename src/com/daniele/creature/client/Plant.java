package com.daniele.creature.client;
import java.util.Random;

public class Plant extends Cell{
	
	//PLANT PROPERTIES
	private int energyStart;
	private int energyPerTurn;
		protected final int ENERGY_PER_TURN_MIN = 0;
		protected final int ENERGY_PER_TURN_MAX = 10;
	private int energyNeededGrow;
		protected final int ENERGY_NEEDED_GROW_MIN = 0;
		protected final int ENERGY_NEEDED_GROW_MAX = Integer.MAX_VALUE;
		
	public Plant(Board board, int x, int y, long turn){
		super(board, x, y, turn);
		
		this.setEnergyStart(100);
		this.setEnergy(this.getEnergyStart());
		this.setEnergyPerTurn(10);
		this.setEnergyNeededGrow(120);
		this.setCanEat(new String[]{"nothing"});
		this.setType("plant");
		this.setColor(calculateColor());
	
		rand = new Random();
	}
	
	private Color calculateColor(){
		float brightness = Math.min(this.getEnergy()/(this.getEnergyStart()*5f),0.6f);
		brightness = Math.abs(brightness);
		brightness = Math.min(brightness, 1f);
		
		Color newColor = new Color(0.1f,1f-brightness,0.1f);
		return newColor;
	}
	
	public void next(long turn){
		if(turn > lastTurn){ //check if the turn has advanced
			lastTurn = turn;
			increaseAge();
			this.isDead();
			
			//update energy based on turn
			this.changeEnergy(this.getEnergyPerTurn());
			
			//grow
			if(this.getEnergy() > this.getEnergyNeededGrow()){			
				if(grow()){
					this.changeEnergy(-this.getEnergyStart());
				}
			}

			this.setColor(calculateColor());
		}		
	}
	
	
	private boolean grow(){
		boolean didGrow = false;
		
		for(int xDelta = -1; xDelta < 2; xDelta++){
			for(int yDelta = -1; yDelta < 2; yDelta++){
				int currX = this.getX() + xDelta;
				int currY = this.getY() + yDelta;
				if(this.canIEat(this.getCanEat(), board.getCell(currX, currY).getType())){					
					Plant child = new Plant(board, currX, currY, lastTurn);
					child.setEnergy(this.getEnergyStart());
					child.setEnergyStart(this.getEnergyStart() + (rand.nextInt(51)-25));
					child.setEnergyPerTurn(this.getEnergyPerTurn() + (rand.nextInt(51)-25));
					child.setEnergyNeededGrow(this.getEnergyNeededGrow() + (rand.nextInt(51)-25));
					
					board.setCell(child.getX(), child.getY(), child);
					
					didGrow = true;
				}
			}
		}
		
		return didGrow;
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

	public int getEnergyNeededGrow() {
		return energyNeededGrow;
	}

	public void setEnergyNeededGrow(int energyNeededGrow) {
		if(energyNeededGrow < ENERGY_NEEDED_GROW_MIN){
			this.energyNeededGrow = ENERGY_NEEDED_GROW_MIN;
		} else if (energyNeededGrow > ENERGY_NEEDED_GROW_MAX){
			this.energyNeededGrow = ENERGY_NEEDED_GROW_MAX;
		} else {
			this.energyNeededGrow = energyNeededGrow;
		}
	}
	
	public String toString(){
		String out = "";
		
		out += "Energy: " + getEnergy();
		out += "\nAge: " + getAge();
		//out += "\nType: " + getType();
		out += "\n-------------------";
		out += "\nEnergy per turn:" + getEnergyPerTurn();
		out += "\nEnergy grow:" + getEnergyNeededGrow();
		out += "\nEnergy of child:" + getEnergyStart();
		
		return out;
	}
	
	public int[] getDNA(){
		int dna[] = {getEnergy(), getAge(), getEnergyPerTurn(), getEnergyNeededGrow(), getEnergyStart()};
		return dna;
	}
	
	public static String[] getDNALabel(){
		return new String[]{"Energy", "Age", "Energy per turn", "Energy grow", "Energy of child"};
	}
	
	public static int[] getDNAEmpty(){
		return new int[]{0, 0, 0, 0, 0};
	}
	

}
