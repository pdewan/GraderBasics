package gradingTools.logs.localChecksStatistics.dataStorage;

import java.util.List;

public class SuitesAndTests {
	private final List<String> suites;
	private final List<String> tests;
	private final int assignmentNumber;
	private final SuiteMapping mapping;
	
	public SuitesAndTests(List<String> suites,List<String>tests,int assignmentNumber, SuiteMapping map){
		this.suites=suites;
		this.tests=tests;
		this.assignmentNumber=assignmentNumber;
		this.mapping=map;
	}
	
	public List<String> getSuites(){
		return suites;
	}
	
	public List<String> getTests(){
		return tests;
	}
	
	public int getAssignmentNumber(){
		return assignmentNumber;
	}

	public SuiteMapping getMapping() {
		return mapping;
	}
	
}
