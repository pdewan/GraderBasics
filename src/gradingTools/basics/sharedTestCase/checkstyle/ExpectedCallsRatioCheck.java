package gradingTools.basics.sharedTestCase.checkstyle;

import gradingTools.basics.sharedTestCase.checkstyle.CheckStyleWarningsCountingTestCase;

public class ExpectedCallsRatioCheck extends CheckStyleWarningsRatioTestCase {
	//expectedMethodCall = method {1} in class matching {2}  made expected call {0}. Good!
	//missingMethodCall = method {1}  in class matching {2} has not made expected call {0}.
	public static final String WARNING_NAME = "No method .* has made expected call";
	public static final String INFO_NAME = "has made expected call.*Good!";

	public static final String MESSAGE = "Expected Calls";
	public static final double DEFAULT_PENALTY_PER_MISTAKE = 0.1;

	public ExpectedCallsRatioCheck(String aMessage) {
		super(null, aMessage);
//		penaltyPerMistake = DEFAULT_PENALTY_PER_MISTAKE;
	}

	public ExpectedCallsRatioCheck() {
		super(null, MESSAGE);
//		penaltyPerMistake = DEFAULT_PENALTY_PER_MISTAKE;
	}

	public ExpectedCallsRatioCheck(double aPenaltyPerMistake) {
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
