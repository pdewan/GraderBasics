package gradingTools.logs.models;

import util.annotations.Position;
import util.annotations.StructurePattern;
import util.annotations.StructurePatternNames;
@StructurePattern(StructurePatternNames.BEAN_PATTERN)
public class ATestMetrics implements TestMetrics {
	String testName = "";
	double attempts = 0;
	double regressions = 0;
	
	double breaks;
	String timeWorked = "";
	public ATestMetrics() {
	
	}
	public ATestMetrics(String testName, double attempts, int breaks, String timeWorked) {
		super();
		this.testName = testName;
		this.attempts = attempts;
		this.breaks = breaks;
		this.timeWorked = timeWorked;
	}
	
	@Override
	public void setAttempts(double attempts) {
		this.attempts = attempts;
	}
	@Override
	@Position(0)
	public String getTestName() {
		return testName;
	}
	@Override
	@Position(1)
	public String getTimeWorked() {
		return timeWorked;
	}
	@Override
	@Position(2)
	public double getAttempts() {
		return attempts;
	}
	@Override
	@Position(3)
	public double getRegressions() {
		return regressions;
	}
	@Override
	@Position(4)
	public double getBreaks() {
		return breaks;
	}
	@Override
	public void setTestName(String testName) {
		this.testName = testName;
	}
	
	@Override	
	public void setTimeWorked(String timeWorked) {
		this.timeWorked = timeWorked;
	}
	 	
	@Override	
	public void setRegressions(double regressions) {
		this.regressions = regressions;
	}
	@Override
	public void setBreaks(double newVal) {
		breaks = newVal;
	}

}
