package gradingTools.logs.localChecksStatistics.collectors.IntervalReplayer;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import analyzer.logAnalyzer.AnIntervalReplayer;

import grader.basics.interval.IntervalDriver;
import gradingTools.logs.localChecksStatistics.collectors.AbstractCollector;

public abstract class AbstractIntervalReplayerBasedCollector extends AbstractCollector {

	public static final String numberReplace = "###";

	protected File studentProject;

	protected long startTime=-1,testStartTime=-1,lastTestTime=-1,currentTestTime=-1;
	protected final AnIntervalReplayer replayer;

	
	public AbstractIntervalReplayerBasedCollector() {
		replayer = new AnIntervalReplayer(IntervalDriver.MULTIPLIER, IntervalDriver.DEFAULT_THRESHOLD, IntervalDriver.TRACE); 
		
	}
	
	public void setStudentProjectLocation(File project) {
		studentProject=project;
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
	public boolean requiresStudentName() {
		return false;
	}
	
	@Override
	public boolean requiresAssignmentNum() {
		return false;
	}
	
	@Override
	public boolean requiresTestNames() {
		return false;
	}
}
