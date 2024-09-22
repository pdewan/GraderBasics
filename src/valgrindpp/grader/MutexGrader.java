package valgrindpp.grader;
import java.util.ArrayList;
import java.util.List;
public class MutexGrader extends AbstractGrader {
	public MutexGrader(String studentDir, String filename) throws Exception {
		super(studentDir, filename);
	}
	public List<Test> grade() {
		List<Test> tests = new ArrayList<Test>();		
		tests.add(countFuncCall("pthread_create", 2, 2));
		tests.add(countFuncCall("pthread_join", 2, 2));
		tests.add(countFuncCall("pthread_mutex_lock", 21, 21));
		tests.add(countFuncCall("pthread_mutex_unlock", 21, 21));
		tests.add(countFuncCall("pthread_cond_signal", 20, 20));
		tests.add(countFuncCall("pthread_cond_wait", 19, 19));
		tests.add(countFuncCall("sleep", 0, 0));


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
