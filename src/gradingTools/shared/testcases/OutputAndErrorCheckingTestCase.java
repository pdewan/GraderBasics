package gradingTools.shared.testcases;

import grader.basics.execution.BasicProjectExecution;
import grader.basics.execution.ResultingOutErr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import util.misc.Common;

public abstract class OutputAndErrorCheckingTestCase extends
		MethodExecutionTest {

	public static String toRegex(String aString) {
		return MATCH_ANY + aString + MATCH_ANY;
	}

	static String[] emptyStrings = {};

	protected String[] getExpectedOutputs() {
		return emptyStrings;
	}

//	protected String correctOutputButErrorsMessage() {
//		String aPrefix = "Correct output but errors";
//		String aReasons = possibleReasonsForErrors();
//		if (aReasons.isEmpty())
//			return aPrefix;
//		return aPrefix + ". Possible reasons:" + aReasons;
//	}

//	protected double correctOutputButErrorsCredit() {
//		return 0.5;
//	}

//	protected String incorrectOutputMessage() {
//		String aPrefix = "Incorrect output";
//		String aReasons = possibleReasonsForIncorrectOutput();
//		if (aReasons.isEmpty())
//			return aPrefix;
//		return aPrefix + ". Possible reasons:" + aReasons;
//	}
	protected String getMethodName() {
		return "main";
	}

	

	protected boolean outputsMustBeInDifferentLines() {
		return true;
	}

//	protected String possibleReasonsForIncorrectOutput() {
//		return "";
//	}
//
//	protected String possibleReasonsForErrors() {
//		return "";
//	}

	public static boolean isValidOutputInDifferentLines(List<String> anOutput,
			String[] anExpectedStrings) {
		for (String anExpectedString : anExpectedStrings) {
			// if (!anOutput.contains(anExpectedString))
			// if (!anOutput.match(anExpectedString))
			if (!matchesConsuming(anOutput, anExpectedString))
				return false;
		}
		return true;
	}

	public static boolean isValidOutputInSameOrDifferentLines(
			List<String> anOutput, String[] anExpectedStrings) {
		for (String anExpectedString : anExpectedStrings) {
			// if (!anOutput.contains(anExpectedString))
			// if (!anOutput.match(anExpectedString))
			if (!matchesNonConsuming(anOutput, anExpectedString))
				return false;
		}

		return true;
	}

	protected boolean isValidOutput() {
		return isValidOutput(getOutput(), getExpectedOutputs());
	}

	protected boolean isValidOutput(String anOutput, String[] anExpectedStrings) {
		List<String> anOutputLines = new ArrayList(Arrays.asList(anOutput
				.split("\n")));

		return isValidOutput(anOutputLines, anExpectedStrings);
	}

	protected boolean isValidOutput(List<String> anOutput,
			String[] anExpectedStrings) {
		if (outputsMustBeInDifferentLines())
			return isValidOutputInDifferentLines(anOutput, anExpectedStrings);
		return isValidOutputInSameOrDifferentLines(anOutput, anExpectedStrings);
	}

	public static int indexOf(List<String> anOutputs, String anExpectedString) {
		for (int index = 0; index < anOutputs.size(); index++) {
			String anOutput = anOutputs.get(index);
			if (anOutput.matches(anExpectedString))
				return index;
		}
		return -1;

	}

	public static boolean matchesNonConsuming(List<String> anOutputs,
			String anExpectedString) {
		return indexOf(anOutputs, anExpectedString) != -1;

	}

	public static boolean matchesConsuming(List<String> anOutputs,
			String anExpectedString) {
		int index = indexOf(anOutputs, anExpectedString);
		if (index == -1)
			return false;
		anOutputs.remove(index);
		return true;
	}

//	protected boolean hasError(String anError) {
//		return !anError.isEmpty();
//	}
//
//	protected boolean hasError() {
//		return !getError().isEmpty();
//	}

	protected String[] emptyStringArray = {};

	protected void traceMainCall(String aMainName, String anInput,
			String[] anExpectedStrings) {
		System.out.println("Calling main in:" + aMainName);
		System.out.println("Providing input:" + anInput);
		System.out.println("Expecting output:"
				+ Arrays.toString(anExpectedStrings));

	};
	

	protected String[] getStringArgs() {
		return emptyStringArray;
	}

	@Override
	protected boolean doTest() throws Throwable {
		String[] aMainClasses = getClassNames();
		for (String aMainClass : aMainClasses) {
			traceMainCall(aMainClass, getInput(), getExpectedOutputs());
			// resultingOutError =
			// BasicProjectExecution.callMain(getClassName(),
			// getStringArgs(), getInput());
			resultingOutError = BasicProjectExecution.callMain(aMainClass,
					getStringArgs(), getInput());
			if (resultingOutError != null) {
				setOutputErrorStatus();
				return true;
			} else {
				System.out.println("Could not execite main class");
			}
		}
		return false;

	}

//	protected void setOutputErrorStatus() {
//		outputErrorStatus = computeOutputErrorStatus();
//	}
//
//	protected OutputErrorStatus computeOutputErrorStatus() {
//		ResultingOutErr aResult = getResultingOutErr();
//		if (aResult == null) {
//			return OutputErrorStatus.NO_OUTPUT;
//		}
//		boolean validOutput = isValidOutput();
//		boolean hasError = hasError(aResult.err);
//		if (validOutput && !hasError) {
//			return OutputErrorStatus.CORRECT_OUTPUT_NO_ERRORS;
//		}
//		if (validOutput && hasError) {
//			return OutputErrorStatus.CORRECT_OUTPUT_ERRORS;
//		}
//		if (!validOutput && !hasError) {
//			return OutputErrorStatus.INCORRECT_OUTPUT_NO_ERRORS;
//		}
//		return OutputErrorStatus.INCORRECT_OUTPUT_ERRORS;
//	}

	protected OutputErrorStatus test(String aMainName, String anInput,
			String[] anExpectedStrings) {
		traceMainCall(aMainName, anInput, anExpectedStrings);
		// Project project = CurrentProjectHolder.getOrCreateCurrentProject();
		// RunningProject runner = project.launch(anInput, 1);
		// String output = runner.await();

		ResultingOutErr aResult = BasicProjectExecution.callMain(aMainName,
				emptyStringArray, anInput);
		// System.out.println ("Input:" + anInput);
		// System.out.println ("Output:" + aResult.out);
		// System.out.println ("Errors:" + aResult.err);
		if (aResult == null) {
			return OutputErrorStatus.NO_OUTPUT;
		}
		boolean validOutput = isValidOutput(aResult.out, anExpectedStrings);
		boolean hasError = hasError(aResult.err);
		if (validOutput && !hasError) {
			return OutputErrorStatus.CORRECT_OUTPUT_NO_ERRORS;
		}
		if (validOutput && hasError) {
			return OutputErrorStatus.CORRECT_OUTPUT_ERRORS;
		}
		if (!validOutput && !hasError) {
			return OutputErrorStatus.INCORRECT_OUTPUT_NO_ERRORS;
		}
		return OutputErrorStatus.INCORRECT_OUTPUT_ERRORS;
	}

	public static void main(String[] args) {
		System.out.println("30".matches(".*30"));
		System.out.println(" 30".matches(".*30"));
		System.out.println("\nfoo is 30 \n".matches(".*30.*"));
	}

}
