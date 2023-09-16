package mains;

import java.io.File;

import gradingTools.logs.bulkLogProcessing.collectors.Collector;
import gradingTools.logs.bulkLogProcessing.collectors.EventCollectors.TestWorkingEvent;
import gradingTools.logs.bulkLogProcessing.collectors.StandardCollectors.AttemptsCollectorV2;
import gradingTools.logs.bulkLogProcessing.collectors.StandardCollectors.DateFirstTestedCollector;
import gradingTools.logs.bulkLogProcessing.collectors.StandardCollectors.DateLastTestedCollector;
import gradingTools.logs.bulkLogProcessing.collectors.StandardCollectors.DecreasingAttemptsCollectorV2;
import gradingTools.logs.bulkLogProcessing.collectors.StandardCollectors.FinalStatusCollector;
import gradingTools.logs.bulkLogProcessing.collectors.StandardCollectors.IncreasingAttemptsCollector;
import gradingTools.logs.bulkLogProcessing.collectors.StandardCollectors.PercentPassedCollector;
import gradingTools.logs.bulkLogProcessing.collectors.StandardCollectors.TotalAttemptsCollector;
import gradingTools.logs.bulkLogProcessing.collectors.StandardCollectors.TotalTimeCollector;
import gradingTools.logs.bulkLogProcessing.compiledLogGenerator.CollectorManager;
import gradingTools.logs.bulkLogProcessing.compiledLogGenerator.SemesterLogGenerator;
import gradingTools.logs.bulkLogProcessing.selectYearMapping.Comp524Fall2020;
import gradingTools.logs.bulkLogProcessing.selectYearMapping.YearSelectFactory;
import gradingTools.logs.bulkLogProcessing.tools.ASakaiStructureAssignmentLogIteratorAndUnzipper;
import gradingTools.logs.bulkLogProcessing.tools.LogAnalyzerAssignmentLogIteractorFactory;

//import gradingTools.logs.localChecksStatistics.collectors.Collector;
//import gradingTools.logs.localChecksStatistics.collectors.IntervalReplayer.AbstractIntervalReplayerBasedCollector;
//import gradingTools.logs.localChecksStatistics.compiledLogGenerator.CollectorManager;

//import gradingTools.logs.bulkLogProcessing.collectors.AbstractIntervalReplayerBasedCollector;
//import gradingTools.logs.bulkLogProcessing.collectors.Collector;
//import gradingTools.logs.localChecksStatistics.collectors.StandardCollectors.WorkTimeCollector;
//import gradingTools.logs.bulkLogProcessing.collectors.EventCollectors.*;
//import gradingTools.logs.bulkLogProcessing.collectors.EventCollectors.FineGrained.*;
//import gradingTools.logs.bulkLogProcessing.collectors.FineGrained.*;
//import gradingTools.logs.bulkLogProcessing.collectors.IntervalReplayer.*;
//import gradingTools.logs.bulkLogProcessing.collectors.IntervalReplayer.FineGrained.*;
//import gradingTools.logs.bulkLogProcessing.collectors.IntervalReplayer.Runs.*;
//import gradingTools.logs.bulkLogProcessing.collectors.IntervalReplayer.Timing.*;
//import gradingTools.logs.bulkLogProcessing.collectors.StandardCollectors.*;
//import gradingTools.logs.bulkLogProcessing.compiledLogGenerator.CollectorManager;
//import gradingTools.logs.bulkLogProcessing.compiledLogGenerator.LocalChecksLogData;
//import gradingTools.logs.bulkLogProcessing.compiledLogGenerator.SemesterLogGenerator;
//import gradingTools.logs.bulkLogProcessing.selectYearMapping.Comp524Fall2020;
//import gradingTools.logs.bulkLogProcessing.selectYearMapping.YearSelectFactory;
//import gradingTools.logs.bulkLogProcessing.tools.concurrency.ThreadManagerFactory;
//import gradingTools.logs.bulkLogProcessing.tools.files.CSVParser;
//import gradingTools.logs.bulkLogProcessing.tools.files.LogWriter;
public class LogAnalyzerMain {

	private static final int breakTime=(int)(10*60);
	private static final int kenBreakTime= (int)(1.5*60*60);

	
	public static void main(String[] args) {
		try {

//			File data=new File("E:\\submissions_2\\Assignment 1");
//			File data = new File(args[0]);
//			File output = new File("C:\\Users\\Zhizhou\\git\\Hermes_Log_Parser\\NewVersionTest");
//			File output=new File("LocalChecks Logs");
			
			bulkProcessFromSakai();
			//runEventsAnalysis(args);
//			runAnalysis();
//			eventLogs();
//			soloTesting();
//			replayData();
//			attemptsAndTimings();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void bulkProcessFromSakai() throws Exception {
		File [] inputs = {
//			new File("I:\\Research\\Log_Parsing\\ClassFolders\\Comp524\\Fall2020"),
				new File("D:\\sakaidownloads_anonymyzed\\Comp524\\F22"),
		
		};
		File [] outputs = {
			new File("D:\\testoutput"),
		};
		
		Collector [] collectors = {
			new TotalAttemptsCollector(),
		//		new WorkingSetStatisicsCollector(),
		//		new TestingSetStatisicsCollector(),
		//		new NaiveAttemptsCollector(),
			
			new PercentPassedCollector(4),
			new TotalTimeCollector(),
			new DateFirstTestedCollector(),
			new DateLastTestedCollector(),
			
			new AttemptsCollectorV2(),
		//		new WorkTimeCollector(breakTime, false),
			new IncreasingAttemptsCollector(),
			new DecreasingAttemptsCollectorV2(),
			new FinalStatusCollector(),
		};
	
	//YearSelectFactory.setYearMap(new Comp524Fall2020());
	LogAnalyzerAssignmentLogIteractorFactory.setIterator(ASakaiStructureAssignmentLogIteratorAndUnzipper.class);
	for(int i=0;i<inputs.length;i++) {
		try {
			new SemesterLogGenerator(collectors,true,"assignment#.csv").generateData(inputs[i], outputs[i]);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}		
		
		
	}
	
//	private static void attemptsAndTimings() throws Exception {
////		final File rawFolder = new File("I:\\Research\\Log_Parsing\\ClassFolders\\Comp301\\Summer20");
//		final String pathing = "Assignment "+AbstractIntervalReplayerBasedCollector.numberReplace+"_named/Assignment "+AbstractIntervalReplayerBasedCollector.numberReplace;
//		Collector [] collectors = {
////				new WorkTimeCollector(0),
//				
////				new AttemptsCollectorV2(),
////				new AvgTimeToSolveIRCollector(rawFolder,pathing),
////				new AvgTestFocusedTimeToSolveIRCollector(rawFolder,pathing),
////				new TotalSessionsCollector(),
////				new TestPassPercentCollector(),
////				new TestScorePercentCollector(),
////				new DateFirstTestedCollector(),
////				new DateLastTestedCollector(),
////				new ContextBasedWorkTimeIRCollector(rawFolder,pathing),
////				new FixedWorkTimeIRCollector(rawFolder,pathing),
////				new TestFocusedFixedWorkTimeIRCollector(rawFolder,pathing),
////				new TestFocusedContextBasedWorkTimeIRCollector(rawFolder,pathing),
//				new TotalImprovementsCollector(),
//				new NumTestsFixedInXMinCollector(0.5),
//				new NumTestsFixedInXMinCollector(1),
////				new NumTestsFixedInXMinCollector(3),
//				new NumTestsFixedInXMinCollector(5),
////				new NumTestsFixedInXMinCollector(10),
////				new NumTestsFixedInXMinCollector(30),
////				new NumTestsFixedInXMinCollector(60),
////				new NumTestsFixedInXMinCollector(300),
////				new NumTestsFixedInXMinCollector(60*24*21),
////				new RunAttempts(),
//				new FileSpacingCollector(),
//				new TestFocusedFixesInXMinCollector(0.5),
//				new FileSpacingCollector(),
//				new TestFocusedFixesInXMinCollector(1),
//				new FileSpacingCollector(),
//				new TestFocusedFixesInXMinCollector(5),
//		};
//		
//		File [] inputs = {
//				new File("<>"),
//		};
//		File [] outputs = {
//				new File("<>"),
//		};
//
//		
//		for(int i=0;i<inputs.length;i++) {
//			try {
//				SemesterLogGenerator smg = new SemesterLogGenerator(collectors,true,"assignment#_runAttempts.csv");
//				smg.generateData(inputs[i], outputs[i],4);
//				ThreadManagerFactory.terminateThreadManager();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		
//	}
	
	
//	private static void replayData() throws Exception {
//		final File rawFolder = new File("<>");
//		final String pathing = "Assignment "+AbstractIntervalReplayerBasedCollector.numberReplace+"_named/Assignment "+AbstractIntervalReplayerBasedCollector.numberReplace;
//	
//		Collector [] collectors = {
//			new ContextBasedWorkTimeIRCollector(rawFolder,pathing),
//			new FixedWorkTimeIRCollector(rawFolder,pathing),
//			new RunAvgFixedWorkTimeIRCollector(rawFolder,pathing),
//			new RunAvgContextBasedWorkTimeIRCollector(rawFolder,pathing),
////			new EditsIRCollector(rawFolder,pathing),
////			new RunsIRCollector(rawFolder,pathing),
//			new TestFocusedFixedWorkTimeIRCollector(rawFolder,pathing),
//			new TestFocusedContextBasedWorkTimeIRCollector(rawFolder,pathing),
//		};
//	
//	
//		File [] inputs = {
//				new File("<>"),
//		};
//		File [] outputs = {
//				new File("<>"),
//		};
//
//		
//		for(int i=0;i<inputs.length;i++) {
//			try {
//				
//				SemesterLogGenerator smg = new SemesterLogGenerator(collectors,true,"assignment#_IntervalReplayer.csv");
//				smg.generateData(inputs[i], outputs[i]);
//				ThreadManagerFactory.terminateThreadManager();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		
//	}
	
//	private static void runAnalysis() {
//		File [] inputs = {
//				new File("<>"),
//				
//		};
//		File [] outputs = {
//				new File("<>"),
//		};
//		
//		Collector [] collectors = {
//				new TotalAttemptsCollector(),
////				new WorkingSetStatisicsCollector(),
////				new TestingSetStatisicsCollector(),
////				new NaiveAttemptsCollector(),
//				
//				new PercentPassedCollector(4),
//				new TotalTimeCollector(),
//				new DateFirstTestedCollector(),
//				new DateLastTestedCollector(),
//				
//				new AttemptsCollectorV2(),
////				new WorkTimeCollector(breakTime, false),
//				new IncreasingAttemptsCollector(),
//				new DecreasingAttemptsCollectorV2(),
//				new FinalStatusCollector(),
//		};
//		
//		YearSelectFactory.setYearMap(new Comp524Fall2020());
//		
//		for(int i=0;i<inputs.length;i++) {
//			try {
//				new SemesterLogGenerator(collectors,true,"assignment#.csv").generateData(inputs[i], outputs[i]);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		
//	}
	
//	private static void soloTesting() {
//		File directory = new File("<>");
//		Collector [] collectors = {
//				new TotalAttemptsCollector(),
//				new AttemptsCollectorV2(),
//		};
//		LocalChecksLogData.getData(directory,3,collectors);
//	}
//	
//	private static void eventLogs() {
//		
//		File [] inputs = {
//				new File("<>"),
//				
//		};
//		File [] outputs = {
//				new File("<>"),
//		};
//		
//		YearSelectFactory.setYearMap(new Comp524Fall2020());
//		
//		Collector [] test = {new TestWorkingEvent()};
//		Collector [] suite = {new SuiteWorkingEvent()};
//		Collector [] tts = {new TestToSuiteWorkingEvent()};
//		Collector [] btc = {new BreakEvent(breakTime)};
//		
//		for(int i=0;i<inputs.length;i++) {
//			try {
//				new SemesterLogGenerator(suite,false,"assignment#_suiteEvents.csv").generateData(inputs[i], outputs[i]);
//				new SemesterLogGenerator(test,false,"assignment#_testEvents.csv").generateData(inputs[i], outputs[i]);
//				new SemesterLogGenerator(tts,false,"assignment#_testToSuiteEvents.csv").generateData(inputs[i], outputs[i]);
//				new SemesterLogGenerator(btc,false,"assignment#_breakEvents.csv").generateData(inputs[i], outputs[i]);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}
	
	private static void runEventsAnalysis(String[] args) {
		File [] inputs = {
				//new File(args[0]),
				new File("I:\\Research\\Log_Parsing\\Research\\LogParser\\InputFolders\\Comp533\\Spring20\\0c1e468e4e4b1d39812fd2c8c5a0cba81828a5682eced2a6ca1e50afe43e42, 13ae3ed6fe76d459c9c66fe38ff187593561a1f24d34cb22e06148c77e4cc02b(2627dc3fbf149f58a61f9f94d28baed5bb59cf68f64b4ff611d3bc54a0cb81da)\\comp533s20_assignment2_Assignment2Suite.csv")
		};
		File [] outputs = {
				//new File("LocalChecks Logs"),
				new File("scripts/events.csv"),
		};
		
		Collector [][] recursives = {
//				{new TestWorkingSetEvent("ListToStringDeepChecker")},
//				{new TestWorkingSetEvent("ListToStringChecker")},
//				{new TestWorkingSetEvent("BaseCaseSExpressionToStringChecker")},
//				{new TestWorkingSetEvent("BaseCaseListToStringChecker")}
//				{new TestRunsCollector()},
				{new TestWorkingEvent()},
//				{new TestStatusEvent()},
//				{new TestAttemptTimeEvent()},
		};
		
		
		
//		YearSelectFactory.setYearMap(new Comp524Fall2020());
		CollectorManager.enableConcurrency=false;
		for(int i=0;i<recursives.length;i++) {
			try {
//				new SemesterLogGenerator(recursives[i],false,i+"assignment#_events.csv").generateData(inputs[0], outputs[0]);
			    SemesterLogGenerator aSemesterLogGenerator = new SemesterLogGenerator(recursives[i],false,i+"assignment#_events.csv");
			    aSemesterLogGenerator.generateDataSingleAssignmentWithHeaders(inputs[0], outputs[0], "0");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * This was just used to correct data displaying in the WorkTimeStats Collector
	 */
//	private static void fixCSVData() throws Exception {
//		for(int i=0;i<7;i++) {
//			File csvLoc = new File("./NewVersionTest/Assignment"+i+"_TestWorkTimes.csv");
//			String [][] readFile = (new CSVParser(csvLoc)).readCSV();
//			
//			for(int j=0;j<readFile[0].length;j++) {
//				int lastOpen=0;
//				for(int k=0;k<readFile.length;k++) {
//					System.out.println(readFile[k][j]);
//					if(!readFile[k][j].equals("")) {
//						if(lastOpen!=k) {
//							readFile[lastOpen][j] = readFile[k][j];
//							readFile[k][j]="";
//						}
//						lastOpen++;
//					}else {
//						System.out.println(readFile[k][j]);
//					}
//				}
//			}
//			
//			FileWriter writeTo = new FileWriter(csvLoc);
//			LogWriter.writeToFileMultipleLines(writeTo, readFile);
//			writeTo.close();
//		}
//	}
	
}
