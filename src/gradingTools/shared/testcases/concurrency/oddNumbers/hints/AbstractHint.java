package gradingTools.shared.testcases.concurrency.oddNumbers.hints;

import java.util.Arrays;
import java.util.List;

import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.observers.AnAbstractTestLogFileWriter;
import grader.basics.observers.TestLogFileWriterFactory;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import grader.basics.testcase.PassFailJUnitTestCase;
import gradingTools.shared.testcases.concurrency.oddNumbers.PostTestExecutorOfForkJoin;

public abstract class AbstractHint extends PostTestExecutorOfForkJoin {
	Class[] noHints = {};

	protected abstract Class[] previousHints();

	protected Class[] noPreviousHints() {
		return noHints;
	}

	protected abstract String hint();

//	protected boolean precedingTestsCorrect() {
//		List<PassFailJUnitTestCase> aPrecedingTestInstances = 
//				getPrecedingTestInstances();
//		for (PassFailJUnitTestCase aTestCase:aPrecedingTestInstances) {
//			if (!aTestCase.getLastResult().isPass()) {
//				return false;
//			}
//		}
//		return false;
//	}

	public static String toString(Class[] aClasses) {
		if (aClasses.length == 1) {
			return aClasses[0].getSimpleName();
		}
		String[] aSimpleNames = new String[aClasses.length];
		for (int anIndex = 0; anIndex < aSimpleNames.length; anIndex++) {
			aSimpleNames[anIndex] = aClasses[anIndex].getSimpleName();
		}
		return Arrays.toString(aSimpleNames);
	}

	private static boolean checkIfPrecedingTestIsCorrect = true;
	public static boolean isCheckIfPrecedingTestIsCorrect() {
		return checkIfPrecedingTestIsCorrect;
	}
	public static void setCheckIfPrecedingTestIsCorrect(boolean newVal) {
		checkIfPrecedingTestIsCorrect = newVal;
	}

	private static boolean checkIfPrecedingHintHasBeenExecuted = false;

	public static boolean isCheckIfPrecedingHintHasBeenExecuted() {
		return checkIfPrecedingHintHasBeenExecuted;
	}

	public static void setCheckIfPrecedingHintHasBeenExecuted(boolean newVal) {
		checkIfPrecedingHintHasBeenExecuted = newVal;
	}

	@Override
	public TestCaseResult test(Project project, boolean autoGrade)
			throws NotAutomatableException, NotGradableException {
		Class[]	aPrecedingTests = precedingTests();
		if (aPrecedingTests.length != 0) {
		String aPrecedingTestName = aPrecedingTests[0].getSimpleName() ;
		if (precedingTestsMustBeCorrected()) {
//			return partialPass(0.2, "Hint cannot be  given until you run the test "
//					+ precedingTests()[0].getSimpleName() + "\n You need to successfully execute its preceding test");
			return partialPass(0.2, "Hint cannot be  given until "+ aPrecedingTestName + " runs its checks. \n"
					+ "Look at the output of this test to see which preceding test should successfully execute");
		}
		}
		if (isCheckIfPrecedingTestIsCorrect()) {
			if (precedingTestsCorrect()) {
//		if (precedingTestCorrect()) {
				return partialPass(0.1,
						"\nNo hint needed for " + toString(precedingTests()) + " as its test is successful");
			}
		}
		if (isCheckIfPrecedingHintHasBeenExecuted()) {
//			if (precedingTestsMustBeCorrected()) {
//				return partialPass(0.2,
//						"Hint cannot be  given until you run the test " + precedingTests()[0].getSimpleName()
//								+ "\n You need to successfully execute its preceding test");
//			}
			AnAbstractTestLogFileWriter[] aFileWriters = TestLogFileWriterFactory.getFileWriter();
			AnAbstractTestLogFileWriter aFineGrainedWriter = aFileWriters[1];

			String aPreviousContents = aFineGrainedWriter.getPreviousContents();

			for (Class aPreviousHint : previousHints()) {
				String aPreviousName = aPreviousHint.getSimpleName();
				if (aPreviousContents != null && aPreviousContents.contains(aPreviousName)) {
					continue;
				}
				return fail("\nPlease execute preceding hint:" + aPreviousName
						+ " and try to follow it first. \nThen rerun the test program and ask for this hint");
			}
		}

		return partialPass(0.5, hint());
//		PassFailJUnitTestCase aTestCase = JUnitTestsEnvironment
//		.getAndPossiblyRunGradableJUnitTest(FairAllocationSmallProblem.class);
//		return null;
	}

}
