package application;

import java.util.ArrayList;
import java.util.Arrays;

class board {
	private ArrayList<tile> tiles = new ArrayList<tile>();
	private int chance = 15; // percent for a tile to be a bomb
	private int sumOfBombs;
	
	public board(int columnCount, int rowCount) {
		for (int i = 0; i < rowCount*columnCount; i++) {
			tiles.add(new tile(chance));
		}
		sumOfBombs = checkSumOfBombs();
	}
	
	public board(String loadedBoard) {
		ArrayList<String> tilesString = new ArrayList<String>(Arrays.asList(loadedBoard.split("_")));
		
		for (int i = 0; i < tilesString.size(); i++) {
			ArrayList<String> tile = new ArrayList<String>(Arrays.asList(tilesString.get(i).split("-")));
			
			if (tile.get(0).equals("1")) {
				tiles.add(new tile(200));
			}
			else {
				tiles.add(new tile(-1));
			}
			if(tile.get(1).equals("0")) {
				tiles.get(i).open();
			}
			if(tile.get(2).equals("1")) {
				tiles.get(i).flag();
			}
		}
		sumOfBombs = checkSumOfBombs();
	}
	
	public tile getTile(int index) {
		return tiles.get(index);
	}
	
	
	int nearbyBombs(int posX, int posY, int maxX, int maxY) {
		int numberOfBombs = 0;
		for (int y = -1; y < 2; y++) {
			for (int x = -1; x < 2; x++) {
				int checkPosX = posX + x;
				int checkPosY = posY + y;
				if ((checkPosX < maxX && checkPosX >= 0) && (checkPosY < maxY && checkPosY >= 0)) {
					if (getTile((maxX * checkPosY) + checkPosX).isBomb()) {
						numberOfBombs++;
					}
				}	
			}
		}
		return numberOfBombs;
	}
	
	
	boolean checkVictory() {
		for (tile tile : tiles) {
			if(tile.isClosed() && ! tile.isBomb()) {
				return false;
			}
		}
		return true;
	}
	
	
	private int checkSumOfBombs() {
		int bombs = 0;
		for (tile tile : tiles) {
			if (tile.isBomb()) {
				bombs++;
			}
		}
		return bombs;
	}
	
	int getSumOfBombs() {
		return this.sumOfBombs;
	}
	
	ArrayList<tile> getBoard(){
		return tiles;
	}
}
