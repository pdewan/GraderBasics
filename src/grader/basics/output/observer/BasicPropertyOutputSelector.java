package grader.basics.output.observer;

import java.beans.PropertyChangeEvent;

public class BasicPropertyOutputSelector implements PropertyOutputSelector {
	@Override
	public boolean selectOutput(Object anOutput, PropertyChangeEvent aPropertyChangeEvent) {
		return true;
	}
}
