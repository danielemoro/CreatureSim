public class Board {
	private Cell[][] board;
	private int width;
	private int heigth;
	
	public Board(int width, int heigth){
		this.width = width;
		this.heigth = heigth;
		board = new Cell[width][heigth];
		
		//fill in board with empty cells
		for(int col = 0; col < width; col++){
			for(int row = 0; row < heigth; row++){
				board[col][row] = new Cell(this, col, row);
			}
		}
	}
	
	public Cell[][] getBoard(){
		return board;
	}
	
	public void setCell(int x, int y, Cell cell){
		if(x >= width || x < 0 || y >= heigth || y < 0){
			return;
		}
		
		board[x][y] = cell;
	}
	
	public void setCell(int i, Cell cell){
		board[i % width][i / heigth] = cell;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeigth(){
		return heigth;
	}
	
	public Cell getCell(int i){
		if(i > width*heigth)
			return new Wall(this, width, heigth);
		
		return board[i % width][i / heigth];
	}
	
	public void killCell(Cell cell){
		this.setCell(cell.getX(), cell.getY(), new Cell(this, cell.getX(), cell.getY()));
	}
	
	public Cell getCell(int x, int y){
		if(x >= width || x < 0 || y >= heigth || y < 0){
			return new Wall(this, x, y);
		} else {			
			return board[x][y];
		}
		
	}
	
	public int getNumCells(){
		return width*heigth;
	}
	
	public String toString(){
		String out = "-----------------------\n";
		for(int row = 0; row < heigth; row++){
			for(int col = 0; col < width; col++){
				out += (board[col][row].getType().substring(0, 1));
			}
			out += ("\n");
		}
		out += "-----------------------\n";
		
		return out;
	}
	
	public void move(int origX, int origY, int destX, int destY){
		//System.out.println("moving: " + board[origX][origY].getType() + " to " + board[destX][destY].getType());
		board[destX][destY] = board[origX][origY];
		board[origX][origY] = new Cell(this, origX, origY);
	}
	
	public Board clone(){
		Board newBoard = new Board(width, heigth);
		for(int row = 0; row < heigth; row++){
			for(int col = 0; col < width; col++){
				newBoard.setCell(col, row, board[col][row]);
			}
		}
		return newBoard;
	}
}
