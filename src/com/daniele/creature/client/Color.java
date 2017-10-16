package com.daniele.creature.client;

public class Color {

	private int r;
	private int g;
	private int b;
	
	public static Color WHITE = new Color(255,255,255);
	public static Color RED = new Color(255,0,0);
	public static Color YELLOW = new Color(255,255,0);
	public static Color GREEN = new Color(0,255,0);
	public static Color GRAY = new Color(104,104,104);
	
	Color (int red, int green, int blue) {
		r = red;
		g = green;
		b = blue;
	}
	
	Color (float red, float green, float blue) {
		r = (int) (red*255);
		g = (int) (green*255);
		b = (int) (blue*255);
	}


	public int getR() {
		return r;
	}


	public void setR(int r) {
		this.r = r;
	}


	public int getG() {
		return g;
	}


	public void setG(int g) {
		this.g = g;
	}


	public int getB() {
		return b;
	}


	public void setB(int b) {
		this.b = b;
	}
	

	
}
