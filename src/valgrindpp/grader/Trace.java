package valgrindpp.grader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Trace {
	public long timestamp, thread;
	public String fnname, result;
	public String[] arguments;
	
	public class TraceParsingException extends Exception {
		public TraceParsingException(String trace) {
			super(trace);
		}

		private static final long serialVersionUID = 1;
	}
	
	public Trace(String trace) throws Exception {
		Pattern pattern = Pattern.compile("([0-9]+) - Thread: ([0-9]+) - (.*): (.*) -> (.*)");
		Matcher m = pattern.matcher(trace);
		
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
