package gradingTools.basics.sharedTestCase.checkstyle;

import gradingTools.basics.sharedTestCase.checkstyle.CheckStyleWarningsCountingTestCase;

public class ClassHasOneExtendingInterfaceRatioCheck extends CheckStyleWarningsRatioTestCase {
	//classHasExactlyOneInterface = Class {0} has exactly one programmer-defined multiply extending interface. Good! 
	//classDoesNotHaveExactlyOneInterface =  Class {0} with instance methods does not have exactly one interface.      
	public static final String WARNING_NAME = "does not have exactly one interface.";
	public static final String INFO_NAME = "has exactly one programmer-defined.*Good!";	
	public static final String MESSAGE = "Class Has One Multiple Extending Interface";
	public static final double DEFAULT_PENALTY_PER_MISTAKE = 0.1;

	public ClassHasOneExtendingInterfaceRatioCheck(String aMessage) {
		super(null, aMessage);
//		penaltyPerMistake = DEFAULT_PENALTY_PER_MISTAKE;
	}

	public ClassHasOneExtendingInterfaceRatioCheck() {
		super(null, MESSAGE);
//		penaltyPerMistake = DEFAULT_PENALTY_PER_MISTAKE;
	}

	public ClassHasOneExtendingInterfaceRatioCheck(double aPenaltyPerMistake) {
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
