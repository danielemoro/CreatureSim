import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GUIboard extends JPanel {
	private JPanel gridPanel;
	private JPanel infoPanel;
	private JButton[] buttonList;
	private Board board;
	private int selectedIndex;

	/**
	 * main panel constructor
	 */
	public GUIboard(Board board) {
		this.board = board;
		this.setLayout(new BorderLayout());
		
		gridPanel = new JPanel(new GridLayout(board.getHeigth(), board.getWidth()));
		infoPanel = new JPanel();
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		buttonList = new JButton[board.getHeigth()*board.getWidth()];

		createGridPanel(board);
		// createDisplayList();
	}

	/**
	 * Where the simulation is shown
	 * @param board
	 */
	public void createGridPanel(Board board) {
		this.board = board;
		
		if(gridPanel.isVisible()){
			gridPanel.removeAll();
		}
		
		int row = board.getHeigth();
		int col = board.getWidth();

		for (int r = 0; r < row; r++) {
			for (int c = 0; c < col; c++) {
				JButton newButton = new JButton();
				newButton.setFont(new Font("Arial", Font.BOLD, 50));
				newButton.setBackground(board.getCell(c, r).getColor());
				newButton.addActionListener(new ButtonListener());
				
				buttonList[r*col+c] = newButton;
				gridPanel.add(newButton);
			}
		}

		this.add(gridPanel, BorderLayout.CENTER);
		this.revalidate();

	}
	
	public void showSelectedInfo(){
		createInfoPanel(board.getCell(selectedIndex));
	}
	
	private void createInfoPanel(Cell cell){
		infoPanel.removeAll();
		
		JLabel title = new JLabel(cell.getType());
		title.setFont(new Font("Arial", Font.BOLD, 100));
		
//		JLabel ene = new JLabel("Energy: " + Float.toString(cell.getEnergy()));
//		ene.setFont(new Font("Arial", Font.BOLD, 40));
//		JLabel age = new JLabel("Age: " + Integer.toString(cell.getAge()));
//		age.setFont(new Font("Arial", Font.BOLD, 40));
//		if(cell.getType().equals("plant")){
//		Plant plant = (Plant) cell;
//		info += ("<html>Start Energy: " + plant.ENERGY_AT_START + "<br>");
//		info += ("<html>Turn Gain: " + plant.ENERGY_GAINED_PER_TURN + "<br>");
//		info += ("<html>Needed To Grow: " + plant.ENERGY_NEEDED_TO_GROW + "</html>");
//	}
//	if(cell.getType().equals("animal") || cell.getType().equals("carnivore")){
//		Animal animal = (Animal) cell;
//		info += ("<html>Turn Cost: " + animal.ENERGY_LOST_PER_TURN + "<br>");
//		info += ("Teeth: " + animal.NUM_OF_TEETH + "<br>");
//		info += ("Needed To Move: " + animal.ENERGY_NEEDED_TO_MOVE + "<br>");
//		info+= ("Move Cost: " + animal.ENERGY_USED_TO_MOVE + "<br>");
//		info += ("Split Cost: " + animal.ENERGY_NEEDED_TO_SPLIT + "<br>");
//		info += ("Start Energy: " + animal.ENERGY_AT_START + "</html>");
//	}
		String info = cell.toString();
		info = "<html>" + info;
		info = info.replace("\n", "<br>");
		//info = info.replace(",", "<br>");
		//info = info.replace("=", ":     ");
		info = info + "</html>";
		
		JLabel infoLabel = new JLabel(info);
		infoLabel.setFont(new Font("Arial", Font.PLAIN, 40));
		
		infoPanel.add(title);
		infoPanel.add(infoLabel);
		
		this.add(infoPanel, BorderLayout.EAST);
		this.revalidate();
	}

	/**
	 * Listens for New Game button click and replaces the existing panel with a
	 * fresh game panel.
	 */
	private class ButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// find what button was pressed
			for (int i = 0; i < buttonList.length; i++) {
				if (e.getSource() == buttonList[i]) {
					createInfoPanel(board.getCell(i));
				}
			}
		}
	}
	
	
}
