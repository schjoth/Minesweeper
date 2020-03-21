package application;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class stopWatch implements Runnable {
	@FXML Label time;
	
	private boolean paused = false;
	private Thread stopWatchThread = null;
	private msController game;
	private int time1 = 0;
	
	public stopWatch(msController msController) {
		game = msController;
		start();
		stopWatchThread.start();
	}

	public void start() {
		if (stopWatchThread == null) {
			stopWatchThread = new Thread(this);
		}
	}	
	
	public void Pause() {
		this.paused = true;
	}
	
	public void run() {
		 while (true) { 
	         try {
	        	Thread.sleep(1000);
	        	
	            if (paused) {
	               synchronized(this) {
	                  while (paused) {
	                     wait();
	                  }
	               }
	            }
	         } catch (InterruptedException e) {System.err.println("Clock thread interrupted");}
	         Count();
	      }
	}
	
	private void Count() {
		if (time1 < 999) {
			time1 += 1;
		}
		game.updateTime(time1);
	}
	
	public void stop() {
		stopWatchThread = null;
	}
}

