package gradingTools.basics.sharedTestCase.checkstyle;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import grader.basics.config.BasicExecutionSpecificationSelector;
import grader.basics.execution.GradingMode;
import grader.basics.execution.NotRunnableException;
import grader.basics.execution.RunningProject;
import grader.basics.junit.JUnitTestsEnvironment;
import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import grader.basics.project.source.ABasicTextManager;
import grader.basics.testcase.PassFailJUnitTestCase;
import gradingTools.basics.sharedTestCase.checkstyle.CheckStyleWarningsRatioTestCase;
import gradingTools.basics.sharedTestCase.checkstyle.CheckStyleWarningsTestCase;
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
import gradingTools.shared.testcases.utils.LinesMatchKind;
import gradingTools.shared.testcases.utils.LinesMatcher;
import gradingTools.utils.RunningProjectUtils;
import util.annotations.IsExtra;
import util.annotations.MaxValue;
import util.trace.Tracer;

public class SimplifyBooleanExpressionTestCase extends CheckstyleSpecificWarningTestCase {

	static final String WARNING = "SimplifyBooleanExpression";
	public SimplifyBooleanExpressionTestCase() {
		super (WARNING);
	}
	


}
