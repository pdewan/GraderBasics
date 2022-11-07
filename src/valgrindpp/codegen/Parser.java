package valgrindpp.codegen;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Scanner;

public class Parser {
	private String filename;
	
	public Parser(String filename) {
		this.filename = filename;
	}
	
	public Wrapper parse() throws Exception {
//		String aSingleSlashName = filename.replaceAll("//", "/");
//		InputStream stream = Parser.class.getResourceAsStream("/"+filename);
//		if (stream == null) {
//		
//		 stream = Parser.class.getResourceAsStream("/home/"+filename);
//		}
		
//		InputStream stream = Parser.class.getResourceAsStream(filename);
		
		InputStream stream = new FileInputStream(filename);
//		InputStream stream = new FileInputStream(aSingleSlashName);

		
		Scanner scanner = new Scanner(stream);
		
		Wrapper wrapper = new Wrapper();
		
		while(scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if(line.startsWith("//") || isBlank(line)) continue;
			
			if(line.startsWith("#include")) {
				wrapper.imports.add(line);
			} else {
				wrapper.functions.add(new Function(line));
			}
		}
		
		scanner.close();
		
		return wrapper;
	}
	
	private boolean isBlank(String string) {
		for(char c: string.toCharArray()) {
			if(!Character.isWhitespace(c)) return false;
		}
		
		return true;
	}
}
