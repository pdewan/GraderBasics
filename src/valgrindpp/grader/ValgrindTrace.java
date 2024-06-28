package valgrindpp.grader;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValgrindTrace {
	public long timestamp, thread;
	public String fnname, result;
	public String[] arguments;
	
	public class TraceParsingException extends Exception {
		public TraceParsingException(String trace) {
			super(trace);
		}

		private static final long serialVersionUID = 1;
	}
	
	public String toString() {
		String aString = 
				"Funname:" + fnname +
				" Timestamp:" + timestamp + 
				" Thread:" + thread + 
				" Arguments:" + Arrays.toString(arguments) + 
				" Result:" + result;
		return aString;
	}
	
	public ValgrindTrace(String trace) throws Exception {
		String aPrcessedTrace = trace;
		if (trace.endsWith("\n")) {
			aPrcessedTrace = trace.substring(0, trace.length() - 1);
		}
		Pattern pattern = Pattern.compile("([0-9]+) - Thread: ([0-9]+) - (.*): (.*) -> (.*)");
		Matcher m = pattern.matcher(aPrcessedTrace);
		
		if(m.matches()) {
			timestamp = Long.parseLong(m.group(1));
			thread = Long.parseLong(m.group(2));
			fnname = m.group(3);
			arguments = m.group(4).split(",");
			result = m.group(5);

			for(int i=0; i<arguments.length; i++) {
				arguments[i] = arguments[i].trim();
			}
		} else {
			throw new TraceParsingException(trace);
		}
	}
}
