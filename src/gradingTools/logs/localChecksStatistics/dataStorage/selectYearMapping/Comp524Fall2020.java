package gradingTools.logs.localChecksStatistics.dataStorage.selectYearMapping;

import java.util.ArrayList;
import java.util.List;

import gradingTools.logs.localChecksStatistics.dataStorage.SuiteMapping;
import gradingTools.logs.localChecksStatistics.dataStorage.SuiteTestPairings;

public class Comp524Fall2020 implements YearMap {

	@Override
	public SuiteMapping getMapping(int assignmentNumber) {
		switch(assignmentNumber) {
		case 0:
			return getA0_1();
		case 1:
			return getA1();
		case 2:
			return getA2();
		case 3:
			return getA3();
		case 4:
			return getA4();
		case 5:
			return getA5();
		case 6:
			return getA6();
		}
		return null;
	}

	
	private SuiteMapping getA0_1() {
		List<SuiteTestPairings> testPool = new ArrayList<SuiteTestPairings>();
		testPool.add(new SuiteTestPairings("GreetingMainProvided","GreetingMainProvided"));
		testPool.add(new SuiteTestPairings("GreetingClassRegistryProvided","GreetingClassRegistryProvided"));
		testPool.add(new SuiteTestPairings("GreetingRun","GreetingRun"));
		testPool.add(new SuiteTestPairings("GreetingCheckstyle","GreetingCheckstyle"));
		List<SuiteTestPairings> suiteAll = new ArrayList<SuiteTestPairings>();
		for(SuiteTestPairings sp:testPool)
			suiteAll.add(new SuiteTestPairings("F20Assignment0_1Suite",sp.testName));
		testPool.addAll(suiteAll);
		return new SuiteMapping(testPool);
	}
	
	private SuiteMapping getA1() {
		List<SuiteTestPairings> testPool = new ArrayList<SuiteTestPairings>();
		testPool.add(new SuiteTestPairings("RequiredClassesSuite","SocialDistanceClassRegistryProvided"));
		testPool.add(new SuiteTestPairings("RequiredClassesSuite","SocialDistanceUtilityTesterMainProvided"));
		testPool.add(new SuiteTestPairings("RequiredClassesSuite","SocialDistanceUtilityProvided"));
		testPool.add(new SuiteTestPairings("RequiredClassesSuite","SocialDistanceBasicMainProvided"));
		testPool.add(new SuiteTestPairings("RequiredClassesSuite","SocialDistanceInterpolatingMainProvided"));
		testPool.add(new SuiteTestPairings("RequiredClassesSuite","SocialDistanceDerivingMainProvided"));
		testPool.add(new SuiteTestPairings("RequiredClassesSuite","SocialDistanceInferringMainProvided"));
		testPool.add(new SuiteTestPairings("RequiredClassesSuite","SafeSocializationtxtProvided"));
		
		testPool.add(new SuiteTestPairings("MainClassSuite","BasicMainTest"));
		testPool.add(new SuiteTestPairings("MainClassSuite","InterpolatingMainTest"));
		testPool.add(new SuiteTestPairings("MainClassSuite","DerivingMainTest"));
		testPool.add(new SuiteTestPairings("MainClassSuite","InferringMainTest"));
		testPool.add(new SuiteTestPairings("MainClassSuite","UtilityTesterMainTest"));
		
		testPool.add(new SuiteTestPairings("UtilityClassSuite","GivenSafetyTest"));
		testPool.add(new SuiteTestPairings("UtilityClassSuite","InterpolatedSafetyTest"));
		testPool.add(new SuiteTestPairings("UtilityClassSuite","IsInterpolatedTwoParameterSafetyTest"));
		testPool.add(new SuiteTestPairings("UtilityClassSuite","IsInterpolatedOneParameterSafetyTest"));
		testPool.add(new SuiteTestPairings("UtilityClassSuite","DerivedSafetyTest"));
		testPool.add(new SuiteTestPairings("UtilityClassSuite","PrintGeneratedCombinationDerivedSafetyTest"));
		testPool.add(new SuiteTestPairings("UtilityClassSuite","PrintGivenAndGeneratedCombinationDerivedTest"));
		testPool.add(new SuiteTestPairings("UtilityClassSuite","GenerateSafeDistancesAndDurationsTest"));
		testPool.add(new SuiteTestPairings("UtilityClassSuite","PrintSafeDistancesAndDurationsTest"));
		testPool.add(new SuiteTestPairings("UtilityClassSuite","IsInferredSafeTest"));
		testPool.add(new SuiteTestPairings("UtilityClassSuite","PrintGivenAndGeneratedCombinationsInferredSafety"));
		testPool.add(new SuiteTestPairings("UtilityClassSuite","CompareSafetyComputationsTest"));
		testPool.add(new SuiteTestPairings("UtilityClassSuite","WekaFileSizeTest"));
		
		testPool.add(new SuiteTestPairings("A1GeneralStyleSuite","A1NoCheckstyleWarnings")); //all ec
		testPool.add(new SuiteTestPairings("A1GeneralStyleSuite","A1NamedConstants"));
		testPool.add(new SuiteTestPairings("A1GeneralStyleSuite","A1PublicMethodsOverride"));
		testPool.add(new SuiteTestPairings("A1GeneralStyleSuite","A1InterfaceAsType"));
		testPool.add(new SuiteTestPairings("A1GeneralStyleSuite","A1MnemonicNames"));
		testPool.add(new SuiteTestPairings("A1GeneralStyleSuite","A1AccessModifiersMatched"));

		testPool.add(new SuiteTestPairings("A1ModularitySuite","ModelReuse")); //all ec
		testPool.add(new SuiteTestPairings("A1ModularitySuite","InterpolatedReuse"));
		testPool.add(new SuiteTestPairings("A1ModularitySuite","PrintingCodeReuse"));
		testPool.add(new SuiteTestPairings("A1ModularitySuite","ExpectedSubtleJavaDocs"));
		testPool.add(new SuiteTestPairings("A1ModularitySuite","MVCAndFactoryCalls"));
		
		testPool.add(new SuiteTestPairings("FactoriesSuite","ModelFactoryTest"));
		testPool.add(new SuiteTestPairings("FactoriesSuite","ViewFactoryTest"));
		testPool.add(new SuiteTestPairings("FactoriesSuite","ControllerFactoryTest"));
		testPool.add(new SuiteTestPairings("FactoriesSuite","ClassifierFactoryTest"));
		
		List<SuiteTestPairings> suiteAll = new ArrayList<SuiteTestPairings>();
		for(SuiteTestPairings sp:testPool)
			suiteAll.add(new SuiteTestPairings("F20Assignment1Suite",sp.testName));
		testPool.addAll(suiteAll);
		return new SuiteMapping(testPool);
	}

	private SuiteMapping getA2() {
		List<SuiteTestPairings> testPool = new ArrayList<SuiteTestPairings>();
		testPool.add(new SuiteTestPairings("SocialDistanceFileTestSuite","SocialDistancePlProvided"));
		testPool.add(new SuiteTestPairings("SocialDistanceFileTestSuite","SocialDiststancePlNoMagicNumbersTest"));
		testPool.add(new SuiteTestPairings("SocialDistanceFileTestSuite","SocialDiststancePlNoExternalFunctionsTest"));
		
		testPool.add(new SuiteTestPairings("GivenSizesTestSuite","GivenSizes_OutputGeneration"));
		testPool.add(new SuiteTestPairings("GivenSizesTestSuite","GivenSizes_TableTest"));
		
		testPool.add(new SuiteTestPairings("GivenSafeTestSuite","GivenSafe_OutputGeneration"));
		testPool.add(new SuiteTestPairings("GivenSafeTestSuite","GivenSafe_TableTest"));
		
		testPool.add(new SuiteTestPairings("DerivedSafeTestSuite","DerivedSafe_OutputGeneration"));
		testPool.add(new SuiteTestPairings("DerivedSafeTestSuite","DerivedSafe_TableTest"));
		testPool.add(new SuiteTestPairings("DerivedSafeTestSuite","DerivedSafe_OffTableTrueTest"));
		testPool.add(new SuiteTestPairings("DerivedSafeTestSuite","DerivedSafe_OffTableFalseTest"));
		
		testPool.add(new SuiteTestPairings("InterpolatedSafeTestSuite","InterpolatedSafe_OutputGeneration"));
		testPool.add(new SuiteTestPairings("InterpolatedSafeTestSuite","InterpolatedSafe_TableTest"));
		testPool.add(new SuiteTestPairings("InterpolatedSafeTestSuite","InterpolatedSafe_OffTableTrueTest"));
		testPool.add(new SuiteTestPairings("InterpolatedSafeTestSuite","InterpolatedSafe_OffTableFalseTest"));

		testPool.add(new SuiteTestPairings("InterpolatedSafeOneTwoParamTestSuite","InterpolatedSafe_OutputGeneration_OneTwoParam"));
		testPool.add(new SuiteTestPairings("InterpolatedSafeOneTwoParamTestSuite","InterpolatedSafe_OneParamTrueValueTest"));
		testPool.add(new SuiteTestPairings("InterpolatedSafeOneTwoParamTestSuite","InterpolatedSafe_OneParamFalseValueTest"));
		testPool.add(new SuiteTestPairings("InterpolatedSafeOneTwoParamTestSuite","InterpolatedSafe_TwoParamTrueValueTest"));
		testPool.add(new SuiteTestPairings("InterpolatedSafeOneTwoParamTestSuite","InterpolatedSafe_TwoParamFalseValueTest"));

		testPool.add(new SuiteTestPairings("GenerateSafeDistancesAndDurationsTestSuite","GenerateSafeDistancesAndDurations_OutputGeneration"));
		testPool.add(new SuiteTestPairings("GenerateSafeDistancesAndDurationsTestSuite","GenerateSafeDistancesAndDurations_FalseValueTest"));
		testPool.add(new SuiteTestPairings("GenerateSafeDistancesAndDurationsTestSuite","GenerateSafeDistancesAndDurations_TrueValueTest"));
		
		testPool.add(new SuiteTestPairings("ListGivenSafeTestSuite","ListGivenSafe_OutputGeneration"));
		testPool.add(new SuiteTestPairings("ListGivenSafeTestSuite","ListGivenSafe_SafeTupleTest"));
		testPool.add(new SuiteTestPairings("ListGivenSafeTestSuite","ListGivenSafe_ValuesTest"));
		
		testPool.add(new SuiteTestPairings("PrintGivenCombinationsTestSuite","PrintGivenCombinations_OutputGeneration"));
		testPool.add(new SuiteTestPairings("PrintGivenCombinationsTestSuite","PrintGivenCombinations_TableSizedInputs"));
		testPool.add(new SuiteTestPairings("PrintGivenCombinationsTestSuite","PrintGivenCombinatations_NonTableSizedInputs"));
		
		testPool.add(new SuiteTestPairings("ListGenerateSafeDistancesAndDurationsTestSuite","ListGenerateSafeDistancesAndDurations_OutputGeneration"));
		testPool.add(new SuiteTestPairings("ListGenerateSafeDistancesAndDurationsTestSuite","ListGenerateSafeDistancesAndDurations_FalseValueTest"));
		testPool.add(new SuiteTestPairings("ListGenerateSafeDistancesAndDurationsTestSuite","ListGenerateSafeDistancesAndDurations_TrueValueTest"));
	
		List<SuiteTestPairings> suiteAll = new ArrayList<SuiteTestPairings>();
		for(SuiteTestPairings sp:testPool)
			suiteAll.add(new SuiteTestPairings("F20Assignment2Suite",sp.testName));
		testPool.addAll(suiteAll);
		return new SuiteMapping(testPool);
	}
	
	private SuiteMapping getA3() {
		List<SuiteTestPairings> testPool = new ArrayList<SuiteTestPairings>();
		testPool.add(new SuiteTestPairings("SocialDistanceFileTestSuite","SocialDistanceSMLProvided"));
		
		testPool.add(new SuiteTestPairings("FunctionalProgrammingTestSuite","GivenSafeSML"));
		testPool.add(new SuiteTestPairings("FunctionalProgrammingTestSuite","InterpolatedSafeSML"));
		
		testPool.add(new SuiteTestPairings("ListsAndRecursionTestSuite","ListDerivedSafeSML"));
		
		testPool.add(new SuiteTestPairings("FunctionParametersTestSuite","PrintSafety"));
		testPool.add(new SuiteTestPairings("FunctionParametersTestSuite","ConcisePrintSafety"));
		testPool.add(new SuiteTestPairings("FunctionParametersTestSuite","ListPrintSafety"));
		testPool.add(new SuiteTestPairings("FunctionParametersTestSuite","MatchingSafe"));
		testPool.add(new SuiteTestPairings("FunctionParametersTestSuite","MatchingGivenSafe"));
		testPool.add(new SuiteTestPairings("FunctionParametersTestSuite","MatchingDerivedSafe"));
		
		testPool.add(new SuiteTestPairings("FunctionReturnValuesTestSuite","CurriedOnceInterpolatedSafe"));
		testPool.add(new SuiteTestPairings("FunctionReturnValuesTestSuite","CurriedTwiceInterpolatedSafe"));
		testPool.add(new SuiteTestPairings("FunctionReturnValuesTestSuite","CurryableInterpolatedSafe"));
		testPool.add(new SuiteTestPairings("FunctionReturnValuesTestSuite","CurryableMatchingSafe"));
		testPool.add(new SuiteTestPairings("FunctionReturnValuesTestSuite","CurriedMatchingDerivedSafe"));
		testPool.add(new SuiteTestPairings("FunctionReturnValuesTestSuite","CurriedMatchingGivenSafe"));
		
		testPool.add(new SuiteTestPairings("StyleTest","StyleTest")); //extra
		
		List<SuiteTestPairings> suiteAll = new ArrayList<SuiteTestPairings>();
		for(SuiteTestPairings sp:testPool)
			suiteAll.add(new SuiteTestPairings("F20Assignment3Suite",sp.testName));
		testPool.addAll(suiteAll);
		return new SuiteMapping(testPool);
	}
	
	private SuiteMapping getA4() {
		List<SuiteTestPairings> testPool = new ArrayList<SuiteTestPairings>();
		testPool.add(new SuiteTestPairings("SocialDistanceFileTestSuite","SocialDistanceLispProvided"));
		
		testPool.add(new SuiteTestPairings("ListsAndRecursionTestSuite","ListDerivedSafeLisp")); 
		testPool.add(new SuiteTestPairings("ListsAndRecursionTestSuite","SafetyTableTest")); 
		
		List<SuiteTestPairings> suiteAll = new ArrayList<SuiteTestPairings>();
		for(SuiteTestPairings sp:testPool)
			suiteAll.add(new SuiteTestPairings("F20Assignment4Suite",sp.testName));
		testPool.addAll(suiteAll);
		return new SuiteMapping(testPool);
	}
	
	private SuiteMapping getA5() {
		List<SuiteTestPairings> testPool = new ArrayList<SuiteTestPairings>();
		testPool.add(new SuiteTestPairings("A1RequiredClassesSuite","ClassRegistryProvided"));
		testPool.add(new SuiteTestPairings("A1RequiredClassesSuite","SExpressionClassProvided"));
		testPool.add(new SuiteTestPairings("A1RequiredClassesSuite","MainClassProvided"));
		
		testPool.add(new SuiteTestPairings("ToStringSuite_Updated","BaseCaseSExpressionToStringChecker")); 
		testPool.add(new SuiteTestPairings("ToStringSuite_Updated","BaseCaseListToStringChecker")); 
		testPool.add(new SuiteTestPairings("ToStringSuite_Updated","ListToStringChecker")); 
		testPool.add(new SuiteTestPairings("ToStringSuite_Updated","ListToStringDeepChecker"));
		
		testPool.add(new SuiteTestPairings("TestLispFileProvided","TestLispFileProvided"));
		
		testPool.add(new SuiteTestPairings("A1LoadSuite","TestLispFileProvided"));
		testPool.add(new SuiteTestPairings("A1LoadSuite","LoadChecker"));
		testPool.add(new SuiteTestPairings("A1LoadSuite","ConsTestedChecker"));
		testPool.add(new SuiteTestPairings("A1LoadSuite","QuoteTestedChecker"));
		testPool.add(new SuiteTestPairings("A1LoadSuite","ListTestedChecker"));
		testPool.add(new SuiteTestPairings("A1LoadSuite","EvalTestedChecker"));
		testPool.add(new SuiteTestPairings("A1LoadSuite","CondTestedChecker"));
		testPool.add(new SuiteTestPairings("A1LoadSuite","ConsTestedTwoResultsChecker"));
		testPool.add(new SuiteTestPairings("A1LoadSuite","QuoteTestedTwoResultsChecker"));
		testPool.add(new SuiteTestPairings("A1LoadSuite","ListTestedTwoResultsChecker"));
		testPool.add(new SuiteTestPairings("A1LoadSuite","EvalTestedTwoResultsChecker"));
		testPool.add(new SuiteTestPairings("A1LoadSuite","CondTestedResultsChecker"));
		
		testPool.add(new SuiteTestPairings("A1LoadLogicalSuite","TestLispFileProvided"));
		testPool.add(new SuiteTestPairings("A1LoadLogicalSuite","LoadChecker"));
		testPool.add(new SuiteTestPairings("A1LoadLogicalSuite","AndTestedChecker"));
		testPool.add(new SuiteTestPairings("A1LoadLogicalSuite","AndTestedTwoResultsChecker"));
		testPool.add(new SuiteTestPairings("A1LoadLogicalSuite","NotTestedChecker"));
		testPool.add(new SuiteTestPairings("A1LoadLogicalSuite","NotTestedTwoResultsChecker"));
		testPool.add(new SuiteTestPairings("A1LoadLogicalSuite","OrTestedChecker"));
		testPool.add(new SuiteTestPairings("A1LoadLogicalSuite","OrTestedTwoResultsChecker"));

		testPool.add(new SuiteTestPairings("A1LoadRelationSuite","TestLispFileProvided"));
		testPool.add(new SuiteTestPairings("A1LoadRelationSuite","LoadChecker"));
		testPool.add(new SuiteTestPairings("A1LoadRelationSuite","GreaterTestedChecker"));
		testPool.add(new SuiteTestPairings("A1LoadRelationSuite","GreaterTestedTwoResultsChecker"));
		testPool.add(new SuiteTestPairings("A1LoadRelationSuite","LessTestedChecker"));
		testPool.add(new SuiteTestPairings("A1LoadRelationSuite","LessTestedTwoResultsChecker"));
		testPool.add(new SuiteTestPairings("A1LoadRelationSuite","GorETestedChecker"));
		testPool.add(new SuiteTestPairings("A1LoadRelationSuite","GorETestedTwoResultsChecker"));
		testPool.add(new SuiteTestPairings("A1LoadRelationSuite","LorETestedChecker"));
		testPool.add(new SuiteTestPairings("A1LoadRelationSuite","LorETestedTwoResultsChecker"));

		
		List<SuiteTestPairings> suiteAll = new ArrayList<SuiteTestPairings>();
		for(SuiteTestPairings sp:testPool)
			suiteAll.add(new SuiteTestPairings("F20Assignment5Suite",sp.testName));
		testPool.addAll(suiteAll);
		return new SuiteMapping(testPool);
	}
	
	private SuiteMapping getA6() {
		List<SuiteTestPairings> testPool = new ArrayList<SuiteTestPairings>();
		testPool.add(new SuiteTestPairings("A6RequiredClassesSuite","ClassRegistryProvided"));
		testPool.add(new SuiteTestPairings("A6RequiredClassesSuite","ClassRegistryA3Provided"));
		testPool.add(new SuiteTestPairings("A6RequiredClassesSuite","MainClassProvided"));
		testPool.add(new SuiteTestPairings("A6RequiredClassesSuite","OperationRegistryProvided"));
		
		testPool.add(new SuiteTestPairings("BasicOperationSuite","CondChecker"));
		testPool.add(new SuiteTestPairings("BasicOperationSuite","ExtraCondChecker"));
		testPool.add(new SuiteTestPairings("BasicOperationSuite","QuoteChecker"));
		testPool.add(new SuiteTestPairings("BasicOperationSuite","EvalChecker"));
		testPool.add(new SuiteTestPairings("BasicOperationSuite","ListChecker"));
		testPool.add(new SuiteTestPairings("BasicOperationSuite","ListChecker2"));
		testPool.add(new SuiteTestPairings("BasicOperationSuite","CombinationChecker"));
		
		testPool.add(new SuiteTestPairings("LogicalSuite","BasicAndChecker"));
		testPool.add(new SuiteTestPairings("LogicalSuite","BasicOrChecker"));
		testPool.add(new SuiteTestPairings("LogicalSuite","NotChecker"));
		testPool.add(new SuiteTestPairings("LogicalSuite","VariableArgumentAndChecker"));
		testPool.add(new SuiteTestPairings("LogicalSuite","VariableArgumentOrChecker"));
		
		testPool.add(new SuiteTestPairings("RelationalSuite","GreaterChecker"));
		testPool.add(new SuiteTestPairings("RelationalSuite","GreaterOrEqualChecker"));
		testPool.add(new SuiteTestPairings("RelationalSuite","LessChecker"));
		testPool.add(new SuiteTestPairings("RelationalSuite","LessOrEqualChecker"));

		testPool.add(new SuiteTestPairings("LispToStringSuite","TestLispFileProvided"));
		testPool.add(new SuiteTestPairings("LispToStringSuite","LoadLispChecker"));
		testPool.add(new SuiteTestPairings("LispToStringSuite","LispIsListChecker"));
		testPool.add(new SuiteTestPairings("LispToStringSuite","LispIsListChecker2"));
		testPool.add(new SuiteTestPairings("LispToStringSuite","LispIsListChecker3"));
		testPool.add(new SuiteTestPairings("LispToStringSuite","LispIsListChecker4"));
		testPool.add(new SuiteTestPairings("LispToStringSuite","LispToStringChecker"));
		testPool.add(new SuiteTestPairings("LispToStringSuite","LispToStringChecker2"));
		testPool.add(new SuiteTestPairings("LispToStringSuite","LispToStringChecker3"));
		testPool.add(new SuiteTestPairings("LispToStringSuite","LispToStringChecker4"));
		
		testPool.add(new SuiteTestPairings("LispSetqSuite","LispSetqChecker"));
		testPool.add(new SuiteTestPairings("LispSetqSuite","LispSetqChecker2"));
		testPool.add(new SuiteTestPairings("LispSetqSuite","LoadSetqChecker"));
		testPool.add(new SuiteTestPairings("LispSetqSuite","LoadSetqChecker2"));
		testPool.add(new SuiteTestPairings("LispSetqSuite","LoadSetqChecker3"));
		
		testPool.add(new SuiteTestPairings("LispLambdaSuite","LispLambdaChecker"));
		testPool.add(new SuiteTestPairings("LispLambdaSuite","LispLambdaChecker2"));
		testPool.add(new SuiteTestPairings("LispLambdaSuite","LispLambdaChecker3"));
		testPool.add(new SuiteTestPairings("LispLambdaSuite","LoadLambdaChecker"));
		testPool.add(new SuiteTestPairings("LispLambdaSuite","LoadLambdaChecker2"));
		testPool.add(new SuiteTestPairings("LispLambdaSuite","LoadLambdaChecker3"));
		
		testPool.add(new SuiteTestPairings("FuncallSocialDistanceSuite","DerivedSafeChecker"));
		
		testPool.add(new SuiteTestPairings("BasicFunctionExpressionSuite","TimesGeneratorChecker"));
		testPool.add(new SuiteTestPairings("BasicFunctionExpressionSuite","PlusGeneratorChecker"));
		
		testPool.add(new SuiteTestPairings("BasicCurrySuite","CurryOnceChecker"));
		testPool.add(new SuiteTestPairings("BasicCurrySuite","CurryTwiceChecker"));
		testPool.add(new SuiteTestPairings("BasicCurrySuite","CurryTwoArgsChecker"));
		
		List<SuiteTestPairings> suiteAll = new ArrayList<SuiteTestPairings>();
		for(SuiteTestPairings sp:testPool)
			suiteAll.add(new SuiteTestPairings("F20Assignment6Suite",sp.testName));
		testPool.addAll(suiteAll);
		return new SuiteMapping(testPool);
	}
}
