package gradingTools.basics.sharedTestCase.checkstyle;


public class MnemonicNames extends CheckStyleWarningsRatioTestCase {
	public static final String WARNING_NAME = "Component .* in Identifier .* is not in dictionary.";
	public static final String MESSAGE = "Variable types using interface";
	public static final String INFO_NAME = "Component .* in Identifier .* is in dictionary. Good!";

	public MnemonicNames(String aMessage) {
		super(null, aMessage);
	}

	public MnemonicNames() {
		super(null, MESSAGE);
	}

	public MnemonicNames(double aPenaltyPerMistake) {
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
