package gradingTools.logs.localChecksStatistics.tools;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import gradingTools.logs.localChecksStatistics.dataStorage.SuiteMapping;
import gradingTools.logs.localChecksStatistics.dataStorage.SuiteTestPairings;
import gradingTools.logs.localChecksStatistics.dataStorage.SuitesAndTests;


public class TestingData {

	private static final int TEST_SUITE_INDEX=4,TEST_PASS_INDEX=5,TEST_PARTIAL_INDEX=6,TEST_FAIL_INDEX=7,TEST_UNTESTED_INDEX=8,TEST_SIZE=9;
	
	public static SuitesAndTests findAllSuitesAndTests(List<List<String>> assignmentData, int assignmentNumber) throws FileNotFoundException{
		List<String> suites=new ArrayList<String>();
		List<String> tests=new ArrayList<String>();
		List<KeyValue<SuiteTestPairings,Integer>> testPool = new ArrayList<KeyValue<SuiteTestPairings,Integer>>();
		
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
						testPool.add(new KeyValue<SuiteTestPairings,Integer>(stp,1));
					
						
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
		Iterator<Entry<String, Integer>> hashmapValues = testCollector.entrySet().iterator();
		List<String> wrongTests = new ArrayList<String>();
		while(hashmapValues.hasNext()){
			Entry<String,Integer> entry = hashmapValues.next();
			if(entry.getValue().intValue()>=halfNumStudents)
				tests.add(entry.getKey());
			else
				wrongTests.add(entry.getKey());
		}
		
		List<SuiteTestPairings> mappingPool = new ArrayList<SuiteTestPairings>();
		
		int thirdStudents = assignmentData.size()/3;
		for(KeyValue<SuiteTestPairings,Integer> kv:testPool) 
			if(kv.value>thirdStudents)
				mappingPool.add(kv.key);
		
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
	
	private static boolean inTestPool(List<KeyValue<SuiteTestPairings,Integer>> testPool, SuiteTestPairings stp) {
		for(KeyValue<SuiteTestPairings,Integer> pool:testPool)
			if(pool.key.equals(stp)) {
				pool.value++;
				return true;
			}
				
		return false;
	}
	
	
}

class KeyValue<K,V>{
	public K key;
	public V value;
	
	public KeyValue(K key, V value) {
		this.key=key;
		this.value=value;
	}
	
}

