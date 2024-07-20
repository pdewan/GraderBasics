package grader.basics.concurrency.propertyChanges;

import java.util.List;

public interface ValgrindConcurrentPropertyChangeSupport extends ConcurrentPropertyChangeSupport {

	List<Thread> getReturnedThreads();

}
