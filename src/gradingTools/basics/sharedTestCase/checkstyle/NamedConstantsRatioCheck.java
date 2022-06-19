package gradingTools.basics.sharedTestCase.checkstyle;

import gradingTools.basics.sharedTestCase.checkstyle.CheckStyleWarningsCountingTestCase;

public class NamedConstantsRatioCheck extends CheckStyleWarningsRatioTestCase {
	public static final String WARNING_NAME = "LiberalMagicNumber";
	public static final String INFO_NAME = "ConstantDefined";

//	public static final String WARNING_NAME = "Magic number.*LiberalMagicNumber";
//	public static final String INFO_NAME = "Named Constant .* defined. Good!";

	public static final String MESSAGE = "No magic number";
	public static final double DEFAULT_PENALTY_PER_MISTAKE = 0.1;

//	public NamedConstantsRatioCheck(String aMessage) {
//		super(null, aMessage);
////		penaltyPerMistake = DEFAULT_PENALTY_PER_MISTAKE;
//	}

	public NamedConstantsRatioCheck() {
		super(null, MESSAGE);
//		penaltyPerMistake = DEFAULT_PENALTY_PER_MISTAKE;
	}

//	public NamedConstantsRatioCheck(double aPenaltyPerMistake) {
//		super(null, MESSAGE, aPenaltyPerMistake);
//
//	}

	@Override
	protected String warningName() {
		return WARNING_NAME;
	}
	
	

	@Override
	protected String infoName() {
		return INFO_NAME;
	}
	protected String namedConstantNegativeRegexLineFilter () {
		String aWarningClassName = infoName(); // it is both a warning and info class
		if(addBrackets()) {
			return ".*" + "WARN" + ".*" +"\\[" + aWarningClassName +"\\]" + ".*";
		}
		return ".*" + "WARN" + ".*" + aWarningClassName + ".*";
	}
	
	public String negativeRegexLineFilter () {
		return super.negativeRegexLineFilter() + "|" + namedConstantNegativeRegexLineFilter ();
	}
	
	
}
