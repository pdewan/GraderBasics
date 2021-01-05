package gradingTools.basics.sharedTestCase.checkstyle;

import gradingTools.basics.sharedTestCase.checkstyle.CheckStyleWarningsCountingTestCase;

public class ClassIsGenericRatioCheck extends CheckStyleWarningsRatioTestCase {
//	classIsNotGeneric = Class {0} should be generic.
//	classIsGeneric = Class {0} is generic. Good!
	public static final String WARNING_NAME = "should be generic.";
	public static final String INFO_NAME = "is generic. Good!";	
	public static final String MESSAGE = "Class Is Generic";
	public static final double DEFAULT_PENALTY_PER_MISTAKE = 0.1;

	public ClassIsGenericRatioCheck(String aMessage) {
		super(null, aMessage);
//		penaltyPerMistake = DEFAULT_PENALTY_PER_MISTAKE;
	}

	public ClassIsGenericRatioCheck() {
		super(null, MESSAGE);
//		penaltyPerMistake = DEFAULT_PENALTY_PER_MISTAKE;
	}

	public ClassIsGenericRatioCheck(double aPenaltyPerMistake) {
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
