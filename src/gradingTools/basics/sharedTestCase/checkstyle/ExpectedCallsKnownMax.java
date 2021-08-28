package gradingTools.basics.sharedTestCase.checkstyle;

import java.util.Arrays;

public abstract class ExpectedCallsKnownMax extends WarningsRatioKnownMax{
	protected abstract String[][] tagAndMethods();
	public static final String WARNING_NAME = "No method .* has made expected call";
	public static final String INFO_NAME = "has made expected call.*Good!";
	protected String warningString;
	protected String infoString;
//	static StringBuilder stringBuilder = new StringBuilder();

	
	public ExpectedCallsKnownMax() {
		makeWarning();
		makeInfo();
		
	}
//	protected static String makeMethodAndTagRegex(String aBeforeMethod, String aBeforeTag, String anAfterTag, String[][] aTagAndMethods ) {
//		
//	
//		stringBuilder.setLength(0);
//		stringBuilder.append(aBeforeMethod);
//
//		stringBuilder.append("(");
//		for (int anIndex = 0; anIndex < aTagAndMethods.length; anIndex++) {
//			String[] aTagAndMethod = aTagAndMethods[anIndex];
//			if (aTagAndMethod.length != 2) {
//				System.err.println(Arrays.toString(aTagAndMethod) + " should have exactly two elements ");
//				return null;
//			}
//			stringBuilder.append(aTagAndMethod[1]);
//			if (anIndex != aTagAndMethods.length - 1) {
//				stringBuilder.append("|");
//			}			
//		}
//		stringBuilder.append(")");
//		stringBuilder.append(aBeforeTag);
//		for (int anIndex = 0; anIndex < aTagAndMethods.length; anIndex++) {
//			String[] aTagAndMethod = aTagAndMethods[anIndex];
//			
//			stringBuilder.append(aTagAndMethod[0]);
//			if (anIndex != aTagAndMethods.length - 1) {
//				stringBuilder.append("|");
//			}			
//		}
//		stringBuilder.append(")");
//		stringBuilder.append(anAfterTag);
//		return stringBuilder.toString();
//		
//	}
	protected void makeWarning() {
//		warningString = makeMethodAndTagRegex("method.*", ".*in class matching.*", ".*not made expected call", tagAndMethods());
		warningString = makeMethodAndTagRegex("ethod.*", ".*", ".*not made expected call", tagAndMethods());

		
	}
	protected void makeInfo() {
		infoString = makeMethodAndTagRegex("ethod.*", ".*", ".*alled expected method.*Good", tagAndMethods());

//		infoString = makeMethodAndTagRegex("method.*", ".*in class matching.*", ".*alled expected method.*Good", tagAndMethods());
		
		
	}
	@Override
	protected String warningName() {
		return warningString;
	}
	@Override
	protected String infoName() {
		return infoString;
	}
}
