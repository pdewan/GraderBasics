package gradingTools.logs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import util.annotations.Explanation;
import util.annotations.MaxValue;
import util.misc.Common;
import util.trace.Tracer;

public class DifficultyCommandsExtractor {
	static Set<String> commandFilesProcessed = new HashSet();
	static Set<String> metricsFilesProcessed = new HashSet();
	static Set<String> localChecksFilesProcessed = new HashSet();


//	static List<String> difficultyCommands = new ArrayList();
//	static List<Date> difficultyDates = new ArrayList();
	static List<DatedDifficultyContext> difficultyContext = new ArrayList();
	static Date firstDate = null;
	static Date lastDate = null;

	
	static int numDifficultyCommands;
	static int numCommands;
	static int numFilledDifficultyCommands;
	

	static StringBuffer stringBuffer = new StringBuffer();
	static final String LOG_DIRECTORY = "Logs";
	static final String ECLIPSE_LOGS = "Eclipse";
	static final String METRICS_LOGS = "Metrics";
	static final String LOCAL_CHECKS_LOGS = "LocalChecks";
	static final String DIFFICULTY_FILE_NAME = "DifficultiesOnly.txt";
	static final String DIFFICULTY_CONTEXT_FILE_NAME = "DifficultiesWithContext.txt";
	static final int DIFFICULTY_CONTEXT_MINUTES = 5;
	static final int DIFFICULTY_CONTEXT_MS = DIFFICULTY_CONTEXT_MINUTES*60*1000;


	
	
	public static void init() {
		commandFilesProcessed.clear();
		metricsFilesProcessed.clear();
		localChecksFilesProcessed.clear();
//		difficultyCommands.clear();
		difficultyContext.clear();

		numDifficultyCommands = 0;
		numCommands = 0;
		numFilledDifficultyCommands = 0;
//		difficultyDates.clear();
	}

	public static boolean hasDifficultyCommandStart(String aLine) {
		return aLine.contains("\"DifficultyCommand\"");
	}
	public static boolean hasCommandStart(String aLine) {
		return aLine.startsWith("  <Command __id");
	}
	public static boolean hasCommandEnd(String aLine) {
		return aLine.contains("</Command>");
	}
	public static boolean isFilledLine(String aLine) {
		return !aLine.contains("CDATA[]");
	}
	public static String getFilledData(String aLine) {
		return StringUtils.substringBetween(aLine, "CDATA[", "]");
	}
	public static boolean  fillDifficultyCommandsInLogDirectory (String aFolderName) {
		
		return fillDifficultyCommandsInLogDirectory(new File(aFolderName));
		
		
	}
public static boolean  fillDifficultyCommandsInEclipseDirectory (File aFolder) {
		Tracer.info(DifficultyCommandsExtractor.class, "Eclipse Directory:" + aFolder);
		
		if (!aFolder.exists())
			return false;		
		File[] aChildren = aFolder.listFiles();
		for (File aFile:aChildren) {
			String aFileName = aFile.getName();
			if (aFileName.startsWith("Log") && (aFileName.endsWith(".xml"))) {
				fillDificultyCommands(aFile);
			}
		
		}
		return numCommands > 0;
		
	}
public static void  processMetricsDirectory (File aFolder) {
	Tracer.info(DifficultyCommandsExtractor.class, "Metrics Directory:" + aFolder);
	
	if (!aFolder.exists())
		return ;		
	File[] aChildren = aFolder.listFiles();
	for (File aFile:aChildren) {
		String aFileName = aFile.getName();
		if (aFileName.startsWith(METRICS_LOGS) && (aFileName.endsWith(".csv"))) {
			processMetrics(aFile);
		}
	
	}
	
}
public static void  processLocalChecksDirectory (File aFolder) {
	Tracer.info(DifficultyCommandsExtractor.class, "Local Checks Directory:" + aFolder);
	
	if (!aFolder.exists())
		return ;		
	File[] aChildren = aFolder.listFiles();
	for (File aFile:aChildren) {
		String aFileName = aFile.getName();
		if ((aFileName.endsWith(".csv"))) {
			processLocalChecks(aFile);
		}
	
	}
	
}

//public static void sortDifficultyDates() {
//	Collections.sort(difficultyDates);
//}

public static boolean  processLogsInProjectFolder (File aFolder) {
	boolean aFilled = fillDifficultyCommandsInProjectDirectory (aFolder);
	if (!aFilled)
		return aFilled;
	Tracer.info(DifficultyCommandsExtractor.class, "Project Directory:" + aFolder);
	File[] aChildren = aFolder.listFiles();
	for (File aChild:aChildren) {
		if (aChild.getName().contains(LOG_DIRECTORY)) {
			 processLogsInLogDirectory(aChild); 			
		}	
	}
	return aFilled;
}

public static boolean  fillDifficultyCommandsInProjectDirectory (File aFolder) {
	Tracer.info(DifficultyCommandsExtractor.class, "Project Directory:" + aFolder);
	File[] aChildren = aFolder.listFiles();
	boolean found = false;
	for (File aChild:aChildren) {
		if (aChild.getName().contains(LOG_DIRECTORY)) {
			 found = fillDifficultyCommandsInLogDirectory(aChild); 			
		}	
	}
	if (found) {
		Collections.sort(difficultyContext);
		try {
		FileWriter writer = new FileWriter(aFolder+"/" + DIFFICULTY_FILE_NAME); 
		for(DatedDifficultyContext aContext: difficultyContext) {
		  writer.write(aContext.difficultyContext );
		}
		writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
//		Path out = Paths.get(aFolder+"/" + DIFFICULTY_FILE_NAME);
//		try {
////			Files.write(out, difficultyCommands);
//			Files.write(out, difficultyCommands);
//
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	return found;
}


public static boolean  fillDifficultyCommandsInLogDirectory (File aFolder) {
		
		if (!aFolder.exists())
			return false;	
		Tracer.info(DifficultyCommandsExtractor.class, "Log Directory:" + aFolder);

		File[] aChildren = aFolder.listFiles();
		boolean foundEclipseLogs = false;
		for (File aChild:aChildren) {
			if (aChild.getName().contains(ECLIPSE_LOGS)) {
				
				foundEclipseLogs = fillDifficultyCommandsInEclipseDirectory(aChild);
				
			}
			if (aChild.getName().contains(LOG_DIRECTORY)) {
				foundEclipseLogs = fillDifficultyCommandsInLogDirectory(aChild);
				
			}		
		}
		return foundEclipseLogs;
		
	}
public static void  processLogsInLogDirectory (File aFolder) {
	
	if (!aFolder.exists())
		return ;	
	Tracer.info(DifficultyCommandsExtractor.class, "Log Directory:" + aFolder);

	File[] aChildren = aFolder.listFiles();
	for (File aChild:aChildren) {
		if (aChild.getName().contains(METRICS_LOGS)) {
			
			processMetricsDirectory(aChild);
			
		}
		if (aChild.getName().contains(LOCAL_CHECKS_LOGS)) {
			
//			foundEclipseLogs = fillDifficultyCommandsInEclipseDirectory(aChild);
			processLocalChecksDirectory(aChild);
			
		}
		if (aChild.getName().contains(LOG_DIRECTORY)) {
			processLogsInLogDirectory(aChild);
			
		}		
	}
	
}
public static void fillDificultyCommands (String aFileName) {
	fillDificultyCommands(new File(aFileName));
	
}
	public static void fillDificultyCommands (File aFile) {
		String aShortName = aFile.getName();
		if (commandFilesProcessed.contains(aShortName)) {
			Tracer.info(DifficultyCommandsExtractor.class, "Skipping Duplicate Eclipse Command File:" + aFile);
			return;

		}
		commandFilesProcessed.add(aShortName);
		Tracer.info(DifficultyCommandsExtractor.class, "Eclipse Command File:" + aFile);
		

		try {
			Scanner aScanner = new Scanner(aFile);
			while (fillNextDifficultyCommand(aScanner)) {
			}
			aScanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	public static void processMetrics (File aFile) {
		String aShortName = aFile.getName();
		if (metricsFilesProcessed.contains(aShortName)) {
			Tracer.info(DifficultyCommandsExtractor.class, "Skipping Duplicate Metrics File:" + aFile);
			return;

		}
		metricsFilesProcessed.add(aShortName);
		Tracer.info(DifficultyCommandsExtractor.class, "Metrics File:" + aFile);
		

		try {
			Scanner aScanner = new Scanner(aFile);
			while (aScanner.hasNextLine()) {
				String aLine = aScanner.nextLine();
				String[] aComponents = aLine.split(",");
				if (aComponents.length > 2) {
					String aDateString = aComponents[2];
					try {
					Date aDate = new Date(aDateString);
					System.out.println("metrics date" + aDate);

					} catch (Exception e) {
						
					}
				}
			}
			
			aScanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	public static void processLocalChecks (File aFile) {
		String aShortName = aFile.getName();
		if (localChecksFilesProcessed.contains(aShortName)) {
			Tracer.info(DifficultyCommandsExtractor.class, "Skipping Duplicate Local Checks File:" + aFile);
			return;

		}
		localChecksFilesProcessed.add(aShortName);
		Tracer.info(DifficultyCommandsExtractor.class, "Localchecks File:" + aFile);
		

		try {
			Scanner aScanner = new Scanner(aFile);
			while (aScanner.hasNextLine()) {
				String aLine = aScanner.nextLine();
				String[] aComponents = aLine.split(",");
				if (aComponents.length > 1) {
					String aDateString = aComponents[1];
					try {
					Date aDate = new Date(aDateString);
					System.out.println("local checks date" + aDate);

					} catch (Exception e) {
						
					}
				}
			}
			aScanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	public static String getDateFromEclipseCommand(String aLine) {
		//date="Sat Nov 16 09:07:39 EST 2019"
		return StringUtils.substringBetween(aLine, "date=\"", "\"");
	}
	public static String getDifficultyTypeFromEclipseCommand(String aLine) {
		// type="Surmountable">
		return StringUtils.substringBetween(aLine, " type=\"", "\"");
	}
	public static void processMetricsFile (Scanner aScanner) {
		try {
			while (aScanner.hasNextLine()) {
				String aLine = aScanner.nextLine();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public static boolean fillNextDifficultyCommand (Scanner aScanner) {
		try {
			int prevCommands = numCommands; 
			while (aScanner.hasNextLine()) {
				String aLine = aScanner.nextLine();
//				System.out.println(aLine);
				if (!hasCommandStart(aLine)) continue;
				numCommands++;
				String aDateString = getDateFromEclipseCommand(aLine);
				if (aDateString == null) {
					continue;
				}
				Date aDate = new Date(aDateString);
				if (firstDate == null) {
					firstDate = aDate;
					lastDate = aDate;
				} else {
					if (lastDate.compareTo(aDate) < 0) {
						lastDate = aDate;
					}
				}
				


				if (!hasDifficultyCommandStart(aLine)) continue;
				
				stringBuffer.setLength(0);
				String aTypeString = getDifficultyTypeFromEclipseCommand(aLine);
				DatedDifficultyContext aDatedContext = new DatedDifficultyContext();
				difficultyContext.add(aDatedContext);
				aDatedContext.date = aDate;
//				difficultyDates.add(aDate);
				stringBuffer.append(aDate + "(" + aTypeString + ")" + "\n");
				boolean hasFilledLine = false;
				while (aScanner.hasNextLine()) {
					String aDifficultyLine = aScanner.nextLine();
					if (hasCommandEnd(aDifficultyLine)) {
//						stringBuffer.append("\n");
//						difficultyCommands.add(stringBuffer.toString());
						aDatedContext.difficultyContext = stringBuffer.toString();
						

						numDifficultyCommands++;
						if (hasFilledLine) {
							numFilledDifficultyCommands++;
							Tracer.info(DifficultyCommandsExtractor.class, "Filled Difficulty Command:" + stringBuffer);

							aDatedContext.hasExpanation = true;
						} else {
							Tracer.info(DifficultyCommandsExtractor.class, "Unfilled Difficulty Command:" + stringBuffer);

						}
						break;
					}
					String aFilledData = getFilledData(aDifficultyLine);
//					System.out.println("jjj" + aDifficultyLine);
					if (aFilledData != null && !aFilledData.isEmpty()) {
						hasFilledLine = true;
						stringBuffer.append("\t" + aFilledData + "\n");
//						aDatedContext.hasExpanation = true;
//						numFilledDifficultyCommands++;
						
					}	
					
				}
			}
			return (prevCommands != numCommands);

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}
	public static List<DatedDifficultyContext> getDifficultyContext() {
		return difficultyContext;
	}
	public static Date getFirstDate() {
		return firstDate;
	}

	public static Date getLastDate() {
		return lastDate;
	}
	
	public static int getNumFilledDifficultyCommands() {
		return numFilledDifficultyCommands;
	}
	

}
