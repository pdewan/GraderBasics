package gradingTools.shared.testcases.concurrency.oddNumbers;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.core.dom.Modifier;

import grader.basics.execution.GradingMode;
import grader.basics.junit.TestCaseResult;
import grader.basics.trace.GraderInfo;
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
	protected TestCaseResult isOnlyOneMethodSynchronizedResult = null;
	protected List<Method> synchronizedMethods;
	
//	public TestCaseResult getIsAddOddNumberResult() {
//		return isAddOddNumberResult;
//	}
	
	protected void findSynchronizedMethods() {
		if (synchronizedMethods != null) {
			return;
		}

		synchronizedMethods = new ArrayList();
		try {
			Class aSynchronizationClass = Class.forName("BasicSynchronizationDemo");
			isOnlyOneMethodSynchronizedResult = partialPass(0.1, "Exactly one method synchronized");

			Method[] aMethods = aSynchronizationClass.getDeclaredMethods();
//			int aNumSynchronizedMethods = 0;

			for (int anIndex = 0; anIndex < aMethods.length; anIndex++) {
				Method aMethod = aMethods[anIndex];
				if (Modifier.isSynchronized(aMethod.getModifiers())) {
					synchronizedMethods.add(aMethod);
					if (aMethod.getName().equals("addOddNumber")) {
						isAddOddNumberResult = partialPass(0.1, "No race conditions");
					}
				}
			}
			
			if (synchronizedMethods.size() == 0) {				
				isAddOddNumberResult = fail("Race conditions exist");
				isOnlyOneMethodSynchronizedResult = fail ("No synchronized method in class SynchrnoizationDemo"); 
				return;
			}
			if (synchronizedMethods.size() == 1) {
				if (isAddOddNumberResult == null) {
					isAddOddNumberResult = fail("Race conditions exist");
					isOnlyOneMethodSynchronizedResult = fail ("Think of synchronizing a method other than: " + synchronizedMethods);
				} else {
//					isAddOddNumberResult = fail("No race conditions");
					isOnlyOneMethodSynchronizedResult = partialPass (0.1, "Correct method synchronized: " + synchronizedMethods);
				}
				return;
			}
			
			if (isAddOddNumberResult == null) {
				isAddOddNumberResult = fail("Race conditions exist");
//				isOnlyOneMethodSynchronizedResult = fail ("More than one method synchronized: " + synchronizedMethods);
//				return;

			}
			
			isOnlyOneMethodSynchronizedResult = fail ("More than one method synchronized: " + synchronizedMethods);

			
//			if (synchronizedMethods.size() > 1) {
//				isOnlyOneMethodSynchronizedResult = fail ("More than one synchronized method:" + synchronizedMethods); 
//			} 
//			if (synchronizedMethods.size() == 1 && isAddOddNumberResult == null) {
//				isOnlyOneMethodSynchronizedResult = fail ("Think of synchronizing a method other than: " + synchronizedMethods);
//				isAddOddNumberResult = fail("Race conditions exist");
//
//			} 
//			if (isAddOddNumberResult == null) {
//				isAddOddNumberResult = fail("Race conditions exist");
//				isOnlyOneMethodSynchronizedResult = fail ("Think of synchronizing a method other than: " + synchronizedMethods);
//
//			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//			return aSynchrnoizedMethods;
		}


	}
	protected void determineIfAddOddNumberIsSynchronized() {
		if (isAddOddNumberResult != null) {
			return;
		}
		try {
//			List<Method> aSynchronizedMethods = findSynchronizedMethods();
			
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
	
//	protected boolean isAddNumberSynchronized(List<Method> aSynchronizedMethods) {
//	
//	}
	
	@Override
	protected TestCaseResult[] maybeAddToResults (TestCaseResult[] aResults) {
//		determineIfAddOddNumberIsSynchronized();
//		List<Method> aSynchronizedMethods = findSynchronizedMethods();
//		if (aSynchronizedMethods)
		if (GradingMode.getGraderRun()) {
			return aResults; // cannot reflect on non public class
		}
		List<TestCaseResult> aNewResults = new ArrayList<TestCaseResult>();
		findSynchronizedMethods();
		if (aResults.length >= 1) {
			TestCaseResult aResult = aResults[aResults.length - 1];
			if (aResult.getPercentage() == 1.0) {
				aResult.setPercentage(0.8); // will add two new results below
			}
			
		}
		aNewResults.addAll(Arrays.asList(aResults));
		aNewResults.add(isAddOddNumberResult);
		aNewResults.add(isOnlyOneMethodSynchronizedResult);
		return aNewResults.toArray(aResults);
//		return super.maybeAddToResults(aResults);
	}

}
