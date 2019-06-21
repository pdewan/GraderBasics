package grader.basics.testcase;

import grader.basics.execution.RunningProject;
//import grader.basics.execution.RunningProject;
import grader.basics.junit.BasicJUnitUtils;
import grader.basics.junit.JUnitTestsEnvironment;
import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.CurrentProjectHolder;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import gradingTools.shared.testcases.utils.ABufferingTestInputGenerator;

import org.junit.Test;


/**
 * All test cases should extend this class.
 * Subclasses will implement the {@link TestCase#test(framework.project.Project, boolean)} method.
 * This method should call and return one of the following helper functions:
 
 *
 * An example:
 * <pre>
 * {@code
 * return partialPass(0.5, "Only got half of the points");
 * }
 * </pre>
 */
public abstract class PassFailJUnitTestCase implements JUnitTestCase {
	protected String name = "anonymous";
	protected TestCaseResult lastResult; // last run, for depndent tests
	
	protected ABufferingTestInputGenerator outputBasedInputGenerator ;
	protected RunningProject interactiveInputProject;
    /**
     * This is where we add an instance of the test case.
     * This means only subclasses of PassFailJUnitTestCase can
     * be made dependent.This makese sense since they have 
     * results etc.
     * 
     * This is also the superclass of a gradet testcase, and the clases
     * of these have not been regustered.
     */
    public PassFailJUnitTestCase() {
    	JUnitTestsEnvironment.addPassFailJUnitTestInstance(this);
    }
   
   

    protected TestCaseResult partialPass(double percentage, boolean autograded) {
        return new TestCaseResult(percentage, name, autograded);
    }

    protected TestCaseResult partialPass(double percentage, String notes, boolean autograded) {
        return new TestCaseResult(percentage, notes, name, autograded);
    }
    protected TestCaseResult partialPass(double percentage, String notes) {
        return partialPass(percentage, notes, true);
    }

    protected TestCaseResult pass() {
        return new TestCaseResult(true, name, true);
    }

    protected TestCaseResult pass(boolean autograded) {
        return new TestCaseResult(true, name, autograded);
    }

    protected TestCaseResult pass(String notes) {
        return new TestCaseResult(true, notes, name, true);
    }
    protected TestCaseResult pass(String notes, boolean autograded) {
        return new TestCaseResult(true, notes, name, autograded);
    }

    protected TestCaseResult fail(String notes) {
        return new TestCaseResult(false, notes, name, true);
    }

    protected TestCaseResult fail(String notes, boolean autograded) {
        return new TestCaseResult(false, notes, name, autograded);

    }
    public abstract TestCaseResult test(Project project, boolean autoGrade) throws NotAutomatableException,
	NotGradableException;
    @Test
	public void defaultTest() {
    	passfailDefaultTest();
    }

    
	
	public void passfailDefaultTest() {
//		TestCaseResult result = null;
		TestCaseResult lastResult = null;
		
        try {
        	lastResult = test(CurrentProjectHolder.getOrCreateCurrentProject(), true);  
        	
    		BasicJUnitUtils.assertTrue(lastResult.getNotes(), lastResult.getPercentage(), lastResult.isPass());
        } catch (Throwable e) {
        	e.printStackTrace();
        	if (lastResult != null) {
        		BasicJUnitUtils.assertTrue(e, lastResult.getPercentage());
        	} else {
        		BasicJUnitUtils.assertTrue(e, 0);
        	}
        }
	}
	@Override
    public String getName() {
        return name;
    }
    
    @Override
    public void setName (String aName) {
    	name = aName;
    }

	@Override
	public void setLastResult(TestCaseResult lastResult) {
		this.lastResult = lastResult;
	}
	@Override
	public TestCaseResult getLastResult() {
		return lastResult;
	}
	@Override
	public ABufferingTestInputGenerator getOutputBasedInputGenerator() {
		return outputBasedInputGenerator;
	}
	@Override
	public void setOutputBasedInputGenerator(ABufferingTestInputGenerator newVal) {
		 outputBasedInputGenerator = newVal;
	}
	@Override
	public RunningProject getInteractiveInputProject() {
		return interactiveInputProject;
	}
	@Override
	public void setInteractiveInputProject(RunningProject aProject) {
		 interactiveInputProject = aProject;
	}
}
	
