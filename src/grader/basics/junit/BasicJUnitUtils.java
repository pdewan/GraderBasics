package grader.basics.junit;

import grader.basics.observers.FileWriterFactory;
import grader.basics.project.BasicProjectIntrospection;
//import grader.junit.GraderTestCase;



import grader.basics.project.CurrentProjectHolder;
import grader.basics.project.Project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runners.Suite;

import bus.uigen.ObjectEditor;


public class BasicJUnitUtils {

	public static void assertTrue (Throwable e, double aFractionComplete) {
		if (e instanceof AssertionError) {
			Assert.assertTrue(e.getMessage() + NotesAndScore.PERECTAGE_CHARACTER + aFractionComplete, false);

		} else {
		Assert.assertTrue(e.getClass().getName() + " " + e.getMessage() + NotesAndScore.PERECTAGE_CHARACTER + aFractionComplete, false);
		}
	}
	public static void assertTrue (String aMessage, double aFractionComplete, boolean aCondition) {
		Assert.assertTrue(aMessage + NotesAndScore.PERECTAGE_CHARACTER + aFractionComplete, aCondition);

	}

//	public static  Map<String,List<GraderTestCase>>  toGraderTestCaseMap (Map<String,List<GradableJUnitTest>> aGradableJUnitTestCaseMap) {
//		Map<String,List<GraderTestCase>> retVal = new HashMap();
//		for (String aGroup:aGradableJUnitTestCaseMap.keySet()) {
//			retVal.put(aGroup, toGraderTestCaseList(aGradableJUnitTestCaseMap.get(aGroup)));
//		}
//		return retVal;
//	}
	public static Set<GradableJUnitSuite> findGradableTrees(Collection<Class> aClasses) {
		Set<Class> aSuiteClasses = findTopLevelSuites(aClasses);
		return toGradableTrees(aSuiteClasses);
	}
	
	public static Set<GradableJUnitSuite> toGradableTrees(Collection<Class> aJUnitClasses ) {
		Set<GradableJUnitSuite> result = new HashSet();
		for (Class aClass:aJUnitClasses) {
			GradableJUnitSuite aTree = toGradableTree (aClass);
			if (aTree != null) {
				result.add(aTree);
			}
		}
		return result;
	}
	public static GradableJUnitTest toGradable (Class<?> aSuiteOrTestClass) {
		if (aSuiteOrTestClass == null) {
			return null;
		}
	
	if (!isJUnitSuite(aSuiteOrTestClass)) {
		
		return new AGradableJUnitTest(aSuiteOrTestClass);
	}
	return toGradableTree(aSuiteOrTestClass);
	}
	public  static GradableJUnitTest createGradable (String aKeyWord) {
		Class aClass = BasicProjectIntrospection.findClassByKeyword(aKeyWord);
		return toGradable(aClass);
	}
	
	public static void interactiveTestAll(Class<?> aJUnitSuiteClass) {
		GradableJUnitSuite aGradable = BasicJUnitUtils.toGradableTree(aJUnitSuiteClass);
		RunNotifierFactory.getRunNotifier().addListener(FileWriterFactory.getFileWriter());
		aGradable.testAll();
		ObjectEditor.treeEdit(aGradable);
	}
	public static void interactiveTestAll(String aSourceFilePattern, Class<?> aJUnitSuiteClass) {
		CurrentProjectHolder.setProject(aSourceFilePattern);
		interactiveTestAll(aJUnitSuiteClass);
	}
	public static void jUnitCoreTestAll(Class<?> aJUnitSuiteClass) {
		Result aResult = JUnitCore.runClasses(aJUnitSuiteClass);
		for (Failure failure : aResult.getFailures()) {
	         System.out.println("To string" + failure.toString());
	         System.out.println("Header" + failure.getTestHeader());
	         System.out.println ("Trace:" + failure.getTrace());
	         System.out.println ("Description:" + failure.getDescription());
	      }
	    System.out.println(aResult.wasSuccessful());

	}
	public static GradableJUnitSuite toGradableTree (Class<?> aJUnitSuiteClass) {
		if (aJUnitSuiteClass == null) {
			return null;
		}
//		if (!isJUnitSuite(aJUnitSuiteClass)) {
//			
//			return new AGradableJUnitTest(aJUnitSuiteClass);
//		}
		Map<String,List<GradableJUnitTest>> aGradableJUnitTestCaseMap = BasicJUnitUtils.toGroupedGradables(aJUnitSuiteClass);
		GradableJUnitSuite retVal = new AGradableJUnitTopLevelSuite(aJUnitSuiteClass);
//		List<GradableJUnitTest> retVal = new ArrayList();
		for (String aGroup:aGradableJUnitTestCaseMap.keySet()) {
			List<GradableJUnitTest> aGradables = aGradableJUnitTestCaseMap.get(aGroup);
			if (aGradables.size() == 2) { // an ungrouped test
				retVal.add(aGradables.get(1));
			} else {
				retVal.add(aGradables.get(0)); // will also have the children
			}
//			retVal.put(aGroup, toGraderTestCaseList(aGradableJUnitTestCaseMap.get(aGroup)));
		}
		return retVal;
	}
//	public static GraderTestCase toGraderTestCase(GradableJUnitTest aGradableJUnitCase){
//		return new AGraderTestCase(aGradableJUnitCase);
//	}
//	public static  List<GraderTestCase> toGraderTestCaseList(List<GradableJUnitTest> aGradableJUnitCaseList){
//		List<GraderTestCase> retVal = new ArrayList();
//		for (GradableJUnitTest aGradableJUnitTestCase:aGradableJUnitCaseList) {
//			if (aGradableJUnitTestCase instanceof GradableJUnitSuite)  
//				continue; // that is just for display and hierarchy purposes
//			retVal.add(new AGraderTestCase(aGradableJUnitTestCase));
//		}
//		return retVal;
//	}
//	@Override
//	public void addJUnitTestSuite (Class<?> aJUnitSuiteClass) {
//		List<Class> aJUnitOrSuiteClasses = getTopLevelJUnitTestsAndSuites(aJUnitSuiteClass);
//		List<Class> aSuiteClasses = selectJUnitSuites(aJUnitOrSuiteClasses);
//		List<Class> aTestCases = new ArrayList(aJUnitOrSuiteClasses);
//		aTestCases.removeAll(aSuiteClasses);
//		Map<String, List<GradableJUnitTest>>  aGroupedTopLevelGradables = toTopLevelGradables(aTestCases);
//		Map<String, List<GradableJUnitTest>>  aGroupedSuiteGradables = toSuiteGradables(aSuiteClasses);
//		Map<String,List<GradableJUnitTest>> aGroupedGradables = new HashMap();
//		aGroupedGradables.putAll(aGroupedTopLevelGradables);
//		aGroupedGradables.putAll(aGroupedSuiteGradables);
//		List<GradableJUnitTest> aGradableTree = toGradableTree(aGroupedGradables);
//		ObjectEditor.treeEdit(aGradableTree);
//		Map<String, List<GraderTestCase>> aGroupedTestCases = toGraderTestCaseMap(aGroupedGradables);		
//		addGroupedTwoLevelTestCases(aGroupedTestCases);		
//	}
	public static List<Class> selectJUnitSuites (List<Class> aJUnitSuiteAndTestCases) {
		List<Class> retVal = new ArrayList();
		for (Class aJUnitTestCase:aJUnitSuiteAndTestCases) {
			if (isJUnitSuite(aJUnitTestCase)) {
				retVal.add(aJUnitTestCase);
			}
		}
		return retVal;
	}
	
//	public void addJUnitTestSuiteFlat (Class<?> aJUnitSuiteClass) {
//		List<Class> aJUnitClasses = getJUnitTestClassesDeep(aJUnitSuiteClass);
//		Map<String, List<JUnitTestToGraderTestCase>>  aGroupedTestCases = createAndCollectTopLevelTestCases(aJUnitClasses);
//		Map<String, List<JUnitTestToGraderTestCase>>  aGroupedSuiteTestCases = createAndCollectSuiteTestCases(aSuiteClasses)(aJUnitClasses);
//
//		addGroupedTwoLevelTestCases(aGroupedTestCases);
//		
//	}
//	static GraderTestCase[] testCasesType = {};
	
//	public void addGroupedTwoLevelTestCases(Map<String, List<GraderTestCase>> aTestCases) {
//		for (String aGroup:aTestCases.keySet()) {
//			List<GraderTestCase> aJUnitTestToGraderTestCases = aTestCases.get(aGroup);
//			double aTotalScore = computeTotalScore(aJUnitTestToGraderTestCases);
//			setPointWeights(aJUnitTestToGraderTestCases, aTotalScore);
//			GraderTestCase aFirstCase = aJUnitTestToGraderTestCases.get(0);
//			
//			boolean anIsRestriction = aFirstCase.isRestriction();
//			boolean anIsExtraCredit = aFirstCase.isExtra();
//			if (anIsRestriction) {
//				addRestriction(aGroup, aTotalScore, aJUnitTestToGraderTestCases.toArray(testCasesType));
//			} else {
//				addFeature(aGroup, aTotalScore, anIsExtraCredit, aJUnitTestToGraderTestCases.toArray(testCasesType));
//			}			
//		}		
//	}
//	public void separateAndAddtestCasesAndSuite(Map<String, List<JUnitTestToGraderTestCase>> aTestCases) {
//		for (String aGroup:aTestCases.keySet()) {
//			List<JUnitTestToGraderTestCase> aJUnitTestToGraderTestCases = aTestCases.get(aGroup);
//			double aTotalScore = computeTotalScore(aJUnitTestToGraderTestCases);
//			setPointWeights(aJUnitTestToGraderTestCases, aTotalScore);
//			JUnitTestToGraderTestCase aFirstCase = aJUnitTestToGraderTestCases.get(0);
//			
//			boolean anIsRestriction = aFirstCase.isRestriction();
//			boolean anIsExtraCredit = aFirstCase.isExtra();
//			if (anIsRestriction) {
//				addRestriction(aGroup, aTotalScore, aJUnitTestToGraderTestCases.toArray(testCasesType));
//			} else {
//				addFeature(aGroup, aTotalScore, anIsExtraCredit, aJUnitTestToGraderTestCases.toArray(testCasesType));
//			}			
//		}		
//	}
//	public void addGroupedFlatTestCases(Map<String, List<GraderTestCase>> aTestCases) {
//		for (String aGroup:aTestCases.keySet()) {
//			List<GraderTestCase> aJUnitTestToGraderTestCases = aTestCases.get(aGroup);
//			double aTotalScore = computeTotalScore(aJUnitTestToGraderTestCases);
//			setPointWeights(aJUnitTestToGraderTestCases, aTotalScore);
//			GraderTestCase aFirstCase = aJUnitTestToGraderTestCases.get(0);
//			
//			boolean anIsRestriction = aFirstCase.isRestriction();
//			boolean anIsExtraCredit = aFirstCase.isExtra();
//			if (anIsRestriction) {
//				addRestriction(aGroup, aTotalScore, aJUnitTestToGraderTestCases.toArray(testCasesType));
//			} else {
//				addFeature(aGroup, aTotalScore, anIsExtraCredit, aJUnitTestToGraderTestCases.toArray(testCasesType));
//			}			
//		}		
//	}
	
	
//	public static double  computeTotalScore (List<GraderTestCase> aJUnitTestToGraderTestCases) {
//		double aRetVal = 0;
//		for (GraderTestCase aJUnitTestToGraderTestCase:aJUnitTestToGraderTestCases) {
//			Double aMaxScore = aJUnitTestToGraderTestCase.getMaxScore();
//			if (aMaxScore != null)
//			aRetVal += aJUnitTestToGraderTestCase.getMaxScore();
//		}
//		return aRetVal;
//	}
//	public static void setPointWeights (List<GraderTestCase> aJUnitTestToGraderTestCases) {
//		double aTotalScore = computeTotalScore(aJUnitTestToGraderTestCases);
//		setPointWeights(aJUnitTestToGraderTestCases, aTotalScore);
//	}
//	
//	public static void setPointWeights (List<GraderTestCase> aJUnitTestToGraderTestCases, double aTotalScore) {
//		for (GraderTestCase aJUnitTestToGraderTestCase:aJUnitTestToGraderTestCases) {
//			Double aMaxScore = aJUnitTestToGraderTestCase.getMaxScore();
//			if (aMaxScore != null)
//			aJUnitTestToGraderTestCase.setPointWeight(aMaxScore/aTotalScore);
//		}
//	}
//	public  Map<String, List<GraderTestCase>> createAndCollectSuiteTestCases(List<Class> aSuiteClasses) {
//		Map<String, List<GraderTestCase>> aResult = new HashMap();
//		for (Class aSuiteClass:aSuiteClasses) {
//			GradableJUnitTest aSuiteProperties = new AGradableJUnitTest(aSuiteClass);
////			String aFeatureName =  aProperties.getExplanation();
//			Double aFeatureScore = aSuiteProperties.getMaxScore();
//			List<Class> aLeafTestCases = getJUnitTestClassesDeep(aSuiteClass);
//			Double aTestMaxScore = null;
//			if (aSuiteProperties.getMaxScore() != null) {
//				aTestMaxScore = aSuiteProperties.getMaxScore()/aLeafTestCases.size();
//			}
//
//			List<GraderTestCase> aTestCases = new ArrayList();
//			for (Class aLeafTestCase:aLeafTestCases) {
//				GradableJUnitTest aTestCaseProperties = new AGradableJUnitTest(aLeafTestCase); 
//				if (aTestMaxScore != null) {
//					aTestCaseProperties.setMaxScore(aTestMaxScore);	
//					aTestCaseProperties.setGroup(aSuiteProperties.getExplanation());
//					aTestCaseProperties.setRestriction(aSuiteProperties.isRestriction());
//					aTestCaseProperties.setExtra(aSuiteProperties.isExtra());
//				}
//				GraderTestCase aJUnitTestToGraderTestCase =
//					 	new AGraderTestCase(aTestCaseProperties);
//				aTestCases.add(aJUnitTestToGraderTestCase);
//			}
//			
//			aResult.put(aSuiteProperties.getExplanation(), aTestCases);
//			
//		}
//		return aResult;
//	}
	public static  Map<String, List<GradableJUnitTest>> toSuiteGradables(List<Class> aSuiteClasses) {
		Map<String, List<GradableJUnitTest>> aResult = new HashMap();
		for (Class aSuiteClass:aSuiteClasses) {
			GradableJUnitSuite aSuiteGradable = new AGradableJUnitSuite(aSuiteClass);
//			String aFeatureName =  aProperties.getExplanation();
			Double aFeatureScore = aSuiteGradable.getMaxScore();
			List<Class> aLeafTestCases = getJUnitTestClassesDeep(aSuiteClass);
			Double aTestMaxScore = null;
			if (aSuiteGradable.getMaxScore() != null) {
				aTestMaxScore = aSuiteGradable.getMaxScore()/
						aLeafTestCases.size();
			}

			List<GradableJUnitTest> aGradables = new ArrayList();
			for (Class aLeafTestCase:aLeafTestCases) {
				GradableJUnitTest aGradable = new AGradableJUnitTest(aLeafTestCase); 
				if (aTestMaxScore != null) {
					aGradable.setMaxScore(aTestMaxScore);	
					aGradable.setGroup(aSuiteGradable.getExplanation());
					aGradable.setRestriction(aSuiteGradable.isRestriction());
					aGradable.setExtra(aSuiteGradable.isExtra());
				}
				
				aGradables.add(aGradable);
			}
			aSuiteGradable.addAll(aGradables);
//			if (aGradables.size() > 1) { // they need a parent
				aGradables.add(0, aSuiteGradable); // for consistency add it always
//			}
			aResult.put(aSuiteGradable.getExplanation(), aGradables);
			
		}
		return aResult;
	}
	public static Map<String,List<GradableJUnitTest>> toGroupedGradables(Class<?> aJUnitSuiteClass) {
		List<Class> aJUnitOrSuiteClasses = BasicJUnitUtils.getComponentTestsAndSuites(aJUnitSuiteClass);
		List<Class> aSuiteClasses = BasicJUnitUtils.selectJUnitSuites(aJUnitOrSuiteClasses);
		List<Class> aTestCases = new ArrayList(aJUnitOrSuiteClasses);
		aTestCases.removeAll(aSuiteClasses);
		Map<String, List<GradableJUnitTest>>  aGroupedTopLevelGradables = BasicJUnitUtils.toTopLevelGradables(aTestCases);
		Map<String, List<GradableJUnitTest>>  aGroupedSuiteGradables = BasicJUnitUtils.toSuiteGradables(aSuiteClasses);
		Map<String,List<GradableJUnitTest>> aGroupedGradables = new HashMap();
		aGroupedGradables.putAll(aGroupedTopLevelGradables);
		aGroupedGradables.putAll(aGroupedSuiteGradables);
		return aGroupedGradables;
	}
	
//	public  Map<String, List<GraderTestCase>> createAndCollectTopLevelTestCases(List<Class> aJUnitClasses) {
//		Map<String, List<GraderTestCase>> aResult = new HashMap();
//		for (Class aJUnitClass:aJUnitClasses) {
//			GraderTestCase aJUnitTestToGraderTestCase =
//					 	new AGraderTestCase(new AGradableJUnitTest(aJUnitClass));
//			String aGroup = aJUnitTestToGraderTestCase.getGroup();
//			List<GraderTestCase> aClasses = aResult.get(aGroup);
//			if (aClasses == null) {
//				aClasses = new ArrayList();
//				aResult.put(aGroup, aClasses);
//			}
//			aClasses.add(aJUnitTestToGraderTestCase);			
//		}
//		return aResult;
//	}
	
	public static  Map<String, List<GradableJUnitTest>> toTopLevelGradables
	(List<Class> aJUnitClasses) {
		Map<String, List<GradableJUnitTest>> aResult = new HashMap();
		for (Class aJUnitClass:aJUnitClasses) {
			GradableJUnitTest aGradableJUnitTest = new AGradableJUnitTest(aJUnitClass);			
			String aGroup = aGradableJUnitTest.getGroup();
			List<GradableJUnitTest> aClasses = aResult.get(aGroup);
			GradableJUnitSuite aSuite;
			if (aClasses == null) {
				aClasses = new ArrayList();
				aResult.put(aGroup, aClasses);
				aSuite = new AGradableJUnitSuite(aJUnitClass);
				aSuite.setExplanation(aGroup);
				aClasses.add(aSuite);
			}
			aSuite = (GradableJUnitSuite) aClasses.get(0);
			aClasses.add(aGradableJUnitTest);
			aSuite.add(aGradableJUnitTest);
		}
		return aResult;
	}
//	public  Map<String, List<JUnitTestToGraderTestCase>> createAndCollectTwoLevelTestCases(List<Class> aJUnitClasses) {
//		Map<String, List<JUnitTestToGraderTestCase>> aResult = new HashMap();
//		for (Class aJUnitClass:aJUnitClasses) {
//			JUnitTestToGraderTestCase aJUnitTestToGraderTestCase =
//					 	new AJUnitTestToGraderTestCase(aJUnitClass, new AJUnitTestToGraderProperties(aJUnitClass));
//			String aGroup = aJUnitTestToGraderTestCase.getGroup();
//			List<JUnitTestToGraderTestCase> aClasses = aResult.get(aGroup);
//			if (aClasses == null) {
//				aClasses = new ArrayList();
//				aResult.put(aGroup, aClasses);
//			}
//			aClasses.add(aJUnitTestToGraderTestCase);			
//		}
//		return aResult;
//	}
	
	public static List<Class> getJUnitTestClassesDeep (Class<?> aJUnitSuiteClass) {
		Suite.SuiteClasses aSuiteClassAnnotation = aJUnitSuiteClass.getAnnotation(Suite.SuiteClasses.class);
		if (aSuiteClassAnnotation == null)
			return null;
		Class[] aTestClasses = aSuiteClassAnnotation.value();
		List<Class> retVal = new ArrayList();
		for (Class aTestClass: aTestClasses) {
			List<Class> aSubList = getJUnitTestClassesDeep(aTestClass);
			if (aSubList == null) {
				retVal.add(aTestClass);
			} else {
				retVal.addAll(aSubList);
			}
		}
		return retVal;
	}
//	public static List<Class> getTopLevelSuites(List<Class> aSuiteOrTestClasses) {
//		
//	}
	public static boolean isJUnitSuite (Class<?> aClass) {
		return aClass.getAnnotation(Suite.SuiteClasses.class) != null;
	}
	public static List<Class> selectSuites(Collection<Class> aClasses) {
		List<Class> result = new ArrayList();
		for (Class aClass:aClasses) {
			if (isJUnitSuite(aClass)) {
				result.add(aClass);
			}
		}
		return result;
	}
	public static List<Class> getComponentSuites(Class aContainingSuite) {
		List<Class> aTestsAndSuites = getComponentTestsAndSuites (aContainingSuite);
		return selectSuites(aTestsAndSuites);		
	}
	public static Set<Class> getAllComponentSuites(Collection<Class> aSuites) {
		Set<Class>	result = new HashSet();	
		for (Class aSuite:aSuites) {
			result.addAll(getComponentSuites(aSuite));
		}
		return result;
		
	}
	
	public static Set<Class> findTopLevelSuites(Collection<Class> aClasses) {
		List<Class> aSuites = selectSuites(aClasses);
		return selectTopLevelSuites(aClasses);		
	}
	
	public static Set<Class> selectTopLevelSuites(Collection<Class> aClasses) {
		Set<Class> aComponentSuites = getAllComponentSuites(aClasses);
		Set<Class> aResult = new HashSet(aClasses);
		aResult.removeAll(aComponentSuites);
		return aResult;		
		
	}
	public static List<Class> getComponentTestsAndSuites (Class<?> aJUnitSuiteClass) {
		Suite.SuiteClasses aSuiteClassAnnotation = aJUnitSuiteClass.getAnnotation(Suite.SuiteClasses.class);
		if (aSuiteClassAnnotation == null)
			return null;
		Class[] aTestClasses = aSuiteClassAnnotation.value();
		return Arrays.asList(aTestClasses);
	}
	static {
//		RunNotifierFactory.getRunNotifier().addListener(FileWriterFactory.getFileWriter());
	}
}
