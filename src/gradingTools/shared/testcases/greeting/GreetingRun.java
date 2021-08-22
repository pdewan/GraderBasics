package gradingTools.shared.testcases.greeting;

import java.util.Arrays;
import java.util.regex.Pattern;

import grader.basics.config.BasicExecutionSpecificationSelector;
import grader.basics.execution.NotRunnableException;
import grader.basics.execution.RunningProject;
import grader.basics.junit.JUnitTestsEnvironment;
import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import grader.basics.testcase.PassFailJUnitTestCase;
import gradingTools.shared.testcases.SubstringSequenceChecker;
import gradingTools.shared.testcases.utils.LinesMatchKind;
import gradingTools.shared.testcases.utils.LinesMatcher;
import gradingTools.utils.RunningProjectUtils;
import util.annotations.MaxValue;
@MaxValue(6)
public class GreetingRun extends PassFailJUnitTestCase {
	public static final int TIME_OUT_SECS = 1; // secs
	protected SubstringSequenceChecker checker = new AGreetingChecker();	

	public GreetingRun() {
	}

	protected RunningProject createRunningProject (Project aProject) {
		GreetingMainProvided aHelloWorkdClassProvided = (GreetingMainProvided) JUnitTestsEnvironment.getAndPossiblyRunGradableJUnitTest(GreetingMainProvided.class);
		Class aHelloClass = aHelloWorkdClassProvided.getGreetingMain();
		if (aHelloClass == null) {
			System.err.println("Cannot run test, no main class");
			return null;
		}
		BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setEntryPoint(aHelloClass.getName());
		RunningProject aRunningProject = RunningProjectUtils.runProject(aProject, TIME_OUT_SECS);
		return aRunningProject;
	}
	

	@Override
	public TestCaseResult test(Project project, boolean autoGrade) throws NotAutomatableException,
			NotGradableException {
		try {
//			setEntryPoint(project);
//    		GreetingMainProvided aHelloWorkdClassProvided = (GreetingMainProvided) JUnitTestsEnvironment.getAndPossiblyRunGradableJUnitTest(GreetingMainProvided.class);
//    		Class aHelloClass = aHelloWorkdClassProvided.getGreetingMain();
//			BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setEntryPoint(aHelloClass.getName());
//			RunningProject aRunningProject = RunningProjectUtils.runProject(project, TIME_OUT_SECS);
			RunningProject aRunningProject = createRunningProject(project);
			if (aRunningProject == null) {
				return fail ("Could not create project. See console messages.");
			}

			String anOutput = aRunningProject.await();
			LinesMatcher aLinesMatcher = aRunningProject.getLinesMatcher();
			boolean aRetval = checker.check(aLinesMatcher, LinesMatchKind.ONE_TIME_LINE, Pattern.DOTALL);
			

			String anExpectedLines = Arrays.toString(checker.getSubstrings());

			if (!aRetval) {
				return fail("Output  did not match:" + anExpectedLines);

			}
			return pass();
			

		} catch (NotRunnableException e) {
			throw new NotGradableException();
		}
	}
//	public static void processExternalMethodSNodes (RootOfProgramSNode aRootOfProgramSNode, RootOfFileSNode aRootOfFileSNode) {
//		for (SNode anSNode:aRootOfFileSNode.getChildren()) {
//			if (anSNode instanceof ExternalMethodSNode) {
//				processExternalMethodSNode(aRootOfProgramSNode, aRootOfFileSNode, (ExternalMethodSNode) anSNode);
//			}
//		}
//	}
//	public static void processExternalMethodSNode (RootOfProgramSNode aRootOfProgramSNode, RootOfFileSNode aRootOfFileSNode, ExternalMethodSNode anExternalMethodSNode) {
//		MethodSNode aMethodSNode = aRootOfProgramSNode.getExternalToInternalMethod().get(anExternalMethodSNode.toString());
//		if (aMethodSNode == null) {
//			aMethodSNode = findMethodSNode(aRootOfProgramSNode, aRootOfFileSNode, anExternalMethodSNode);
//			if (aMethodSNode != null) {
//				aRootOfProgramSNode.getExternalToInternalMethod().put(anExternalMethodSNode.toString(),aMethodSNode );
//			}
//		}
//		if (aMethodSNode != null) {
//			anExternalMethodSNode.setActualMethodSNode(aMethodSNode);
//		}
//	}
//	public static MethodSNode findMethodSNode (RootOfProgramSNode aRootOfProgramSNode, RootOfFileSNode aRootOfFileSNode, ExternalMethodSNode anExternalMethodSNode) {
////		MethodSNode foundMethodSNode = null;
//		for (String aFileName:aRootOfProgramSNode.getFileNameToSNode().keySet()) {
//			if (aFileName.equals(aRootOfFileSNode.getFileName()))
//				continue;
//			
//			RootOfFileSNode aSearchedRootOfFileSNode = aRootOfProgramSNode.getFileNameToSNode().get(aFileName);
//			 for (SNode anSNode:aSearchedRootOfFileSNode.getChildren()) {
//				if (anSNode instanceof MethodSNode && !(anSNode instanceof ExternalMethodSNode)) {
//					if (anSNode.toString().equals(anExternalMethodSNode)) {
//						return (MethodSNode) anSNode;
//						
//					}
////					processExternalMethodSNode(aRootOfProgramSNode, aRootOfFileSNode, (ExternalMethodSNode) anSNode);
//				}
//			}
//		}
//		return null;
//	}
//	public static void processExternalMethodSNodes (RootOfProgramSNode aRootOfProgramSNode) {
//		for (String aFileName:aRootOfProgramSNode.getFileNameToSNode().keySet()) {
//			RootOfFileSNode aRootOfFileSNode = aRootOfProgramSNode.getFileNameToSNode().get(aFileName);
//			processExternalMethodSNodes(aRootOfProgramSNode, aRootOfFileSNode);
//			
//		}
//
//	}
}
