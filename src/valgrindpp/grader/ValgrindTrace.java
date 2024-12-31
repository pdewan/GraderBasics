package valgrindpp.grader;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValgrindTrace {
	public long timestamp, thread;
	public int process;
	public String fnname, result;
	public String[] arguments;
	public int repetitons = 1;
	public int threadArgument = -1;
	
	public class TraceParsingException extends Exception {
		public TraceParsingException(String trace) {
			super(trace);
		}

		private static final long serialVersionUID = 1;
	}
	
	public String toString() {
		return 
				"Timestamp:" + timestamp +
						toStringWithoutTimestamp();
//		String aString = 
//				"Timestamp:" + timestamp + 
//				  "Funname:" + fnname +
//				" Thread:" + thread + 
//				" Arguments:" + Arrays.toString(arguments) + 
//				" Result:" + result;
//		return aString;
	}
	
	public String toStringWithoutTimestamp() {
		String aString = 
				 " Funname:" + fnname +
				 " Process:" + process +
				" Thread:" + thread + 
				" Arguments:" + Arrays.toString(arguments) + 
				" Result:" + result;
		return aString;
	}
	
	
	public ValgrindTrace(long timestamp, int process, long thread, String fnname, String result, String[] arguments) {
		super();
		this.timestamp = timestamp;
		this.process = process;
		this.thread = thread;
		this.fnname = fnname;
		this.result = result;
		this.arguments = arguments;
	}


	public ValgrindTrace(String trace) throws Exception {
//		System.err.println("Converting o ValgrindTrace:" + trace);
		String aPrcessedTrace = trace;
		if (trace.endsWith("\n")) {
			aPrcessedTrace = trace.substring(0, trace.length() - 1);
		}
//		Pattern pattern = Pattern.compile("I\\*\\*\\*([0-9]+) - Thread: ([0-9]+) - (.*): (.*) -> (.*)");
//1735493359 - Thread: 86762304 - allocate_and_initialize_array: 9 -> 0x6179f50
//		Pattern pattern = Pattern.compile("([0-9]+) - Thread: ([0-9]+) - (.*): (.*) -> (.*)");

		Pattern pattern = Pattern.compile("([0-9]+) - Process: ([0-9]+) Thread: ([0-9]+) - (.*): (.*) -> (.*)");
		Matcher m = pattern.matcher(aPrcessedTrace);
		
		if(m.matches()) {
			timestamp = Long.parseLong(m.group(1));
			process = Integer.parseInt(m.group(2));
			thread = Long.parseLong(m.group(3));
			fnname = m.group(4);
			arguments = m.group(5).split(",");
			result = m.group(6);

			for(int i=0; i<arguments.length; i++) {
				arguments[i] = arguments[i].trim();
			}
		} else {
//			System.err.println("Trace did not match");
			throw new TraceParsingException(trace);
		}
	}
}
