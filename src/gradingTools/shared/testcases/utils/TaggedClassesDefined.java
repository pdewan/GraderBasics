package gradingTools.shared.testcases.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import grader.basics.config.BasicExecutionSpecificationSelector;
import grader.basics.execution.NotRunnableException;
import grader.basics.execution.RunningProject;
import grader.basics.junit.JUnitTestsEnvironment;
import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.BasicProjectIntrospection;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import grader.basics.project.source.ABasicTextManager;
import grader.basics.testcase.PassFailJUnitTestCase;
import gradingTools.shared.testcases.SubstringSequenceChecker;
import gradingTools.shared.testcases.openmp.OpenMPPragma;
import gradingTools.shared.testcases.openmp.OpenMPUtils;
import gradingTools.shared.testcases.openmp.scannedTree.AssignmentSNode;
import gradingTools.shared.testcases.openmp.scannedTree.ExternalMethodSNode;
import gradingTools.shared.testcases.openmp.scannedTree.ForSNode;
import gradingTools.shared.testcases.openmp.scannedTree.MethodSNode;
import gradingTools.shared.testcases.openmp.scannedTree.OMPForSNode;
import gradingTools.shared.testcases.openmp.scannedTree.OMPParallelSNode;
import gradingTools.shared.testcases.openmp.scannedTree.OMPSNodeUtils;
import gradingTools.shared.testcases.openmp.scannedTree.RootOfFileSNode;
import gradingTools.shared.testcases.openmp.scannedTree.RootOfProgramSNode;
import gradingTools.shared.testcases.openmp.scannedTree.SNode;
import gradingTools.utils.RunningProjectUtils;
import util.annotations.MaxValue;

public abstract class TaggedClassesDefined extends PassFailJUnitTestCase {
	
	Map<String[], Class> tagToClass = new HashMap<String[], Class>();
	Map<Class, String[]> classToTag = new HashMap<>();
	int numDuplicateClasses = 0;
	int numMissingClasses = 0;
	double score = 0;

	public double getScore() {
		return score;
	}
	public int getNumMissingClasses() {
		return numMissingClasses;
	}
	public int getNumDuplicateClasses() {
		return numDuplicateClasses;
	}
	public TaggedClassesDefined() {
	}

	public Map<String[], Class> getTagToClass() {
		return tagToClass;
	}
	
	public Map<Class, String[]> getClassToTag() {
		return classToTag;
	}
	
	public abstract String[][] getClassesTags() ;

	@Override
	public TestCaseResult test(Project project, boolean autoGrade) throws NotAutomatableException,
			NotGradableException {
//		Map<String[], Class> tagToClass = new HashMap<String[], Class>();
//		Map<Class, String[]> classToTag = new HashMap<>();
//		int aNumDuplicateClasses = 0;
//		int aNumMissingClasses = 0;

		try {
			String[][] aClassesTags = getClassesTags();
			for (String[] aClassTags:aClassesTags) {
				Class aMatchedClass = BasicProjectIntrospection.findClassByTags(aClassTags);
				if (aMatchedClass != null) {					
//					tagToClass.put(aClassTags, aMatchedClass);
					String[] aPreviousTags = classToTag.get(aMatchedClass);
					tagToClass.put(aClassTags, aMatchedClass);
					if (classToTag.get(aMatchedClass) != null) {
						System.err.println(aMatchedClass + " matching tags" + Arrays.toString(aPreviousTags) + " and " + Arrays.toString(aClassTags));
						numDuplicateClasses++;
					} else {
						classToTag.put(aMatchedClass, aClassTags);
					}
				} else {
					System.err.println("No class found for tags:" + aClassTags + ".\n Please define at least an empty class with these tags to receive non-zero style credit");
					numMissingClasses++;
				}
			}

			double aNumTags = getClassesTags().length;
			if (numMissingClasses > 0) {
				return fail (numMissingClasses + " classes for which required tags are missing.");
			}
			score = 1.0;
			if (numDuplicateClasses > 0) {
				score  = (aNumTags - numDuplicateClasses )/aNumTags;

				return partialPass (score, numDuplicateClasses + " classes that combine tags that should be associated with separate classes. Style sscores will be normalized");
				
//				return fail (numMissingClasses + " classes for which required tags are missing.");
			}
				
//			double aScore  = (aNumTags - numDuplicateClasses - numMissingClasses)/aNumTags;
//			if (aScore < 1.0) {
//				return partialPass(aScore, "Did not find unique classes for all expected tags. See console trace");
//			}
			return pass();
			

		} catch (NotRunnableException e) {
			throw new NotGradableException();
		}
	}

	public TestCaseResult computeResultBasedOnTaggedClasses(TestCaseResult aSuperResult) {
		if (getScore() == 1.0) {
			return aSuperResult;
		} else if (getScore() == 0) {
			return fail("Create " + getNumMissingClasses() + "  classes with missing tags to receive non zero score on this test");

		} else  {
			double anOriginalPercentage = aSuperResult.getPercentage();
			double aPercentage = getScore()*anOriginalPercentage;
			
			return partialPass(aPercentage, "Raw score of " + anOriginalPercentage + " scaled by " + getScore() + " because of duplicate tagged classes. See console trace of failing and passing lines");
		}
		
        
    }
	 
}
