package gradingTools.interpreter;

import grader.basics.execution.RunningProject;
import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import grader.basics.requirements.interpreter.BasicInterpretedVariablesSubstituter;
import grader.basics.requirements.interpreter.specification.CSVRequirementsSpecification;
import grader.basics.testcase.PassFailJUnitTestCase;
import gradingTools.interpreter.checkers.CheckerResult;
import gradingTools.interpreter.checkers.InterpretedChecker;
import gradingTools.interpreter.checkers.InterpretedCheckerRegistry;

public class AnInterpretingJUnitTestCase extends PassFailJUnitTestCase {
	CSVRequirementsSpecification csvRequirementsSpecification;
	int featureNumber = 0;
	String simpleName;
	public AnInterpretingJUnitTestCase(String aSimpleName, CSVRequirementsSpecification aCSVRequirementsSpecification, int aFeatureNumber) {
		simpleName = aSimpleName;
		csvRequirementsSpecification = aCSVRequirementsSpecification;
		featureNumber = aFeatureNumber;
	}
	@Override
	public TestCaseResult test(Project project, boolean autoGrade)
			throws NotAutomatableException, NotGradableException {		
		String anInput = BasicInterpretedVariablesSubstituter.getInput(csvRequirementsSpecification, featureNumber);
		Integer aTimeOut = csvRequirementsSpecification.getTimeOut(featureNumber);
		String anOutput = "";
		RunningProject aRunningProject = null;
		if (aTimeOut != null) {
			aRunningProject = project.launch(anInput, aTimeOut);
		    anOutput = aRunningProject.await();
		} else { // use the I/O from last run, could also store I/O mapping in project
			anInput = project.getCurrentInput();
			anOutput = project.getCurrentOutput().toString();
		}
		String aComparator = csvRequirementsSpecification.getChecker(featureNumber);
		InterpretedChecker aChecker = InterpretedCheckerRegistry.getInterpretedChecker(aComparator);
		int numArgs = aChecker.getNumArgs();
		String[] anArgs = new String[numArgs];
		String allArgs = "";
		for (int i = 0; i < numArgs; i++) {
			String anArg = csvRequirementsSpecification.getArg(featureNumber, i);
//			String anActualArg = InterpretedVariablesSubstituter.getValue(aSakaiProject, csvRequirementsSpecification, featureNumber, anOutput, anArg);
			String anActualArg = BasicInterpretedVariablesSubstituter.getValue(project, csvRequirementsSpecification, featureNumber, anOutput, anArg);
			anArgs[i] = anActualArg;
			allArgs += " " + anArg;
		}
		CheckerResult aResult = aChecker.check(anArgs);
		String aFunctionCall = aComparator + " " + allArgs;
		TestCaseResult retVal;
		
		if (aResult.isSucceeded()) {
			retVal = pass("");
		} else {
			retVal = fail(aFunctionCall + " failed \n" + aResult.getNotes());
		}
		setLastResult(retVal);
		return retVal;

	}
	

}
