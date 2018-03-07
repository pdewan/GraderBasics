package grader.basics.execution;

public class AProcessOutput implements Comparable<AProcessOutput>{
	public long time;
	public AProcessOutput(long time, String process, String output) {
		super();
		this.time = time;
		this.process = process;
		this.output = output;
	}
	public String process;
	public String output;
	@Override
	public int compareTo(AProcessOutput o) {
		return Long.compare(time, o.time);
	}
	
	@Override
	public String toString() {
		return time + " - " + process + " - " + output;
	}

}
