package gradingTools.logs.localChecksStatistics.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CSVParser {

	private final InputStream input;
	private char current;
	private StringBuilder currentSpelling;
	private List<List<String>> output;
	private int rowNumber=0;
	private boolean eof=false;
	
	public CSVParser(File csvFile) throws Exception {
		input = new FileInputStream(csvFile);
		output=new ArrayList<List<String>>();
		output.add(new ArrayList<String>());
		currentSpelling=new StringBuilder();
		nextChar();
	}
	
	public String [][] readCSV() throws Exception{
		 scan();
		 input.close();
		 
		 if(output.get(output.size()-1).size()==0) 
			 output.remove(output.size()-1);
		 
		 
		 String [][] retval = new String[output.size()][];
		 
		 for(int i=0;i<output.size();i++){
			 String [] row = new String[output.get(i).size()];
			 output.get(i).toArray(row);
			 retval[i]=row;
		 }
		 
		return retval;
	}
	
	private void acceptToQuotes() throws Exception {
		if(current=='"') {
			nextChar();
			for(;;) {
				if(eof)
					throw new Exception("Error with quotations in file");
				if(current=='"') {
					nextChar();
					if(current=='"')
						take();
					else
						break;
				}else 
					take();
			}
		}
	}
	
	private void scan() throws Exception {
		for(;;) {
			if(eof)
				return;
			if(current=='"')
				acceptToQuotes();
			else if(current==',') 
				addToOutput();
			else if(current=='\n') {
				addToOutput();
				output.add(new ArrayList<String>());
//				System.out.println();
				rowNumber++;
			}else 
				take();
			
		}
	}
	
	private void addToOutput() throws IOException {
		nextChar();
//		System.out.print(currentSpelling.toString()+" ");
		output.get(rowNumber).add(currentSpelling.toString());
		currentSpelling=new StringBuilder();
	}
	
	private void take() throws IOException {
		currentSpelling.append(current);
		nextChar();
	}
	
	private void nextChar() throws IOException {
		int c = input.read();
		current = (char)c;
		if(c==-1)
			eof=true;
	}
	
}
