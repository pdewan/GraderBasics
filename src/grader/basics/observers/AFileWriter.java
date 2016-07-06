package grader.basics.observers;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import grader.basics.junit.GradableJUnitSuite;
import grader.basics.junit.GradableJUnitTest;
import grader.basics.junit.RunNotifierFactory;
import grader.basics.junit.TestCaseResult;
import grader.basics.settings.BasicGradingEnvironment;
import grader.basics.trace.JUnitLogFileCreatedOrLoaded;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

public class AFileWriter extends RunListener {
	public static final String LOG_DIRECTORY = "checkersData";
	public static final String LOG_SUFFIX = ".csv";
	protected  PrintWriter out = null;
	protected  BufferedWriter bufWriter;
	@Override
	public void testRunStarted(Description description) throws Exception {
		try {
			super.testRunStarted(description);
			Field id = Description.class.getDeclaredField("fUniqueId"); // ugh but why not give us the id?
			id.setAccessible(true);
			GradableJUnitSuite aSuite = (GradableJUnitSuite) id.get(description);
//			System.out.println (aSuite.getJUnitClass().getName());
//			Class aJunitClass = aSuite.getJUnitClass();
			System.out.println (toFileName(aSuite));
			
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
	}
	
	public static String toDirectoryName(GradableJUnitTest aTest) {
		return LOG_DIRECTORY + "/" + toFileName(aTest);
	}
	
	 void createOrLoadAppendableFile(String aFileName) {
	        String userName = BasicGradingEnvironment.get().getUserName();
	//
//			if (userName == null || userName.isEmpty())
//				userName = "";
////			else
////				userName = userName;
//			fileName = interactionLogFolder + "/" + userName + SEPARATOR + LOG_FILE_PREFIX + suffix + ".csv";
	        out = null;
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
	


}
