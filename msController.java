package app;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;


public class msController {
	@FXML Label remainingBombs;
	@FXML Label time;
	@FXML Button reset;
	@FXML AnchorPane boardfx;
	@FXML GridPane griden;
	@FXML Label message;
	
	
	private board board; 
	//private boolean begun = false;
	//Timer timeline;
	
	
	@FXML void initialize() {
		reset();
		//timeline = new Timer();
	}
	
	@FXML void load() {
		countTime();
	}
	
	
	@FXML void reset(){
		board = new board(griden.getColumnCount(), griden.getRowCount());
		removeOldButtons();
		addNewButtons();
		checkRemainingBombs();
		resetTime();
		
	}
	
	private void removeOldButtons() {
		griden.getChildren().clear();
	}
	
	
	private void addNewButtons() {
		int id = 0;
		for (int i = 0; i < griden.getRowCount(); i++) {
			for (int j = 0; j < griden.getColumnCount(); j++) {
				Button b = new Button();
				b.setMaxWidth(25.0);
				b.setMaxHeight(25.0);
				b.setStyle("-fx-font-size:12; -fx-background-color: #AAAAAA");
				b.setText("");
				b.setOnMouseClicked(e -> handleClick(e));
				b.setId(Integer.toString(id));
				id++;
				griden.add(b, j, i);
			}
		}
	}
	
	
	private void checkRemainingBombs() {
		int numberOfRemainingBombs = board.getSumOfBombs();
		for (Node node : griden.getChildren()) {
			if (node.getId() != null) {
				if (board.getTile(Integer.parseInt(node.getId())).isFlagged()) {
					numberOfRemainingBombs--;
				}
			}
		}
		remainingBombs.setText(Integer.toString(numberOfRemainingBombs));
	}


	private void resetTime() {
		//timeline = new Timer();
		//this.begun = false;
		//countTime();
		time.setText(Double.toString(0.0));
	}
	
	
	@FXML void handleClick(MouseEvent e) {
		if (reset.getText() != ":)") {
			reset.setTextFill(Color.rgb(229, 15, 15));
			reset.setText(":)");
		}
		if (message.getText() != "You can do it!") {
			message.setTextFill(Color.rgb(229, 15, 15));
			message.setText("You can do it!");
		}
		
		/*if (! this.begun) {
			this.begun = true;
			this.timeline.schedule(task, 100);
		}*/
		
		Node source = (Node) e.getSource();
		int id = Integer.parseInt(source.getId());
        int posX = GridPane.getColumnIndex(source);
        int posY = GridPane.getRowIndex(source);
		
		if (e.getButton() == MouseButton.PRIMARY) {
			openTile(posX, posY, id);
		}
		if (e.getButton() == MouseButton.SECONDARY) {
			((Labeled) source).setText(flag(id));	
		}
	}
	
	
	
	private void openTile(int posX, int posY, int id) {
		tile thisTile = board.getTile(id);
		if(thisTile.isClosed()) {
			thisTile.open();
			
			String text;
			Color color = null;
			
			if (thisTile.isBomb()) {
				text = "B";
				color = Color.RED;
				disableButton(id);
				changeTile(text, posX, posY, color);
				defeat();
			}
			else {
				int numberOfNearbyBombs = board.nearbyBombs(posX, posY, griden.getColumnCount(), griden.getRowCount());
				if (numberOfNearbyBombs == 0) {
					text = "";
					//removeButton(id);
					openNearbyTiles(posX, posY);
				}
				else {
					text = Integer.toString(numberOfNearbyBombs);
					color = chooseColor(numberOfNearbyBombs);
				}
				
				disableButton(id);
				changeTile(text, posX, posY, color);
				
				if (board.checkVictory()) {
					victory();
				}
			}
		}
	}
	
	
	private void disableButton(int id) {
		for (Node node : griden.getChildren()) {
			if (node.getId() != null) {
				if (Integer.parseInt(node.getId()) == id) {
					node.setDisable(true);
				}
			}
		}
	}
	

	private Color chooseColor(int number) {
		Color color;
		ArrayList<Color> colors = new ArrayList<Color>(Arrays.asList(Color.BLUE, Color.GREEN, Color.RED, 
				Color.DARKBLUE, Color.DARKRED, Color.CYAN, Color.YELLOW, Color.BLACK));
		
		color = colors.get(number - 1);
		return color;
	}
	
	
	private void changeTile(String text, int posX, int  posY, Color color) {
		Label label = new Label();
		label.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 12));
		label.setTextFill(color);
		label.setMaxWidth(25.0);
		label.setMaxHeight(25.0);
		label.setTextAlignment(TextAlignment.CENTER); 
		label.setText(text);
		griden.add(label, posX, posY);
	}
	
	
	private void openNearbyTiles(int posX, int posY) {
		for (int y = -1; y < 2; y++) {
			for (int x = -1; x < 2; x++) {
				int openPosX = posX + x;
				int openPosY = posY + y;
				if (isValidTile(openPosX, openPosY) && !(posX == openPosX && posY == openPosY)){
					openTile(openPosX, openPosY, posToIndex(openPosX, openPosY));
				}
			}
		}
	}
	
	
	private boolean isValidTile(int posX, int posY) {
		if (posX < 0 || posX >= griden.getColumnCount()) {
			return false;
		}
		if (posY < 0 || posY >= griden.getRowCount()) {
			return false;
		}
		return true;
	}
	
	
	
	int posToIndex(int posX, int posY) {
		int index = (griden.getColumnCount() * posY) + posX;
		return index;
	}
	
	
	private String flag(int id) {
		board.getTile(id).flag();
		checkRemainingBombs();
		if (board.getTile(id).isFlagged()) {
			return "*";
		}
		else {
			return "";
		}
	}
	
	
	private void count() {
		double newTime = Double.parseDouble(time.getText()) + 0.1;
		System.out.println(Double.toString(newTime));
		time.setText(Double.toString(newTime));
	};
	
	void countTime() {
		System.out.println("kjørt");
		while (true) {
			try {
				wait(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			count();
		}
	}
	
	
	/*ArrayList<Integer> indexToPos(int index) {
		ArrayList<Integer> coordinates = new ArrayList<Integer>(); 
		System.out.println("\n\n" + index);
		System.out.println(griden.getColumnCount());
		
		int posY = Math.floorDiv(index, (griden.getColumnCount() -1));
		int posX = index % griden.getColumnCount();
		
		//System.out.println(posX);
		//System.out.println(posY);
		
		coordinates.add(posX);
		coordinates.add(posY);
		return coordinates;
	}
	*/
	
	//ObservableList<Node> liste = griden.getChildren();
	
	
	
	private void defeat() {
		String newText = "You hit a bomb! :(";
		message.setTextFill(Color.BLACK);
		message.setText(newText);
		reset.setText(":(");
		reset();
	}
	
	private void victory() {
		String newText = "You made it! :)";
		message.setText(newText);
		reset.setTextFill(Color.GOLD);
		reset.setText(":D");
		reset();
	}
	
	
	//Timer timer = new Timer(true);
	//timer.schedule(new countTime(), 0, 5000);
	
	
	
}


/* class countTime extends TimerTask {
	@FXML Label time;
	
	public void run() {
		double lastTime = Integer.parseInt(time.getText());
		time.setText(Double.toString(lastTime + 0.1));
	}
}
*/
