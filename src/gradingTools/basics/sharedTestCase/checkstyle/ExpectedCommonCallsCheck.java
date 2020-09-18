package gradingTools.basics.sharedTestCase.checkstyle;

import java.util.Arrays;

public abstract class ExpectedCommonCallsCheck extends ExpectedCallsCheck{
	
	protected void makeWarning() {
		warningString = makeMethodAndTagRegex("multiple methods.*", ".*in class matching.*", ".*not make expected call", tagAndMethods());
		
		
	}
	protected void makeInfo() {
		infoString = makeMethodAndTagRegex("multiple methods.*", ".*in class matching.*", ".*alled expected method.*Good", tagAndMethods());
		
	}	
	
}
