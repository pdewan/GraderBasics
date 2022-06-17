package grader.basics.output.observer;

import gradingTools.shared.testcases.ASubstringSequenceChecker;

public class APropertyBasedStringChecker extends ASubstringSequenceChecker implements PropertyBasedStringChecker {

protected String[][] propertiesList;
	
public APropertyBasedStringChecker () {
		
	}
public APropertyBasedStringChecker (String[][] aProperties) {
	init(aProperties);
}

protected void init(String[][] aProperties) {
	propertiesList = aProperties;
	String[] aPatterns = new String[aProperties.length];
	for (int index = 0; index < aProperties.length; index++) {
		aPatterns[index] = ObservablePrintStreamUtility.toCompletePropertyChangeRegex(aProperties[index]);
	}
	init(aPatterns);
}

@Override
public String[][] getProperties() {
	
	return propertiesList;
}
			
}
