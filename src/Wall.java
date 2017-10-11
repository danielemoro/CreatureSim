import java.awt.Color;

public class Wall extends Cell{
	
	public Wall(Board board, int x, int y) {
		super(board, x, y);
		
		this.setColor(Color.DARK_GRAY);
		this.setType("wall");
	}
	

}
