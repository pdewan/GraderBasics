package grader.basics.junit;

import bus.uigen.ObjectEditor;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.undo.ExecutableCommand;

public class AGradableJUnitTestAR implements ExecutableCommand {

	@Override
	public Object execute(Object arg0) {
		ObjectEditor.setAttribute(AGradableJUnitTest.class, AttributeNames.USE_NAME_AS_LABEL, true);
		ObjectEditor.setPropertyAttribute(AGradableJUnitTest.class, "Status", AttributeNames.LABELLED, false);
		ObjectEditor.setPropertyAttribute(AGradableJUnitTest.class, "DisplayedScore", AttributeNames.LABEL, "Score");

//		ObjectEditor.setMethodAttribute(AGradableJUnitTest.class, "getExplanation", AttributeNames.RETURNS_CLASS_EXPLANATION, true);

//		ObjectEditor.setPropertyAttribute(AGradableJUnitTest.class, "Message", AttributeNames.LABELLED, false);

//		ObjectEditor.setAttribute(AGradableJUnitTest.class, AttributeNames.COMPONENT_FOREGROUND, Color.RED);
//		ObjectEditor.setAttribute(AGradableJUnitTest.class, AttributeNames.CONTAINER_BACKGROUND, Color.RED);
// Cannot be overidden by temp dynamic attribute
//		ObjectEditor.setAttribute(AGradableJUnitTest.class, AttributeNames.COMPONENT_FOREGROUND, Color.RED);



		return null;
	}

}
