package application;

import java.util.Random;

class tile {
	private boolean isBomb, closed = true, flagged = false;
	//private String text;
	
	public tile (int chance) {
		randomState(chance);
	}

	private void randomState(int chance) {
		int number = new Random().nextInt(101);
		if (number <= chance) {
			this.isBomb = true;
		}
		else {
			this.isBomb = false;
		}
		//System.out.println(number + " <= " + chance + " = " + (number <= chance));
	}

	public boolean isBomb() {
		return isBomb;
	}
	
	public boolean isClosed() {
		return closed;
	}
	
	public void open() {
		this.closed = false;
	}
	
	public void close() {
		this.closed = true;
	}
	
	public void flag() {
		this.flagged = ! this.flagged;
	}
	
	public boolean isFlagged() {
		return this.flagged;
	}
	
//	public void setText(String text) {
//		if ( ! this.opened) {
//			this.opened = true;
//			this.text = text;
//		}
//	}
	
	@Override
	public String toString() {
		String text = "";
		if (isBomb) {
			text += "1";
		}
		else {
			text += "0";
		}
		text += "-";
		if(closed) {
			text += "1";
		}
		else {
			text += "0";
		}
		text += "-";
		if (flagged) {
			text += "1";
		}
		else {
			text += "0";
		}
		
		
		return text;
	}
}
