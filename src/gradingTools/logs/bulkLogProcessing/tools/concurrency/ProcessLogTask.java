package gradingTools.logs.bulkLogProcessing.tools.concurrency;

import java.util.List;

import gradingTools.logs.bulkLogProcessing.collectors.Collector;

public class ProcessLogTask implements Task {

	
	private final Collector collector;
	private final Joiner joiner;
	private final List<String> data;
	
	public ProcessLogTask(Collector c, Joiner j, List<String> dataLines) {
		collector=c;
		joiner=j;
		data=dataLines;
	}
	
	
	@Override
	public void RunTask() {
		boolean exception = false;
		try {
			for(String line:data) {
				String [] dataArr = line.split(",");
				collector.logData(dataArr);
			}
		}catch(Exception e) {
			e.printStackTrace();
			exception=true;
		}
		joiner.join(exception);
	}

}
