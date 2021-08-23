package gradingTools.logs.localChecksStatistics.collectors.IntervalReplayer;

import java.util.List;

import grader.basics.interval.IntervalDriver;

public class TestFocusedFixedWorkTimeIRCollector extends AbstractIntervalReplayerBasedCollector{

	public TestFocusedFixedWorkTimeIRCollector() {
		super();
		this.reqPass=1;
	}

	
	private double [] workingResults;
	
	@Override
	public void logData(String[] data) throws IllegalArgumentException{
		updateTests(data);
	
		List<String> movedTests = getChangedTests();
		String [] finishedTests = new String[movedTests.size()];
		movedTests.toArray(finishedTests);
		
		if(finishedTests.length>0){
			
			//We only want to use the replayer if an event occurs otherwise it takes up a massive amount of time
			super.logData(data);
			long time=0;
			try {
				time += replayer.getWorkTime(this.studentProject, this.lastTestTime, this.currentTestTime)[1];
			}catch(Exception e) {
				throw new IllegalArgumentException(e.getMessage());
			}
			
			double amount = (double)time/finishedTests.length;
			for(int i=0;i<testNames.length;i++)
				for(int j=0;j<finishedTests.length;j++)
					if(testNames[i].equals(finishedTests[j].replace("+", "").replace("-", "")))
						workingResults[i]+=amount;
		}
	}
	
	@Override
	public String[] getResults() {
		for(int i=0;i<workingResults.length;i++)
			results[i]=IntervalDriver.format((long)workingResults[i]);
		return super.getResults();
	}
	
	@Override
	public void reset() {
		workingResults=new double[testNames.length];
		super.reset();
	}
	
	@Override
	protected String getHeaderPhrase() {
		return " Fixed Work Time";
	}

	@Override
	public boolean requiresTestNames() {
		return true;
	}
	
}
