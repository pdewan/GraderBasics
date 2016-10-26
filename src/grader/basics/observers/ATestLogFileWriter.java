package grader.basics.observers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.spi.DateFormatProvider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import grader.basics.junit.GradableJUnitSuite;
import grader.basics.junit.GradableJUnitTest;
import grader.basics.trace.CheckersLogFolderCreated;
import grader.basics.trace.JUnitLogFileCreatedOrLoaded;










import grader.basics.util.ClassComparator;
import grader.basics.vetoers.AConsentFormVetoer;

import org.junit.rules.TestName;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import util.trace.Tracer;


public class ATestLogFileWriter extends RunListener {
	public static final String NAME_SEPARATOR = " ";
	public static final String LOG_SUFFIX = ".csv";
	// must be consecutine indices
	public static final int RUN_INDEX = 0; 
	public static final int DATE_INDEX = 1;
	public static final int SCORE_INDEX = 2;
	public static final int SCORE_INCREMENT_INDEX = 3;
	public static final int TEST_NAME_INDEX = 4;
	public static final int PASS_INDEX = 5;
	public static final int PARTIAL_PASS_INDEX = 6;
	public static final int FAIL_INDEX = 7;
	public static final int UNTESTED_INDEX = 8;
	public static final String HEADER= "#,Time,%Passes,Change,Test,Pass,Partial,Fail,Untested";
	int numRuns = 0;
	int numTotalRuns = 0;
	Integer totalTests = null;
	
	protected  PrintWriter out = null;
	protected  BufferedWriter bufWriter;
	protected String logFileName;
	Field idField;
	List<String[]> previousRunData = new ArrayList();
	String[] normalizedLastLines = null;
	Set<String> previousPasses = new HashSet();
	Set<String> previousPartialPasses = new HashSet();
	Set<String> previousFails = new HashSet();
	Set<String> previousUntested = new HashSet();
	double previousScore = 0.0;
	int previousPassPercentage = 0;
	int currentPassPercentage = 0;
	
	// may not really use them
	Set<String> currentPasses = new HashSet();
	Set<String> currentPartialPasses = new HashSet();
	Set<String> currentFails = new HashSet();
	Set<String> currentUntested = new HashSet();
	double currentScore = 0.0;
	
	// sorted lists
	List<String> sortedPasses = new ArrayList();
	List<String> sortedPartialPasses = new ArrayList();
	List<String> sortedFails = new ArrayList();
	List<String> sortedUntested = new ArrayList();

	
	GradableJUnitSuite currentTopSuite;
	String currentTest;
	String lastLine, lastLineNormalized;
	

	public ATestLogFileWriter() {
		maybeCreateChecksFolder();
	}
	@Override
	public void testRunStarted(Description description) throws Exception {
		try {
			super.testRunStarted(description);
			if (idField == null) {
		    idField = Description.class.getDeclaredField("fUniqueId"); // ugh but why not give us the id?
			idField.setAccessible(true);
			}
			GradableJUnitSuite aSuite = (GradableJUnitSuite) idField.get(description);
//			System.out.println (aSuite.getJUnitClass().getName());
//			Class aJunitClass = aSuite.getJUnitClass();
			if (numRuns == 0) {
				totalTests = aSuite.getLeafClasses().size();				
				logFileName = AConsentFormVetoer.LOG_DIRECTORY + "/" + toFileName(aSuite) + LOG_SUFFIX;
				
//				if (!maybeReadLastLineOfLogFile(logFileName)) {
//					return;
//				};
				if (maybeReadLastLineOfLogFile(logFileName)) {
				maybeLoadSavedSets();
				maybeCreateOrLoadAppendableFile(logFileName);
				}

			}
			currentTopSuite = aSuite;
			currentTest = description.getClassName();
			saveState();
//			System.out.println (toFileName(aSuite));
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
			
    }
	protected void closeFile() {
		if (out == null)
			return;
		out.close();
		bufWriter = null;
	}
	@Override
	public void testStarted(Description description) throws Exception {
		super.testStarted(description);

			
    }
	@Override
	public void testAssumptionFailure(Failure aFailure) {
		super.testAssumptionFailure(aFailure);

	}
	
	
	StringBuilder passStringBulder = new StringBuilder();
	StringBuilder partialPassStringBulder = new StringBuilder();
	StringBuilder failStringBulder = new StringBuilder();
	StringBuilder untestedStringBuilder = new StringBuilder();
	StringBuilder fullTrace = new StringBuilder();
	
	

	public String getPassMarker(String aPass) {
		if (previousPasses.contains(aPass)) {
			return "";
		}
		return "+"; // things can only improve if put in pass
	}
	public String getPartialPassMarker(String aPartialPass) {
		if (previousPartialPasses.contains(aPartialPass)) {
			return "";
		}
		if (previousPasses.contains(aPartialPass))
			return "-"; 
		return "+"; // otherwise it is an improvement
	}
	public String getFailMarker(String aFail) {
		if (previousFails.contains(aFail)) {
			return "";
		}
		if (previousUntested.contains(aFail))
			return "+"; 
		return "-"; // otherwise it is a come down
	}
	public void composePassString() {
		passStringBulder.setLength(0);
		List<String> aPassList = sort(currentPasses);
		boolean aFirstName = true;

		for (String aPass:aPassList) {
			if (!aFirstName) {
				passStringBulder.append(NAME_SEPARATOR);
			} else {
				aFirstName = false;
			}
			passStringBulder.append(aPass);
			passStringBulder.append(getPassMarker(aPass));

		}
	}
	
	public void composePartialPassString() {
		partialPassStringBulder.setLength(0);
		List<String> aParialPassList = sort(currentPartialPasses);
		boolean aFirstName = true;

		for (String aPartialPass:aParialPassList) {
			if (!aFirstName) {
				partialPassStringBulder.append(NAME_SEPARATOR);
			} else {
				aFirstName = false;
			}
			partialPassStringBulder.append(aPartialPass);
			partialPassStringBulder.append(getPartialPassMarker(aPartialPass));

		}
	}
	
	public void composeFailString() {
		failStringBulder.setLength(0);
		List<String> aFailList = sort(currentFails);
		boolean aFirstName = true;
		for (String aFail:aFailList) {
			if (!aFirstName) {
				failStringBulder.append(NAME_SEPARATOR);
			} else {
				aFirstName = false;
			}
			failStringBulder.append(aFail);
			failStringBulder.append(getFailMarker(aFail));
			
		}
	}
	public void composeUntestedString() {
		untestedStringBuilder.setLength(0);
		List<String> anUntestedList = sort(currentUntested);
		boolean aFirstName = true;
		for (String anUntested:anUntestedList) {
			if (!aFirstName) {
				untestedStringBuilder.append(NAME_SEPARATOR);
			} else {
				aFirstName = false;
			}
			untestedStringBuilder.append(anUntested);
		}
	}
	public void composeTrace() {
		fullTrace.setLength(0);
		composePassString();
		composePartialPassString();
		composeFailString();
		composeUntestedString();
		fullTrace.append(""+numTotalRuns);
		Date aDate = new Date(System.currentTimeMillis());
		fullTrace.append("," + aDate);
		fullTrace.append("," + currentPassPercentage);
		fullTrace.append("," + (currentPassPercentage - previousPassPercentage));
		fullTrace.append("," + currentTest);
		fullTrace.append("," + passStringBulder + NAME_SEPARATOR);
		fullTrace.append("," + partialPassStringBulder + NAME_SEPARATOR);
		fullTrace.append("," + failStringBulder  + NAME_SEPARATOR);
		fullTrace.append("," + untestedStringBuilder + NAME_SEPARATOR + ",");
		
	}

	
	@Override
	public void testFailure(Failure aFailure) throws Exception {
	  
			super.testFailure(aFailure);
			
	}
	@Override
	public void testFinished(Description description) {
		try {
			super.testFinished(description);
			
//			System.out.println ("Test finished:"+ description);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		

	}
	public void testIgnored(Description description) throws Exception {
		super.testIgnored(description);

	}
	protected void setPassPercentage() {
		currentPassPercentage = (int) (100 * ((double) currentPasses.size())/totalTests);

	}
	@Override
	public void testRunFinished(Result aResult) throws Exception {
		try {
		super.testRunFinished(aResult);
		loadCurrentSets(currentTopSuite);
		correctUntested();
//		currentPassPercentage = (int) (100 * ((double) currentPasses.size())/totalTests);
		setPassPercentage();
		composeTrace();
		appendLine(fullTrace.toString());		
		numRuns++;
		numTotalRuns++;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String toDirectoryName(GradableJUnitTest aTest) {
		return AConsentFormVetoer.LOG_DIRECTORY + "/" + toFileName(aTest);
	}
	public static void maybeCreateChecksFolder() {
       
        File folder = new File(AConsentFormVetoer.LOG_DIRECTORY);
        if (folder.mkdirs()) { // true if dirs made, false otherwise
            CheckersLogFolderCreated.newCase(folder.getAbsolutePath(), ATestLogFileWriter.class);
        }

    }
	
	void appendLine(String aLine) {
		if (out == null) return;
		Tracer.info(this, aLine);
		out.println(aLine);
		out.flush();
	}
	
	 void maybeCreateOrLoadAppendableFile(String aFileName) {
		 if (out != null && bufWriter != null) {
			 return;
		 }
	        String aFullFileName = aFileName;
	        File aFile = new File(aFullFileName);
	        boolean aNewFile = !aFile.exists();
	        try {
	            bufWriter
	                    = Files.newBufferedWriter(
	                            Paths.get(aFullFileName),
	                            Charset.forName("UTF8"),
	                            StandardOpenOption.WRITE,
	                            StandardOpenOption.APPEND,
	                            StandardOpenOption.CREATE);
	            out = new PrintWriter(bufWriter, true);
	            if (aNewFile) {
	            	appendLine(HEADER);
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	            //Oh, no! Failed to create PrintWriter
	        }

	        JUnitLogFileCreatedOrLoaded.newCase(aFullFileName, this);

	    }
	public static String toFileName (GradableJUnitTest aTest) {
		String aClassName = aTest.getJUnitClass().getName();
		String[] aClassParts = aClassName.split("\\.");
		if (aClassParts.length <= 1) {
			return aClassName;
		}
		if (aClassParts[0].startsWith("grad") ||
				aClassParts[0].startsWith("check") ||
				aClassParts[0].startsWith("junit") ||
				aClassParts[0].startsWith("test") ||
				aClassParts[0].startsWith("suite")) {
			if (aClassParts.length == 2) {
				return aClassParts[1];
			}			
//			if (aClassParts.length == 3) {
//				String aFileName = aClassParts[2];
//				String aDirectoryName = aClassParts[1];
//				return aDirectoryName + "/" + aFileName;
//			}
//			String aDirectoryName = aClassParts[1];
//			String aFileName = aClassParts[2];
			String aFileName = aClassParts[1];
			for (int i = 2; i < aClassParts.length; i++) {
				aFileName += "_" + aClassParts[i];
			}
			return aFileName;
		} else {
			return aClassName.replaceAll(".", "_");
		}
	}
	
	protected boolean maybeReadLastLineOfLogFile(String aLogFileName) {
		File aFile = new File(aLogFileName);
		if (!aFile.exists()) {
			return true;
		}
		lastLine = tail(aFile, 1).trim();
		if (lastLine.startsWith("#")) {
			System.err.println ("Corrupt kog file " + aLogFileName + " has only header");
			return false;
		}
//		String lastLineNormalized = lastLine.replaceAll("+|-", ""); // normalize it
		lastLineNormalized = lastLine.replaceAll("\\+|-", ""); // normalize it
		normalizedLastLines = lastLineNormalized.split(",");
		return true;
	}
	public static String tail( File file, int lines) {
	    java.io.RandomAccessFile fileHandler = null;
	    try {
	        fileHandler = 
	            new java.io.RandomAccessFile( file, "r" );
	        long fileLength = fileHandler.length() - 1;
	        StringBuilder sb = new StringBuilder();
	        int line = 0;

	        for(long filePointer = fileLength; filePointer != -1; filePointer--){
	            fileHandler.seek( filePointer );
	            int readByte = fileHandler.readByte();

	             if( readByte == 0xA ) {
	                if (filePointer < fileLength) {
	                    line = line + 1;
	                }
	            } else if( readByte == 0xD ) {
	                if (filePointer < fileLength-1) {
	                    line = line + 1;
	                }
	            }
	            if (line >= lines) {
	                break;
	            }
	            sb.append( ( char ) readByte );
	        }

	        String lastLine = sb.reverse().toString();
	        return lastLine;
	    } catch( java.io.FileNotFoundException e ) {
	        e.printStackTrace();
	        return null;
	    } catch( java.io.IOException e ) {
	        e.printStackTrace();
	        return null;
	    }
	    finally {
	        if (fileHandler != null )
	            try {
	                fileHandler.close();
	            } catch (IOException e) {
	            }
	    }
	}
	public static Set<String> toSet(String[] anArray) {
		return new HashSet(Arrays.asList(anArray));
	}
	
	public static Set<String> toStringSet(Set<Class> aClassSet) {
		Set<String> result = new HashSet();
		for (Class aClass:aClassSet) {
			result.add(aClass.getSimpleName());
		}
		return result;
	}
	
	protected void computePassString() {
		
	}
	
	protected void loadCurrentSets(GradableJUnitSuite aSuite) {
		
		currentScore = aSuite.getUnroundedScore();
		currentPasses = toStringSet(aSuite.getPassClasses());
		currentPartialPasses = toStringSet(aSuite.getPartialPassClasses());
		currentFails = toStringSet(aSuite.getFailClasses());
		currentUntested = toStringSet(aSuite.getUntestedClasses());
		
		
	}
	protected void correctUntested() {
		Set<String> aCorrectUntested = new HashSet();
		for (String aClass:currentUntested) {
			if (previousPasses.contains(aClass)) {
				currentPasses.add(aClass);
			} else if (previousPartialPasses.contains(aClass)) {
				currentPartialPasses.add(aClass);
			} else if (previousFails.contains(aClass)) {
				currentFails.add(aClass);
			} else {
				aCorrectUntested.add(aClass);
			}
		}
		currentUntested = aCorrectUntested;
	}
//	protected void loadCurentSets(GradableJUnitSuite aSuite) {
//		
//		currentScore = aSuite.getPreviousScore();
//		previousPasses = toStringSet(aSuite.getPassClasses());
//		previousPartialPasses = toStringSet(aSuite.getPartialPassClasses());
//		previousFails = toStringSet(aSuite.getFailClasses());
//		previousUntested = toStringSet(aSuite.getFailClasses());
//		
//	}
	
	protected void saveState() {
		previousPasses = currentPasses;
		previousPartialPasses = currentPartialPasses;
		previousFails = currentFails;
		previousUntested = currentUntested;
		previousScore = currentScore;
		previousPassPercentage = currentPassPercentage;
	}
	
	protected List<String> sort(Set<String> aSet) {
		List<String> result = new ArrayList();
		result.addAll(aSet);
		Collections.sort(result);
		return result;
	}
	
	protected void sortCurrentSets() {
		sortedPasses = sort (currentPasses);
		sortedPartialPasses = sort (currentPartialPasses);
		sortedFails = sort (currentFails);
		sortedUntested = sort (currentUntested);	
	}
	
	protected void maybeLoadSavedSets() {
		if (normalizedLastLines == null || numRuns > 0)
			return;
		String aRunsString = normalizedLastLines[RUN_INDEX];
//		String aScore = normalizedLastLines[SCORE_INDEX].trim();
		String aScoreIncrement = normalizedLastLines[SCORE_INCREMENT_INDEX].trim();
		String[] aPasses = normalizedLastLines[PASS_INDEX].split(NAME_SEPARATOR);
		String[] aPartialPasses = normalizedLastLines[PARTIAL_PASS_INDEX].split(NAME_SEPARATOR);
		String[] aFails = normalizedLastLines[FAIL_INDEX].split(NAME_SEPARATOR);
		String[] anUntested = normalizedLastLines[UNTESTED_INDEX].split(NAME_SEPARATOR);
//		currentScore = Double.parseDouble(aScore);
		numTotalRuns = Integer.parseInt(aRunsString) + 1;
		currentPasses = toSet(aPasses);
		currentPartialPasses = toSet(aPartialPasses);
		currentFails = toSet(aFails);
		currentUntested = toSet(anUntested);
		setPassPercentage();
	}
//	public static void main (String[] anArgs) {
//		long time = System.currentTimeMillis();
//		Date aDate = new Date(time);
//		System.out.println (aDate);
//
//		Date aDate2 = new Date(aDate.toString());
//		
//		System.out.println (aDate2);
//
//		long time2 = aDate2.getTime();
//		System.out.println ("written time == read time" + (time2 - time));
//		
//		
//	}
}
