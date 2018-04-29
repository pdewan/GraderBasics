package gradingTools.shared.testcases.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import util.pipe.AnAbstractInputGenerator;
import util.trace.Tracer;

public class ABufferingTestInputGenerator extends AnAbstractInputGenerator {
	public static final int INITIAL_NUM_LINES = 1000;
	public static final int EXPECTED_LINE_LENGTH = 256;
	public static final int EXPECTED_STRING_LENGTH = INITIAL_NUM_LINES*EXPECTED_LINE_LENGTH;
	public static final String PROCESS_SEPARATOR = "!";

	protected List<String> allProcessOutputLines = new ArrayList(INITIAL_NUM_LINES );
	public List<String> getAllProcessOutputLines() {
		return allProcessOutputLines;
	}
	public StringBuffer getAllProcessOutput() {
		return allProcessOutput;
	}
	protected StringBuffer allProcessOutput = new StringBuffer(EXPECTED_STRING_LENGTH);
	public void clear() {
		allProcessOutputLines.clear();
		allProcessOutput.setLength(0);
	}
	@Override
	public void newOutputLine(String aProcessName, String anOutputLine) {
		String aProcessLine = aProcessName + PROCESS_SEPARATOR + anOutputLine;
		allProcessOutput.append(aProcessName);
		allProcessOutputLines.add(aProcessLine);
		
		
		
	}
	
	
	
}
