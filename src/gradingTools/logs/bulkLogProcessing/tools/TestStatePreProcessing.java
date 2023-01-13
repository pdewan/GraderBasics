package gradingTools.logs.bulkLogProcessing.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gradingTools.logs.bulkLogProcessing.collectors.Collector;
import gradingTools.logs.bulkLogProcessing.tools.dataStorage.TestState;

public class TestStatePreProcessing {
	
	public void reset() {
		tests = new HashMap<String,TestState>();
	}
	
	private HashMap<String,TestState> tests = new HashMap<String,TestState>();
	private List<String> improvedTests, reducedTests, changedTests;
	public void updateTests(String [] log) {
		improvedTests = new ArrayList<String>();
		reducedTests = new ArrayList<String>();
		changedTests = new ArrayList<String>();
		
		String [] passedTests = log[Collector.TEST_PASS_INDEX].split(" "),
				  partialTests = log[Collector.TEST_PARTIAL_INDEX].split(" "),
				  failedTests = log[Collector.TEST_FAIL_INDEX].split(" "),
				  untestedTests = log[Collector.TEST_UNTESTED_INDEX].split(" ");
		
		for(String test:untestedTests) {
			String str = test.replace("+", "").replace("-", "");
			if(tests.containsKey(str)) 
				continue;
			else 
				tests.put(str, TestState.Untested);
		}
		
		for(String test:failedTests) {
			String str = test.replace("+", "").replace("-", "");
			if(tests.containsKey(str)) {
				TestState current = tests.get(str);
				if(current==TestState.Fail)
					continue;
				else if(current == TestState.Pass || current == TestState.Partial) {
					reducedTests.add(str);
					changedTests.add(str);
					tests.put(str, TestState.Fail);
				}else 
					tests.put(str, TestState.Fail);
				
			} else 
				tests.put(str, TestState.Fail);
		}
		
		for(String test:partialTests) {
			String str = test.replace("+", "").replace("-", "");
			if(tests.containsKey(str)) {
				TestState current = tests.get(str);
				if(current==TestState.Partial)
					continue;
				else if(current == TestState.Pass) {
					reducedTests.add(str);
					changedTests.add(str);
					tests.put(str, TestState.Partial);
				}else {
					improvedTests.add(str);
					changedTests.add(str);
					tests.put(str, TestState.Partial);
				}
			} else {
				improvedTests.add(str);
				changedTests.add(str);
				tests.put(str, TestState.Partial);
			}
		}
		
		for(String test:passedTests) {
			String str = test.replace("+", "").replace("-", "");
			if(tests.containsKey(str) && tests.get(str)== TestState.Pass) 
				continue;
			improvedTests.add(str);
			changedTests.add(str);
			tests.put(str, TestState.Pass);
		}
		
	}
	
	public List<String> getImprovedTests(String [] log){
		return improvedTests;
	}
	public List<String> getReducedTests(String [] log){
		return reducedTests;
	}
	public List<String> getChangedTests(String [] log){
		return changedTests;
	}
	public Map<String,TestState> getTestStates(String [] log){
		return tests;
	}
	
}
