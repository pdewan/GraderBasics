package gradingTools.basics.sharedTestCase.checkstyle;


public class VariableHasInterfaceType extends CheckStyleWarningsRatioTestCase {
	public static final String WARNING_NAME = "rather than interface used as the type of variable";
	public static final String MESSAGE = "Variable types using interface";
	public static final String INFO_NAME = "used as the type of variable.* Good";

	public VariableHasInterfaceType(String aMessage) {
		super(null, aMessage);
	}

	public VariableHasInterfaceType() {
		super(null, MESSAGE);
	}

	public VariableHasInterfaceType(double aPenaltyPerMistake) {
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
