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

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;


public class AFileWriter extends RunListener {
	public static final String NAME_SEPARATOR = " ";
	public static final String LOG_DIRECTORY = "Checks";
	public static final String LOG_SUFFIX = ".csv";
	// must be consecutine indices
	public static final int DATE_INDEX = 0;
	public static final int SCORE_INDEX = 1;
	public static final int SCORE_INCREMENT_INDEX = 2;
	public static final int PASS_INDEX = 3;
	public static final int PARTIAL_PASS_INDEX = 4;
	public static final int FAIL_INDEX = 5;
	public static final int UNTESTED_INDEX = 6;
	int numRuns = 0;
	
	protected  PrintWriter out = null;
	protected  BufferedWriter bufWriter;
	protected String logFileName;
	List<String[]> previousRunData = new ArrayList();
	String[] previousRunLastLine = null;
	Set<String> previousPasses = new HashSet();
	Set<String> previousPartialPasses = new HashSet();
	Set<String> previousFails = new HashSet();
	Set<String> previousUntested = new HashSet();
	double previousScore = 0.0;
	
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

	
	GradableJUnitSuite currentSuite;
	

	public AFileWriter() {
		maybeCreateChecksFolder();
	}
	@Override
	public void testRunStarted(Description description) throws Exception {
		try {
			super.testRunStarted(description);
			Field id = Description.class.getDeclaredField("fUniqueId"); // ugh but why not give us the id?
			id.setAccessible(true);
			GradableJUnitSuite aSuite = (GradableJUnitSuite) id.get(description);
//			System.out.println (aSuite.getJUnitClass().getName());
//			Class aJunitClass = aSuite.getJUnitClass();
			if (logFileName == null) {
				logFileName = LOG_DIRECTORY + "/" + toFileName(aSuite);
				maybeReadLastLineOfLogFile(logFileName);
				maybeLoadPreviousSavedSets();
				maybeCreateOrLoadAppendableFile(logFileName);

			}
			currentSuite = aSuite;
//			System.out.println (toFileName(aSuite));
			
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
			
    }
	@Override
	public void testStarted(Description description) throws Exception {
		super.testStarted(description);

			
    }
	@Override
	public void testAssumptionFailure(Failure aFailure) {
		super.testAssumptionFailure(aFailure);

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
	@Override
	public void testRunFinished(Result aResult) throws Exception {
		super.testRunFinished(aResult);
		maybeLoadPreviousSets(currentSuite);
		numRuns++;
	}
	
	public static String toDirectoryName(GradableJUnitTest aTest) {
		return LOG_DIRECTORY + "/" + toFileName(aTest);
	}
	public static void maybeCreateChecksFolder() {
       
        File folder = new File(LOG_DIRECTORY);
        if (folder.mkdirs()) { // true if dirs made, false otherwise
            CheckersLogFolderCreated.newCase(folder.getAbsolutePath(), AFileWriter.class);
        }

    }
	
	 void maybeCreateOrLoadAppendableFile(String aFileName) {
		 if (out != null && bufWriter != null) {
			 return;
		 }
	        String aFullFileName = aFileName + LOG_SUFFIX;
	        try {
	            bufWriter
	                    = Files.newBufferedWriter(
	                            Paths.get(aFullFileName),
	                            Charset.forName("UTF8"),
	                            StandardOpenOption.WRITE,
	                            StandardOpenOption.APPEND,
	                            StandardOpenOption.CREATE);
	            out = new PrintWriter(bufWriter, true);
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
	protected void maybeReadLastLineOfLogFile(String aLogFileName) {
		File aFile = new File(aLogFileName);
		if (!aFile.exists()) {
			return;
		}
		String aLastLine = tail(aFile, 1);
		previousRunLastLine = aLastLine.split(".");
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
	
	protected void maybeLoadPreviousSets(GradableJUnitSuite aSuite) {
		if (numRuns == 0)
			return;
		currentScore = aSuite.getPreviousScore();
		currentPasses = toStringSet(aSuite.getPreviousPassClasses());
		currentPartialPasses = toStringSet(aSuite.getPreviousPartialPassClasses());
		currentFails = toStringSet(aSuite.getPreviousFailClasses());
		currentUntested = toStringSet(aSuite.getPreviousFailClasses());
		
	}
	protected void loadCurentSets(GradableJUnitSuite aSuite) {
		
		currentScore = aSuite.getPreviousScore();
		previousPasses = toStringSet(aSuite.getPassClasses());
		previousPartialPasses = toStringSet(aSuite.getPartialPassClasses());
		previousFails = toStringSet(aSuite.getFailClasses());
		previousUntested = toStringSet(aSuite.getFailClasses());
		
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
	
	protected void maybeLoadPreviousSavedSets() {
		if (previousRunLastLine == null || numRuns > 0)
			return;
		String aScore = previousRunLastLine[SCORE_INDEX].trim();
		String aScoreIncrement = previousRunLastLine[SCORE_INCREMENT_INDEX].trim();
		String[] aPasses = previousRunLastLine[PASS_INDEX].split(NAME_SEPARATOR);
		String[] aPartialPasses = previousRunLastLine[PARTIAL_PASS_INDEX].split(NAME_SEPARATOR);
		String[] aFails = previousRunLastLine[FAIL_INDEX].split(NAME_SEPARATOR);
		String[] anUntested = previousRunLastLine[UNTESTED_INDEX].split(NAME_SEPARATOR);
		previousScore = Double.parseDouble(aScore);
		previousPasses = toSet(aPasses);
		previousPartialPasses = toSet(aPartialPasses);
		previousFails = toSet(aFails);
		previousUntested = toSet(anUntested);
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
