package gradingTools.interpreter;

import grader.basics.execution.RunningProject;
import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import grader.basics.requirements.interpreter.specification.CSVRequirementsSpecification;
import grader.basics.testcase.PassFailJUnitTestCase;

public class AnInterpretingJUnitTestCase extends PassFailJUnitTestCase {
	CSVRequirementsSpecification csvRequirementsSpecification;
	int requirementNumber = 0;
	String simpleName;
	public AnInterpretingJUnitTestCase(String aSimpleName, CSVRequirementsSpecification aCSVRequirementsSpecification, int aRequirementNumber) {
		simpleName = aSimpleName;
		csvRequirementsSpecification = aCSVRequirementsSpecification;
		requirementNumber = aRequirementNumber;
	}
	@Override
	public TestCaseResult test(Project project, boolean autoGrade)
			throws NotAutomatableException, NotGradableException {
		RunningProject aRunningProject = project.launch("45");
		aRunningProject.await();
		return null;
	}

}
