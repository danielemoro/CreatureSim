package com.daniele.creature.client;

public class Wall extends Cell{
	
	public Wall(Board board, int x, int y) {
		super(board, x, y);
		
		this.setColor(Color.GRAY);
		this.setType("wall");
	}
	

}
