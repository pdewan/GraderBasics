package gradingTools.logs.bulkLogProcessing.tools;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import gradingTools.logs.bulkLogProcessing.tools.dataStorage.SuiteMapping;
import gradingTools.logs.bulkLogProcessing.tools.dataStorage.SuiteTestPairings;
import gradingTools.logs.bulkLogProcessing.tools.dataStorage.SuitesAndTests;


public class TestingData {

	private static final int TEST_SUITE_INDEX=4,TEST_PASS_INDEX=5,TEST_PARTIAL_INDEX=6,TEST_FAIL_INDEX=7,TEST_UNTESTED_INDEX=8,TEST_SIZE=9;
	
	
	public static SuitesAndTests findAllSuitesAndTests(List<String> assignmentData) throws FileNotFoundException{
		ArrayList<List<String>> values = new ArrayList<>();
		values.add(assignmentData);
		return findAllSuitesAndTests(values,-1);
	}
	
	public static SuitesAndTests findAllSuitesAndTests(List<List<String>> assignmentData, int assignmentNumber) throws FileNotFoundException{
		List<String> suites=new ArrayList<String>();
		List<String> tests=new ArrayList<String>();
		List<Pairing<SuiteTestPairings,Integer>> testPool = new ArrayList<Pairing<SuiteTestPairings,Integer>>();
		
		Map<String,Integer> testCollector = new HashMap<String, Integer>();
		
		top:
		for(List<String> studentLogData:assignmentData){
			
			if(studentLogData.size()==0)
				continue;
			
			for(String suiteTested:studentLogData){
				String[]line=suiteTested.split(",");
				if(line.length < TEST_SIZE)
					continue top;
				if(!suites.contains(line[TEST_SUITE_INDEX]))
					suites.add(line[TEST_SUITE_INDEX]);
				
				List<String> movedTests =  testsMoved(line[TEST_PASS_INDEX].split(" "), line[TEST_FAIL_INDEX].split(" "),line[TEST_PARTIAL_INDEX].split(" "));
				for(String test:movedTests) {
					SuiteTestPairings stp = new SuiteTestPairings(line[TEST_SUITE_INDEX],test);
					if(!inTestPool(testPool,stp)) 
						testPool.add(new Pairing<SuiteTestPairings,Integer>(stp,1));
					
						
				}
				
				
			}
			
			String [] lastLine=studentLogData.get(studentLogData.size()-1).replaceAll("\\+", "").replaceAll("-","").split(",");
			
			for(String testName:lastLine[TEST_PASS_INDEX].split(" "))
				if(testCollector.containsKey(testName))
					testCollector.replace(testName, testCollector.get(testName)+1);
				else
					testCollector.put(testName, 1);
			for(String testName:lastLine[TEST_PARTIAL_INDEX].split(" "))
				if(testCollector.containsKey(testName))
					testCollector.replace(testName, testCollector.get(testName)+1);
				else
					testCollector.put(testName, 1);
			for(String testName:lastLine[TEST_FAIL_INDEX].split(" "))
				if(testCollector.containsKey(testName))
					testCollector.replace(testName, testCollector.get(testName)+1);
				else
					testCollector.put(testName, 1);
			for(String testName:lastLine[TEST_UNTESTED_INDEX].split(" "))
				if(testCollector.containsKey(testName))
					testCollector.replace(testName, testCollector.get(testName)+1);
				else
					testCollector.put(testName, 1);
		}
		
		int halfNumStudents=assignmentData.size()/2;
		Iterator<Entry<String, Integer>> hashmapseconds = testCollector.entrySet().iterator();
		List<String> wrongTests = new ArrayList<String>();
		while(hashmapseconds.hasNext()){
			Entry<String,Integer> entry = hashmapseconds.next();
			if(entry.getValue().intValue()>=halfNumStudents)
				tests.add(entry.getKey());
			else
				wrongTests.add(entry.getKey());
		}
		
		List<SuiteTestPairings> mappingPool = new ArrayList<SuiteTestPairings>();
		
		int thirdStudents = assignmentData.size()/3;
		for(Pairing<SuiteTestPairings,Integer> kv:testPool) 
			if(kv.second>thirdStudents)
				mappingPool.add(kv.first);
		
//		for(int i=0;i<testPool.size();i++)
//			if(wrongTests.contains(testPool.get(i).testName)) {
//				testPool.remove(i);
//				i=0;
//			}
			
		return new SuitesAndTests(suites,tests,assignmentNumber,new SuiteMapping(mappingPool));
	}
	
	private static List<String> testsMoved(String [] ... categories){
		List<String> retval = new ArrayList<String>();
		for(String[] category:categories)
			for(String test:category)
				if(test.matches(".*[\\+-]")) 
					retval.add(test.replaceAll("[\\+-]", ""));
		return retval;
	}
	
	private static boolean inTestPool(List<Pairing<SuiteTestPairings,Integer>> testPool, SuiteTestPairings stp) {
		for(Pairing<SuiteTestPairings,Integer> pool:testPool)
			if(pool.first.equals(stp)) {
				pool.second++;
				return true;
			}
				
		return false;
	}
	
	
}

