package gradingTools.logs.localChecksStatistics.collectors.IntervalReplayer;

import grader.basics.interval.IntervalDriver;

public class FixedWorkTimeIRCollector extends AbstractIntervalReplayerBasedCollector {
	
	private final String headerPhrase;
	
	public FixedWorkTimeIRCollector() {
		this("Fixed Work Time");
	}
	
	public FixedWorkTimeIRCollector(String header) {
		super();
		headerPhrase=header;
		generateHeaders();
		reset();
		this.reqPass=1;
	}
	
	@Override
	public String[] getResults() {
		long result = this.replayer.getWorkTime(this.studentProject,this.startTime,this.lastTestTime)[1];
		results[0] = IntervalDriver.format(result);
		return results;
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
	
}
