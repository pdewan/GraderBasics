package valgrindpp.grader;

import java.util.ArrayList;
import java.util.List;

public class SimpleGrader extends AbstractGrader {

	public SimpleGrader(String studentDir, String filename) throws Exception {
		super(studentDir, filename);
	}

	public List<Test> grade() {
		List<Test> tests = new ArrayList<Test>();
		
		tests.add(countFuncCall("pthread_create", 2));
		tests.add(countFuncCall("pthread_join", 2));
		tests.add(countFuncCall("pthread_mutex_lock", 20));
		tests.add(countFuncCall("pthread_mutex_unlock", 20));
				
		return tests;
	}
	
	private Test countFuncCall(String fnname, int requiredCount) {
		int count = 0; 
		
		for(Trace trace: traces) {
			if(trace.fnname.equals(fnname)) {
				count ++;
			}
		}
		
		return new Test("Called " + fnname, count >= requiredCount);
	}
}
