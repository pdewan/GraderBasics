package gradingTools.basics.sharedTestCase.checkstyle;

import gradingTools.basics.sharedTestCase.checkstyle.CheckStyleWarningsCountingTestCase;

public class ExpectedSignaturesRatioCheck extends CheckStyleWarningsRatioTestCase {
	//Missing signature {0} in type {1}
	//Expected signature {0} in type {1}. Good!
	public static final String WARNING_NAME = "Missing signature.*";
	public static final String INFO_NAME = "Expected signature.*Good!";

	public static final String MESSAGE = "Expected Signatures";
	public static final double DEFAULT_PENALTY_PER_MISTAKE = 0.1;

	public ExpectedSignaturesRatioCheck(String aMessage) {
		super(null, aMessage);
//		penaltyPerMistake = DEFAULT_PENALTY_PER_MISTAKE;
	}

	public ExpectedSignaturesRatioCheck() {
		super(null, MESSAGE);
//		penaltyPerMistake = DEFAULT_PENALTY_PER_MISTAKE;
	}

	public ExpectedSignaturesRatioCheck(double aPenaltyPerMistake) {
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

	@Override 
	protected boolean addBrackets() {
		return false;
	}
	
}
