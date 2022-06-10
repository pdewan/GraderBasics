package gradingTools.shared.testcases;

import java.util.Arrays;

import grader.basics.project.BasicProject;

public class ConcurrencySuiteSkeleton extends AssignmentSuiteSkeleton {
	static {
		BasicProject.setCheckEclipseFolder(false);
		BasicProject.setCheckCheckstyleFolder(false);
	}
}
