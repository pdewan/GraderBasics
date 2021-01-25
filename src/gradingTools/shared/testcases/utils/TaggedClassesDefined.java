package gradingTools.shared.testcases.utils;

import java.util.ArrayList;
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
import unc.checks.STBuilderCheck;
import util.annotations.MaxValue;
@MaxValue(10)
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
	
	public String[][] getClassesTags(Project project) {
		return null;
	}
	List<String> matchedTags = new ArrayList<String>();
	List<String> unmatchedTags = new ArrayList<String>();

	public TestCaseResult checkTagsFromCheckstyleText(Project project) {
		String aText = project.getCheckstyleText();
		if (aText == null) {
//			System.err.println("Did not find checkstyle text");
			return fail("Did not find checkstyle text");

		}
		int aTagsIndex = aText.indexOf("Expected type names/tags:");
		int aListStart = aText.indexOf('[', aTagsIndex);
		int aListEnd = aText.indexOf(']', aListStart);
		if (aListStart < 0 || aListEnd < 0) {
			System.err.println("Did not find expected types");
			return pass();
		}
		String aList = aText.substring(aListStart + 1, aListEnd );
		String[] anAndedTagsList = aList.split(",");
//		String[][] retVal = new String[anAndedTags.length][0];
		List<String> aFoundTags = new ArrayList<String>();
		matchedTags.clear();
		unmatchedTags.clear();
		String[] aLines = project.getCheckstyleLines();
		for (int anIndex = 0; anIndex < anAndedTagsList.length; anIndex++) {
			String anAndedTags = anAndedTagsList[anIndex];
//			String aNormalizedTags = anAndedTags.replaceAll(" ", "");
			String aNormalizedTags = anAndedTags.trim();

			String aContainingText = /*".*matches tags.*" +*/ "(" + aNormalizedTags + ")";
			boolean found = false;
			for (String aLine:aLines ) {
//				String aNormalizedLine = aLine.replaceAll(" ", "");
				String aNormalizedLine = aLine;

//				if (aLine.contains("matches tags")) {
//					System.err.println(" found matches tags");
//					System.err.println(aLine);
//					System.err.println(anAndedTags);
//				}
				
//				if (aLine.matches(aRegex)) {
				if (aNormalizedLine.contains(aContainingText)) {

					found = true;
					break;
				}
			}
//			boolean found = aText.matches(aRegex);
			if (found) {
				matchedTags.add(anAndedTags);
			} else {
				unmatchedTags.add(anAndedTags);
			}
		}
		double aNumUnmatchedTags = unmatchedTags.size();
		if (aNumUnmatchedTags > 0) {
			double aNumMatchedTags = matchedTags.size();
			System.err.println ("Unmatched tags:" + unmatchedTags);
			System.err.println ("Matched tags:" + matchedTags);
			double aTotalTags = matchedTags.size() + unmatchedTags.size();
			return partialPass(aNumMatchedTags/aTotalTags, "Only " + aNumMatchedTags + " matched out of " + aTotalTags + " tags.\n See console text");
		}
		
		return pass();
	}

	@Override
	public TestCaseResult test(Project project, boolean autoGrade) throws NotAutomatableException,
			NotGradableException {
//		Map<String[], Class> tagToClass = new HashMap<String[], Class>();
//		Map<Class, String[]> classToTag = new HashMap<>();
//		int aNumDuplicateClasses = 0;
//		int aNumMissingClasses = 0;

		try {
			String[][] aClassesTags = getClassesTags(project);
			if (aClassesTags == null) {
				return checkTagsFromCheckstyleText(project);
			}
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
					System.err.println("No class found for tags:" + Arrays.toString(aClassTags) + ".\nPlease define at least an empty class with these tags to receive non-zero style credit");
					numMissingClasses++;
				}
			}

			double aNumTags = getClassesTags(project).length;
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
