package gradingTools.logs.bulkLogProcessing.collectors.IntervalReplayer.Timing;

import java.io.File;
import java.util.List;

import gradingTools.logs.bulkLogProcessing.collectors.AbstractIntervalReplayerBasedCollector;

public class TestFocusedFixedWorkTimeIRCollector extends AbstractIntervalReplayerBasedCollector{

	public TestFocusedFixedWorkTimeIRCollector(File semesterFolderLocation, String pathToStudents) throws Exception {
		super(semesterFolderLocation, pathToStudents);
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
				time += replayer.getWorkTime(this.studentProject, this.lastTestTime+1, this.currentTestTime)[1];
//				System.out.println("Last Time Tested: "+(lastTestTime+1));
//				System.out.println("Last Time Tested: "+currentTestTime);
//				System.out.println(time +"--"+(currentTestTime - (1+lastTestTime)) );
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
//		int numNeverAttempted=0;
//		for(int i=0;i<workingResults.length;i++)
//			if(workingResults[i]==0)
//				numNeverAttempted++;
//		double amount=-1.0*((double)time/numNeverAttempted);
		for(int i=0;i<workingResults.length;i++)
			results[i]=Double.toString(workingResults[i]);//==0?amount:workingResults[i]);
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
