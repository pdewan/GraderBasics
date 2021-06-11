package gradingTools.logs.localChecksStatistics.dataStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class SuiteMapping {


	private HashMap<String,List<String>> suitePairings, testPairings;
	
	public SuiteMapping(List<SuiteTestPairings> testPool) {
		suitePairings = new HashMap<String,List<String>>();
		testPairings = new HashMap<String,List<String>>();
		
		for(SuiteTestPairings pairing:testPool) {
			if(!suitePairings.containsKey(pairing.suiteName))
				suitePairings.put(pairing.suiteName, new ArrayList<String>());
			if(!testPairings.containsKey(pairing.testName))
				testPairings.put(pairing.testName, new ArrayList<String>());
			
			suitePairings.get(pairing.suiteName).add(pairing.testName);
			testPairings.get(pairing.testName).add(pairing.suiteName);
			
		}

	}
	
	public boolean containsSuiteMapping(String suiteName) {
		return suitePairings.containsKey(suiteName);
	}
	
	public boolean containsTestMapping(String testName) {
		return testPairings.containsKey(testName);
	}
	
	public List<String> getSuiteInstances(String suiteName){
		return suitePairings.get(suiteName);
	}
	
	public List<String> getTestInstances(String testName){
		return testPairings.get(testName);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(Entry<String,List<String>> entry:suitePairings.entrySet())
			sb.append(entry.getKey() + " - " +entry.getValue()+"\n");
		return sb.toString();
	}
	
}
