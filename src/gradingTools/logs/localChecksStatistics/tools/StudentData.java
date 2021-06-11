package gradingTools.logs.localChecksStatistics.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import gradingTools.logs.localChecksStatistics.dataStorage.StudentTestData;

public class StudentData {

	private List<String> allTests = new ArrayList<String>();
	private HashMap<String,StudentTestData> testData = new HashMap<String,StudentTestData>();
	private HashMap<String,Double> standardizedData = new HashMap<String,Double>();
	private HashMap<String,Double> workedOnZScores = new HashMap<String,Double>();
	private HashMap<String,Double> workTimeZScores = new HashMap<String,Double>();
	private HashMap<String,Double> breaksZScores = new HashMap<String,Double>();
	
	
	
	private static double [] catagoryWeights={
		1/7.0,
		3/7.0,
		3/7.0
	};
	
	
	private double workedOnAvg=Double.NaN;
	private double workedOnST=Double.NaN;
	private double workTimeAvg=Double.NaN;
	private double workTimeST=Double.NaN;
	private double breaksAvg=Double.NaN;
	private double breaksST=Double.NaN;
	
	
	private final String studentID;
	
	public StudentData(String studentID){
		this.studentID=studentID;
	}
	
	public void addTestData(String testName, StudentTestData testData){
		allTests.add(testName);
		this.testData.put(testName, testData);
	}

	public String getStudentID() {
		return studentID;
	}
	
	public void runAnalysis(){
		double numTests = allTests.size();
		double totalTimesTested =0;
		double totalTime =0;
		double totalBreaks=0;
		
		for(String test:allTests){
			StudentTestData data = testData.get(test);
			if(data.isCompleted()){
				totalTimesTested+=data.getTimesWorkedOn();
				totalTime+=data.getTimeTaken();
				totalBreaks+=data.getBreaksTaken();
			}else{
				numTests--;
			}

		}
		
		
		workedOnAvg=totalTimesTested/numTests;
		workTimeAvg=totalTime/numTests;
		breaksAvg=totalBreaks/numTests;
		
		totalTimesTested =0;
		totalTime =0;
		totalBreaks=0;
		
		for(String test:allTests){
			StudentTestData data = testData.get(test);
			if(data.isCompleted()){
				totalTimesTested+=Math.pow(data.getTimesWorkedOn()-workedOnAvg,2);
				totalTime+=Math.pow(data.getTimeTaken()-workTimeAvg,2);
				totalBreaks+=Math.pow(data.getBreaksTaken()-breaksAvg,2);
			}
		}
		
		workedOnST=Math.sqrt(totalTimesTested/numTests);
		workTimeST=Math.sqrt(totalTime/numTests);
		breaksST=Math.sqrt(totalBreaks/numTests);
		
		for(String test:allTests){
			StudentTestData data = testData.get(test);
			if(data.isCompleted()){
				
				double workedOnZScore = ((data.getTimesWorkedOn()-workedOnAvg)/workedOnST);
				double timeTakenZScore = ((data.getTimeTaken()-workTimeAvg)/workTimeST);
				double breaksZScore = ((data.getBreaksTaken()-breaksAvg)/breaksST);
				
				
				double weightedZscore=(catagoryWeights[0]*workedOnZScore)
									+ (catagoryWeights[1]*timeTakenZScore)
									+ (catagoryWeights[2]*breaksZScore);
				
				standardizedData.put(test, weightedZscore);
				workedOnZScores.put(test, workedOnZScore);
				workTimeZScores.put(test, timeTakenZScore);
				breaksZScores.put(test, breaksZScore);
				
			}else{
				standardizedData.put(test, Double.NaN);
				workedOnZScores.put(test, Double.NaN);
				workTimeZScores.put(test, Double.NaN);
				breaksZScores.put(test, Double.NaN);
			}
		}
	}
	
	public Double getStandardizedData(String testName){
		if(standardizedData.containsKey(testName))
			return standardizedData.get(testName);
		return null;
	}
	
	public Double getWorkedOnZScore(String testName){
		if(workedOnZScores.containsKey(testName))
			return workedOnZScores.get(testName);
		return null;
	}
	
	public Double getWorkTimeZScore(String testName){
		if(workTimeZScores.containsKey(testName))
			return workTimeZScores.get(testName);
		return null;
	}
	
	public Double getBreaksZScore(String testName){
		if(breaksZScores.containsKey(testName))
			return breaksZScores.get(testName);
		return null;
	}
	
}
