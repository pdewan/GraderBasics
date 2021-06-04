package gradingTools.basics.sharedTestCase.checkstyle;


public class MnemonicNameRatioTestCase extends CheckStyleWarningsRatioTestCase {
	public static final String WARNING_NAME = "MnemonicName";

//	public static final String WARNING_NAME = "Component .* in Identifier .* is not in dictionary.";
	public static final String MESSAGE = "Variable types using interface";
	public static final String INFO_NAME = "Component .* in Identifier .* is in dictionary. Good!";

	public MnemonicNameRatioTestCase(String aMessage) {
		super(null, aMessage);
	}

	public MnemonicNameRatioTestCase() {
		super(null, MESSAGE);
	}

	public MnemonicNameRatioTestCase(double aPenaltyPerMistake) {
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
