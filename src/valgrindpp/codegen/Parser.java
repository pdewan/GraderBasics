package valgrindpp.codegen;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Scanner;

import util.trace.Tracer;

public class Parser {
	private String relativeFilename;
	private String fileName;
	
	public Parser(String aFileName, String aRelativeFileName) {
		fileName = aFileName;
		relativeFilename = aRelativeFileName;
	}
	
	public Wrapper parse() throws Exception {

		InputStream stream = Parser.class.getResourceAsStream("/" + relativeFilename);
		if (stream == null) {
			stream = new FileInputStream(fileName);
			Tracer.info(this, "Parsing stream from absolute file name:" + fileName );
		} else {
			Tracer.info(this, "Parsing resurce stream from relaitve file name:" + "/" + relativeFilename );
		}


		
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
		Tracer.info(this, "Added imports:" + wrapper.imports);
		Tracer.info(this, "Added functions:" + wrapper.functions);

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
