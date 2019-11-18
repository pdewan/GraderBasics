package gradingTools.logs;

import java.util.Date;

public class DatedDifficultyContext implements Comparable<DatedDifficultyContext> {
	public Date date;
	String difficultyContext;
	StringBuffer preLocalChecksContext = new StringBuffer();
	StringBuffer postLocalChecksContext = new StringBuffer();

	StringBuffer preMetricsContext = new StringBuffer();
	StringBuffer postMetricsContext = new StringBuffer();

	boolean hasExpanation;
	
	
	@Override
	public int compareTo(DatedDifficultyContext o) {
		// TODO Auto-generated method stub
		return date.compareTo(o.date);
	}

}
