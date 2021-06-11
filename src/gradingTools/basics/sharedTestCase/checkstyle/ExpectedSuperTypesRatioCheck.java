package gradingTools.basics.sharedTestCase.checkstyle;

import gradingTools.basics.sharedTestCase.checkstyle.CheckStyleWarningsCountingTestCase;

public class ExpectedSuperTypesRatioCheck extends CheckStyleWarningsRatioTestCase {
	//expectedMethodCall = method {1} in class matching {2}  made expected call {0}. Good!
	//missingMethodCall = method {1}  in class matching {2} has not made expected call {0}.
	public static final String WARNING_NAME = "ExpectedSuperTypes";

//	public static final String WARNING_NAME = "Missing supertype.*";
	public static final String INFO_NAME = "Expected supertype:.*Good!";

	public static final String MESSAGE = "Expected Super Types";
	public static final double DEFAULT_PENALTY_PER_MISTAKE = 0.1;

	public ExpectedSuperTypesRatioCheck(String aMessage) {
		super(null, aMessage);
//		penaltyPerMistake = DEFAULT_PENALTY_PER_MISTAKE;
	}

	public ExpectedSuperTypesRatioCheck() {
		super(null, MESSAGE);
//		penaltyPerMistake = DEFAULT_PENALTY_PER_MISTAKE;
	}

	public ExpectedSuperTypesRatioCheck(double aPenaltyPerMistake) {
		super(null, MESSAGE, aPenaltyPerMistake);

	}

	@Override
	protected String warningName() {
		return WARNING_NAME;
	}

//	@Override
//	protected String infoName() {
//		return INFO_NAME;
//	}

}
