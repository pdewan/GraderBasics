package gradingTools.logs.localChecksStatistics.collectors.IntervalReplayer;

public class ContextBasedWorkTimeIRCollector extends AbstractIntervalReplayerBasedCollector {
	
	private final String headerPhrase;
	
	public ContextBasedWorkTimeIRCollector(){
		this("Context Based Time");
	}
	
	public ContextBasedWorkTimeIRCollector(String header) {
		super();
		headerPhrase=header;
		generateHeaders();
		reset();
		this.reqPass=1;
	}
	
	@Override
	public String[] getResults() {
		long [] result = this.replayer.getWorkTime(this.studentProject,this.startTime,this.lastTestTime);
		results[0] = Long.toString(result[0]);
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
