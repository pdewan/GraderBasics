package grader.basics.assignment;

import java.io.File;

import grader.basics.file.RootFolderProxy;

public class AssignmentDataHolder {
	static File assignmentDataFile;
	static RootFolderProxy assignmentDataFileProxy;
	public static File getAssignmentDataFile() {
		return assignmentDataFile;
	}
	public static void setAssignmentDataFile(File assignmentDataFile) {
		AssignmentDataHolder.assignmentDataFile = assignmentDataFile;
	}
	public static RootFolderProxy getAssignmentDataFileProxy() {
		return assignmentDataFileProxy;
	}
	public static void setAssignmentDataFileProxy(RootFolderProxy assignmentDataFileProxy) {
		AssignmentDataHolder.assignmentDataFileProxy = assignmentDataFileProxy;
	}
}
