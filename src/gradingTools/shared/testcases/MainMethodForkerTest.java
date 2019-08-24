package gradingTools.shared.testcases;

import grader.basics.config.BasicExecutionSpecificationSelector;
import grader.basics.execution.BasicProjectExecution;
import grader.basics.execution.GradingMode;
import grader.basics.execution.ResultingOutErr;
import grader.basics.junit.JUnitTestsEnvironment;
import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;

public class MainMethodForkerTest extends MethodExecutionTest {
	protected Class mainClass;

	protected Project project;
	protected String mainClassName;
	protected String[] emptyStringArgs = emptyStringLines;
    
    public String getMainClassName() {
    	if (mainClassName == null) {
    		mainClassName = mainClassName();
    	}
		return mainClassName;
	}
	public void setMainClassName(String mainClassName) {
		this.mainClassName = mainClassName;
	}
	protected Class mainClass() {
    	return null;
    }
    protected Class getMainClass() {
    	if (mainClass == null) {
    		mainClass = mainClass();
    	}
		return mainClass;
	}
	public void setMainClass(Class mainClass) {
		this.mainClass = mainClass;
	}
	protected String mainClassName() {
    	return getMainClass().getName();
    }	
	protected void setMainClass() {
		
	}
	public String[] getMainArgs() {
		return emptyStringArgs;
	}
//	protected String[] getInputLines() {
//		return  new String[]{"3", "a an the a an the a a a an an an the the the"};
//	}
//	@Override
//	public String[] getStudentArgs() {
//		return new String[]{"3", "a an the a an the a a a an an an the the the"};
//	}
	
	/*
	 * This stuff is inconsistent with the FORK_MAIN_PROPERTY
	 */
	protected boolean forkMain() {
//		return true;
		return BasicExecutionSpecificationSelector.getBasicExecutionSpecification().isForkMain();
	}
	protected void callOrForkMain(boolean aFork) throws Throwable {
//		setupProcesses();
		resultingOutError = BasicProjectExecution.callOrForkMain(
				aFork, getMainClassName(), getMainArgs(),getInputLines());
			interactiveInputProject = resultingOutError.getRunningProject();
	}
	protected void setupProcesses() {
		
	}
	
	protected void callOrForkInteractiveMain(boolean aFork) throws Throwable {
		callOrForkMain(aFork);
		
//		resultingOutError = BasicProjectExecution.callOrForkMain(
//			true, getMainClassName(), getMainArgs(),getInputLines());
//		interactiveInputProject = resultingOutError.getRunningProject();

//		error = resultingOutError.getErr();
//		output = resultingOutError.getOut();
		error = interactiveInputProject.getErrorOutput();
		output = interactiveInputProject.getOutput();
		resultingOutError = new ResultingOutErr(output, error);
		

	}
		@Override
	protected boolean isValidOutput() {
		return output.contains("View:java.beans.PropertyChangeEvent[propertyName=Result; oldValue=null; newValue={the=5, a=5, an=5}; propagationId=null; source=Model]");
	}
	@Override
	protected boolean hasError(String anError) {
		return false;
	}
	@Override
	public TestCaseResult test(Project aProject, boolean autoGrade)
			throws NotAutomatableException, NotGradableException {
		try {
			project = aProject;
			setMainClass();
		
			callOrForkInteractiveMain(forkMain());
			independentSetLinesMatcher();
			setOutputErrorStatus();
			processOutputErrorStatus();
			return pass();
//			if (checkWithChecker()) {
//				return pass();
//			} else {
//				return fail ("Test failed:");
//			}
//			processOutputErrorStatus();
//			return pass();
			
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return fail(e.getMessage());

		}
//		String aMainClassName = aConfigurationProvided.getTestConfiguration().getStandAloneTokenCounter().getName();
//		BasicStaticConfigurationUtils.setBasicCommandToDefaultEntryPointCommand();
//		String[] emptyArgs = {};
//		try {
//			ResultingOutErr anOutError = BasicProjectExecution.callOrForkMain(true, aMainClassName, emptyArgs, "3", "a an the a an the a a a an an an the the the");
//			BasicProjectExecution.callOrForkMain(true, getMainClassName(), emptyArgs, "3", "a an the a an the a a a an an an the the the");
//
//			String anOut = anOutError.getOut();
//			String anError = anOutError.getErr();
//			
//		} catch (Throwable e) {
//			return fail(e.getMessage());
//		}
//		return null;
	}
	public void defaultTest() {
    	passfailDefaultTest();
    }

}
