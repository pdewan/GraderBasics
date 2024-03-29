package gradingTools.logs.bulkLogProcessing.compiledLogGenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import gradingTools.logs.bulkLogProcessing.collectors.Collector;

class LocalLogDataAnalyzer {
	private static final int TEST_PASS_INDEX=5,TEST_PARTIAL_INDEX=6,TEST_FAIL_INDEX=7,TEST_UNTESTED_INDEX=8, MAXIMUM_ASSIGNMENTS=20;
	private static boolean printOutput=true;
	private static final String nonFineGrainedFormatting = ".*Assignment#Suite\\.csv", 
								logsPath="/Logs/LocalChecks",
								localChecksCheckerDataPath=logsPath+"/LocalChecksAnalysis.csv",
								localChecksCheckerDataHeader = "Log Checked,Date,Collectors Used\n";
	
	public static void setPrintOutput(boolean b) {
		printOutput=b;
	}

	public static List<String> getData(File eclipseDirectory, int assignmentNumber, CollectorManager cm) {
		File logsDirectory = new File(eclipseDirectory.getAbsolutePath()+logsPath);
		if(!logsDirectory.exists()|| logsDirectory.isFile())
			throw new IllegalArgumentException("The path "+logsDirectory.getAbsolutePath()+" must be a directory that exists");
			
		if(assignmentNumber!=-1) {
			String assignment = nonFineGrainedFormatting.replace("#", assignmentNumber+"");
			for(File log:logsDirectory.listFiles(File::isFile))
				if(log.getName().matches(assignment))
					try {
						return runEvaluation(log,eclipseDirectory,cm);
					} catch (IOException e) {
						e.printStackTrace();
					}
			throw new IllegalArgumentException("No test log file found in directory: "+logsDirectory.getAbsolutePath()+" matching format: "+assignment);
		}
		
		File [] filesInLogs = logsDirectory.listFiles(File::isFile);
		List<String> retval = new ArrayList<>();
		for(int i=0;i<MAXIMUM_ASSIGNMENTS;i++) {
			String assignment = nonFineGrainedFormatting.replace("#", i+"");
			for(File log:filesInLogs) {
				if(log.getName().matches(assignment))
					try {
						String assignmentNote = "Processing log for assignment "+i;
						retval.add(assignmentNote);
						if(printOutput)
							System.out.println(assignmentNote);
						retval.addAll(runEvaluation(log,eclipseDirectory,cm));
						
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		}
		
		return retval;
	}
	
	public static List<String> runEvaluation(File log, File eclipseDirectory, CollectorManager cm) throws IOException {
		List<String> output = new ArrayList<>();
		List<String> logData = readLog(log);
		String [] tests = determineTests(logData);
		
		//Setting up collectors TODO add functionality to collector manager
		for(Collector c:cm.getCollectors()) {
			if(c.requiresStudentName())
				c.setStudentName("User");
			if(c.requiresTestNames())
				c.setTestNames(tests);
			if(c.requiresSuiteMapping())
				throw new IllegalArgumentException("Tests which require suite to test mapping are unable to be run currently");
		}
		
		//Collector manager processess the provided log
		try {
			cm.processLog(logData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//Gathering Data
		List<String> headers = cm.getOrderedHeaders();
		List<String> data = cm.getOrderedData();
		for(int i=0;i<headers.size();i++) {
			output.add(headers.get(i)+", "+data.get(i));
			if(printOutput)
				System.out.println(output.get(i));
		}	
		cm.reset();
		
		recordDataCheck(log, eclipseDirectory, cm);
		return output;
	}
	
	public static List<String> readLog(File log) throws FileNotFoundException{
		List<String> output = new ArrayList<>();
		Scanner scan = new Scanner(log);
		while(scan.hasNextLine()) 
			output.add(scan.nextLine());
		scan.close();
		output.remove(0); // removing the header
		return output;
	}
	
	public static String [] determineTests(List<String> logData) {
		List<String> output = new ArrayList<>();
		String [] lastLine = logData.get(logData.size()-1).replaceAll("[+-]", "").split(",");
		output.addAll(Arrays.asList(lastLine[TEST_PASS_INDEX].split(" ")));
		output.addAll(Arrays.asList(lastLine[TEST_PARTIAL_INDEX].split(" ")));
		output.addAll(Arrays.asList(lastLine[TEST_FAIL_INDEX].split(" ")));
		output.addAll(Arrays.asList(lastLine[TEST_UNTESTED_INDEX].split(" ")));
		
		String [] retval = new String[output.size()];
		output.toArray(retval);	
		return retval;
	}
	
	public static void recordDataCheck(File log, File eclipseDirectory, CollectorManager cm) throws IOException {
		File localCheckAnalysis = new File(eclipseDirectory.getAbsoluteFile()+localChecksCheckerDataPath);
		FileWriter writer;
		if(localCheckAnalysis.exists()) {
			writer= new FileWriter(localCheckAnalysis,true);
		}else {
			localCheckAnalysis.createNewFile();
			writer= new FileWriter(localCheckAnalysis,true);
			writer.append(localChecksCheckerDataHeader);
		}
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(log.getName()+",");
		sb.append(new Date().toString()+",");
		for(String collector:cm.getCollectorNames()) 
			sb.append(collector+" ");
		sb.append(",\n");
		writer.append(sb.toString());
		writer.close();
	}
}
