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
	static final int SESSION_NUMBER_INDEX=9;
	static final int SESSION_RUN_NUMBER_INDEX=10;
	static final int RUN_IS_SUITE_INDEX=11;
	static final int TESTS_IN_SUITE_INDEX=12;
	static final int PREREQ_TESTS_INDEX=13;
	static final int EXTRA_CREDIT_TESTS_INDEX=14;
	static final int TEST_SCORES_INDEX=15;
	
	public String [] getResults();
	public String [] getHeaders();
	public int getRequiredPass();
	
	//Called per line of data read
	public void logData(String [] data) throws IllegalArgumentException;
	
	public void reset();
	
	public void setStudentName(String name);
	public void setTestNames(String [] names);
	public void setSuiteMapping(SuiteMapping mapping);
	public void setAssignmentNumber(String assignmentnum);
	
	public boolean requiresTestNames();
	public boolean requiresStudentName();
	public boolean requiresSuiteMapping();
	public boolean requiresAssignmentNum();
	public boolean otherCollectorCompatable();
	
}
