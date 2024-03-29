package gradingTools.logs;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

//import gradingTools.logs.bulkLogProcessing.collectors.IntervalReplayer.FineGrained.RunAvgContextBasedWorkTimeIRCollector;
//import gradingTools.logs.bulkLogProcessing.collectors.IntervalReplayer.Runs.RunAvgFixedWorkTimeIRCollector;
//import gradingTools.logs.bulkLogProcessing.collectors.IntervalReplayer.Timing.ContextBasedWorkTimeIRCollector;
//import gradingTools.logs.bulkLogProcessing.collectors.IntervalReplayer.Timing.FixedWorkTimeIRCollector;
//import gradingTools.logs.bulkLogProcessing.collectors.IntervalReplayer.Timing.TestFocusedContextBasedWorkTimeIRCollector;
//import gradingTools.logs.bulkLogProcessing.collectors.IntervalReplayer.Timing.TestFocusedFixedWorkTimeIRCollector;
import gradingTools.logs.localChecksStatistics.collectors.Collector;
import gradingTools.logs.localChecksStatistics.collectors.IntervalReplayer.*;
import gradingTools.logs.localChecksStatistics.collectors.StandardCollectors.AttemptsCollectorV2;
import gradingTools.logs.localChecksStatistics.collectors.StandardCollectors.WorkTimeCollector;
import gradingTools.logs.localChecksStatistics.compiledLogGenerator.CollectorManager;
import gradingTools.logs.localChecksStatistics.compiledLogGenerator.LocalLogDataAnalyzer;
import gradingTools.logs.localChecksStatistics.tools.LogWriter;

import static gradingTools.logs.localChecksStatistics.compiledLogGenerator.LocalLogDataAnalyzer.ALL_ASSIGNMENTS;
public class LocalChecksLogData {
	private static CollectorManager collectors=null;
	private static boolean printOutput=true;
	private static final CollectorManager attemptsCollector, workTimeCollector;
	private static final long defaultBreakTime = 5000;
	private static final File defaultDirectory = new File("./");
	static {
		attemptsCollector = new CollectorManager(new AttemptsCollectorV2());
		workTimeCollector = new CollectorManager(new WorkTimeCollector(defaultBreakTime));
	}
	
	//General Setters
	public static void setPrintOutput(boolean b) {
		printOutput=b;
	}
	
	public static void setCollectors(Collector [] collectorsArray) {
		collectors=new CollectorManager(collectorsArray);
	}
	
	
	public static List<String> getData() {
		return LocalLogDataAnalyzer.getData(defaultDirectory,ALL_ASSIGNMENTS,collectors,printOutput,null);
	}
	
	public static List<String> getData(Collector [] collectorsArray) {
		return LocalLogDataAnalyzer.getData(defaultDirectory, ALL_ASSIGNMENTS,new CollectorManager(collectorsArray),printOutput,null);
	}
	
	public static List<String> getData(String assignmentNumber){
		return LocalLogDataAnalyzer.getData(defaultDirectory,assignmentNumber,collectors,printOutput,null);
	}
	
	public static List<String> getData(File eclipseDirectory) {
		return LocalLogDataAnalyzer.getData(eclipseDirectory,ALL_ASSIGNMENTS,collectors,printOutput,null);
	}
	
	public static List<String> getData(File eclipseDirectory, Collector [] collectorsArray) {
		return LocalLogDataAnalyzer.getData(eclipseDirectory,ALL_ASSIGNMENTS, new CollectorManager(collectorsArray),printOutput,null);	
	}
	
	public static List<String> getData(File eclipseDirectory, String assignmentNumber){
		return LocalLogDataAnalyzer.getData(eclipseDirectory,assignmentNumber,collectors,printOutput,null);
	}
	
	public static List<String> getData(String assignmentNumber, Collector [] collectorsArray) {
		return LocalLogDataAnalyzer.getData(defaultDirectory, assignmentNumber,new CollectorManager(collectorsArray),printOutput,null);
	}
	
	public static List<String> getData(File eclipseDirectory, String assignmentNumber, Collector [] collectorsArray) {
		return LocalLogDataAnalyzer.getData(eclipseDirectory,assignmentNumber,new CollectorManager(collectorsArray),printOutput,null);
	}
	public static void writeData (List<String> aData, File anEclipseDirectory, String anAssignmentIndex, File output) {
//		int assignmentIndex = Integer.parseInt(anEclipseDirectory.getName().substring(anEclipseDirectory.getName().toLowerCase().indexOf("assignment")+"assignment".length()+1));
		try {
		
//		List<String> aData = getData(anEclipseDirectory, anAssignmentIndex);
		String path=output.toString()+"/assignment"+anAssignmentIndex+"_events.csv";
		File outputData=new File(path);
		outputData.mkdirs();
		if (outputData.exists()) {
			outputData.delete();
		}
		outputData.createNewFile();
		FileWriter dataOut=new FileWriter(outputData);
		LogWriter.writeToFileMultipleLines(dataOut, aData);
		} catch (Exception e) {
			System.err.println ("Could not write data:" + e);
		}
//		LogWriter.writeToFile(dataOut,dataCollection.getOrderedHeaders());
		
	}
	
	public static void transformData(File anEclipseDirectory, Collector[] aCollectors, File anOutputDirectory) {
		String anAssignmentIndex = anEclipseDirectory.getName().substring(anEclipseDirectory.getName().toLowerCase().indexOf("assignment")+"assignment".length()+1);
		List<String> aData = getData(anEclipseDirectory, anAssignmentIndex, aCollectors);
		writeData(aData, anEclipseDirectory, anAssignmentIndex, anOutputDirectory);
	}
	
	public static List<String> getAttemptsData(){
		return LocalLogDataAnalyzer.getData(defaultDirectory,ALL_ASSIGNMENTS, attemptsCollector,printOutput,null);
	}
	
	public static List<String> getAttemptsData(File eclipseDirectory){
		return LocalLogDataAnalyzer.getData(eclipseDirectory,ALL_ASSIGNMENTS, attemptsCollector,printOutput,null);
	}
	
	public static List<String> getAttemptsData(String assignmentNumber){
		return LocalLogDataAnalyzer.getData(defaultDirectory,assignmentNumber,attemptsCollector,printOutput,null);
	}
	
	public static List<String> getAttemptsData(File eclipseDirectory, String assignmentNumber){
		return LocalLogDataAnalyzer.getData(eclipseDirectory,assignmentNumber,attemptsCollector,printOutput,null);
	}
	
	public static List<String> getWorkTimeData(){
		return LocalLogDataAnalyzer.getData(defaultDirectory,ALL_ASSIGNMENTS,workTimeCollector,printOutput,null);
	}
	
	public static List<String> getWorkTimeData(File eclipseDirectory){
		return LocalLogDataAnalyzer.getData(eclipseDirectory,ALL_ASSIGNMENTS,workTimeCollector,printOutput,null);
	}
	
	public static List<String> getWorkTimeData(String assignmentNumber){
		return LocalLogDataAnalyzer.getData(defaultDirectory,assignmentNumber,workTimeCollector,printOutput,null);
	}
	
	public static List<String> getWorkTimeData(File eclipseDirectory, String assignmentNumber){
		return LocalLogDataAnalyzer.getData(eclipseDirectory,assignmentNumber,workTimeCollector,printOutput,null);
	}
	
	public static List<String> getData(File eclipseDirectory, String assignmentNumber, Collector [] collectorsArray, boolean printOutput, String ... desiredTests) {
		return LocalLogDataAnalyzer.getData(eclipseDirectory,assignmentNumber,new CollectorManager(collectorsArray),printOutput,desiredTests);
	}
	
	public static void writeDataToFile(List<String> data, File output) throws IOException {
		FileWriter writeTo = new FileWriter(output);
		for(String dataPoint:data)
			writeTo.write(dataPoint);
		writeTo.close();
	}
	
}
