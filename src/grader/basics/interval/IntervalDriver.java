package grader.basics.interval;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import analyzer.logAnalyzer.AnIntervalReplayer;

public class IntervalDriver {
	//Path to student folder
	public static final File STUDENT_FOLDER = new File("C:\\Users\\Zhizhou\\eclipse2021-workspace\\A1");
	//Parse time stamps to long if they are strings
	public static final DateFormat DF = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
	//For context based work time, a pause is considered a rest if pauseTime > MULTIPLIER * context-based-threshold
	public static final double MULTIPLIER = 1;
	//DEFAULT_THRESHOLD in minutes
	//For fixed work time, a pause is considered a rest if pauseTime > DEFAULT_THRESHOLD
	//For context based work time, a threshold = DEFAULT_THRESHOLD if it is undefined for that type of command
	public static final int DEFAULT_THRESHOLD = 5;
//	public static final long START_TIME = 0;
//	public static final long END_TIME = Long.MAX_VALUE;
	//Print traces messages 
	public static final boolean TRACE = false;
	
	public static void main(String[] args) {
		if (!STUDENT_FOLDER.exists()) {
			System.err.println("Error: Student Folder does not exist");
			return;
		}
		AnIntervalReplayer replayer = new AnIntervalReplayer(MULTIPLIER, DEFAULT_THRESHOLD, TRACE);
		//getStartTime(File studentFolder) returns the wall time of first FileOpenCommand in the project. -1 when failed
		long startTime = replayer.getStartTime(STUDENT_FOLDER);
		//getEndTime(File studentFolder) returns the wall time of last edit, -1 when failed
		long endTime = replayer.getEndTime(STUDENT_FOLDER);
		System.out.println("Start Time: " + parseWallTime(startTime));
		System.out.println("End Time: " + parseWallTime(endTime));
		
		/* 
		 * getWorkTime(File studentFolder, long startTime, long endTime)
		 * returns a long[] workTimes where 
		 * workTimes[0] = context based work time, -1 if failed
		 * workTimes[1] = fixed work time, -1 if failed
		 */
		long[] workTimes = replayer.getWorkTime(STUDENT_FOLDER, startTime, endTime);
		System.out.println("Context Based Work Time: " + format(workTimes[0]));
		System.out.println("Fixed Work Time (" + DEFAULT_THRESHOLD + "min): " + format(workTimes[1]));
		
		/*
		 * getEdits(File studentFolder, long starTime, long endTime)
		 * returns a int[] edits where 
		 * edits[0] = number of insert commands
		 * edits[1] = number of characters inserted, -1 if failed
		 * edits[2] = number of delete commands
		 * edits[3] = number of characters deleted, -1 if failed 
		 */
		int[] edits = replayer.getEdits(STUDENT_FOLDER, startTime, endTime);
		System.out.println("Number of Insert: " + edits[0]);
		System.out.println("Characters inserted: " + edits[1]);
		System.out.println("Number of Delete: " + edits[2]);
		System.out.println("Characters deleted: " + edits[3]);
		
		/*
		 * getRuns(File studentFolder, long starTime, long endTime)
		 * returns a int runs = number of run commands
		 */
		int runs = replayer.getRuns(STUDENT_FOLDER, startTime, endTime);
		System.out.println("Number of Insert: " + runs);
	}
	
	public static long parseTime(String date, Scanner scanner) {
		try {
			return DF.parse(date).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public static String parseWallTime(long time) {
		return new Date(time).toString();
	}
	
	public static String format(long time){
		String sign = "";
		if (time < 0) {
			sign += "-";
			time = -1 * time;
		}
		long hour = time / 3600000;
		long minute = time % 3600000 / 60000;
		long second = time % 60000 / 1000;
		return sign + String.format("%d:%02d:%02d", hour, minute, second);
	}
}
