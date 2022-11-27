package grader.basics.observers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.print.attribute.HashAttributeSet;

import org.junit.runner.Description;
import org.junit.runner.Result;

import grader.basics.junit.GradableJUnitTest;
import grader.basics.project.CurrentProjectHolder;
import grader.basics.project.Project;
import grader.basics.project.source.ABasicTextManager;
import grader.basics.vetoers.AConsentFormVetoer;
import name.fraser.neil.plaintext.diff_match_patch;
import name.fraser.neil.plaintext.diff_match_patch.Diff;
import util.misc.Common;

public class ASourceAndTestLogWriter extends AFineGrainedTestLogFileWriter {
	static final String SOURCE_LOG_FILE_NAME_MODIFIER = "_sources_log";
	static final String SOURCE_LOG_FILE_NAME_SUFFIX = ".txt";
	static final String LAST_SOURCES_FILE_NAME_MODIFIER = "_last_sources";
	static final String LAST_SOURCES_FILE_NAME_SUFFIX = ".txt";
	static final String REPLAYED_SOURCE_FILE_NAME_MODIFIER = "_replayed";
	static final String REPLAYED_SOURCE_FILE_NAME_SUFFIX = ".txt";

	Project project;
	List<String> lastSourcesLines;
	List<String> currentSourcesLines;

	Map<String, String> lastSourcesMap;
	Map<String, String> currentSourcesMap;
	Map<String, String> diffMap;
	String lastSourceFileName;
	String sourceLogFileName;
	String replayedSourceFileName;
	boolean isAppended;
	String[] emptyStrings = {};

	
	public ASourceAndTestLogWriter() {
		instance = this;
//		appendChanges();
	}
	
	protected static final String SESSION_END = "//SESSION END";
	protected static final String SESSION_START = "//SESSION START";

	public  String toLogEntry(String aText) {
		StringBuffer retVal = new StringBuffer(ABasicTextManager.MAX_SOURCE_SIZE);
		int aSessionNumber = getSessionNumber();
		retVal.append(SESSION_START + "\n" + aSessionNumber + ",");		
		Date aDate = getLastDate();
		if (aDate == null) {
			aDate = new Date (System.currentTimeMillis());
		}
		retVal.append(aDate.toString() + "\n");
		retVal.append(aText);
		retVal.append("\n");
		retVal.append(SESSION_END);
		return retVal.toString();
		
	}
//	public void append(String aFile, String aText) {
//		boolean firstLine = false;
//		try {
//			File file = new File(aFile);
//			if (!file.exists()) {
////			file.mkdirs();
//			file.createNewFile();
//			firstLine = true;
//			}
//			
//			FileWriter fr = new FileWriter(file, true);
//			if (!firstLine) {
//			fr.write("\n");
//			}
//			int aSessionNumber = getSessionNumber();
//			fr.write(SESSION_START + Integer.toString(aSessionNumber));
//			fr.write(",");
//			Date aDate = getLastDate();
//			if (aDate == null) {
//				aDate = new Date (System.currentTimeMillis());
//			}
//			fr.write(aDate.toString());
//			fr.write("\n");
//			fr.write(aText);
//			fr.write("\n");
//			fr.write(SESSION_END);
//			fr.write("\n");
//			fr.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	public void append(String aFile, String aText) {
		boolean firstLine = false;
		try {
			File file = new File(aFile);
			if (!file.exists()) {
//			file.mkdirs();
			file.createNewFile();
			firstLine = true;
			}
			
			FileWriter fr = new FileWriter(file, true);
			if (!firstLine) {
			fr.write("\n");
			}
			fr.write(aText);
//			int aSessionNumber = getSessionNumber();
//			fr.write(SESSION_START + Integer.toString(aSessionNumber));
//			fr.write(",");
//			Date aDate = getLastDate();
//			if (aDate == null) {
//				aDate = new Date (System.currentTimeMillis());
//			}
//			fr.write(aDate.toString());
//			fr.write("\n");
//			fr.write(aText);
//			fr.write("\n");
//			fr.write(SESSION_END);
			fr.write("\n");
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void append(String aText) {
		append(getSourceLogFileName(), aText);
	}
	public void appendChanges() {
		Map<String, String> aDiffMap = getDiffMap();
		String aNextText = ABasicTextManager.toString(aDiffMap);
		
		if (!aNextText.isEmpty()) {
			String aNextLogEntry = toLogEntry(aNextText);
			try {
//				LogSender.sendToServer(fullTrace.toString(), topLevelInfo, numTotalRuns);
				getLogSender().addToQueue(aNextLogEntry, getTopLevelInfo(), numTotalRuns);
			}catch(Exception e) {
//				System.err.println("Error resolving local checks server sending");
//				System.err.println("Thrown message:\n"+e.getMessage());
			}
		append( aNextLogEntry);
		}
	}
	protected String getSourceLogFileName() {
		if (sourceLogFileName == null) {
			sourceLogFileName = getProject().getProjectFolder().getAbsolutePath() + "/" + AConsentFormVetoer.LOG_DIRECTORY + "/" + toFileName(topLevelSuite)
					+ SOURCE_LOG_FILE_NAME_MODIFIER + SOURCE_LOG_FILE_NAME_SUFFIX;
		}
		return sourceLogFileName;
//		logFileName = toFileName(aTopLevelSuite) + FILENAME_MODIFIER + LOG_SUFFIX;
	}

	protected String getReplayedSourcesFileName() {
		if (replayedSourceFileName == null) {
			replayedSourceFileName = getProject().getProjectFolder().getAbsolutePath() + "/" +
					AConsentFormVetoer.LOG_DIRECTORY + "/" + toFileName(topLevelSuite)
					+ REPLAYED_SOURCE_FILE_NAME_MODIFIER + REPLAYED_SOURCE_FILE_NAME_SUFFIX;
		}
		return lastSourceFileName;
//		logFileName = toFileName(aTopLevelSuite) + FILENAME_MODIFIER + LOG_SUFFIX;
	}

	protected String getLastSourcesFileName() {
		if (lastSourceFileName == null) {
			lastSourceFileName = getProject().getProjectFolder().getAbsolutePath() + "/" +
					AConsentFormVetoer.LOG_DIRECTORY + "/" + toFileName(topLevelSuite)
					+ LAST_SOURCES_FILE_NAME_MODIFIER + LAST_SOURCES_FILE_NAME_SUFFIX;
		}
		return lastSourceFileName;
//		logFileName = toFileName(aTopLevelSuite) + FILENAME_MODIFIER + LOG_SUFFIX;
	}

	protected List<String> getLastSourcesLines() {
		if (lastSourcesLines == null) {
//			maybeSetLastSourcesFileName();
			File aLastSourceFile = new File(getLastSourcesFileName());
			if (!aLastSourceFile.exists()) {
				lastSourcesLines = new ArrayList();
				return lastSourcesLines ;
			}
			try {
				lastSourcesLines = Files.readAllLines(aLastSourceFile.toPath(), Charset.defaultCharset());
			} catch (IOException e) {
				e.printStackTrace();
				lastSourcesLines = new ArrayList();
			
			}
		}
		return lastSourcesLines;

	}
	protected Map<String, String> getCurrentSourcesMap() {
		if (currentSourcesMap == null) {
			currentSourcesMap = getProject().getTextManager().getFileToText();
		}
		return currentSourcesMap;
	}
	
	
	protected Project getProject() {
		if (project == null) {
			project = CurrentProjectHolder.getCurrentProject();
		}
		return project;
	}
	protected void writeLastSourcesText() {
		Project aProject = getProject();
		StringBuffer aStringBuffer = aProject.getTextManager().getAllSourcesText();
		File aLastSourcesFile = new File (getLastSourcesFileName());
//		File aFile = new File ();
		try {
			Common.writeText(aLastSourcesFile, aStringBuffer.toString());
		} catch (IOException e) {
			e.printStackTrace();
			
		}
	}

//	protected void setLastSources() {
//		project.getTextManager().
//	}
//	protected void setNewSources() {
//		
//	}
	static final Map<String, String> emptyMap = new HashMap<>();
	protected Map<String, String> getLastSourcesMap() {
		if (lastSourcesMap == null) {
		List<String> aLastSourceLines = getLastSourcesLines();
		if (aLastSourceLines.isEmpty()) {
			return emptyMap;
		}
		String[] aLastSourceLinesArray = lastSourcesLines.toArray(emptyStrings);
		lastSourcesMap = 
				ABasicTextManager.extractFileContents(aLastSourceLinesArray);
		}
		return lastSourcesMap;		
	}
	
	protected static final String DELETED_FILE = "//@#$DELETED FILE&^%$";
//	public static final String NEW_FILE = "//@#$%NEW_FILE*&^%";
	
	public static Set subtract (Set anOriginal, Set aSubtracted) {
		Set retVal = new HashSet(anOriginal);
		retVal.removeAll(aSubtracted);
		return retVal;
	}
	public static Set intersect (Set anOriginal, Set aSecond) {
		Set retVal = new HashSet(anOriginal);
		retVal.retainAll(aSecond);
		return retVal;
	}
	public static final String EMPTY_STRING = "";
	public static String toDelta(String aSource, String aSecondSource) {
		if (aSource.equals(aSecondSource)) {
			return EMPTY_STRING;
		}
		diff_match_patch aDiffer = new diff_match_patch();
		LinkedList<Diff> aDiffs = aDiffer.diff_main(aSource, aSecondSource);
		String aDeltas = aDiffer.diff_toDelta(aDiffs);
//		System.out.println("Original:");
//		System.out.println(aSource);
//		System.out.println("New:");
//		System.out.println(aSecondSource);
//		System.out.println("Delta:");
//		System.out.println(aDeltas);
		return aDeltas;
	}
//	public static String toMergedFile(String aSource, String aDiff) {
//		
//		diff_match_patch aDiffer = new diff_match_patch();
//		LinkedList<Diff> aDiffs = aDiffer.diff_main(aSource, aSecondSource);
//		String aDeltas = aDiffer.diff_toDelta(aDiffs);
//		return aDeltas;
//	}
	
	public static String fromDelta (String anOriginal, String aDelta) {
//		System.out.println("Original");
//		System.out.println(anOriginal);
//		System.out.println("Delta:");
//		System.out.println(aDelta);
		diff_match_patch aDiffer = new diff_match_patch();
		LinkedList<Diff> aDiffs = aDiffer.diff_fromDelta(anOriginal, aDelta);
		String aRetVal = aDiffer.diff_text2(aDiffs);
//		System.out.println("Return:");
//		System.out.println(aRetVal);
		return aRetVal;
	}
	
	
	
//	public static void  replayToNextSession (
//			BufferedReader aBufferedReader, 
//			Map<String, String> aFileToString) {
////		while true
//	}
	
	public static int readNextSession(BufferedReader aBufferedReader, StringBuffer retVal) {
//    	StringBuffer aCurrentSessionText = new StringBuffer(ABasicTextManager.MAX_SOURCE_SIZE);
		boolean readData = false;
		int aSessionNumber = 0;
		try {
//			aBufferedReader.readLine(); //SESSION_START
		String aNextLine = aBufferedReader.readLine();
		if (aNextLine == null) {
			return -1;
		}
		while (!aNextLine.equals(SESSION_START)) {
			aNextLine = aBufferedReader.readLine();
		}
//		if (!aNextLine.equals(SESSION_START)) {
//			return -1;
//		}
		aNextLine = aBufferedReader.readLine();
		String[] aSessionAndDate = aNextLine.split(",");
		aSessionNumber = Integer.parseInt(aSessionAndDate[0 ]);
		while (true) {
		
			    aNextLine = aBufferedReader.readLine();

				if (aNextLine == null) {
					return -1;
				}
				else if (aNextLine.equals(SESSION_END)) {
					return aSessionNumber;
				} else {
					retVal.append(aNextLine);
					retVal.append("\n");
				}
						
		}
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public static void merge (Map<String, String> anOriginalSourcesMap, Map<String, String> aNextSourceDelta) {
		Set<String> aNewFiles = subtract(aNextSourceDelta.keySet(), anOriginalSourcesMap.keySet());
		Set<String> aDeletedFiles = subtract(anOriginalSourcesMap.keySet(), aNextSourceDelta.keySet());
		Set<String> aCommonFiles = intersect (anOriginalSourcesMap.keySet(), aNextSourceDelta.keySet());
		Map<String, String> retVal = new HashMap();
		for (String key:aNewFiles) {
			retVal.put(key, aNextSourceDelta.get(key).toString());
		}
		for (String key:aCommonFiles) {
			String aCurrentFileDiff = aNextSourceDelta.get(key).toString();
			String anOriginalFile = anOriginalSourcesMap.get(key).toString();
			String aCurrentFile = fromDelta (anOriginalFile.toString(), aCurrentFileDiff.toString());			
			anOriginalSourcesMap.put(key, aCurrentFile);
		}		
		for (String key:aDeletedFiles) {
			anOriginalSourcesMap.remove(key);
		}		

	}
	

			
	protected Map<String, String> getDiffMap() {
		Map<String, String> aCurrentSourcesMap = getCurrentSourcesMap();
		Map<String, String> aLastSourcesMap = getLastSourcesMap();
		

		Set<String> aNewFiles = subtract(aCurrentSourcesMap.keySet(), aLastSourcesMap.keySet());
		
		Set<String> aDeletedFiles = subtract(aLastSourcesMap.keySet(), aCurrentSourcesMap.keySet());
		Set<String> aCommonFiles = intersect (aCurrentSourcesMap.keySet(), aLastSourcesMap.keySet());
		
		Map<String, String> retVal = new HashMap();
		for (String aDeleted:aDeletedFiles) {
			retVal.put(aDeleted, DELETED_FILE);
		}
		for (String key:aNewFiles) {
			retVal.put(key, aCurrentSourcesMap.get(key).toString());
		}

		for (String key:aCommonFiles) {
//			if (key.contains("slack")) {
//				System.out.println("found slack");
//			}
			String aCurrentFile = aCurrentSourcesMap.get(key);
			String anOriginalFile = aLastSourcesMap.get(key);
			String aDiff = toDelta (anOriginalFile, aCurrentFile );
			if (aDiff == EMPTY_STRING)
				continue;
			retVal.put(key, aDiff);			
		}
		return retVal;
		// it will be nice to find renamed files
		

	}
//	
	public static String replayToSessionString(String aFile, int aSessionNumber) {
		Map<String, String> aFileToSources = replayToSessionMap(aFile, aSessionNumber);
		if (aFileToSources == null) {
			System.err.println("Nothing to replay for session:" + aSessionNumber);
			return EMPTY_STRING;
		}
		return ABasicTextManager.toString(aFileToSources);
	}
	

	public  String replayToSessioString(int aSessionNumber) {
		return replayToSessionString(getSourceLogFileName(), aSessionNumber);
	}	
	
	public static Map<String, String> replayToSessionMap(String aFile, int aSessionNumber) {
		BufferedReader aBufferedReader;
		try {
			aBufferedReader = new BufferedReader(new FileReader(aFile));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			return null;
		}
		StringBuffer aNextSessionContents = new StringBuffer(ABasicTextManager.MAX_SOURCE_SIZE);
		Map<String, String> retVal = new HashMap();

		try {
			int aNumMerges = 0;
			while (true) {
				aNextSessionContents.setLength(0);

				int aNextSession = readNextSession(aBufferedReader, aNextSessionContents);

				if (aNextSession == -1) {
					System.err.println("Log file ended before session " + aSessionNumber);
					return retVal;
				}
				
				Map<String, String> aNextSessionMap = ABasicTextManager
						.extractFileContents(aNextSessionContents.toString());
				if (aNumMerges == 0) {
					retVal = aNextSessionMap;
					
				} else {
					merge(retVal, aNextSessionMap);
					
				}
				if (aNextSession == aSessionNumber) {
					return retVal;
				}
				aNumMerges++;

			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	
	
//    public static StringBuffer readNextFile (BufferReader aBufferReader, 
//    		int aSessionNumber, 
//    		String[] aDeltaLines, 
//    		int aNumSessionsRead,
//    		Map<String, String> aFileToString
//) {
//    	Map<String, String> retVal = new HashMap<>();
//    	int aNumSessionsRead;
//    	try (BufferedReader br = new BufferedReader(new FileReader(aFile))) {
//    	    String line;
//    	    while ((line = br.readLine()) != null) {
//    	       int aNextSessionNumber = Integer.parseInt(line);
//    	       String aFileName = br.readLine();
//    	       
//    	       if (aNextSessionNumber == aSessionNumber) {
//    	    	   break;
//    	       }
//    	    }
//    	} catch (Exception e) {
//    		e.printStackTrace();
//    		System.err.println("Corrupted " + aFile + " after entries: " + aNumSessionsRead);
//    	}
//	}
	
	

	protected void writeLogData() {
		if (!isAppended) {
			appendChanges();
			writeLastSourcesText();
			isAppended = true;
		}
	}
	@Override
	public void testRunFinished(Result aResult) throws Exception {
		super.testRunFinished(aResult);
		writeLogData();
	}
	@Override
	public void testRunStarted(Description description) throws Exception {
		try {
			
			super.testRunStarted(description);
//			if (!isAppended) {
//				appendChanges();
//				writeLastSourcesText();
//				isAppended = true;
//			}
//			
			
//			getLastSourcesLines();
//			String[] aLastSourceLinesArray = lastSourcesLines.toArray(emptyStrings);
//			lastSourcesMap = 
//					ABasicTextManager.extractFileContents(aLastSourceLinesArray);
			
//			int i = 5;
//			writeLastSourcesText();
			
//			setSourceLogFileName();
//			setLastSourcesFileName();
//			project = CurrentProjectHolder.getCurrentProject();

//			System.out.println("Session number " + sessionNumber);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	static ASourceAndTestLogWriter instance;
	public static ASourceAndTestLogWriter getInstance() {
		return instance;
	}
	public static void main (String[] args) {
		String anOriginal = "hello";
		String aNew = "my hel to world";
		String aDelta = toDelta (anOriginal, aNew);
		System.out.println(aDelta);
		String aRecreatedNew = fromDelta(anOriginal, aDelta);
		System.out.println(aRecreatedNew);

	}

}
