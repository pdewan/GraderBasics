package gradingTools.shared.testcases;

import grader.basics.execution.BasicProjectExecution;
import grader.basics.execution.ResultingOutErr;
import grader.basics.project.CurrentProjectHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import util.misc.Common;
import util.trace.Tracer;

public abstract class OutputAndErrorCheckingTestCase extends
		BeanExecutionTest {

//	public static String toRegex(String aString) {
//		return MATCH_ANY + aString + MATCH_ANY;
//	}

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

	public  boolean isValidOutputInDifferentLines(List<String> anOutput,
			String[] anExpectedStrings) {
		for (String anExpectedString : anExpectedStrings) {
			// if (!anOutput.contains(anExpectedString))
			// if (!anOutput.match(anExpectedString))
			if (!matchesConsuming(anOutput, anExpectedString)) {
				setIncorrectOutputDetails(anOutput, anExpectedString);

				return false;
			}
		}
		return true;
	}
	protected void setIncorrectOutputDetails(List<String> anOutput, 
			String anExpectedString) {
		incorrectOutputDetails = "Pattern:" + anExpectedString + 
				" not found in output line: " + anOutput;
		System.err.println (incorrectOutputDetails);
	}
	
//	protected String incorrectOutputDetails = ";";

	public boolean isValidOutputInSameOrDifferentLines(
			List<String> anOutput, String[] anExpectedStrings) {
		for (String anExpectedString : anExpectedStrings) {
			// if (!anOutput.contains(anExpectedString))
			// if (!anOutput.match(anExpectedString))
			if (!matchesNonConsuming(anOutput, anExpectedString)) {
				setIncorrectOutputDetails(anOutput, anExpectedString);
//				incorrectOutputDetails = "Pattern:" + anExpectedString + 
//						" not found in output line: " + anOutput;
//				System.err.println (incorrectOutputDetails);
				return false;
			}
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
		incorrectOutputDetails = "";
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
//		Tracer.info (this, "Checking if " + anExpectedString + " appears in output");
		int index = indexOf(anOutputs, anExpectedString);
		if (index == -1) {
			Tracer.error (anExpectedString + " did not appear in output");

			return false;
		}
		Tracer.info (OutputAndErrorCheckingTestCase.class, anExpectedString + "appeared in output");

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
	protected void traceStartMainCall(String aMainName, String anInput,
			String[] anExpectedStrings) {
		Tracer.info (this, BasicProjectExecution.DELIMITER);
		Tracer.info (this, "Providing input:" + anInput);
		Tracer.info (this, "Expected outputs:"
				+ Arrays.toString(anExpectedStrings));

	};
	protected void traceEndMainCall(String aMainName, String anInput,
			String[] anExpectedStrings) {
		Tracer.info (this, "Provided input:" + anInput);
		Tracer.info (this, "Expected output:"
				+ Arrays.toString(anExpectedStrings));
		Tracer.info (this, BasicProjectExecution.DELIMITER);

	};
	protected void traceEndMainCall() {
		Tracer.info (this, BasicProjectExecution.DELIMITER);

	}
	protected String toOutputString(String aToken) {
		return aToken;
	}
	protected String[] postTokenOutputLines(String aToken) {
		return emptyStringArray;
	}

	protected String[] getStringArgs() {
		return emptyStringArray;
	}
	
	protected boolean callInteractiveMain() throws Throwable {
		String[] aMainClasses = getClassNames();
		traceStartMainCall("", getInput(), getExpectedOutputs());

		for (String aMainClass : aMainClasses) {
			resetIO();
			// resultingOutError =
			// BasicProjectExecution.callMain(getClassName(),
			// getStringArgs(), getInput());
//			traceStartMainCall(aMainClass, getInput(), getExpectedOutputs());

			resultingOutError = BasicProjectExecution.callMain(aMainClass,
					getStringArgs(), getInput());
			
			if (resultingOutError == null) {
//				maybeAssertInfinite();
				continue;
			}
			output += resultingOutError.out;
			error +=  resultingOutError.err;
			if (resultingOutError != null) {
//				traceMainCall(aMainClass, getInput(), getExpectedOutputs());
//				traceEndMainCall(aMainClass, getInput(), getExpectedOutputs());
				traceEndMainCall();

				setOutputErrorStatus();
				return true;
			} else {
				Tracer.info (this, "Could not execute main class");
			}
		}
		Tracer.info (this, "Could not find main class in:" + Arrays.toString(getClassNames())+ " or previous main run was infinite.");

		return false;

	}

	@Override
	public boolean doTest() throws Throwable {
		return callInteractiveMain();
//		String[] aMainClasses = getClassNames();
//		for (String aMainClass : aMainClasses) {
//			traceMainCall(aMainClass, getInput(), getExpectedOutputs());
//			// resultingOutError =
//			// BasicProjectExecution.callMain(getClassName(),
//			// getStringArgs(), getInput());
//			resultingOutError = BasicProjectExecution.callMain(aMainClass,
//					getStringArgs(), getInput());
//			if (resultingOutError != null) {
//				setOutputErrorStatus();
//				return true;
//			} else {
//				Tracer.info (this, "Could not execite main class");
//			}
//		}
//		return false;
		

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
			String[] anExpectedStrings) throws Throwable {
		traceStartMainCall(aMainName, anInput, anExpectedStrings);
		// Project project = CurrentProjectHolder.getOrCreateCurrentProject();
		// RunningProject runner = project.launch(anInput, 1);
		// String output = runner.await();

		ResultingOutErr aResult = BasicProjectExecution.callMain(aMainName,
				emptyStringArray, anInput);
		// Tracer.info (this, "Input:" + anInput);
		// Tracer.info (this, "Output:" + aResult.out);
		// Tracer.info (this, "Errors:" + aResult.err);
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

//	public static void main(String[] args) {
//		Tracer.info (this, "30".matches(".*30"));
//		Tracer.info (this, " 30".matches(".*30"));
//		Tracer.info (this, "\nfoo is 30 \n".matches(".*30.*"));
//	}

}
