package gradingTools.logs.bulkLogProcessing.tools;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class AssignmentLogIterator implements Iterator<Pairing<String, File>>{

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
		logLocation = logLocation.getParentFile(); // just trying to see what Anna's iterator does
		String aName = logLocation.getName();		
		logPattern=pattern.replace(replacePattern, assignmentNumber);
		if (aName.contains("ssignment")) {
			fileIterator=Arrays.stream(logLocation.listFiles()).iterator();

		} else {
			File[] aListFiles = logLocation.listFiles(File::isDirectory);
			Stream<File> aStream = Arrays.stream(aListFiles);
			fileIterator = aStream.iterator();
//		fileIterator=Arrays.stream(logLocation.listFiles(File::isDirectory)).iterator();
		}
		nextFile=findNext();
	}
	
	@Override
	public boolean hasNext() {
		return nextFile!=null;
	}

	@Override
	public Pairing<String, File> next() {
		File returnVal=nextFile;
		nextFile=findNext();
		String studentName = determineStudentName(returnVal);
		
		return new Pairing<>(studentName,returnVal);
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

	public static String determineStudentName(File log){
		Matcher studentID=Pattern.compile(".*\\\\(.*)\\\\.*").matcher(log.toString());
		if(studentID.find())
			return "\""+studentID.group(1)+"\"";
		else
			return "I am error";
	}
	
}
