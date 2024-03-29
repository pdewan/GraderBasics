package gradingTools.shared.testcases.concurrency.oddNumbers;

import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.ThisExpression;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import grader.basics.config.BasicExecutionSpecificationSelector;
import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import grader.basics.testcase.PassFailJUnitTestCase;
import gradingTools.basics.sharedTestCase.checkstyle.CheckstyleMethodCalledTestCase;
import gradingTools.shared.testcases.concurrency.outputObserver.AbstractForkJoinChecker;
import unc.checks.TagBasedCheck;
import unc.symbolTable.STMethod;
import unc.symbolTable.STType;
import unc.symbolTable.SymbolTable;
import unc.symbolTable.SymbolTableFactory;
import unc.tools.checkstyle.PostProcessingMain;
import util.annotations.MaxValue;
//@MaxValue(2)
public class FairAllocationProblem extends PostTestExecutorOfForkJoin {
	public static final String EXPLANATION = 
//			"This test checks the output to determine if the bug in the fairThreadRemainderSize has been fixed.\n Improper changes to it can result in the test to check concurrency basics to fail\n Ths test will not be run unless the basics test in this suite succeeds";
			"This test checks the output to determine if the task-allocation bug in the method fairThreadRemainderSize() has been fixed for the ";
	
//	enum FairAllocationErrorInference {
//			NONE,
//			MOD_OPERATOR,
//			OFF_BY_ONE,
//			MULTIPLE_IFS,
//			RETURN_TOO_BIG,
//			UNKNOWN
//	}
//	Class[] PRECEDING_TESTS = {SmallNumberOfRandoms.class};
	String[] relevantCheckNames = {
			this.COMPARE_ITERATION_COUNTS,
			this.TOTAL_ITERATION_COUNT
			};
//	@Override
//	protected abstract Class[] precedingTests() {
//		return PRECEDING_TESTS;
//	}
	
	protected  String[] relevantCheckNames(  ) {
		return relevantCheckNames;
	}
	protected STMethod getAllocationMethod(Project aProject) {
		  SymbolTable aTable = SymbolTableFactory.getCurrentSymbolTable();
		    STType aDispatcherType = aTable.getSTClassByShortName("OddNumbersUtil");
		    STMethod anAllocationMethod = aDispatcherType.getDeclaredMethods("fairThreadRemainderSize")[0];
		    return anAllocationMethod;
	}
	protected List<DetailAST> getIfASTs(STMethod anAllocationMethod) {
		 DetailAST aMethodAST = anAllocationMethod.getAST();
		 List<DetailAST> anIfASTs = TagBasedCheck.
		    		findAllInOrderMatchingNodes(aMethodAST, TokenTypes.LITERAL_IF);
		 return anIfASTs;		
	}
	protected FairAllocationErrorInference makeErrorInference (List<DetailAST> anIfASTs, TestCaseResult aTestCaseResult) {
//		FairAllocationErrorInference retVal = FairAllocationErrorInference.NONE;
		
		if (anIfASTs.size() == 0) {
			return FairAllocationErrorInference.MOD_OPERATOR;
		}
		if (anIfASTs.size() > 1) {
			return FairAllocationErrorInference.MULTIPLE_IFS;
			
		};
		
		return makeErrorInference(anIfASTs.get(0), aTestCaseResult);		

	}
	protected boolean isLEOperator(DetailAST anIfAST) {
		DetailAST aBooleanOperator = TagBasedCheck.getExpression(anIfAST);
		return aBooleanOperator.getType() == TokenTypes.LE;
	}
//	protected boolean isReturn
	protected FairAllocationErrorInference makeErrorInference (DetailAST anIfAST, TestCaseResult aTestCaseResult) {
		if (isLEOperator(anIfAST) && !isPassed(TOTAL_ITERATION_COUNT)) {
			return FairAllocationErrorInference.OFF_BY_ONE;
		}
		DetailAST aThenPart = TagBasedCheck.getThenPart(anIfAST);
		DetailAST aReturnPart = aThenPart.getFirstChild();
		if (aReturnPart == null || aReturnPart.getType() != TokenTypes.LITERAL_RETURN) {
			return FairAllocationErrorInference.UNKNOWN;
		}
		DetailAST aReturnValueExpr = aReturnPart.getFirstChild();
		if (aReturnValueExpr == null) {

			return FairAllocationErrorInference.UNKNOWN;
		
		}
		DetailAST aReturnValue = aReturnValueExpr.getFirstChild();
		if (aReturnValueExpr == null) {

			return FairAllocationErrorInference.UNKNOWN;
		
		}
		String anExpressionString = TagBasedCheck.toStringList(aReturnValueExpr);
		if (!"EXPR 1".equals(anExpressionString) || 
				("EXPR aRemainder".equals(anExpressionString))) {
			return FairAllocationErrorInference.RETURN_TOO_BIG;
		}
			
		return FairAllocationErrorInference.UNKNOWN;
		
//		DetailAST
	
	}
	protected FairAllocationErrorInference makeErrorInference(Project aProject, TestCaseResult aTestCaseResult ) {
		String aCheckStyleText = aProject.getCheckstyleText();
		   
	    SymbolTable aTable = SymbolTableFactory.getCurrentSymbolTable();
	    STType aDispatcherType = aTable.getSTClassByShortName("OddNumbersUtil");
	    STMethod anAllocationMethod = aDispatcherType.getDeclaredMethods("fairThreadRemainderSize")[0];
	    DetailAST aMethodAST = anAllocationMethod.getAST();
	    
	    List<DetailAST> anIfASTs = TagBasedCheck.
	    		findAllInOrderMatchingNodes(aMethodAST, TokenTypes.LITERAL_IF);
	    return makeErrorInference(anIfASTs, aTestCaseResult);

	}
	FairAllocationErrorInference errorInference;
	public FairAllocationErrorInference getFairAllocationErrorInference() {
		if (errorInference == null) {
			TestCaseResult aTestCaseResult =  getLastResult();
			if (!aTestCaseResult.isPass()) {
				errorInference = makeErrorInference(project, aTestCaseResult);
//				System.out.println("Error Inference:" + errorInference);
			} else {
				errorInference = FairAllocationErrorInference.NONE;
			}
		}
		return errorInference;
	}
	private Project project;
	
//	public void getErrorInference () {
//		if (errorInference ==)
//	}
	
	@Override
	public TestCaseResult test(Project aProject, boolean autoGrade)
			throws NotAutomatableException, NotGradableException {
		project = aProject;		
		TestCaseResult aTestCaseResult =  super.test(aProject, autoGrade);
//		if (!aTestCaseResult.isPass()) {
//			errorInference = makeErrorInference(aProject, aTestCaseResult);
////			System.out.println("Error Inference:" + errorInference);
//		} else {
//			errorInference = FairAllocationErrorInference.NONE;
//		}
		return aTestCaseResult;
	}
//	static {
//		BasicExecutionSpecificationSelector.getBasicExecutionSpecification().
//		setCheckStyleConfiguration("unc_checks_533_A0_1.xml");
//	}

}
