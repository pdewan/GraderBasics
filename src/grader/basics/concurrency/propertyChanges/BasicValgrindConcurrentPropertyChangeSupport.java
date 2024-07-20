package grader.basics.concurrency.propertyChanges;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import valgrindpp.grader.ValgrindTrace;

public class BasicValgrindConcurrentPropertyChangeSupport 
	extends BasicConcurrentPropertyChangeSupport
	implements ValgrindConcurrentPropertyChangeSupport{
	private List<Thread> returnedThreads = new ArrayList();
	
	private Map<Long, Thread> threadIdToJavaThread = new HashMap();
	@Override
	protected PropertyChangeEvent recordEventThreadAndTime(PropertyChangeEvent aPropertyChangeEvent) {
		Object anOldValue = aPropertyChangeEvent.getOldValue();
		if ((anOldValue instanceof ValgrindTrace)) {
			ValgrindTrace aValgrindTrace = (ValgrindTrace) anOldValue;
			lastTime = aValgrindTrace.timestamp;
			lastThread = idToJavaThread(aValgrindTrace.thread);
			if (aValgrindTrace.threadArgument >= 0) {
				String aThreadId = 
						//(String) aPropertyChangeEvent.getNewValue();						
						(String) aValgrindTrace.arguments[aValgrindTrace.threadArgument];
				Thread aThread = idToJavaThread(Integer.parseInt(aThreadId));
				if (!returnedThreads.contains(aThread)) {
					returnedThreads.add(aThread);
				}
				PropertyChangeEvent aNewPropertyChangeEvent = new PropertyChangeEvent(
						aThread, 
						aPropertyChangeEvent.getPropertyName(),
						aPropertyChangeEvent.getOldValue(),

						aPropertyChangeEvent.getNewValue());
				return aNewPropertyChangeEvent;
			}
		}
		return aPropertyChangeEvent;
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
	@Override
	public List<Thread> getReturnedThreads() {
		return returnedThreads;
	}

}
