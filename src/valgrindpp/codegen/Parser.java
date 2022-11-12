package valgrindpp.codegen;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Scanner;

public class Parser {
	private String relativeFilename;
	private String fileName;
	
	public Parser(String aFileName, String aRelativeFileName) {
		fileName = aFileName;
		relativeFilename = aRelativeFileName;
	}
	
	public Wrapper parse() throws Exception {
//		String aSingleSlashName = relativeFilename.replaceAll("//", "/");
//		InputStream stream = Parser.class.getResourceAsStream("/"+relativeFilename);
//		if (stream == null) {
//		
//		 stream = Parser.class.getResourceAsStream("/home/"+relativeFilename);
//		}
		InputStream stream = Parser.class.getResourceAsStream("/" + relativeFilename);
		if (stream == null) {
			stream = new FileInputStream(fileName);
		}

//		InputStream stream = Parser.class.getResourceAsStream(relativeFilename);
		
//		InputStream stream = new FileInputStream(fileName);
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
