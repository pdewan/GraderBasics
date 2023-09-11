package gradingTools.logs.bulkLogProcessing.tools;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

public class LogAnalyzerAssignmentLogIteractorFactory {

	private static Class<? extends Iterator<Pairing<String, File>>> iteratorClass = AssignmentLogIterator.class;
	
	public static void setIterator(Class<? extends Iterator<Pairing<String, File>>> clazz) {
		iteratorClass = clazz;
	}
	
	public static Iterator<Pairing<String, File>> createAssignmentLogIterator(File logLocation, String assignmentNumber) {
		try {
			return iteratorClass.getConstructor(File.class, String.class).newInstance(logLocation,assignmentNumber);
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			return null;
		}
	}

	public static Iterator<Pairing<String, File>> createAssignmentLogIterator(File logLocation, String pattern, String assignmentNumber) {
		try {
			return iteratorClass.getConstructor(File.class, String.class, String.class).newInstance(logLocation,pattern,assignmentNumber);
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			return null;
		}
	}
}
