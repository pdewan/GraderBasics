package grader.basics.observers;

import java.util.AbstractMap;

import org.junit.runner.Description;
import org.junit.runner.Result;

import grader.basics.junit.GradableJUnitSuite;
import grader.basics.junit.GradableJUnitTest;
import grader.basics.vetoers.AConsentFormVetoer;

public class AStandardTestLogFileWriter extends AnAbstractTestLogFileWriter{

	public AStandardTestLogFileWriter() {
		super();
	}
	
	@Override
	public void testRunStarted(Description description) throws Exception {
		try {
			super.testRunStarted(description);
			if (idField == null) {
		    idField = Description.class.getDeclaredField("fUniqueId"); // ugh but why not give us the id?
			idField.setAccessible(true);
			}
			AbstractMap.SimpleEntry<GradableJUnitSuite, GradableJUnitTest> anEntry = 
				(AbstractMap.SimpleEntry<GradableJUnitSuite, GradableJUnitTest>) idField.get(description);
				
			GradableJUnitSuite aTopLevelSuite = anEntry.getKey();
			Class aTopLevelSuiteClass = aTopLevelSuite.getJUnitClass();
			GradableJUnitTest aTestOrSuiteSelected = anEntry.getValue();
			Class aTestOrSuiteSelectedClass = aTestOrSuiteSelected.getJUnitClass();

			
			
//			GradableJUnitSuite aSuite = (GradableJUnitSuite) idField.get(description);
//			System.out.println (aSuite.getJUnitClass().getName());
//			Class aJunitClass = aSuite.getJUnitClass();
			if (numRuns == 0) {
				totalTests = aTopLevelSuite.getLeafClasses().size();				
				logFileName = AConsentFormVetoer.LOG_DIRECTORY + "/" + toFileName(aTopLevelSuite) + LOG_SUFFIX;
				
//				if (!maybeReadLastLineOfLogFile(logFileName)) {
//					return;
//				};
				if (maybeReadLastLineOfLogFile(logFileName)) {
				maybeLoadSavedSets();
				maybeCreateOrLoadAppendableFile(logFileName);
				}

			}
			currentTopSuite = aTopLevelSuite;
			currentTest = description.getClassName();
			saveState();
//			System.out.println (toFileName(aSuite));
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
			
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
	
}
