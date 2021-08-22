package gradingTools.logs.localChecksStatistics.compiledLogGenerator;

import java.util.ArrayList;
import java.util.List;

import gradingTools.logs.localChecksStatistics.collectors.Collector;

public class CollectorManager {

	private List<Collector> collectors;
	
	public CollectorManager(){
		collectors=new ArrayList<Collector>();
	}
	
	public CollectorManager(List<Collector> collectors){
		this.collectors=collectors;
	}
	
	public CollectorManager(Collector ... collectors) {
		this.collectors = new ArrayList<>();
		for(Collector c:collectors)
			this.collectors.add(c);
	}
	
	public void addCollector(Collector c){
		collectors.add(c);
	}
	
	public List<Collector> getCollectors(){
		return collectors;
	}
	
	public void processLog(List<String> dataLines, int numPasses) {
		for(int i=1;i<=numPasses;i++)
			for(String dataLine:dataLines){
				String [] splitLine=dataLine.split(",");
				for(Collector collector:collectors)
					if(collector.getRequiredPass()==i)
						collector.logData(splitLine);
			}
		
		String [] lastLine = dataLines.get(dataLines.size()-1).split(",");
		for(Collector collector:collectors)
			if(collector.getRequiredPass()==Integer.MAX_VALUE)
				collector.logData(lastLine);
	}
	
	public List<String> getOrderedHeaders(){
		List<String> headers = new ArrayList<String>();
		for(Collector collector:collectors)
			for(String header:collector.getHeaders())
				headers.add(header);
		return headers;
	}
	
	public List<String> getOrderedData(){
		List<String> dataEntries = new ArrayList<String>();
		for(Collector collector:collectors)
			for(String dataEntry:collector.getResults())
				dataEntries.add(dataEntry);
		return dataEntries;
	}
	
	public void reset(){
		for(Collector collector:collectors)
			collector.reset();
	}
	
	public boolean specialPrint() {
		for(Collector collector:collectors)
			if(!collector.otherCollectorCompatable())
				return true;
		return false;
	}
	
	public List<String> getCollectorNames(){
		List<String> retval = new ArrayList<>();
		for(Collector collector:collectors)
			retval.add(collector.getClass().getSimpleName());
		return retval;
	}
	
	public List<String []> getCertainHeadersAndData(String [] desiredTests) {
		List<String []> retval = new ArrayList<String[]>();
		for(Collector collector:collectors) {
			String [] headers = collector.getHeaders();
			String [] data = collector.getResults();
			boolean requiresTestName = collector.requiresTestNames();
			
			for(int i=0;i<headers.length;i++) {
				if(requiresTestName && !headerContainsTests(headers[i],desiredTests)) continue;
				String [] addToReturn = {headers[i],data[i]};
				retval.add(addToReturn);
			}
		}
		return retval;
	}
	
	private boolean headerContainsTests(String header, String [] desiredTests) {
		if(desiredTests==null) return true;
		for(String test:desiredTests)
			if(header.contains(test))
				return true;
		return false;
	}
	
	
	
	
}

	
