package gradingTools.basics.sharedTestCase.checkstyle;

import gradingTools.basics.sharedTestCase.checkstyle.CheckStyleWarningsCountingTestCase;

public class PublicMethodsOverride extends CheckStyleWarningsRatioTestCase {

	public static final String WARNING_NAME = "The following public methods do not override";
	public static final String INFO_NAME = "public methods override. Good!";	public static final String MESSAGE = "No magic number";
	public static final double DEFAULT_PENALTY_PER_MISTAKE = 0.1;

	public PublicMethodsOverride(String aMessage) {
		super(null, aMessage);
//		penaltyPerMistake = DEFAULT_PENALTY_PER_MISTAKE;
	}

	public PublicMethodsOverride() {
		super(null, MESSAGE);
//		penaltyPerMistake = DEFAULT_PENALTY_PER_MISTAKE;
	}

	public PublicMethodsOverride(double aPenaltyPerMistake) {
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
