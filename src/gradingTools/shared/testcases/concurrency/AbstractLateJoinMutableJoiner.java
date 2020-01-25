package gradingTools.shared.testcases.concurrency;

import util.annotations.MaxValue;

@MaxValue(5)
public abstract class AbstractLateJoinMutableJoiner extends AbstractEarlyJoinMutableJoiner {
	protected void setTimeouts() {
		slaveTimeout = 0;
		masterTimeout = 300;
	}
}
