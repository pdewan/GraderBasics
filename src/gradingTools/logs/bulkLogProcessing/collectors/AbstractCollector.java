package gradingTools.logs.bulkLogProcessing.collectors;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import gradingTools.logs.bulkLogProcessing.compiledLogGenerator.CollectorManager;
import gradingTools.logs.bulkLogProcessing.tools.dataStorage.SuiteMapping;
import gradingTools.logs.bulkLogProcessing.tools.dataStorage.TestState;

public abstract class AbstractCollector implements Collector {

	protected int reqPass=1;
	protected String [] headers, results, testNames;
	protected String studentName, assignmentNum;
	protected SuiteMapping suiteMapping;
	protected final String movementMatchingRegex = ".*[\\+-]", movementMatchString="[\\+-]";
	
	//Core functionality
	@Override
	public void reset() {
		results=new String[headers.length];
		tests = new HashMap<String,TestState>();
	}
	
	@Override
	public String[] getHeaders() {
		if(headers==null)
			generateHeaders();
		return headers;
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
	
	protected void generateHeaders() {
		String [] headers;
		if(requiresTestNames()) {
			headers = new String[testNames.length];
			String headerPhrase = getHeaderPhrase();
			for(int i=0;i<headers.length;i++)
				headers[i]=testNames[i]+headerPhrase;
		}else {
			headers = new String[1];
			headers[0]=getHeaderPhrase();
		}
		this.headers=headers;
	}
	
	//Used by the collector manager to transfer information not provided by the logs
	
	@Override
	public void setStudentName(String name) {
		studentName=name;
	}
	
	@Override
	public int getRequiredPass() {
		return reqPass;
	}
	
	@Override
	public void setSuiteMapping(SuiteMapping mapping) {
		suiteMapping=mapping;
	}
	
	@Override
	public void setAssignmentNumber(String assignmentNum) {
		this.assignmentNum=assignmentNum;
	}
	
	//Shouldn't really be needed but could be useful if attempting to make a little bit more efficient 
	@Override
	public void setCollectorManager(CollectorManager manager) {}
	
	//Boolean requirements of the collector (should be made abstract)
	
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
	
	
	private HashMap<String,TestState> tests = new HashMap<String,TestState>();
	private List<String> improvedTests, reducedTests, changedTests;
	
	/** TODO further verification and fine tuning
	 * Determines test status as improved, reduced, or changed. Also creates a mapping of test names and their status
	 * does not require a prerequisite knowledge of test names to operate (i.e. "requiresTestNames" can be set to false)
	 * Results can be retrieved from subsequent getter methods: 
	 * List<String> getImprovedTests()
	 * List<String> getReducedTests()
	 * List<String> getChangedTests()
	 * Map<String,TestState> getTestStates()
	 * 
	 * @param log
	 */
	protected void updateTests(String [] log) {
		improvedTests = new ArrayList<String>();
		reducedTests = new ArrayList<String>();
		changedTests = new ArrayList<String>();
		
		String [] passedTests = log[TEST_PASS_INDEX].replaceAll("\\+", "").replaceAll("-", "").split(" "),
				  partialTests = log[TEST_PARTIAL_INDEX].replaceAll("\\+", "").replaceAll("-", "").split(" "),
				  failedTests = log[TEST_FAIL_INDEX].replaceAll("\\+", "").replaceAll("-", "").split(" "),
				  untestedTests = log[TEST_UNTESTED_INDEX].replaceAll("\\+", "").replaceAll("-", "").split(" ");
		
		List<String> duplicateTerms = determineDuplicates(passedTests,partialTests,failedTests,untestedTests);
		
		if(duplicateTerms.size()>0) {
			System.out.println(duplicateTerms);
		}
		
		for(String str:untestedTests) {
			if(tests.containsKey(str)) 
				continue;
			else 
				tests.put(str, TestState.Untested);
		}
		
		for(String str:failedTests) {
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
		
		for(String str:partialTests) {
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
		
		for(String str:passedTests) {
			if(tests.containsKey(str) && tests.get(str)==TestState.Pass) 
				continue;
			improvedTests.add(str);
			changedTests.add(str);
			tests.put(str, TestState.Pass);
		}
		
	}

	protected List<String> determineDuplicates(String[]...arrays){
		List<String> retval = new ArrayList<>();
		for(int i=0;i<arrays.length;i++) {
			for(String search:arrays[i]) {
				int timesFound=0;
				for(int j=i;j<arrays.length;j++) 
					for(String sample:arrays[j]) 
						if(search.equals(sample))
							timesFound++;
				if(timesFound>1)
					retval.add(search);
			}
		}
		return retval;
	}
	
	//Helper Methods for collector implementations
	
	protected List<String> getImprovedTests(){
		return improvedTests;
	}
	protected List<String> getReducedTests(){
		return reducedTests;
	}
	protected List<String> getChangedTests(){
		return changedTests;
	}
	protected Map<String,TestState> getTestStates(){
		return tests;
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
	
	protected String [] combineArrays(String[]... arrs) {
		int totalSize=0;
		for(String[] arr:arrs)
			totalSize+=arr.length;
		String[] retval = new String[totalSize];
		int currentSize=0;
		for(String [] arr:arrs) {
			for(int i=0;i<arr.length;i++)
				retval[i+currentSize]=arr[i];
			currentSize+=arr.length;
		}
		return retval;
	}
	
	protected abstract String getHeaderPhrase();

}

