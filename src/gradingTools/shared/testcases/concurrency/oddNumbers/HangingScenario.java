package gradingTools.shared.testcases.concurrency.oddNumbers;

import java.lang.reflect.Method;

import org.eclipse.jdt.core.dom.Modifier;

import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.observers.IOTraceRepository;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import grader.basics.testcase.PassFailJUnitTestCase;

public class HangingScenario extends PostTestExecutorOfForkJoin{
Class[] PRECEDING_TESTS = {LargerNumberOfRandoms.class};
	
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
			Method[] aMethods = aSynchronizationClass.getDeclaredMethods();
			Class[] aFillParameters = {int[].class};
			Method aFillOddNumbers = aSynchronizationClass.getDeclaredMethod("processInputList", aFillParameters);
			String aMessage = "";
			if (!Modifier.isSynchronized(aFillOddNumbers.getModifiers())) {
//				return pass();
				aMessage += "Please make processInputList(int[]) in BasicSynchronizationDemo synchronized ";
				
			}
			Class[] anAddParameters = {Integer.TYPE};
			Method anAddOddNumber = aSynchronizationClass.getDeclaredMethod("addOddNumber", anAddParameters);
			if (!Modifier.isSynchronized(anAddOddNumber.getModifiers())) {
				
				aMessage +=  "Please make addOddNumber(int) in BasicSynchronizationDemo synchronized ";
			}
			
			if (aMessage.isEmpty()) {
//				return partialPass(); 

			return partialPass(0.2, "Please try the hints"); 
			}
			IOTraceRepository.addPostAnnouncement(aMessage);
			
			System.out.println(">>\n" + aMessage + " \n<<");
//			return partialPass(0.2, aMessage);
			return pass();

			
			
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
