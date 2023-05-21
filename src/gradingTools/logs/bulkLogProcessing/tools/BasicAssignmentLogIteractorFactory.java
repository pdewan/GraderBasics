package gradingTools.logs.bulkLogProcessing.tools;

import java.io.File;
import java.util.Iterator;

public class BasicAssignmentLogIteractorFactory implements AssignmentLogIteratorFactory {

	@Override
	public Iterator<File> createAssignmentLogIterator(File logLocation, String assignmentNumber) {
		return new AssignmentLogIterator(logLocation, assignmentNumber);
	}

	@Override
	public Iterator<File> createAssignmentLogIterator(File logLocation, String pattern, String assignmentNumber) {
		return new AssignmentLogIterator(logLocation, pattern, assignmentNumber);

	}

}
