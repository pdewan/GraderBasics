package gradingTools.shared.testcases.logging;

import javax.swing.JOptionPane;

import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.observers.AnAbstractTestLogFileWriter;
import grader.basics.observers.TestLogFileWriterFactory;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import grader.basics.testcase.PassFailJUnitTestCase;

public class ReadPastInstructorHelp extends PassFailJUnitTestCase{

	@Override
	public TestCaseResult test(Project project, boolean autoGrade)
			throws NotAutomatableException, NotGradableException {
		AnAbstractTestLogFileWriter aWriter = TestLogFileWriterFactory.getMainFileWriter();
		String aText = aWriter.readReadHelp();
		if (aText.isEmpty()) {
			return fail ("No Past Help");
		}
		System.out.println(aText);
        JOptionPane.showMessageDialog(null, aText);

		return partialPass(0.5, "Past Help Output on the Console");
	}

}
