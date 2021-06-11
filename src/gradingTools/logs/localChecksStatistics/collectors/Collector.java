package gradingTools.logs.localChecksStatistics.collectors;

import gradingTools.logs.localChecksStatistics.dataStorage.SuiteMapping;

public interface Collector {

	static final int TEST_NUMBER_INDEX=0;
	static final int TIME_TESTED_INDEX=1;
	static final int PERCENT_PASSED_INDEX=2;
	static final int PERCENT_PASSED_CHANGE_INDEX=3;
	static final int TEST_SUITE_INDEX=4;
	static final int TEST_PASS_INDEX=5;
	static final int TEST_PARTIAL_INDEX=6;
	static final int TEST_FAIL_INDEX=7;
	static final int TEST_UNTESTED_INDEX=8;
	
	public String [] getResults();
	public String [] getHeaders();
	public void logData(String [] data) throws IllegalArgumentException;
	public void setTestNames(String [] names);
	public boolean requiresTestNames();
	public void reset();
	public int getRequiredPass();
	public boolean otherCollectorCompatable();
	public boolean requiresStudentName();
	public void setStudentName(String name);
	public boolean requiresSuiteMapping();
	public void setSuiteMapping(SuiteMapping mapping);
	
}
