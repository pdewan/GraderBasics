package gradingTools.basics;

import java.io.File;

import grader.basics.config.BasicExecutionSpecificationSelector;
import grader.basics.junit.BasicJUnitUtils;
import grader.basics.project.BasicProject;
import grader.basics.project.CurrentProjectHolder;
import gradingTools.basics.sharedTestCase.checkstyle.predefined.BasicGeneralStyleSuite;

public class RunBasicStyleChecks {
	public static void main(String[] args) {
		if (args.length < 2) {
			System.err.println(
					"Please enter abosulte names of the source code folder followed by the check style configuration file name");
			System.exit(-1);
		}
		File aSourceFolder = new File(args[0]);

		String aCheckstyleConfiguration = args[1];
		
		BasicProject.setCheckEclipseFolder(false);
		BasicProject.setCheckCheckstyleFolder(false);
		BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setLogTestData(false);
		BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setLoadClasses(false);

		CurrentProjectHolder.setProjectLocation(aSourceFolder);

		BasicExecutionSpecificationSelector.getBasicExecutionSpecification()
				.setCheckStyleConfiguration(aCheckstyleConfiguration);
		BasicJUnitUtils.testAll(BasicGeneralStyleSuite.class);

	}
}
