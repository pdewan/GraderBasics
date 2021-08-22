package gradingTools.logs.localChecksStatistics.collectors;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import gradingTools.logs.localChecksStatistics.dataStorage.SuiteMapping;

public abstract class AbstractCollector implements Collector {

	protected int reqPass;
	protected String [] headers, results, testNames;
	protected String studentName, assignmentNum;
	protected SuiteMapping suiteMapping;
	protected final String movementMatchingRegex = ".*[\\+-]", movementMatchString="[\\+-]";
	
	
	@Override
	public void reset() {
		results=new String[headers.length];
		tests = new HashMap<String,State>();
	}
	
	@Override
	public String[] getHeaders() {
		return headers;
	}

	@Override
	public int getRequiredPass() {
		return reqPass;
	}
	
	@Override
	public String[] getResults() {
		return results;
	}
	
	@Override
	public void setTestNames(String[] names) {
		testNames=names;
		generateHeaders();
		reset();
	}
	
	@Override
	public void setStudentName(String name) {
		studentName=name;
	}
	
	@Override
	public void setSuiteMapping(SuiteMapping mapping) {
		suiteMapping=mapping;
	}
	
	@Override
	public void setAssignmentNumber(String assignmentNum) {
		this.assignmentNum=assignmentNum;
	}
	
	@Override
	public boolean requiresStudentName() {
		return false;
	}
	
	@Override
	public boolean requiresSuiteMapping() {
		return false;
	}
	
	@Override
	public boolean otherCollectorCompatable() {
		return true;
	}
	
	@Override
	public boolean requiresAssignmentNum() {
		return false;
	}
	
	
	protected HashMap<String,State> tests = new HashMap<String,State>();
	private List<String> improvedTests, reducedTests, changedTests;
	
	//TODO this could be done outside of tests (Collector manager?) so that multiple tests don't run the same instructions 
	protected void updateTests(String [] log) {
		improvedTests = new ArrayList<String>();
		reducedTests = new ArrayList<String>();
		changedTests = new ArrayList<String>();
		
		String [] passedTests = log[TEST_PASS_INDEX].split(" "),
				  partialTests = log[TEST_PARTIAL_INDEX].split(" "),
				  failedTests = log[TEST_FAIL_INDEX].split(" "),
				  untestedTests = log[TEST_UNTESTED_INDEX].split(" ");
		
		for(String test:untestedTests) {
			String str = test.replace("+", "").replace("-", "");
			if(tests.containsKey(str)) 
				continue;
			else 
				tests.put(str, State.Untested);
		}
		
		for(String test:failedTests) {
			String str = test.replace("+", "").replace("-", "");
			if(tests.containsKey(str)) {
				State current = tests.get(str);
				if(current==State.Fail)
					continue;
				else if(current == State.Pass || current == State.Partial) {
					reducedTests.add(str);
					changedTests.add(str);
					tests.put(str, State.Fail);
				}else 
					tests.put(str, State.Fail);
				
			} else 
				tests.put(str, State.Fail);
		}
		
		for(String test:partialTests) {
			String str = test.replace("+", "").replace("-", "");
			if(tests.containsKey(str)) {
				State current = tests.get(str);
				if(current==State.Partial)
					continue;
				else if(current == State.Pass) {
					reducedTests.add(str);
					changedTests.add(str);
					tests.put(str, State.Partial);
				}else {
					improvedTests.add(str);
					changedTests.add(str);
					tests.put(str, State.Partial);
				}
			} else {
				improvedTests.add(str);
				changedTests.add(str);
				tests.put(str, State.Partial);
			}
		}
		
		for(String test:passedTests) {
			String str = test.replace("+", "").replace("-", "");
			if(tests.containsKey(str) && tests.get(str)== State.Pass) 
				continue;
			improvedTests.add(str);
			changedTests.add(str);
			tests.put(str, State.Pass);
		}
		
	}
	
	protected List<String> getImprovedTests(){
		return improvedTests;
	}
	protected List<String> getReducedTests(){
		return reducedTests;
	}
	protected List<String> getChangedTests(){
		return changedTests;
	}

	protected boolean contains(String [] searchData, String term){
		for(String data:searchData)
			if(data.equals(term))
				return true;
		return false;
	}
	
	protected boolean matches(String [] searchData, String term){
		for(String data:searchData)
			if(data.matches(term))
				return true;
		return false;
	}
	
	protected final SimpleDateFormat dateFormat=new SimpleDateFormat("EEE MMM dd kk:mm:ss zzz yyyy");
	protected long secondsBetween(String date1, String date2) throws ParseException{
		Date first=dateFormat.parse(date1);
		Date second=dateFormat.parse(date2);
		long secondsBetween=TimeUnit.SECONDS.convert(second.getTime()-first.getTime(), TimeUnit.MILLISECONDS);
		return secondsBetween;
	}
	
	protected Date parseDate(String date) throws ParseException{
		return dateFormat.parse(date);
	}
	 
	protected String [] getMatches(String term, String []... searchData){
		List<String> retVal = new ArrayList<String>();
		for(String [] arr:searchData)
			for(String data:arr)
				if(data.matches(term))
					retVal.add(data);
		String [] values = new String[retVal.size()];
		retVal.toArray(values);
		return values;
	}
	
	
	protected void generateHeaders() {
		String [] headers = new String[testNames.length];
		String headerPhrase = getHeaderPhrase();
		for(int i=0;i<headers.length;i++)
			headers[i]=testNames[i]+headerPhrase;
		this.headers=headers;
	}
	
	protected abstract String getHeaderPhrase();
	
	public enum State{
		Pass,
		Partial,
		Untested,
		Fail,
	}

}

