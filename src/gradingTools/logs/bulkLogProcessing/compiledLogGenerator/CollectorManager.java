package gradingTools.logs.bulkLogProcessing.compiledLogGenerator;

import java.util.ArrayList;
import java.util.List;

import gradingTools.logs.bulkLogProcessing.collectors.Collector;
import gradingTools.logs.bulkLogProcessing.selectYearMapping.YearSelectFactory;
import gradingTools.logs.bulkLogProcessing.tools.concurrency.*;
import gradingTools.logs.bulkLogProcessing.tools.dataStorage.SuiteMapping;

public class CollectorManager {

	public static boolean enableConcurrency = true;
	private List<Collector> collectors;
	private int maxReqPass=1;
//	private TestStatePreProcessing stateManager = new TestStatePreProcessing();
	
	public CollectorManager(){
		collectors = new ArrayList<>();
	}

	public CollectorManager(List<Collector> collectors){
		this();
		for(Collector c:collectors)
			addCollector(c);	
	}
	
	public CollectorManager(Collector ... collectors) {
		this();
		for(Collector c:collectors)
			addCollector(c);
	}
	
	public void processLog(List<String> dataLines) throws Exception {
		for(int i=1;i<=maxReqPass;i++) {
			if(enableConcurrency) {
				int numCollectors=0;
				ThreadManager treadManager = ThreadManagerFactory.getThreadManager();
				for(Collector c:collectors) 
					if(c.getRequiredPass()>=i&&c.getRequiredPass()<=maxReqPass)
						numCollectors++;
				Joiner j = new Joiner(numCollectors);
				for(Collector collector:collectors)
					if(collector.getRequiredPass()>=i&&collector.getRequiredPass()<=maxReqPass)
						treadManager.addTask(new ProcessLogTask(collector,j,dataLines));
				j.finish();
			}else {
				for(String dataLine:dataLines){
					String [] splitLine=dataLine.split(",");
					for(Collector collector:collectors)
						if(collector.getRequiredPass()>=i&&collector.getRequiredPass()<=maxReqPass)
							collector.logData(splitLine);
				}
			}
		}
		
		String [] lastLine = dataLines.get(dataLines.size()-1).split(",");
		for(Collector collector:collectors)
			if(collector.getRequiredPass()==Integer.MAX_VALUE)
				collector.logData(lastLine);
	}
	
	public void addCollector(Collector c){
		if(c.getRequiredPass()>maxReqPass&&c.getRequiredPass()!=Integer.MAX_VALUE)
			maxReqPass = c.getRequiredPass();
		collectors.add(c);
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
	
	//Getters to retrieve processed data
	
	public List<Collector> getCollectors(){
		return collectors;
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
	
	public List<String> getCollectorNames(){
		List<String> retval = new ArrayList<>();
		for(Collector collector:collectors)
			retval.add(collector.getClass().getSimpleName());
		return retval;
	}
	
	//Helpers and Common Use Processing
	
	private boolean headerContainsTests(String header, String [] desiredTests) {
		for(String test:desiredTests)
			if(header.contains(test))
				return true;
		return false;
	}

//	public TestStatePreProcessing getStatePreProcessor() {
//		return stateManager;
//	}
	
	public void setTestNames(String [] tests) {
		for(Collector collector:collectors)
			if(collector.requiresTestNames()) 
				collector.setTestNames(tests);
	}
	
	public void setTestMappings(SuiteMapping mapping) {
		for(Collector collector:collectors) 
			if(collector.requiresSuiteMapping())
				collector.setSuiteMapping(mapping);
	}
	
	public void setAssignmentNumumber(String number) {
		for(Collector collector:collectors) 
			if(collector.requiresAssignmentNum())
				collector.setAssignmentNumber(number);
	}
	
	public void setStudentName(String name) {
		for(Collector collector:collectors)
			if(collector.requiresStudentName()) 
				collector.setStudentName(name);
	}
	
	
}

	
