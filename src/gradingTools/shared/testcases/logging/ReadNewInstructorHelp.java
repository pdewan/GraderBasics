package gradingTools.shared.testcases.logging;

import javax.swing.JOptionPane;

import bus.uigen.ObjectEditor;
import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.observers.AnAbstractTestLogFileWriter;
import grader.basics.observers.TestLogFileWriterFactory;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import grader.basics.testcase.PassFailJUnitTestCase;

public class ReadNewInstructorHelp extends PassFailJUnitTestCase{

	@Override
	public TestCaseResult test(Project project, boolean autoGrade)
			throws NotAutomatableException, NotGradableException {
		AnAbstractTestLogFileWriter aWriter = TestLogFileWriterFactory.getMainFileWriter();
		String aText = aWriter.readReceivedHelp();
		if (aText.isEmpty()) {
			return fail ("No New Help");
		}
		System.out.println(aText);
//		ObjectEditor.edit(aText);
        JOptionPane.showMessageDialog(null, aText);
		aWriter.clearReceivedHelp();
		aWriter.appendToReadHelpFile(aText + "\n");
		return partialPass(0.5, "New Help Output on the Console");
//		return fail("Not yet implemented");
	}

}
