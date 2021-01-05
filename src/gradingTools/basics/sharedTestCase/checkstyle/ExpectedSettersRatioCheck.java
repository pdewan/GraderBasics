package gradingTools.basics.sharedTestCase.checkstyle;

import gradingTools.basics.sharedTestCase.checkstyle.CheckStyleWarningsCountingTestCase;

public class ExpectedSettersRatioCheck extends CheckStyleWarningsRatioTestCase {
//	missingSetter = Missing setter for property {0} of type {1}
//	expectedSetter = Expected setter for property {0} of type {1}. Good!
	public static final String WARNING_NAME = "Missing setter.*";
	public static final String INFO_NAME = "Expected setter.*Good!";

	public static final String MESSAGE = "Expected Getters";
	public static final double DEFAULT_PENALTY_PER_MISTAKE = 0.1;

	public ExpectedSettersRatioCheck(String aMessage) {
		super(null, aMessage);
//		penaltyPerMistake = DEFAULT_PENALTY_PER_MISTAKE;
	}

	public ExpectedSettersRatioCheck() {
		super(null, MESSAGE);
//		penaltyPerMistake = DEFAULT_PENALTY_PER_MISTAKE;
	}

	public ExpectedSettersRatioCheck(double aPenaltyPerMistake) {
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
