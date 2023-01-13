package gradingTools.logs.bulkLogProcessing.collectors;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Date;

import analyzer.logAnalyzer.AnIntervalReplayer;
import grader.basics.interval.IntervalDriver;

public abstract class AbstractIntervalReplayerBasedCollector extends AbstractCollector {

	public static final String numberReplace = "###";

	private final String unfixedPathToStudents;
	private String fixedPathToStudents;
	protected File studentProject;

	protected long startTime=-1,testStartTime=-1,lastTestTime=-1,currentTestTime=-1;
	protected final AnIntervalReplayer replayer;
	
	public AbstractIntervalReplayerBasedCollector(File semesterFolderLocation, String pathToStudents) throws Exception {	
		if(!semesterFolderLocation.exists()) 
			throw new FileNotFoundException();
		if(semesterFolderLocation.isFile())
			throw new Exception("Location must be a directory");
		if(semesterFolderLocation.getAbsolutePath().contains(numberReplace))
			throw new Exception("Directory path must not contain the sequence: "+numberReplace);
		
		reqPass=1;
		replayer = new AnIntervalReplayer(IntervalDriver.MULTIPLIER, IntervalDriver.DEFAULT_THRESHOLD, IntervalDriver.TRACE);
		unfixedPathToStudents=semesterFolderLocation.getAbsolutePath()+"/"+pathToStudents;
	}
	
	@Override
	public void logData(String[] data) throws IllegalArgumentException{
		try {
			if(testStartTime==-1) {
				testStartTime = parseDate(data[TIME_TESTED_INDEX]).getTime();
				startTime = replayer.getStartTime(studentProject);
				lastTestTime=startTime;
				currentTestTime=testStartTime;
			}else {
				lastTestTime = currentTestTime;
				currentTestTime = parseDate(data[TIME_TESTED_INDEX]).getTime();
			}
		}catch(Exception e) {
			throw new IllegalArgumentException("Error when parsing date");
		}
	}
	
	
	
	protected Date monthEarlier(Date init) {
		Calendar c = Calendar.getInstance();
		c.setTime(init);
		c.set(Calendar.MONTH, (c.get(Calendar.MONTH)+11)%12);
		return c.getTime();
	}
	
	
	@Override
	public void reset() {
		super.reset();
		startTime=-1;
		testStartTime=-1;
		lastTestTime=-1;
		currentTestTime=-1;
		studentProject=null;
	}
	
	@Override
	public void setStudentName(String name) {
		super.setStudentName(name);
		if(name.matches("\".*\"")) 
			name = name.replaceAll("\"", "");
		File studentSubmission = new File(fixedPathToStudents+"/"+name+"/Submission attachment(s)");
		try {
			studentProject = findLogsFolder(studentSubmission);		
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private File findLogsFolder(File studentSubmission) throws Exception {
		File [] subdirs = studentSubmission.listFiles(File::isDirectory);
		if(subdirs.length == 0)
			return null;
		
		//Sees if a subdirectory is the Logs folder
		for(File subdir:subdirs) 
			if(subdir.getName().equals("Logs"))
				return studentSubmission;
		
		//If not looks deeper
		for(File subdir:subdirs) {
			File file = findLogsFolder(subdir);
			if(file!=null)
				return file;
		}
		return null;
	}
	
	@Override
	public void setAssignmentNumber(String assignmentNum) {
		super.setAssignmentNumber(assignmentNum);
		fixedPathToStudents = unfixedPathToStudents.replaceAll(numberReplace, assignmentNum);
	}

	@Override
	public boolean requiresStudentName() {
		return true;
	}
	
	@Override
	public boolean requiresAssignmentNum() {
		return true;
	}
	
	@Override
	public boolean requiresTestNames() {
		return false;
	}
}
