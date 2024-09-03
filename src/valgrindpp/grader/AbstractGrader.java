package valgrindpp.grader;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import valgrindpp.grader.ValgrindTrace.TraceParsingException;

public abstract class AbstractGrader implements Grader {
	protected List<ValgrindTrace> traces;
	protected List<String> stdout;
	
	public AbstractGrader(String studentDir, String filename) throws Exception {
		traces = new ArrayList<ValgrindTrace>();
		stdout = new ArrayList<String>();
		
		File file = new File(Paths.get(studentDir, filename).toString());
		Scanner scanner = new Scanner(file);
		
		while(scanner.hasNextLine()) {
			String line = scanner.nextLine();
			try {
				traces.add(new ValgrindTrace(line));
			} catch (TraceParsingException e) {
				stdout.add(line);
			}
		}
		
//		traces.sort(new Comparator<ValgrindTrace>() {
//			public int compare(ValgrindTrace a, ValgrindTrace b) {
//				return (int) (a.timestamp - b.timestamp);
//			}
//		});
		
		scanner.close();
	}
	protected Test countFuncCall(String fnname, int requiredCount) {
		int count = 0; 		
		for(ValgrindTrace trace: traces) {
			if(trace.fnname.equals(fnname)) {
				count ++;
			}
		}		
		return new Test("Called " + fnname, count >= requiredCount);
	}
	
	public abstract List<Test> grade();
}
