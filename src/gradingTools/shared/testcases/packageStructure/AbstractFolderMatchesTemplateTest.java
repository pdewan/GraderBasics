package gradingTools.shared.testcases.packageStructure;

import java.io.File;

import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import grader.basics.testcase.PassFailJUnitTestCase;
import gradingTools.fileTree.Template;
import gradingTools.fileTree.nodes.DirectoryNode;

public abstract class AbstractFolderMatchesTemplateTest extends PassFailJUnitTestCase{

	@Override
	public TestCaseResult test(Project project, boolean autoGrade) throws NotAutomatableException, NotGradableException {
		File srcFolder = sourceFolder(project);
		DirectoryNode src = new DirectoryNode(srcFolder);

		double similarityPercent = getTemplate().compare(src);
		
		if(similarityPercent >= getAcceptabilityCutoff())
				return pass();
		
		System.out.println("\nYour project does not meet the required similarity percent of: "+getAcceptabilityCutoff()+" your similarity: "+similarityPercent);

		System.out.println("|- represents a package, |~ represents a file\n\nThe structure of your project was found to be:");
		System.out.println(src);
		System.out.println("\nAttempting to match to the structure:");
		System.out.println(getTemplate().getTemplateFileTree());
		System.out.println();
		return fail("View console for more information");
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
	protected abstract Template getTemplate();
	protected abstract double getAcceptabilityCutoff();
	
}
