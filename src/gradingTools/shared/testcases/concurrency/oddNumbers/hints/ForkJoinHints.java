package gradingTools.shared.testcases.concurrency.oddNumbers.hints;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import grader.basics.config.BasicExecutionSpecificationSelector;
import grader.basics.junit.BasicJUnitUtils;
import gradingTools.shared.testcases.ConcurrencySuiteSkeleton;
import gradingTools.shared.testcases.concurrency.oddNumbers.ForkJoinSmallProblem;
import gradingTools.shared.testcases.concurrency.oddNumbers.SmallNumberOfRandoms;
import util.annotations.Explanation;

@RunWith(Suite.class)
@Suite.SuiteClasses({
//	SmallNumberOfRandoms.class,
//	ForkJoinSmallProblem.class,
//	ForkJoinHint1OnOutput.class,
//	ForkJoinHint2OnOutput.class,
	ForkJoinHint1OnError.class,
	ForkJoinHint2OnCodeAndOutput.class,
	ForkJoinHint3OnOutput.class,
	ForkJoinHint4OnCode.class,
	ForkJoinHint5OnCode.class,
	ForkJoinHint6OnCode.class

		
})
@Explanation("If the code in forkAndJoinThreads(), the output of the program, and the error message produced by the ForkJoinSmallProblem and ForkJoinLargeProblem tests are not sufficient to identify its bug, then execute the tests in this suite in sequence for increasingly detailed hints on how to find and fix the bug")
public class ForkJoinHints extends ConcurrencySuiteSkeleton{
	public static void main (String[] args) {
		try {
//			BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setLanguage(BasicLanguageDependencyManager.JAVA_LANGUAGE);
//			BasicExecutionSpecificationSelector.getBasicExecutionSpecification().
//			setCheckStyleConfiguration("unc_checks_533_A0_1.xml");
			
			BasicJUnitUtils.interactiveTest(ForkJoinHints.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	

}
