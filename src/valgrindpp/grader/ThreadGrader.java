package valgrindpp.grader;
import java.util.ArrayList;
import java.util.List;
public class ThreadGrader extends AbstractGrader {
	public ThreadGrader(String studentDir, String filename) throws Exception {
		super(studentDir, filename);
	}
	public List<Test> grade() {
		List<Test> tests = new ArrayList<Test>();		
		tests.add(countFuncCall("pthread_create", 2, 2));
		tests.add(countFuncCall("pthread_join", 2, 2));
		return tests;
	}
}
//	private Test countFuncCall(String fnname, int requiredCount) {
//		int count = 0; 
//		
//		for(ValgrindTrace trace: traces) {
//			if(trace.fnname.equals(fnname)) {
//				count ++;
//			}
//		}
//		
//		return new Test("Called " + fnname, count >= requiredCount);
//	}
//}
