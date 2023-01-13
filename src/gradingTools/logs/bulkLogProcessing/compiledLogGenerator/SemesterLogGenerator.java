package gradingTools.logs.bulkLogProcessing.compiledLogGenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import gradingTools.logs.bulkLogProcessing.collectors.Collector;
import gradingTools.logs.bulkLogProcessing.selectYearMapping.YearSelectFactory;
import gradingTools.logs.bulkLogProcessing.tools.TestingData;
import gradingTools.logs.bulkLogProcessing.tools.dataStorage.SuitesAndTests;
import gradingTools.logs.bulkLogProcessing.tools.files.LogReader;
import gradingTools.logs.bulkLogProcessing.tools.files.LogWriter;

public class SemesterLogGenerator {

	private static final String [] nameHeader={	
			"Name",							//0
		  	};	

	private final boolean includeName;
	private CollectorManager manager;
	private String outputFileName;
	
	public SemesterLogGenerator(Collector [] collectors, boolean includeName) {
		this(collectors,includeName,"assignment#.csv");
	}
	
	public SemesterLogGenerator(Collector [] collectors, boolean includeName,String outputName) {
		this.includeName=includeName;
		manager=new CollectorManager(collectors);
		outputFileName=outputName;
	}
	
	/**
	 * Use a # to represent the assignment number
	 */
	public void setAssignmentOutput(String outputName) {
		outputFileName=outputName;
	}
	
	public void setCollectors(Collector [] collectors) {
		manager=new CollectorManager(collectors);
	}
	
	public void generateData(File location, File output) throws IOException, ParseException{
		generateData(location,output,0);
	}
	
	public void generateData(File location, File output, int minimumAssignmentNumber) throws IOException, ParseException{
		
		LogReader logReader = new LogReader(location);
		
		for(int assignmentIndex=minimumAssignmentNumber;assignmentIndex<logReader.getNumAssignments();assignmentIndex++){
			
			List<List<String>> allAssignmentLines = logReader.getAllStudentsAssignmentLogLines(assignmentIndex);
			if(allAssignmentLines==null)
				continue;
			
			List<String> studentNames = logReader.getStudentNames();
			
			SuitesAndTests assignmentSuitesAndTests = TestingData.findAllSuitesAndTests(allAssignmentLines,assignmentIndex);
			
			
			String [] tests = new String[assignmentSuitesAndTests.getTests().size()];
			assignmentSuitesAndTests.getTests().toArray(tests);
			
			
			manager.setTestNames(tests);
			manager.setTestMappings(YearSelectFactory.getYearMap(assignmentIndex));
			manager.setAssignmentNumumber(Integer.toString(assignmentIndex));
			
			List<String> dataCategories = new ArrayList<String>();
			
			if(includeName)
				Collections.addAll(dataCategories, nameHeader);
			
			dataCategories.addAll(manager.getOrderedHeaders());

			//File Writing and output
			String path=output.toString()+"/"+outputFileName.replaceAll("#", Integer.toString(assignmentIndex));
			File outputData=new File(path);
			outputData.createNewFile();
			FileWriter dataOut=new FileWriter(outputData);
			
			LogWriter.writeToFile(dataOut,dataCategories);
			
			//Data processing for each student
			for(int i=0;i<allAssignmentLines.size();i++){
				try{
					String studentName=studentNames.get(i);
					
					manager.setStudentName(studentName);
						
					manager.processLog(allAssignmentLines.get(i));
					
					List<String> resultsList;
					
					//TODO make a name collector
					if(includeName) {
						resultsList = new ArrayList<String>();
						resultsList.add(studentName);
						resultsList.addAll(manager.getOrderedData());
					}else 
						resultsList = manager.getOrderedData();
					
					manager.reset();
				
					if(manager.specialPrint()) 
						LogWriter.simpleWrite(dataOut,resultsList);
					else
						LogWriter.writeToFile(dataOut,resultsList);
				
				}catch(Exception e){
					e.printStackTrace();
					System.out.println(studentNames.get(i) + "   " + path);
					manager.reset();
					
				}
			}
			dataOut.close();
		}
		
	}
	
}
