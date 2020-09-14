package gradingTools.basics.sharedTestCase.checkstyle;

import gradingTools.basics.sharedTestCase.checkstyle.CheckStyleWarningsCountingTestCase;

public class NamedConstants extends CheckStyleWarningsRatioTestCase {
	
	public static final String WARNING_NAME = "Magic number.*LiberalMagicNumber";
	public static final String INFO_NAME = "Named Constant .* defined. Good!";

	public static final String MESSAGE = "No magic number";
	public static final double DEFAULT_PENALTY_PER_MISTAKE = 0.1;

	public NamedConstants(String aMessage) {
		super(null, aMessage);
//		penaltyPerMistake = DEFAULT_PENALTY_PER_MISTAKE;
	}

	public NamedConstants() {
		super(null, MESSAGE);
//		penaltyPerMistake = DEFAULT_PENALTY_PER_MISTAKE;
	}

	public NamedConstants(double aPenaltyPerMistake) {
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
