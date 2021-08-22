package gradingTools.logs;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import gradingTools.logs.localChecksStatistics.collectors.Collector;
import gradingTools.logs.localChecksStatistics.collectors.IntervalReplayer.*;
import gradingTools.logs.localChecksStatistics.collectors.StandardCollectors.AttemptsCollectorV2;
import gradingTools.logs.localChecksStatistics.collectors.StandardCollectors.WorkTimeCollector;
import gradingTools.logs.localChecksStatistics.compiledLogGenerator.CollectorManager;
import gradingTools.logs.localChecksStatistics.compiledLogGenerator.LocalLogDataAnalyzer;

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
		return LocalLogDataAnalyzer.getData(defaultDirectory,-1,collectors,printOutput,null);
	}
	
	public static List<String> getData(Collector [] collectorsArray) {
		return LocalLogDataAnalyzer.getData(defaultDirectory, -1,new CollectorManager(collectorsArray),printOutput,null);
	}
	
	public static List<String> getData(int assignmentNumber){
		return LocalLogDataAnalyzer.getData(defaultDirectory,assignmentNumber,collectors,printOutput,null);
	}
	
	public static List<String> getData(File eclipseDirectory) {
		return LocalLogDataAnalyzer.getData(eclipseDirectory,-1,collectors,printOutput,null);
	}
	
	public static List<String> getData(File eclipseDirectory, Collector [] collectorsArray) {
		return LocalLogDataAnalyzer.getData(eclipseDirectory,-1, new CollectorManager(collectorsArray),printOutput,null);	
	}
	
	public static List<String> getData(File eclipseDirectory, int assignmentNumber){
		return LocalLogDataAnalyzer.getData(eclipseDirectory,assignmentNumber,collectors,printOutput,null);
	}
	
	public static List<String> getData(int assignmentNumber, Collector [] collectorsArray) {
		return LocalLogDataAnalyzer.getData(defaultDirectory, assignmentNumber,new CollectorManager(collectorsArray),printOutput,null);
	}
	
	public static List<String> getData(File eclipseDirectory, int assignmentNumber, Collector [] collectorsArray) {
		return LocalLogDataAnalyzer.getData(eclipseDirectory,assignmentNumber,new CollectorManager(collectorsArray),printOutput,null);
	}
	
	public static List<String> getAttemptsData(){
		return LocalLogDataAnalyzer.getData(defaultDirectory,-1,attemptsCollector,printOutput,null);
	}
	
	public static List<String> getAttemptsData(File eclipseDirectory){
		return LocalLogDataAnalyzer.getData(eclipseDirectory,-1,attemptsCollector,printOutput,null);
	}
	
	public static List<String> getAttemptsData(int assignmentNumber){
		return LocalLogDataAnalyzer.getData(defaultDirectory,assignmentNumber,attemptsCollector,printOutput,null);
	}
	
	public static List<String> getAttemptsData(File eclipseDirectory, int assignmentNumber){
		return LocalLogDataAnalyzer.getData(eclipseDirectory,assignmentNumber,attemptsCollector,printOutput,null);
	}
	
	public static List<String> getWorkTimeData(){
		return LocalLogDataAnalyzer.getData(defaultDirectory,-1,workTimeCollector,printOutput,null);
	}
	
	public static List<String> getWorkTimeData(File eclipseDirectory){
		return LocalLogDataAnalyzer.getData(eclipseDirectory,-1,workTimeCollector,printOutput,null);
	}
	
	public static List<String> getWorkTimeData(int assignmentNumber){
		return LocalLogDataAnalyzer.getData(defaultDirectory,assignmentNumber,workTimeCollector,printOutput,null);
	}
	
	public static List<String> getWorkTimeData(File eclipseDirectory, int assignmentNumber){
		return LocalLogDataAnalyzer.getData(eclipseDirectory,assignmentNumber,workTimeCollector,printOutput,null);
	}
	
	public static List<String> getData(File eclipseDirectory, int assignmentNumber, Collector [] collectorsArray, boolean printOutput, String ... desiredTests) {
		return LocalLogDataAnalyzer.getData(eclipseDirectory,assignmentNumber,new CollectorManager(collectorsArray),printOutput,desiredTests);
	}
	
	public static void writeDataToFile(List<String> data, File output) throws IOException {
		FileWriter writeTo = new FileWriter(output);
		for(String dataPoint:data)
			writeTo.write(dataPoint);
		writeTo.close();
	}
	
	
//	public static void main(String[] args) {
//		Collector [] collectors = {
//			new AttemptsCollectorV2(),
//			new ContextBasedWorkTimeIRCollector(),
//			new FixedWorkTimeIRCollector(),
//			new EditsIRCollector(),
//			new RunsIRCollector(),
////			new TestFocusedContextBasedWorkTimeIRCollector(),
////			new TestFocusedFixedWorkTimeIRCollector(),
//			
//		};
//		File project = new File("");
//		
//		List<String> data = getData(project,4,collectors);
//	}
	
	
}
