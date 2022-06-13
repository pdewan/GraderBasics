package grader.basics.output.observer;

import java.beans.PropertyChangeEvent;

public interface ObjectToPropertyChange {
	PropertyChangeEvent toPropertyChange(Object anObject);

}
