package gradingTools.basics.sharedTestCase.checkstyle;


public class AccessModifiersMatched extends CheckStyleWarningsRatioTestCase {
	public static final String WARNING_NAME = "but needs access";
	public static final String MESSAGE = "Access Modifiers Used";
	public static final String INFO_NAME = "and needs access";

	public AccessModifiersMatched(String aMessage) {
		super(null, aMessage);
	}

	public AccessModifiersMatched() {
		super(null, MESSAGE);
	}

	public AccessModifiersMatched(double aPenaltyPerMistake) {
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
