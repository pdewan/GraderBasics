package gradingTools.basics.sharedTestCase.checkstyle;

import gradingTools.basics.sharedTestCase.checkstyle.CheckStyleWarningsCountingTestCase;

public class ExpectedGettersRatioCheck extends CheckStyleWarningsRatioTestCase {
//	missingGetter = Missing getter for property {0} of type {1}
//	expectedGetter = Expected getter for property {0} of type {1}. Good!
	public static final String WARNING_NAME = "Missing getter.*";
	public static final String INFO_NAME = "Expected getter.*Good!";

	public static final String MESSAGE = "Expected Getters";
	public static final double DEFAULT_PENALTY_PER_MISTAKE = 0.1;

	public ExpectedGettersRatioCheck(String aMessage) {
		super(null, aMessage);
//		penaltyPerMistake = DEFAULT_PENALTY_PER_MISTAKE;
	}

	public ExpectedGettersRatioCheck() {
		super(null, MESSAGE);
//		penaltyPerMistake = DEFAULT_PENALTY_PER_MISTAKE;
	}

	public ExpectedGettersRatioCheck(double aPenaltyPerMistake) {
		super(null, MESSAGE, aPenaltyPerMistake);

	}

	@Override
	protected String warningName() {
		return WARNING_NAME;
	}

	@Override
	protected String infoName() {
		return INFO_NAME;
	}

}
