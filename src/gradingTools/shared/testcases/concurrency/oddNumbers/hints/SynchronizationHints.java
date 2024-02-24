package gradingTools.shared.testcases.concurrency.oddNumbers.hints;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import grader.basics.junit.BasicJUnitUtils;
import gradingTools.shared.testcases.ConcurrencySuiteSkeleton;
import gradingTools.shared.testcases.concurrency.oddNumbers.FairAllocationSmallProblem;
import gradingTools.shared.testcases.concurrency.oddNumbers.SmallNumberOfRandoms;
import gradingTools.shared.testcases.concurrency.oddNumbers.SynchronizationSmallProblem;

@RunWith(Suite.class)
@Suite.SuiteClasses({
//	SmallNumberOfRandoms.class,
//	SynchronizationSmallProblem.class,
//	SynchronizationHint1OnOutput.class,
//	SynchronizationHint2OnOutput.class,
//	SynchronizationHint3OnOutput.class,
//	SynchronizationHint4OnOutput.class,	
	SynchronizationHint1OnError.class,
	SynchronizationHint1OnGeneralRaceConditions.class,
	SynchronizationHint2OnOutputAndAssociatedCode.class,
	SynchronizationHint3OnOutputProperties.class,
	SynchronizationHint4OnLoadStore.class,
	SynchronizationHint5OnThreadSwitch.class,
	SynchronizationHint6OnSharedVariablesAndCriticalCode.class
//	SynchronizationHint3OnOutputProperties.class,
//	SynchronizationHint4OnSpecificOutput.class,


})

public class SynchronizationHints extends ConcurrencySuiteSkeleton {
	public static void main (String[] args) {
		try {
			
			BasicJUnitUtils.interactiveTest(SynchronizationHints.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
