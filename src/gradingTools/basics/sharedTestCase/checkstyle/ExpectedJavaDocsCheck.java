package gradingTools.basics.sharedTestCase.checkstyle;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class ExpectedJavaDocsCheck extends WarningsRatioKnownMax{
	protected abstract String[][] tagAndMethods();
	protected String warningString;
	protected String infoString;
//	static StringBuilder stringBuilder = new StringBuilder();

	
	public ExpectedJavaDocsCheck() {
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
		warningString = null;
		
		
	}
	/* 
	 * method {1} in class matching {2} has JavaDoc {0}. Good! 
	 * 
	 * */
	protected String extractMethod(String aLine) {
		int anEndindex = aLine.indexOf(" in class");
		int aStartIndex =  aLine.indexOf("method ") + "method ".length();
		return aLine.substring(aStartIndex, anEndindex);
	}
	protected String extractJavaDoc(String aLine) {
		int aStartIndex = aLine.indexOf("JavaDoc ") + "JavaDoc ".length() ;
		int anEndIndex = aLine.indexOf(". Good");
		return aLine.substring(aStartIndex, anEndIndex);
	}
//	protected int numFailedMatches (List<String> aFailedMatchedLines) {
//		return aFailedMatchedLines == null?0:aFailedMatchedLines.size();
//
//	}
//	protected static Set<String> methodsSeen = new HashSet<>();
	protected static Set<String> javadocsSeen = new HashSet<>();

	protected int numSucceededMatches (List<String> aSucceededMatchedLines) {
//		methodsSeen.clear();
		javadocsSeen.clear();
		int retVal = 0;
		for (String aLine:aSucceededMatchedLines) {
//			String aMethod = extractMethod(aLine);
//			if (methodsSeen.contains(aMethod))  {
//				continue;
//			}
			String aJavaDoc = extractJavaDoc(aLine);
			if (javadocsSeen.contains(aJavaDoc)) {
				continue;
			}
//			methodsSeen.add(aMethod);
			javadocsSeen.add(aJavaDoc);
			retVal++;
					
		}
		return retVal;

	}
	/*
	 * method {1} in class matching {2} has JavaDoc {0}. Good!
	 */
	protected void makeInfo() {
		infoString = makeMethodAndTagRegex("method.*", ".*in class matching.*", ".*has JavaDoc.*Good", tagAndMethods());
		
		
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
