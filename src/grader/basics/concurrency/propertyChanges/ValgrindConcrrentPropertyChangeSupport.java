package grader.basics.concurrency.propertyChanges;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;

import valgrindpp.grader.ValgrindTrace;

public class ValgrindConcrrentPropertyChangeSupport extends BasicConcurrentPropertyChangeSupport {
	
	private Map<Long, Thread> threadIdToJavaThread = new HashMap();
	@Override
	protected void recordEventThreadAndTime(PropertyChangeEvent aPropertyChangeEvent) {
		Object anOldValue = aPropertyChangeEvent.getOldValue();
		if ((anOldValue instanceof ValgrindTrace)) {
			ValgrindTrace aValgrindTrace = (ValgrindTrace) anOldValue;
			lastTime = aValgrindTrace.timestamp;
			lastThread = idToJavaThread(aValgrindTrace.thread);
		}
	}
	
	protected Thread idToJavaThread (long anId) {
		Thread aThread = threadIdToJavaThread.get(anId);
		if (aThread == null) {
			aThread = new Thread();
			String aNewName = aThread.getName() + ":" + anId;
			aThread.setName(aNewName);
			threadIdToJavaThread.put(anId, aThread);
		}
		return aThread;
	}
}
