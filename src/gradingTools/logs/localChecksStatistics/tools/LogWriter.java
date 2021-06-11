package gradingTools.logs.localChecksStatistics.tools;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class LogWriter {

	public static void writeToFile(FileWriter writeTo,String[]... dataToWrite) throws IOException{
		for(String [] data:dataToWrite)
			for(String dataPoint:data)
				writeTo.write(dataPoint+",");
		writeTo.write("\n");
	}
	
	public static void writeToFile(FileWriter writeTo,List<String> dataToWrite) throws IOException{
		for(String dataPoint:dataToWrite)
			writeTo.write(dataPoint+",");
		writeTo.write("\n");
	}

	public static void simpleWrite(FileWriter writeTo,List<String> dataToWrite) throws IOException {
		for(String dataPoint:dataToWrite)
			writeTo.write(dataPoint);
	}
	
	public static void writeToFileMultipleLines(FileWriter writeTo,String[]... dataToWrite) throws IOException{
		for(String [] data:dataToWrite){
			for(String dataPoint:data)
				writeTo.write(dataPoint+",");
			writeTo.write("\n");
		}		
	}

}
