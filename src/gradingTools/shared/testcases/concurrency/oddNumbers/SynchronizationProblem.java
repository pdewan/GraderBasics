package gradingTools.shared.testcases.concurrency.oddNumbers;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.core.dom.Modifier;

import grader.basics.junit.TestCaseResult;
import util.annotations.MaxValue;
@MaxValue(5)
public class SynchronizationProblem extends PostTestExecutorOfForkJoin {
	public static final String EXPLANATION = "This test checks if race conditions in ConcurrentOddumbers have been eliminated for the ";
//	public static final String SMALL_EXPLANATION = " for the small problem";
	

//	Class[] PRECEDING_TESTS = {
//			SmallNumberOfRandoms.class,
//			ForkJoinSmallProblem.class
//			};
	String[] relevantCheckNames = {
			this.POST_JOIN_EVENTS
			};
//	@Override
//	protected Class[] precedingTests() {
//		return PRECEDING_TESTS;
//	}
	
	protected  String[] relevantCheckNames(  ) {
		return relevantCheckNames;
	}
//	protected AbstractForkJoinChecker oddNumbersExecution() {
//		
////			List<PassFailJUnitTestCase> aPrecedingInstances =
//			return (AbstractOddNumbersExecution) getPrecedingTestInstances().get(0);
//		
//	}
	
//	protected boolean shouldScaleResult() {
//		return false;
//	}

//	@Override
//	public TestCaseResult test(Project project, boolean autoGrade)
//			throws NotAutomatableException, NotGradableException {
//		AbstractForkJoinChecker oddNumbersExecution = oddNumbersExecution();
//		Map<String, TestCaseResult> nameToResult = oddNumbersExecution.getNameToResult();
//		
//		System.out.print(nameToResult);
//		return oddNumbersExecution().getLastResult();
//	}
	protected TestCaseResult isAddOddNumberResult = null;
	public TestCaseResult getIsAddOddNumberResult() {
		return isAddOddNumberResult;
	}
	protected void determineIfAddOddNumberIsSynchronized() {
		if (isAddOddNumberResult != null) {
			return;
		}
		try {
			Class aSynchronizationClass = Class.forName("BasicSynchronizationDemo");
			Class[] aFillParameters = {int.class};
			Method anAddOddNumber = aSynchronizationClass.getDeclaredMethod("addOddNumber", aFillParameters);
			if (Modifier.isSynchronized(anAddOddNumber.getModifiers())) {
				isAddOddNumberResult = partialPass(0.1, "No race conditions");
//				aMessage = "Please make fillOddNumbers(int[]) in BasicSynchronizationDemo synchronized ";
			} else {
				isAddOddNumberResult = fail("Race conditions exist");
			}			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}
	@Override
	protected TestCaseResult[] maybeAddToResults (TestCaseResult[] aResults) {
		determineIfAddOddNumberIsSynchronized();
		List<TestCaseResult> aNewResults = new ArrayList<TestCaseResult>();
		aNewResults.addAll(Arrays.asList(aResults));
		aNewResults.add(isAddOddNumberResult);
		return aNewResults.toArray(aResults);
//		return super.maybeAddToResults(aResults);
	}

}
