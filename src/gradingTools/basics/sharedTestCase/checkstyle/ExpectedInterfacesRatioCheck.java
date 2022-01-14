package gradingTools.basics.sharedTestCase.checkstyle;

import gradingTools.basics.sharedTestCase.checkstyle.CheckStyleWarningsCountingTestCase;

public class ExpectedInterfacesRatioCheck extends CheckStyleWarningsRatioTestCase {
//	expectedInterface = Expected interface {0} of class {1}. Good!
//	missingInterface = Missing interface {0} of class {1}

	public static final String WARNING_NAME = "Missing interface.*";
	public static final String INFO_NAME = "Expected interface:.*Good!";

	public static final String MESSAGE = "Expected Interfaces";
	public static final double DEFAULT_PENALTY_PER_MISTAKE = 0.1;

	public ExpectedInterfacesRatioCheck(String aMessage) {
		super(null, aMessage);
//		penaltyPerMistake = DEFAULT_PENALTY_PER_MISTAKE;
	}

	public ExpectedInterfacesRatioCheck() {
		super(null, MESSAGE);
//		penaltyPerMistake = DEFAULT_PENALTY_PER_MISTAKE;
	}

	public ExpectedInterfacesRatioCheck(double aPenaltyPerMistake) {
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
