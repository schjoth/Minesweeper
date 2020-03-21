package application;

import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.ArrayList;

public interface FileOperations {
	
	public default String read(String filename) {
		String fileContent = "";
		try {
			Scanner file = new Scanner(new FileReader(filename));
			while (file.hasNext()) {
				fileContent += file.next();
			}
			file.close();
		}
		catch(Exception e) {
			fileContent = null;
		}
		return fileContent;
	}
	
	public default boolean write(String filename, ArrayList<tile> board) {
		
		try {
			File thefile = new File(filename);
			PrintWriter file = new PrintWriter(thefile);
			
			for (tile tile : board) {
				int isBomb = 0, closed = 1, flagged = 0;
				if (tile.isBomb()) {
					isBomb = 1;
				}
				if (! tile.isClosed()) {
					closed = 0;
				}
				/*
				if (tile.isFlagged()) {
					flagged = 1;
				}
				*/
				
				String tileFormat = isBomb + "-" + closed + "-" + flagged + "_";
				file.println(tileFormat);
				
				
			}
			
			file.flush();
			file.close();
		} 
		catch (Exception e) {
			return false;
		}
		return true;
	}

}
