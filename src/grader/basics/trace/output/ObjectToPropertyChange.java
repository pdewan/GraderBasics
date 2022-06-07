package grader.basics.trace.output;

import java.beans.PropertyChangeEvent;

public interface ObjectToPropertyChange {
	PropertyChangeEvent toPropertyChange(Object anObject);

}
