package gradingTools.fileTree;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReadFile {
	
	public static List<String> readLines(File file) {
		List<String> lines = new ArrayList<String>();
		try {
			Scanner scan = new Scanner(file);
			while(scan.hasNext()) 
				lines.add(scan.nextLine().replaceAll("\r", ""));
			scan.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return lines;
		
	}
	
}
