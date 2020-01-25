package gradingTools.shared.testcases.concurrency;

import util.annotations.MaxValue;
//@MaxValue(20)
public abstract class AbstractLateJoinBasicJoiner extends AbstractEarlyJoinBasicJoiner {
	
	

	protected void setTimeouts() {
		slaveTimeout = 0;
		masterTimeout = 300;
	}
	
	

}
