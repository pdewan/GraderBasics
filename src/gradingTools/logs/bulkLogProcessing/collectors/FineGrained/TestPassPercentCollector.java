package gradingTools.logs.bulkLogProcessing.collectors.FineGrained;

import gradingTools.logs.bulkLogProcessing.collectors.AbstractCollector;

public class TestPassPercentCollector extends AbstractCollector{

	protected double [] workingResults;
	private final String headerPhrase;
	
	public TestPassPercentCollector(){
		this(" Test Pass Percent");
	}
	public TestPassPercentCollector(String header) {
		headerPhrase=header;
		reqPass=1;
	}
	
	@Override
	public void logData(String[] data) throws IllegalArgumentException {
		String [] testsAndScores = data[TEST_SCORES_INDEX].split(" ");
		
		for(String test:testsAndScores) {
			TestScore val = passPercentParser(test.toCharArray());
			for(int i=0;i<workingResults.length;i++) {
				if(testNames[i].equals(val.test)) {
					workingResults[i]=val.score;
				}
			}
		}
		
	}
	
	@Override
	public String[] getResults() {
		for(int i=0;i<workingResults.length;i++)
			results[i]=Double.toString(workingResults[i]);
		return super.getResults();
	}
	
	@Override
	public void reset() {
		workingResults=new double[testNames.length];
		super.reset();
	}
	
	@Override
	public boolean requiresTestNames() {
		return true;
	}

	@Override
	protected String getHeaderPhrase() {
		return headerPhrase;
	}
	
	private TestScore passPercentParser(char [] arr) {
		int i=0;
		StringBuilder sb=new StringBuilder();
		
		while(arr[i]!='-') {
			sb.append(arr[i]);
			i++;
		}
			
		i+=2;
		String test=sb.toString();
		
		sb=new StringBuilder();
		while(arr[i]!='/') {
			sb.append(arr[i]);
			i++;
		}
			
		i++;
		double score=Double.parseDouble(sb.toString());
		sb=new StringBuilder();
		while(arr[i]!=')') {
			sb.append(arr[i]);
			i++;
		}
			
		
		
		return new TestScore(test,score/Double.parseDouble(sb.toString()));
	}
	
	class TestScore{
		String test;
		double score;
		public TestScore(String t, double s) {
			test=t;
			score=s;
		}
	}
	
}
