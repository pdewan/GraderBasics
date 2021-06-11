package gradingTools.logs.localChecksStatistics.tools;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;

public class AssignmentLogIterator implements Iterator<File>{

	private File nextFile;
	
	private Iterator<File> fileIterator;
	private String logPattern;
	
	public AssignmentLogIterator(File logLocation, String assignmentNumber){
		logPattern=".*Assignment"+assignmentNumber+"Suite\\.csv";
		fileIterator=Arrays.stream(logLocation.listFiles(File::isDirectory)).iterator();
		nextFile=findNext();
	}
	
	@Override
	public boolean hasNext() {
		return nextFile!=null;
	}

	@Override
	public File next() {
		File returnVal=nextFile;
		nextFile=findNext();
		
		return returnVal;
	}
	
	private File findNext(){
		while(fileIterator.hasNext()){
			File [] logFiles=fileIterator.next().listFiles();
			for(File logFile:logFiles){
				if(logFile.length()==0)
					continue;
				String lfs=logFile.toString();
				if(lfs.matches(logPattern))
					return logFile;
			}
		}
		return null;
	}

}
