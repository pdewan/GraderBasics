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
import gradingTools.logs.bulkLogProcessing.tools.LogAnalyzerAssignmentLogIteractorFactory;
import gradingTools.logs.bulkLogProcessing.tools.Pairing;

public class LogReader {

	private List<Iterator<Pairing<String, File>>> allAssignmentIterators;
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
		Iterator<Pairing<String, File>> assignmentLogIterator=allAssignmentIterators.get(assignmentIndex);
		if(assignmentLogIterator==null)
			return null;
		
		List<List<String>> allAssignmentLines = new ArrayList<List<String>>();
		studentNames = new ArrayList<String>();
		
		while(assignmentLogIterator.hasNext()){
			Pairing<String, File> assignmentLog=assignmentLogIterator.next();
			studentNames.add(assignmentLog.first);
			
			List<String> lines = getStudentAssignmentLogLines(assignmentLog.second);
			if(lines==null) continue;

			allAssignmentLines.add(lines);
		}
		
		return allAssignmentLines;
	}
	
	public List<String> getStudentNames(){
		return studentNames;
	}
	
	private static List<Iterator<Pairing<String, File>>> getAllAssignmentIterators(File location){
		ArrayList<Iterator<Pairing<String, File>>> allAssignments=new ArrayList<Iterator<Pairing<String, File>>>();
		for(int i=0;i<13;i++){
			String assignmentNumber = i==0?i+"[-_]?1?":Integer.toString(i); // this seems to be peculiar to 524
			
			Iterator<Pairing<String, File>> testVal= LogAnalyzerAssignmentLogIteractorFactory.createAssignmentLogIterator(location, AssignmentLogIterator.fineGrainedPattern,assignmentNumber);
			if(testVal.hasNext() && testVal!=null)
				allAssignments.add(testVal);
			else// if(i==0)
				allAssignments.add(null);
//			else
//				break;
		}
		return allAssignments;
	}
}
