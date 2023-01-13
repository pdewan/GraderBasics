package gradingTools.logs.bulkLogProcessing.tools;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;

public class AssignmentLogIterator implements Iterator<File>{

	private static final String replacePattern = "###";
	public static final String fineGrainedPattern =  ".*Assignment"+replacePattern+"SuiteFineGrained\\.csv",
							   nonFineGrainedPattern=".*Assignment"+replacePattern+"Suite\\.csv";
	
	
	private File nextFile;
	
	private Iterator<File> fileIterator;
	private final String logPattern;
	
	public AssignmentLogIterator(File logLocation, String assignmentNumber){
		this(logLocation,nonFineGrainedPattern,assignmentNumber);
	}
	
	public AssignmentLogIterator(File logLocation, String pattern, String assignmentNumber){
		logPattern=pattern.replace(replacePattern, assignmentNumber);
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
