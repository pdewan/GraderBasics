package valgrindpp.grader;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import valgrindpp.grader.Trace.TraceParsingException;

public abstract class AbstractGrader implements Grader {
	protected List<Trace> traces;
	protected List<String> stdout;
	
	public AbstractGrader(String studentDir, String filename) throws Exception {
		traces = new ArrayList<Trace>();
		stdout = new ArrayList<String>();
		
		File file = new File(Paths.get(studentDir, filename).toString());
		Scanner scanner = new Scanner(file);
		
		while(scanner.hasNextLine()) {
			String line = scanner.nextLine();
			try {
				traces.add(new Trace(line));
			} catch (TraceParsingException e) {
				stdout.add(line);
			}
		}
		
//		traces.sort(new Comparator<Trace>() {
//			public int compare(Trace a, Trace b) {
//				return (int) (a.timestamp - b.timestamp);
//			}
//		});
		
		scanner.close();
	}
	
	public abstract List<Test> grade();
}
