package gradingTools.shared.testcases.concurrency.oddNumbers;

import java.lang.reflect.Method;

import org.eclipse.jdt.core.dom.Modifier;

import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import grader.basics.testcase.PassFailJUnitTestCase;

public class HangingScenario extends PostTestExecutorOfForkJoin{
Class[] PRECEDING_TESTS = {SmallNumberOfRandoms.class};
	
//	String[] relevantCheckNames = {this.HAS_INTERLEAVING};

	@Override
	protected Class[] precedingTests() {
		return PRECEDING_TESTS;
	}

	@Override
	public TestCaseResult test(Project project, boolean autoGrade)
			throws NotAutomatableException, NotGradableException {
		try {
			Class aSynchronizationClass = Class.forName("BasicSynchronizationDemo");
			Class[] aFillParameters = {int[].class};
			Method aFillOddNumbers = aSynchronizationClass.getDeclaredMethod("fillOddNumbers", aFillParameters);
			String aMessage = "";
			if (!Modifier.isSynchronized(aFillOddNumbers.getModifiers())) {
				aMessage += "Please make fillOddNumbers(int[]) in BasicSynchronizationDemo synchronized ";
			}
			Class[] anAddParameters = {Integer.TYPE};
			Method anAddOddNumber = aSynchronizationClass.getDeclaredMethod("addOddNumber", anAddParameters);
			if (!Modifier.isSynchronized(anAddOddNumber.getModifiers())) {
				aMessage +=  "\nPlease make addOddNumber(int) in BasicSynchronizationDemo synchronized ";
			}
			
			if (aMessage.isEmpty()) {
			return fail("Please try the hints"); 
			}
			System.out.println(aMessage);
			return pass("");
			
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Method aForkingMethod;
		return null;
	}

}
