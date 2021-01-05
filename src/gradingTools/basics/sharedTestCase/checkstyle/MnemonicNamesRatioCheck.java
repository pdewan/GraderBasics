package gradingTools.basics.sharedTestCase.checkstyle;


public class MnemonicNamesRatioCheck extends CheckStyleWarningsRatioTestCase {
	public static final String WARNING_NAME = "Component .* in Identifier .* is not in dictionary.";
	public static final String MESSAGE = "Variable types using interface";
	public static final String INFO_NAME = "Component .* in Identifier .* is in dictionary. Good!";

	public MnemonicNamesRatioCheck(String aMessage) {
		super(null, aMessage);
	}

	public MnemonicNamesRatioCheck() {
		super(null, MESSAGE);
	}

	public MnemonicNamesRatioCheck(double aPenaltyPerMistake) {
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
