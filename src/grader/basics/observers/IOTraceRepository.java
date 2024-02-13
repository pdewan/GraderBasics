package grader.basics.observers;

import java.util.ArrayList;
import java.util.List;

public class IOTraceRepository {
	private static List<String> preAnnouncements = new ArrayList<String>();
	private static List<String> postAnnouncements = new ArrayList<String>();
	private static StringBuffer output = new StringBuffer();
	private static StringBuffer error = new StringBuffer();

	public static void addPreAnnouncement(String aNewValue) {
		preAnnouncements.add(aNewValue);
	}
	public static void addPostAnnouncement(String aNewValue) {
		postAnnouncements.add(aNewValue);
	}
	public static void clearAnnouncements() {
		preAnnouncements.clear();
	}
	public static List getPreAnnouncements() {
		return preAnnouncements;
	}
	public static List getPostAnnouncements() {
		return postAnnouncements;
	}
	public static void setOutput(String newVal) {
		output.append(newVal);
	}
	public static String getOutput() {
		return output.toString();
	}
	public static String getError() {
		return error.toString();
	}
	public static void clearOutput() {
		output.setLength(0);
	}
	public static void setError(String newVal) {
		error.append(newVal);
	}
	public static void clearError() {
		error.setLength(0);
	}
	public static void clearAll() {
		clearAnnouncements();
		clearOutput();
		clearError();		
	}
	
}
