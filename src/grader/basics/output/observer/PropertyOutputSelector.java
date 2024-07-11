package grader.basics.output.observer;

import java.beans.PropertyChangeEvent;

public interface PropertyOutputSelector {
	
	boolean selectOutput(Object anOutput, PropertyChangeEvent aPropertyChangeEvent);
}
