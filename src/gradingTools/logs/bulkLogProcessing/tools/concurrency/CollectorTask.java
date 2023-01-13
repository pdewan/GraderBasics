package gradingTools.logs.bulkLogProcessing.tools.concurrency;

import gradingTools.logs.bulkLogProcessing.collectors.Collector;

public class CollectorTask implements Task{

	private final Collector c;
	private final Joiner j;
	private final String [] data;
	
	public CollectorTask(Collector c, Joiner j, String [] data) {
		this.c=c;
		this.j=j;
		this.data=data;
	}
	
	@Override
	public void RunTask() {
		try {
			c.logData(data);
		}catch(Exception e) {
			e.printStackTrace();
			j.join(true);
		}
		
		j.join(false);
	}

}
