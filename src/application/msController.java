package application;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
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


public class msController implements FileOperations{
	@FXML Label remainingBombs;
	@FXML Label time;
	@FXML Button Save;
	@FXML Button Load;
	@FXML Button reset;
	@FXML AnchorPane boardfx;
	@FXML GridPane griden;
	@FXML Label message;
	
	
	
	private board board; 
	private stopWatch timer = null;
	private boolean timerStarted = false;
	private String filepath; //Path to the file where you want to save game state,
	//will fail if not assigned value

	@FXML void initialize() {
		reset();
	}
	
	@FXML void reset(){
		/*
		 * if (timer != null) {
			timer.stop();
		}
		*/
		board = new board(griden.getColumnCount(), griden.getRowCount());
		removeOldButtons();
		addNewButtons();
		checkRemainingBombs();
		//resetTime();
	}
	
	private void resetTime() {
		timer = new stopWatch(this);
		timerStarted = false;
		time.setText(Double.toString(0.0));
	}
	
	private void resetMessage() {
		if (reset.getText() != ":)") {
			reset.setTextFill(Color.rgb(229, 15, 15));
			reset.setText(":)");
		}
		if (message.getText() != "You can do it!") {
			message.setTextFill(Color.rgb(229, 15, 15));
			message.setText("You can do it!");
		}
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

	
	@FXML void handleClick(MouseEvent e) {
		resetMessage();
		
		/*
		if (! timerStarted) {
			timer.run();
			timerStarted = true;
		}
		*/
		
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
		label.setAlignment(Pos.CENTER);
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

	void updateTime(int number) {
		time.setText(Integer.toString(number));
	}
	
	
	@FXML void save() {
		if(write(filepath, board.getBoard())) {
			message.setText("Game saved");
		}
		else {
			message.setText("Save Failed");
		}
	}
	
	@FXML void load() {
		/*
		 * if (timer != null) {
			timer.stop();
		}
		*/
		String loadedGame = read(filepath);
		if(loadedGame != null) {
			removeOldButtons();
			board = new board(loadedGame);
			addNewButtons();
			updateBoard();
			checkRemainingBombs();
			message.setText("Loaded game without flags");
		}
		else {
			message.setText("Could not load game");
		}
		//resetTime();
	}

	private void updateBoard() {
		ArrayList<tile> tiles = board.getBoard();
		for (int i = 0; i < tiles.size(); i++) {
			if ( ! tiles.get(i).isClosed()) {
				tiles.get(i).close();
				int posY = i / griden.getColumnCount();
				int posX = i % griden.getColumnCount();
				openTile(posX, posY, i);
			}
			/*
			else if (tiles.get(i).isFlagged()) {
				
			}
			*/
		}
	}
}

