package gradingTools.basics.sharedTestCase.checkstyle;


public class VariableHasInterfaceTypeRatioCheck extends CheckStyleWarningsRatioTestCase {
	public static final String WARNING_NAME = "rather than interface used as the type of variable";
	public static final String MESSAGE = "Variable Types Using Interface";
	public static final String INFO_NAME = "used as the type of variable.* Good";

	public VariableHasInterfaceTypeRatioCheck(String aMessage) {
		super(null, aMessage);
	}

	public VariableHasInterfaceTypeRatioCheck() {
		super(null, MESSAGE);
	}

	public VariableHasInterfaceTypeRatioCheck(double aPenaltyPerMistake) {
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
