package gradingTools.logs.localChecksStatistics.dataStorage;

public class StudentTestData {

	private final double timeTaken;
	private final double timesWorkedOn;
	private final double breaksTaken;
	private final boolean completed;
	private final String testName;
	
	
	
	public StudentTestData(String name, double d, double f, double e){
		testName=name;
		timesWorkedOn=d;
		breaksTaken=e;
		timeTaken=f;
		completed=f>=0;
		
	}
	
	public long getTimeTaken(){
		return (long)timeTaken;
	}
	
	public int getTimesWorkedOn(){
		return (int)timesWorkedOn;
	}
	
	public int getBreaksTaken(){
		return (int)breaksTaken;
	}
	
	public String getTestName(){
		return testName;
	}
	
	public boolean isCompleted(){
		return completed;
	}
	
	
}
