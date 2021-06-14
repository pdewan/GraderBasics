package gradingTools.logs.models;

public interface TestMetrics {

	double getAttempts();

	void setAttempts(double attempts);

	String getTestName();

	double getBreaks();
	
	void setBreaks(double newVal);

	String getTimeWorked();

	void setTestName(String testName);

	void setTimeWorked(String timeWorked);

	double getRegressions();

	void setRegressions(double regressions);

}