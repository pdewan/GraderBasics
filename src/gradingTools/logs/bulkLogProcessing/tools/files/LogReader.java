package gradingTools.logs.bulkLogProcessing.tools.files;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gradingTools.logs.bulkLogProcessing.tools.AssignmentLogIterator;

public class LogReader {

	private List<Iterator<File>> allAssignmentIterators;
	private List<String> studentNames;
	
	public LogReader() {}
	
	public LogReader(File location){
		allAssignmentIterators=getAllAssignmentIterators(location); 
	}
	
	public int getNumAssignments(){
		return allAssignmentIterators.size();
	}
	
	public List<String> getStudentAssignmentLogLines(File assignmentLog) throws FileNotFoundException {
		Scanner scan = new Scanner(assignmentLog);
		ArrayList<String> logLines = new ArrayList<String>();
		
		while(scan.hasNext()){
			String nextLine=scan.nextLine();
			if(nextLine.matches(".*,,$")) 
				nextLine=nextLine.replaceAll(",,", ", ,");
			
			
			String [] split = nextLine.split(",");
			if(split.length==9||split.length==17)
				logLines.add(nextLine);
			else {
				System.err.println("Error with log length: "+assignmentLog.getAbsolutePath());
			}
		}
		scan.close();
		if(logLines.size()==0)
			return null;
		
		logLines.remove(0);
		return logLines;
	}
	
	public List<List<String>> getAllStudentsAssignmentLogLines(int assignmentIndex) throws FileNotFoundException{
		Iterator<File> assignmentLogIterator=allAssignmentIterators.get(assignmentIndex);
		if(assignmentLogIterator==null)
			return null;
		
		List<List<String>> allAssignmentLines = new ArrayList<List<String>>();
		studentNames = new ArrayList<String>();
		
		while(assignmentLogIterator.hasNext()){
			File assignmentLog=assignmentLogIterator.next();
			studentNames.add(determineStudentName(assignmentLog));
			
			List<String> lines = getStudentAssignmentLogLines(assignmentLog);
			if(lines==null) continue;

			allAssignmentLines.add(lines);
		}
		
		return allAssignmentLines;
	}
	
	public List<String> getStudentNames(){
		return studentNames;
	}
	
	private static String determineStudentName(File log){
		Matcher studentID=Pattern.compile(".*\\\\(.*)\\\\.*").matcher(log.toString());
		if(studentID.find())
			return "\""+studentID.group(1)+"\"";
		else
			return "I am error";
	}
	
	private static List<Iterator<File>> getAllAssignmentIterators(File location){
		ArrayList<Iterator<File>> allAssignments=new ArrayList<Iterator<File>>();
		for(int i=0;i<13;i++){
			String assignmentNumber = i==0?i+"_1":Integer.toString(i); // this seems to be peculiar to 524
			
			Iterator<File> testVal=new AssignmentLogIterator(location, AssignmentLogIterator.fineGrainedPattern,assignmentNumber);
			if(testVal.hasNext())
				allAssignments.add(testVal);
			else// if(i==0)
				allAssignments.add(null);
//			else
//				break;
		}
		return allAssignments;
	}
}
