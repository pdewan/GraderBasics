package gradingTools.logs.bulkLogProcessing.collectors.FineGrained;

import gradingTools.logs.bulkLogProcessing.collectors.AbstractCollector;

public class TestScorePercentCollector extends AbstractCollector{

	protected double [][] workingResults;
	private final String headerPhrase;
	
	public TestScorePercentCollector(){
		this("Assignment Score");
	}
	public TestScorePercentCollector(String header) {
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
					workingResults[i][0]=val.points;
					workingResults[i][1]=val.total;
				}
			}
		}	
	}
	
	@Override
	public String[] getResults() {
		double points=0,total=0;
		for(double [] vals:workingResults) {
			points+=vals[0];
			total+=vals[1];
		}
		results[0]=Double.toString(points/total);
		return super.getResults();
	}
	
	@Override
	public void reset() {
		workingResults=new double[testNames.length][2];
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
	
	@Override
	protected void generateHeaders() {
		headers = new String[1];
		headers[0]=getHeaderPhrase();
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
		return new TestScore(test,score,Double.parseDouble(sb.toString()));
	}
	
	class TestScore{
		String test;
		double points;
		double total;
		public TestScore(String t, double s, double tot) {
			test=t;
			points=s;
			total=tot;
		}
	}
	
}
