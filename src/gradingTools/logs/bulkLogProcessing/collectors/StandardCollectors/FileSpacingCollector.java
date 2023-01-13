package gradingTools.logs.bulkLogProcessing.collectors.StandardCollectors;

import gradingTools.logs.bulkLogProcessing.collectors.AbstractCollector;

public class FileSpacingCollector extends AbstractCollector{
	
	
	public FileSpacingCollector(){
		this(" "," ");
	}
	public FileSpacingCollector(String header, String data) {
		this.headers = new String[1];
		headers[0]=header;
		this.results = new String[1];
		results[0]=data;
	}
	
	@Override
	public void logData(String[] data) throws IllegalArgumentException {}
	
	@Override
	public void reset() {}
	
	@Override
	public boolean requiresTestNames() {return false;}

	@Override
	protected String getHeaderPhrase() {return null;}

}
