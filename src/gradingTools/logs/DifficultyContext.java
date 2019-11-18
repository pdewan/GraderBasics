package gradingTools.logs;

import java.util.Date;

public class DifficultyContext implements Comparable<DifficultyContext> {
	public Date date;
	String difficultyContext;
	StringBuffer preLocalChecksContext = new StringBuffer();
	StringBuffer postLocalChecksContext = new StringBuffer();
	StringBuffer preMetricsContext = new StringBuffer();
	StringBuffer postMetricsContext = new StringBuffer();
	static final String METRICS_HEADER = "Id,Prediction,Date,File,StartTime,ElapsedTime,BusyTime,Commands,editOrInsertPercentage,debugPercentage,navigationPercentage,focusPercentage,removePercentage,editPercentage,COMMAND_RATE,NAVIGATION_RATE,DEBUG_RATE,FOCUS_RATE,EDIT_RATE,REMOVE_RATE,INSERT_RATE,webLinkTimes,Web Page Visits"; 
			
	static final String LOCAL_CHECKS_HEADER = "#,Time,%Passes,Change,Test,Pass,Partial,Fail,Untested";

	boolean hasExpanation;
	
	public DifficultyContext() {
		preMetricsContext.append(METRICS_HEADER);
		preLocalChecksContext.append(LOCAL_CHECKS_HEADER);		
	}

	public String toString() {
		return
				"-------------------------------------------------------------\n"
				+ difficultyContext + "\n"
				+ preMetricsContext + "\n"
				+ postMetricsContext + "\n\n"
				+ preLocalChecksContext + "\n"
				+ postLocalChecksContext + "\n";
		}

	
	
	@Override
	public int compareTo(DifficultyContext o) {
		// TODO Auto-generated method stub
		return date.compareTo(o.date);
	}
	
}
