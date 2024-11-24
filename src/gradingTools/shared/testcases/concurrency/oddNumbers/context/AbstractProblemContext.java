package gradingTools.shared.testcases.concurrency.oddNumbers.context;

import java.util.Map;

import grader.basics.junit.GradableJUnitSuite;
import grader.basics.observers.AFineGrainedTestLogFileWriter;
import grader.basics.observers.ATraceSourceAndTestLogWriter;
import grader.basics.observers.TestLogFileWriterFactory;
import grader.basics.project.Project;
import gradingTools.shared.testcases.concurrency.oddNumbers.hints.AbstractHint;

public abstract  class AbstractProblemContext extends AbstractHint{
	private static Map<String, String> currentSourcesMap; // same and static for all tests, so static
	private static int MAX_TRACE_LENGTH = 3000;
	StringBuffer output = new StringBuffer(MAX_TRACE_LENGTH);
	
	static final String CONTEXT_LOG_FILE_NAME_MODIFIER = "_context_log";
	static final String CONTEXT_LOG_FILE_NAME_SUFFIX = ".txt";
	static final String LAST_CONTEXT_FILE_NAME_MODIFIER = "_last_context";
	static final String LAST_CONTEXT_FILE_NAME_SUFFIX = ".txt";

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
	
	protected String getCompleteCode() {
		return "";
	}
	protected String getRelevantCode() {
		String aCompleteCode = getCompleteCode();
		int aStartIndex = aCompleteCode.indexOf(getRelevantCodeStart());
		int anEndIndex = aCompleteCode.indexOf(getRelevantCodeEnd(), aStartIndex);
		String retVal = aCompleteCode.substring(aStartIndex, anEndIndex);
		return retVal;
	}
	protected String getRelevanTraceStart(Class aPrecedingTestClass) {
		String aPrecedingTestName = aPrecedingTestClass.getSimpleName();
		return aPrecedingTestName + getRelevantTraceStartSuffix();
		
	}
	protected String completeTrace;
	protected String relevantTrace;
	
	
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
	
	protected String getTemplate() {
		String aCommentStartPrompt = "Please edit the template below, using as many lines as necessary, do describe the issue you are facing, being as speciric a possible";
		String anIssueTemplate = "_________________________\n__________________";
		String aCommentEndPrompt = "End of Problem Description\n";
		return aCommentStartPrompt + anIssueTemplate + aCommentEndPrompt;
	}
	protected String getMarkedRelevantTrace() {
		String aRelevantTraceStart = "Start of Relevant Test Message";
		String aRelevantTrace = getRelevantTrace();
		String aRelevantTraceEnd = "End of Relevant Test Message\n";
		return aRelevantTraceStart + aRelevantTrace + aRelevantTraceEnd;
	}
	
	protected String getMarkedRelevantCode() {
		String aRelevantCodeStart = "Start of Relevant Code";
		String aRelevantCode = getRelevantCode();
		String aRelevantCodeEnd = "End of Relevant Code\n";
		return aRelevantCodeStart + aRelevantCode + aRelevantCodeEnd;
	}
	
	protected String getMarkedRelevantCompleteTrace() {
		String aCompleteTraceStart = "Start of Complete Trace";
		String aCompleteTrace = getCompleteTrace();
		String aCompleteTraceEnd = "End of Complete Trace\n";
		return aCompleteTraceStart + aCompleteTrace + aCompleteTraceEnd;
	}
	
	protected String getMarkedRelevantCompleteCode() {
		String aCompleteCodeStart = "Start of Complete Code";
		String aCompleteCode = getCompleteCode();
		String aCompleteCodeEnd = "End of Complete Code\n";
		return aCompleteCodeStart + aCompleteCode + aCompleteCodeEnd;
	}	
	
	protected String hint() {
		String aHint = getTemplate() + 
				getMarkedRelevantTrace() +
				getMarkedRelevantCode() +
				getMarkedRelevantCompleteTrace() +
				getMarkedRelevantCompleteCode() ;
		String aContextLLogFileName = composeContextLogFileName();
		String aLastContextFileName = composeLastContextFileName();
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
