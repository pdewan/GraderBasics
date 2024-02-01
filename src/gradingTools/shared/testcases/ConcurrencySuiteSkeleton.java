package gradingTools.shared.testcases;

import java.io.PrintStream;

import grader.basics.output.observer.ObservablePrintStream;
import grader.basics.output.observer.ObservablePrintStreamFactory;
import grader.basics.project.BasicProject;

public class ConcurrencySuiteSkeleton extends AssignmentSuiteSkeleton {
	
	static {
		BasicProject.setCheckEclipseFolder(false);
		BasicProject.setCheckCheckstyleFolder(false);
		ObservablePrintStream anObservablePrintStream = ObservablePrintStreamFactory.getObservablePrintStream();
		anObservablePrintStream.setRedirectionFrozen(true);
		System.setOut((PrintStream) anObservablePrintStream);
//		BasicExecutionSpecificationSelector.getBasicExecutionSpecification().
//		setCheckStyleConfiguration("unc_checks_533_A0_1.xml");
	}
}
