package gradingTools.shared.testcases.packageStructure;

import java.io.File;

import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import grader.basics.testcase.PassFailJUnitTestCase;
import gradingTools.fileTree.nodes.DirectoryNode;

public abstract class AbstractFileCompexityTest extends PassFailJUnitTestCase{

	@Override
	public TestCaseResult test(Project project, boolean autoGrade) throws NotAutomatableException, NotGradableException {
		File src = sourceFolder(project);
		int numDirs = new DirectoryNode(src).getNumSubFiles();
		if(numDirs<minimumNumberFiles())
			return fail("The directory: "+src+" does not meet the minumum required number of files of "+minimumNumberFiles());
		if(numDirs<maximumNumberFiles())
			return fail("The directory: "+src+" exceeds the maximum required number of files of "+maximumNumberFiles());
		return pass();
	}
	
	/**
	 * Gives the project and expects the source folder on return 
	 * can be overriden to do the check on other directories
	 * @param project
	 * @return
	 */
	protected File sourceFolder(Project project) {
		return project.getSourceFolder();
	}
	protected abstract int minimumNumberFiles();
	protected abstract int maximumNumberFiles();
	
}
