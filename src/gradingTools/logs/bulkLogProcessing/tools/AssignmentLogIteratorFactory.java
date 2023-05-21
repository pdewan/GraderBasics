package gradingTools.logs.bulkLogProcessing.tools;

import java.io.File;
import java.util.Iterator;

public interface AssignmentLogIteratorFactory {
	Iterator<File> createAssignmentLogIterator(File logLocation, String assignmentNumber) ;
	Iterator<File> createAssignmentLogIterator(File logLocation, String pattern, String assignmentNumber);

}
