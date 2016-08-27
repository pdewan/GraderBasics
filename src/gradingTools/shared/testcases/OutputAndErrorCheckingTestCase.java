package gradingTools.shared.testcases;

import grader.basics.execution.BasicProjectExecution;
import grader.basics.execution.ResultingOutErr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import util.misc.Common;

public abstract class OutputAndErrorCheckingTestCase {

	public enum OutputErrorStatus {
		NO_OUTPUT,
		CORRECT_OUTPUT_NO_ERRORS, CORRECT_OUTPUT_ERRORS, INCORRECT_OUTPUT_NO_ERRORS, INCORRECT_OUTPUT_ERRORS
	}

	protected boolean outputsMustBeInDifferentLines() {
		return true;
	}

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

	public boolean isValidOutput(String anOutput, String[] anExpectedStrings) {
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

	protected boolean hasError(String anError) {
		return !anError.isEmpty();
	}

	protected String[] emptyStringArray = {};

	protected OutputErrorStatus test(String aMainName, String anInput,
			String[] anExpectedStrings) {
		// Project project = CurrentProjectHolder.getOrCreateCurrentProject();
		// RunningProject runner = project.launch(anInput, 1);
		// String output = runner.await();

		ResultingOutErr aResult = BasicProjectExecution.callMain(aMainName,
				emptyStringArray, anInput);
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
