package gradingTools.logs.bulkLogProcessing.compiledLogGenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import gradingTools.logs.bulkLogProcessing.collectors.Collector;
import gradingTools.logs.bulkLogProcessing.collectors.StandardCollectors.AttemptsCollectorV2;
import gradingTools.logs.bulkLogProcessing.collectors.StandardCollectors.WorkTimeCollector;
import gradingTools.logs.localChecksStatistics.tools.LogWriter;

public class LocalChecksLogData {
	private static CollectorManager collectors=null;
	private static final CollectorManager attemptsCollector, workTimeCollector;
	private static final long defaultBreakTime = 5000;
	private static final File defaultDirectory = new File("./");
	static {
		attemptsCollector = new CollectorManager(new AttemptsCollectorV2());
		workTimeCollector = new CollectorManager(new WorkTimeCollector(defaultBreakTime));
	}
	
	//General Setters
	public static void setPrintOutput(boolean b) {
		LocalLogDataAnalyzer.setPrintOutput(b);
	}
	
	public static void setCollectors(Collector [] collectorsArray) {
		collectors=new CollectorManager(collectorsArray);
	}
	
	
	public static List<String> getData() {
		return LocalLogDataAnalyzer.getData(defaultDirectory,-1,collectors);
	}
	
	public static List<String> getData(Collector [] collectorsArray) {
		return LocalLogDataAnalyzer.getData(defaultDirectory, -1,new CollectorManager(collectorsArray));
	}
	
	public static List<String> getData(int assignmentNumber){
		return LocalLogDataAnalyzer.getData(defaultDirectory,assignmentNumber,collectors);
	}
	
	public static List<String> getData(File eclipseDirectory) {
		return LocalLogDataAnalyzer.getData(eclipseDirectory,-1,collectors);
	}
	
	public static List<String> getData(File eclipseDirectory, Collector [] collectorsArray) {
		return LocalLogDataAnalyzer.getData(eclipseDirectory,-1, new CollectorManager(collectorsArray));	
	}
	
	public static List<String> getData(File eclipseDirectory, int assignmentNumber){
		return LocalLogDataAnalyzer.getData(eclipseDirectory,assignmentNumber,collectors);
	}
	
	public static List<String> getData(int assignmentNumber, Collector [] collectorsArray) {
		return LocalLogDataAnalyzer.getData(defaultDirectory, assignmentNumber,new CollectorManager(collectorsArray));
	}
	
	public static List<String> getData(File eclipseDirectory, int assignmentNumber, Collector [] collectorsArray) {
		return LocalLogDataAnalyzer.getData(eclipseDirectory,assignmentNumber,new CollectorManager(collectorsArray));
	}
	
	public static List<String> getAttemptsData(){
		return LocalLogDataAnalyzer.getData(defaultDirectory,-1,attemptsCollector);
	}
	
	public static List<String> getAttemptsData(File eclipseDirectory){
		return LocalLogDataAnalyzer.getData(eclipseDirectory,-1,attemptsCollector);
	}
	
	public static List<String> getAttemptsData(int assignmentNumber){
		return LocalLogDataAnalyzer.getData(defaultDirectory,assignmentNumber,attemptsCollector);
	}
	
	public static List<String> getAttemptsData(File eclipseDirectory, int assignmentNumber){
		return LocalLogDataAnalyzer.getData(eclipseDirectory,assignmentNumber,attemptsCollector);
	}
	
	public static List<String> getWorkTimeData(){
		return LocalLogDataAnalyzer.getData(defaultDirectory,-1,workTimeCollector);
	}
	
	public static List<String> getWorkTimeData(File eclipseDirectory){
		return LocalLogDataAnalyzer.getData(eclipseDirectory,-1,workTimeCollector);
	}
	
	public static List<String> getWorkTimeData(int assignmentNumber){
		return LocalLogDataAnalyzer.getData(defaultDirectory,assignmentNumber,workTimeCollector);
	}
	
	public static List<String> getWorkTimeData(File eclipseDirectory, int assignmentNumber){
		return LocalLogDataAnalyzer.getData(eclipseDirectory,assignmentNumber,workTimeCollector);
	}
	
	public static void writeDataToFile(List<String> data, File output) throws IOException {
		FileWriter writeTo = new FileWriter(output);
		for(String dataPoint:data)
			writeTo.write(dataPoint);
		writeTo.close();
	}
	
	// pd addition
//	public static void writeData (List<String> aData, File anEclipseDirectory, String anAssignmentIndex, File output) {
////		int assignmentIndex = Integer.parseInt(anEclipseDirectory.getName().substring(anEclipseDirectory.getName().toLowerCase().indexOf("assignment")+"assignment".length()+1));
//		try {
//		
////		List<String> aData = getData(anEclipseDirectory, anAssignmentIndex);
//		String path=output.toString()+"/assignment"+anAssignmentIndex+"_events.csv";
//		File outputData=new File(path);
//		outputData.mkdirs();
//		if (outputData.exists()) {
//			outputData.delete();
//		}
//		outputData.createNewFile();
//		FileWriter dataOut=new FileWriter(outputData);
//		LogWriter.writeToFileMultipleLines(dataOut, aData);
//		} catch (Exception e) {
//			System.err.println ("Could not write data:" + e);
//		}
////		LogWriter.writeToFile(dataOut,dataCollection.getOrderedHeaders());
//		
//	}
//	public static void transformData(File anEclipseDirectory, Collector[] aCollectors, File anOutputDirectory) {
//		String anAssignmentIndex = anEclipseDirectory.getName().substring(anEclipseDirectory.getName().toLowerCase().indexOf("assignment")+"assignment".length()+1);
//		List<String> aData = getData(anEclipseDirectory, anAssignmentIndex, aCollectors);
//		writeData(aData, anEclipseDirectory, anAssignmentIndex, anOutputDirectory);
//	}
	
}
