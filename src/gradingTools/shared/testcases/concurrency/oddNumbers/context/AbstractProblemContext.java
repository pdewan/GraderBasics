package gradingTools.shared.testcases.concurrency.oddNumbers.context;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import grader.basics.junit.GradableJUnitSuite;
import grader.basics.observers.AFineGrainedTestLogFileWriter;
import grader.basics.observers.ASourceAndTestLogWriter;
import grader.basics.observers.ATraceSourceAndTestLogWriter;
import grader.basics.observers.TestLogFileWriterFactory;
import grader.basics.project.Project;
import gradingTools.shared.testcases.concurrency.oddNumbers.hints.AbstractHint;
import util.misc.Common;

public abstract  class AbstractProblemContext extends AbstractHint{
	private static Map<String, String> currentSourcesMap; // same and static for all tests, so static
	private static int MAX_TRACE_LENGTH = 3000;
	StringBuffer output = new StringBuffer(MAX_TRACE_LENGTH);
	
	private static final String CONTEXT_LOG_FILE_NAME_MODIFIER = "_context_log";
	private static final String CONTEXT_LOG_FILE_NAME_SUFFIX = ".txt";
	private static final String LAST_CONTEXT_FILE_NAME_MODIFIER = "_last_context";
	private static final String LAST_CONTEXT_FILE_NAME_SUFFIX = ".txt";
	private static final String PROMPT_TEMPLATE = 
			"Please edit the template below, using as many lines as necessary, to describe the issue you are facing, being as specific a possible." +
			"\n\nMention the specific problem you are having interpreting the (a) code, (b) output, (c) test results, and (d) any previous hints you have received." +
			"\n\nDo not feel shy about saying you did not understand the previous hint and want a more specific hint." +
			"\n\nWe will keep giving you as many hints as are needed to solve the problem while ensuring we do not give the solution completely away" +
	        "\n\nSTART OF PROBLEM DESCRIPTION" +
			"\n\n________<replace this line with one or more lines>__________" +
			"\n\nEND OF PROBLEM DESCRIPTION\n";

	private static final String PREVIOUS_HINT_TEMPLATE = 
			"\nSTART OF PREVIOUS HINT FOR THIS TEST" +
			"\n\nNone (Replace this text if you received such a hint)" +
			"\n\nEND OF PREVIOUS HINT FOR THIS TEST\n";
	private static final String SECTION_MARKER =
			"\n___________________________________________________________________________________________________________\n";
	
	private static final String PRE_AUTO_MESSAGE = 
			"\nThe text below is automatically generated" +
			"\nSave this file and paste this text in a private Piazza post to the instructors with the assignment tag" +
			"\nStudy the problem context below while waiting for a response";

//	private String currentTrace;
	public AbstractProblemContext() {
		
	}
	
	public Map<String, String> getOrCreateCurrentSourcesMap() {
		if (currentSourcesMap == null) {
			ATraceSourceAndTestLogWriter aLogWriter = (ATraceSourceAndTestLogWriter) TestLogFileWriterFactory.getMainFileWriter();
			Map<String, String> aMap = aLogWriter.getCurrentSourcesMap();
			currentSourcesMap = aMap;
		}
		return currentSourcesMap;
	}
	public String getCompleteTrace() {
		if (completeTrace == null) {
			
		ATraceSourceAndTestLogWriter.composeCurrentOutput(output); // this can keep changing, so do not cache
		completeTrace = output.toString();
		}
		return completeTrace;		
		
	}
	protected String getRelevantTraceStartSuffix() {
//		return "  test execution time";
		return ",";

	}
	protected String getRelevantTraceEndPrefix() {
		return "<<";
	}
	protected String getRelevantCodeStart() {
		return "";
	}
	
	 protected String getRelevantCodeEnd() {
			return "";
	 }
	 
	
	 
	 protected boolean missingCompleteCode() {
			return false;
		}
	 
	 
	
	protected String getCompleteCode() {
		return "";
	}
	protected String getRelevantCode() {
		String aCompleteCode = getCompleteCode();
		if (aCompleteCode == null) {
			return "Could not find the source code";
		}
		String aStartText = getRelevantCodeStart();
		int aStartIndex = aCompleteCode.indexOf(aStartText);
		if (aStartIndex < 0) {
			if (missingCompleteCode()) {
				return aCompleteCode;
			}
			return "Could not find in complete code text start: " + aStartText;
		}
		String anEndText = getRelevantCodeEnd();
		int anEndIndex = aCompleteCode.indexOf(anEndText, aStartIndex);
		if (anEndIndex < 0) {
			if (missingCompleteCode()) {
				return aCompleteCode;
			}
			return "Could not find in complete code text end: " + anEndText;
		}
		String retVal = aCompleteCode.substring(aStartIndex, anEndIndex);
		return retVal;
	}
	protected String getRelevanTraceStart(Class aPrecedingTestClass) {
		String aPrecedingTestName = aPrecedingTestClass.getSimpleName();
		return aPrecedingTestName + getRelevantTraceStartSuffix();
		
	}
	protected String completeTrace;
	protected String relevantTrace;
	
	protected String getTestName() {
		Class aPrecedingTestClass = precedingTests()[0];
		return aPrecedingTestClass.getSimpleName();
	}
	protected String getRelevantTrace() {
		if (relevantTrace == null) {
		Class aPrecedingTestClass = precedingTests()[0];
//		String aPrecedingTestName = aPrecedingTestClass.getSimpleName();
	    completeTrace = getCompleteTrace();
		String aTraceStart = getRelevanTraceStart(aPrecedingTestClass);
		String aTraceEnd = getRelevantTraceEndPrefix();
		int aStartIndex = completeTrace.indexOf(aTraceStart);
		int anEndIndex = completeTrace.indexOf(aTraceEnd, aStartIndex);
		relevantTrace =  completeTrace.substring(aStartIndex, anEndIndex);
		}
		return relevantTrace;		
	}
	
	protected File writeLastContextText(String aText) {
		
		File aLastContextFile= new File (composeLastContextFileName());
		if (aLastContextFile.exists()) {
			try {
				StringBuilder anOldText = Common.readFile(aLastContextFile);
				
				ASourceAndTestLogWriter.append(composeContextLogFileName(), anOldText.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
//		File aFile = new File ();
		try {
			Common.writeText(aLastContextFile, aText);
			displayFile(aLastContextFile);
		} catch (IOException e) {
			e.printStackTrace();
			
		}
		return aLastContextFile;
	}
	
	protected static void displayFile(File aFile) {
		try {
			java.awt.Desktop.getDesktop().edit(aFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	protected String getPreviousHint() {
		return PREVIOUS_HINT_TEMPLATE;
//		String aPreviousHintStart = "Start of Previous Hint for this Test";
//		String aPreviousHint = "None (Edit if you received such a hint";
//		String aPreviousHintEnd = "End of Previous Hint";
//		return aPreviousHintStart + aPreviousHint + aPreviousHintEnd;
	}

	
	
	protected String getTemplate() {
		return PROMPT_TEMPLATE;
//		String aCommentStartPrompt = "Please edit the template below, using as many lines as necessary, to describe the issue you are facing, being as speciric a possible. "
//				+ "\n\n Mention the specific problem you are having interpreting the code, output, test results, and any previous hints you have received. \n Do not feel shy in saying you did not understand the pevious hint and want a more specific hint."
//				+ "\n\n We will keep giving you as many hints as are needed to solve the problem while enuring we do not give the solution completely away";
//		String anIssueTemplate = "_________________________\n__________________";
//		String aCommentEndPrompt = "End of Problem Description\n";
//		return aCommentStartPrompt + anIssueTemplate + aCommentEndPrompt;
	}
	protected String getMarkedRelevantTrace() {
		String aRelevantTraceStart = "\nSTART OF RELEVANT TEST MESSAGE\n";
		String aRelevantTrace = getRelevantTrace();
		String aRelevantTraceEnd = "END OF RELEVANT TEST MESSAGE\n";
		return aRelevantTraceStart + aRelevantTrace + aRelevantTraceEnd;
	}
	
	protected String getMarkedRelevantCode() {
		String aRelevantCodeStart = "\nSTART OF RELEVANT CODE\n";
		String aRelevantCode = getRelevantCode();
		String aRelevantCodeEnd = "END OF RELEVANT CODE\n";
		return aRelevantCodeStart + aRelevantCode + aRelevantCodeEnd;
	}
	
	protected String getMarkedRelevantCompleteTrace() {
		String aCompleteTraceStart = "\nSTART OF COMPLETE OUTPUT\n";
		String aCompleteTrace = getCompleteTrace();
		String aCompleteTraceEnd = "END OF COMPLETE OUTPUT\n";
		return aCompleteTraceStart + aCompleteTrace + aCompleteTraceEnd;
	}
	
	protected String getMarkedRelevantCompleteCode() {
		String aCompleteCodeStart = "\nSTART OF COMPLETE CODE\n";
		String aCompleteCode = getCompleteCode();
		if (aCompleteCode == null) {
			aCompleteCode = "Could not find source code";
		}
		String aCompleteCodeEnd = "End of COMPLETE CODE\n";
		return aCompleteCodeStart + aCompleteCode + aCompleteCodeEnd;
	}	
	
	protected String hint() {
		String aHint = 
				SECTION_MARKER +
				getTemplate() +
				SECTION_MARKER +
				getPreviousHint() +
				SECTION_MARKER +
				PRE_AUTO_MESSAGE +
				SECTION_MARKER +
				getMarkedRelevantTrace() +
				SECTION_MARKER +
				getMarkedRelevantCode() +
				SECTION_MARKER +
				getMarkedRelevantCompleteTrace() +
				SECTION_MARKER +
				getMarkedRelevantCompleteCode() +
				SECTION_MARKER;
		String aContextLLogFileName = composeContextLogFileName();
		String aLastContextFileName = composeLastContextFileName();
		writeLastContextText(aHint);
		return aHint;
	}
	public static String composeContextLogFileName() {
		AFineGrainedTestLogFileWriter aLogFileWriter = (AFineGrainedTestLogFileWriter) TestLogFileWriterFactory.getMainFileWriter();
		String aPrefix = aLogFileWriter.composeLogFilePrefix();
		return aPrefix + CONTEXT_LOG_FILE_NAME_MODIFIER + CONTEXT_LOG_FILE_NAME_SUFFIX;		
	}
	public static String composeLastContextFileName() {
		AFineGrainedTestLogFileWriter aLogFileWriter = (AFineGrainedTestLogFileWriter) TestLogFileWriterFactory.getMainFileWriter();
		String aPrefix = aLogFileWriter.composeLogFilePrefix();
		return aPrefix + LAST_CONTEXT_FILE_NAME_MODIFIER + LAST_CONTEXT_FILE_NAME_SUFFIX;		
	}
}
