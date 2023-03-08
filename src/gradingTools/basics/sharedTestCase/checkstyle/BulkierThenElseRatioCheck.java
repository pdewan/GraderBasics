package gradingTools.basics.sharedTestCase.checkstyle;


public class BulkierThenElseRatioCheck extends CheckStyleWarningsRatioTestCase {
	public static final String WARNING_NAME = "BulkierThen";
//	public static final String INFO_NAME = "BulkierElse";


//	public static final String WARNING_NAME = "rather than interface used as the type of variable";
	public static final String MESSAGE = "Bulkier Then/Else";
//	public static final String INFO_NAME = "used as the type of variable.* Good";

//	public VariableHasClassTypeRatioCheck(String aMessage) {
//		super(null, aMessage);
//	}

	public BulkierThenElseRatioCheck() {
		super(null, MESSAGE);
	}

//	public VariableHasClassTypeRatioCheck(double aPenaltyPerMistake) {
//		super(null, MESSAGE, aPenaltyPerMistake);
//
//	}

	@Override
	protected String warningName() {
		return WARNING_NAME;
	}
	
//	@Override
//	protected String infoName() {
//		return INFO_NAME;
//	}

//	@Override
//	protected String infoName() {
//		return INFO_NAME;
//	}

}
