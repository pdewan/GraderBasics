package gradingTools.shared.testcases.concurrency.oddNumbers;

import util.annotations.Explanation;
import util.annotations.MaxValue;
@MaxValue(10)
@Explanation(FairAllocationProblem.EXPLANATION)

public class FairAllocationSmallProblem extends FairAllocationProblem {
	
//	enum FairAllocationErrorInference {
//			NONE,
//			MOD_OPERATOR,
//			OFF_BY_ONE,
//			MULTIPLE_IFS,
//			RETURN_TOO_BIG,
//			UNKNOWN
//	}
	Class[] PRECEDING_TESTS = {
			SmallNumberOfRandoms.class,
			BasicsSmallProblem.class

			};
//	String[] relevantCheckNames = {
//			this.COMPARE_ITERATION_COUNTS,
//			this.TOTAL_ITERATION_COUNT
//			};
	@Override
	protected Class[] precedingTests() {
		return PRECEDING_TESTS;
	}
	
//	protected  String[] relevantCheckNames(  ) {
//		return relevantCheckNames;
//	}
//	protected STMethod getAllocationMethod(Project aProject) {
//		  SymbolTable aTable = SymbolTableFactory.getCurrentSymbolTable();
//		    STType aDispatcherType = aTable.getSTClassByShortName("OddNumbersUtil");
//		    STMethod anAllocationMethod = aDispatcherType.getDeclaredMethods("fairThreadRemainderSize")[0];
//		    return anAllocationMethod;
//	}
//	protected List<DetailAST> getIfASTs(STMethod anAllocationMethod) {
//		 DetailAST aMethodAST = anAllocationMethod.getAST();
//		 List<DetailAST> anIfASTs = TagBasedCheck.
//		    		findAllInOrderMatchingNodes(aMethodAST, TokenTypes.LITERAL_IF);
//		 return anIfASTs;		
//	}
//	protected FairAllocationErrorInference makeErrorInference (List<DetailAST> anIfASTs, TestCaseResult aTestCaseResult) {
////		FairAllocationErrorInference retVal = FairAllocationErrorInference.NONE;
//		
//		if (anIfASTs.size() == 0) {
//			return FairAllocationErrorInference.MOD_OPERATOR;
//		}
//		if (anIfASTs.size() > 1) {
//			return FairAllocationErrorInference.MULTIPLE_IFS;
//			
//		};
//		
//		return makeErrorInference(anIfASTs.get(0), aTestCaseResult);		
//
//	}
//	protected boolean isLEOperator(DetailAST anIfAST) {
//		DetailAST aBooleanOperator = TagBasedCheck.getExpression(anIfAST);
//		return aBooleanOperator.getType() == TokenTypes.LE;
//	}
////	protected boolean isReturn
//	protected FairAllocationErrorInference makeErrorInference (DetailAST anIfAST, TestCaseResult aTestCaseResult) {
//		if (isLEOperator(anIfAST) && !isPassed(TOTAL_ITERATION_COUNT)) {
//			return FairAllocationErrorInference.OFF_BY_ONE;
//		}
//		DetailAST aThenPart = TagBasedCheck.getThenPart(anIfAST);
//		DetailAST aReturnPart = aThenPart.getFirstChild();
//		if (aReturnPart == null || aReturnPart.getType() != TokenTypes.LITERAL_RETURN) {
//			return FairAllocationErrorInference.UNKNOWN;
//		}
//		DetailAST aReturnValueExpr = aReturnPart.getFirstChild();
//		if (aReturnValueExpr == null) {
//
//			return FairAllocationErrorInference.UNKNOWN;
//		
//		}
//		DetailAST aReturnValue = aReturnValueExpr.getFirstChild();
//		if (aReturnValueExpr == null) {
//
//			return FairAllocationErrorInference.UNKNOWN;
//		
//		}
//		String anExpressionString = TagBasedCheck.toStringList(aReturnValueExpr);
//		if (!"EXPR 1".equals(anExpressionString) || 
//				("EXPR aRemainder".equals(anExpressionString))) {
//			return FairAllocationErrorInference.RETURN_TOO_BIG;
//		}
//			
//		return FairAllocationErrorInference.UNKNOWN;
//		
////		DetailAST
//	
//	}
//	protected FairAllocationErrorInference makeErrorInference(Project aProject, TestCaseResult aTestCaseResult ) {
//		String aCheckStyleText = aProject.getCheckstyleText();
//		   
//	    SymbolTable aTable = SymbolTableFactory.getCurrentSymbolTable();
//	    STType aDispatcherType = aTable.getSTClassByShortName("OddNumbersUtil");
//	    STMethod anAllocationMethod = aDispatcherType.getDeclaredMethods("fairThreadRemainderSize")[0];
//	    DetailAST aMethodAST = anAllocationMethod.getAST();
//	    
//	    List<DetailAST> anIfASTs = TagBasedCheck.
//	    		findAllInOrderMatchingNodes(aMethodAST, TokenTypes.LITERAL_IF);
//	    return makeErrorInference(anIfASTs, aTestCaseResult);
//
//	}
//	FairAllocationErrorInference errorInference;
//	@Override
//	public TestCaseResult test(Project aProject, boolean autoGrade)
//			throws NotAutomatableException, NotGradableException {
//
////		
//		TestCaseResult aTestCaseResult =  super.test(aProject, autoGrade);
//		if (!aTestCaseResult.isPass()) {
//			errorInference = makeErrorInference(aProject, aTestCaseResult);
//			System.out.println("Error Inference:" + errorInference);
//		}
//		return aTestCaseResult;
//	}


}
