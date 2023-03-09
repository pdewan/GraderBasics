package gradingTools.basics.sharedTestCase.checkstyle;


public class MethodAccessModifierRatioCheck extends CheckStyleWarningsRatioTestCase {
	public static final String WARNING_NAME = "MethodAccessModifier";
//	public static final String WARNING_NAME = "but needs access";
	public static final String MESSAGE = "Access Modifiers Used";
//	public static final String INFO_NAME = "and needs access";

//	public MethodAccessModifierRatioCheck(String aMessage) {
//		super(null, aMessage);
//	}

	public MethodAccessModifierRatioCheck() {
		super(null, MESSAGE);
	}

//	public MethodAccessModifierRatioCheck(double aPenaltyPerMistake) {
//		super(null, MESSAGE, aPenaltyPerMistake);
//
//	}

	@Override
	protected String warningName() {
		return WARNING_NAME;
	}
//
//	@Override
//	protected String infoName() {
//		return INFO_NAME;
//	}

}
